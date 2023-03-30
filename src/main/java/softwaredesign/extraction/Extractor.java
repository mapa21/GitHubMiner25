package softwaredesign.extraction;

import lombok.Getter;
import softwaredesign.UserConsole;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import softwaredesign.utilities.CommandLineManager;

public final class Extractor {
    private static final String LIST_FILE_LOCATION = "metric_types.txt";
    private static Extractor instance = null;
    @Getter
    private final Set<String> metricTypes = new HashSet<>();
    @Getter
    private final String listHash;
    private final Set<Class<? extends Metric>> classes = new HashSet<>();

    public static Extractor getInstance() throws IOException {
        if (instance == null) instance = new Extractor();
        return instance;
    }

    /**
     * Takes the path to a (git) folder and returns a map (name -> metric)
     * of extracted metrics together with the hash of the metrics list that was used.
     *
     * @param path Path to a git repository folder
     * @return Map (name -> metric) of metrics and list hash.
     */
    public ExtractionResult extractMetrics(String path) {
        List<String> output = CommandLineManager.runCommand("git log --numstat", path);
        List<Commit> commits = parseLog(output);
        Map<String, Metric> metrics = new HashMap<>();
        for (Class<? extends Metric> metric : classes) {
            try {
                Metric metricInstance = metric.getConstructor(List.class).newInstance(commits);
                metrics.put(metricInstance.getCommand(), metricInstance);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                     InvocationTargetException e) {
                UserConsole.log(e.toString());
                //Should never occur
            }
        }
        return new ExtractionResult(metrics, listHash);
    }

    /**
     * Loads the list of metrics and tries to initialize the classes set with the given metrics.
     * If the list
     *
     * @throws IOException If the list file can't be found, or no classes from the list could be loaded
     */
    private Extractor() throws IOException {
        try (InputStream input = ClassLoader.getSystemResourceAsStream(LIST_FILE_LOCATION)) {
            if (input == null) throw (new FileNotFoundException("Metric list file not found in resources"));
            Scanner inputReader = new Scanner(input);
            Set<String> metricNames = new TreeSet<>();
            while (inputReader.hasNext()) {
                String name = inputReader.next();
                if (name.length() > 0) metricNames.add(name);
            }
            initClassSet(metricNames);
            if (classes.isEmpty()) throw (new IOException("No compatible metrics could be loaded"));
            listHash = Integer.toString(metricNames.toString().hashCode());
        }
    }

    /**
     * Takes a set of metric names and tries load the associated classes located in the metrics package into the classes set.
     *
     * @param metricNames Set of metric names
     */
    private void initClassSet(Set<String> metricNames) {
        String packageName = this.getClass().getPackageName() + ".metrics.";
        for (String name : metricNames) {
            try {
                Class<?> metricClass = Class.forName(packageName + name);
                if (Metric.class.isAssignableFrom(metricClass)) {
                    this.classes.add((Class<? extends Metric>) metricClass);
                } else throw (new ClassCastException(metricClass.getName() + " does not extend the 'Metric' class"));
            } catch (ClassNotFoundException | ClassCastException e) {
                UserConsole.log(e.toString());
            }
        }
    }

    private static List<Commit> parseLog(List<String> output) {
        List<Commit> commits = new ArrayList<>();
        Iterator<String> lines = output.iterator();
        while (lines.hasNext()) {
            commits.add(getCommit(lines));
        }
        return commits;
    }

    private static Commit getCommit(Iterator<String> lines) {
        final int commitInd = 7;
        String currLine = lines.next();
        assert currLine.contains("commit");
        String hash = currLine.substring(commitInd);

        currLine = lines.next();
        boolean isMerge = currLine.contains("Merge");

        if (isMerge) currLine = lines.next();

        //Get authorName and authorEmail
        String authorName = currLine.substring(currLine.indexOf(":") + 2, currLine.indexOf("<") - 1);
        String authorEmail = currLine.substring(currLine.indexOf("<") + 1, currLine.indexOf(">"));

        currLine = lines.next();
        currLine = currLine.substring(currLine.indexOf(":") + 4);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ccc LLL d HH:mm:ss yyyy xxxx", Locale.US);
        ZonedDateTime dateTime = ZonedDateTime.parse(currLine, formatter);

        //skip line
        lines.next();

        //Get description
        String description = lines.next().trim();

        //skip lines
        while (lines.next().contains("    "));

        //Get insertions, deletions
        Commit.FileStats stats = new Commit.FileStats();

        if (!isMerge) {
            currLine = lines.next();
            while (!currLine.isEmpty()) {
                String[] elements = currLine.split("\\s+");
                if (!elements[0].equals("-")) {
                    stats.incrInsertions(Integer.parseInt(elements[0]));
                }
                if (!elements[1].equals("-")) {
                    stats.incrDeletions(Integer.parseInt(elements[1].trim()));
                }
                stats.incrFilesModified(1);

                if (!lines.hasNext()) break;
                currLine = lines.next();
            }
        }

        //create Commit
        return new Commit(
                hash,
                dateTime,
                authorName,
                authorEmail,
                description,
                stats,
                isMerge);
    }
}