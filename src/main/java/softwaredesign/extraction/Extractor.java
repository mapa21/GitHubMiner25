package softwaredesign.extraction;

import lombok.Getter;
import softwaredesign.App;
import softwaredesign.UserConsole;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

//TODO: Idea! Use multithreading for extraction?

public final class Extractor {
    // attributes
    private static final Extractor instance = new Extractor();
    public static synchronized Extractor get() {
        return instance;
    }
    @Getter
    private final Set<String> metricTypes = new HashSet<>();

    private final String listHash;
    Set<Class<? extends Metric>> classes = new HashSet<>();

    public String getListHash() {
        return listHash;
    }

    //TODO: handle exceptions
    private List<String> gitLog(String path){
        Process process;
        try {
            process = Runtime.getRuntime().exec("git log --numstat", null, new File(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<String> output;
        try {
            output = getOutput(process);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return output;
    }

    public static List<String> getOutput(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        return lines;
    }

    public ExtractionResult extractMetrics(String path) {
        List<String> output = gitLog(path);
        List<Commit> commits = parseLog(output);
        UserConsole.log("COMMITS:");
        for (Commit c : commits) {
            UserConsole.log(c.authorName + " " + c.hash);
        }
        //List<Commit> commits = List.of(new Commit("Tester", "tester@vu.nl", ZonedDateTime.parse("2011-12-03T10:15:30+01:00"), "Created project", 3, "129ac84eb6a", 15, 2, Boolean.FALSE)); //placeholder, replaces by extraction of commits
        Map<String, Metric> metrics = new HashMap<>();

        for (Class<? extends Metric> metric : classes) {
            try {
                Metric metricInstance = metric.getConstructor(List.class).newInstance((Object) commits);
                metrics.put(metricInstance.getCommand(), metricInstance);
//                UserConsole.log(metricInstance.getCommand() + " extracted");
            }
            catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                UserConsole.log(e.toString());
                //TODO: handle exception? => Should never occur
            }
        }

        return new ExtractionResult(metrics, listHash);
    }

    private Extractor() {
        try (InputStream input = ClassLoader.getSystemResourceAsStream("metric_types.txt")){
            if (input == null) throw (new FileNotFoundException("Metric list file not found in resources"));
            Scanner inputReader = new Scanner(input);
            Set<String> metricNames = new HashSet<>();
            while (inputReader.hasNext()) {
                String name = inputReader.next();
                if (name.length() > 0) metricNames.add(name);
            }
            initClassSet(metricNames);
        }
        catch (IOException e) {
            UserConsole.log(e.getMessage());
            System.exit(1);
        }
        listHash = Integer.toString(Objects.hash(classes));
    }

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

    public static List<Commit> parseLog(List<String> output){
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
            if (isMerge) {
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

            if (!isMerge){
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
        assert !commits.isEmpty();      //TODO: DELETE
        return commits;
    }
}
