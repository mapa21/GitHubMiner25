package softwaredesign.extraction;

import lombok.Getter;
import softwaredesign.UserConsole;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    //TODO: Refactor into different location
    public static List<String> runCommand(String command, String path) {
        Process process;
        try {
            process = Runtime.getRuntime().exec(command, null, new File(path));
            assert process != null;
            return getOutput(process);
        } catch (IOException e) {
            UserConsole.log(e.getMessage());
            Thread.currentThread().interrupt();
            return List.of();
        }
    }

    //TODO: Refactor into different location
    private static List<String> getOutput(Process process) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        List<String> lines = new ArrayList<>();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            UserConsole.log(e.getMessage());
        }
        return lines;
    }

    /**
     * Takes the path to a (git) folder and returns a map (name -> metric)
     * of extracted metrics together with the hash of the metrics list that was used.
     * @param path Path to a git repository folder
     * @return Map (name -> metric) of metrics and list hash.
     */
    public ExtractionResult extractMetrics(String path) {
        List<String> output = runCommand("git log --numstat", path);
        List<Commit> commits = parseLog(output);
        Map<String, Metric> metrics = new HashMap<>();
        for (Class<? extends Metric> metric : classes) {
            try {
                Metric metricInstance = metric.getConstructor(List.class).newInstance(commits);
                metrics.put(metricInstance.getCommand(), metricInstance);
            }
            catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                UserConsole.log(e.toString());
                //Should never occur
            }
        }
        return new ExtractionResult(metrics, listHash);
    }

    /**
     * Loads the list of metrics and tries to initialize the classes set with the given metrics.
     * If the list
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
     * @param metricNames Set of metric names
     */
    private void initClassSet(Set<String> metricNames) {
        String packageName = this.getClass().getPackageName() + ".metrics.";
        for (String name : metricNames) {
            try {
                Class<?> metricClass = Class.forName(packageName + name);
                if (Metric.class.isAssignableFrom(metricClass)) {
                    this.classes.add((Class<? extends Metric>) metricClass);
                }
                else throw (new ClassCastException(metricClass.getName() + " does not extend the 'Metric' class"));
            }
            catch (ClassNotFoundException | ClassCastException e) {
                UserConsole.log(e.toString());
            }
        }
    }

    //TODO Maria: Abstract functionality into functions
    private static List<Commit> parseLog(List<String> output){
        final int commitInd = 7;
        List<Commit> commits = new ArrayList<>();

        int counter = 0;
        String currLine;
        while (counter < output.size()) {
            //Get commit hash
            currLine = output.get(counter);
            assert currLine.contains("commit");
            String hash = currLine.substring(commitInd);
            counter++;

            //Check if merge
            currLine = output.get(counter);
            Boolean isMerge = currLine.contains("Merge");
            if (Boolean.TRUE.equals(isMerge)) {
                counter++;
                currLine = output.get(counter);
            }

            //Get authorName and authorEmail
            String authorName = currLine.substring(currLine.indexOf(":") + 2, currLine.indexOf("<") - 1);
            String authorEmail = currLine.substring(currLine.indexOf("<") + 1, currLine.indexOf(">"));
            counter++;

            //Get Date
            currLine = output.get(counter);
            currLine = currLine.substring(currLine.indexOf(":") + 4);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ccc LLL d HH:mm:ss yyyy xxxx", Locale.US);
            ZonedDateTime dateTime = ZonedDateTime.parse(currLine, formatter);
            counter++;

            //skip line
            counter++;

            //Get description
            currLine = output.get(counter);
            String description = currLine.trim();
            counter++;

            //skip line
            while (!output.get(counter).isEmpty() && output.get(counter).contains("    ")) {
                counter++;
            }
            counter++;

            //Get insertions, deletions
            int insertions = 0;
            int deletions = 0;
            int nrOfFilesChanged = 0;

            if (Boolean.FALSE.equals(isMerge)){
                currLine = output.get(counter);
                while (!currLine.isEmpty()) {
                    String[] elements = currLine.split("\\s+");
                    if (!elements[0].equals("-")){
                        insertions += Integer.parseInt(elements[0]);
                    }
                    if (!elements[1].equals("-")){
                        deletions += Integer.parseInt(elements[1].trim());
                    }
                    nrOfFilesChanged++;

                    counter++;
                    if (counter >= output.size()) break;
                    currLine = output.get(counter);
                }
                counter++;
            }

            //create Commit
            Commit currCommit = new Commit(authorName, authorEmail, dateTime, description, nrOfFilesChanged, hash, insertions, deletions, isMerge);
            commits.add(currCommit);
        }
        return commits;
    }
}
