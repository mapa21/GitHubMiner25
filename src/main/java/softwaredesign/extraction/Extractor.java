package softwaredesign.extraction;

import lombok.Getter;
import softwaredesign.extraction.metrics.NumberOfLinesAdded;
import softwaredesign.extraction.metrics.TopCollaborators;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public final class Extractor {
    // attributes
    @Getter
    private static final Extractor instance = new Extractor();
//    public static Extractor getInstance() {
//        return instance;
//    }
    @Getter
    private final Set<String> metricTypes = new HashSet<>();
    @Getter
    private final String listHash;
    List<Class<? extends Metric>> classList = new ArrayList<>();

    public ExtractionResult extractMetrics(String path) {
        Commit[] commits = {new Commit("Tester", "tester@vu.nl", ZonedDateTime.parse("2011-12-03T10:15:30+01:00"), "Created project", 3, "129ac84eb6a", 15, 2, Boolean.FALSE)}; //placeholder, replaces by extraction of commits
        List<Metric> metrics = new ArrayList<>();

        for (Class<? extends Metric> metric : classList) {
            try {
                metrics.add(metric.getConstructor(Commit[].class).newInstance((Object) commits));
            }
            catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                //TODO: handle exception? => Should never occur
            }
        }

        return new ExtractionResult(metrics, listHash);
    }

    private Extractor() {
        classList.add(TopCollaborators.class);
        classList.add(NumberOfLinesAdded.class);
        // ...

        for (Class<? extends Metric> c : classList) {
            System.out.println(c.getName());
            // TODO: remove unnecessary package information from string and add to metricTypes list
        }

        // TODO: hash contents of list and save result in listHash
        listHash = "";
    }

    private List<Commit> parseLog(List<String> output){
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
