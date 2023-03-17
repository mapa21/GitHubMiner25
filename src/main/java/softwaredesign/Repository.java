package softwaredesign;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Repository {
    public String name;
    public String owner;
    private String token;
    private Date lastUpdated;
    private String path;
    private String metricsListHash;
    private List<Metric> metrics;

    public Repository(String name, String owner, String token){
        this.name = name;
        this.owner = owner;
        this.token = token;
    }

    public void enter(){}
    protected Boolean clone(){
        // TODO: how to get userName? Maybe when user is selected we run 'cd <userDir>'?
        String userName = "bob";

        // TODO: get this OS-dependent
        String parentDir = "data";

        Process process;
        //create res folder
        File file = new File("res");
        if (file.mkdir()) {
            System.out.println("dir created successfully");
        } else {
            System.out.println("Unsuccessful dir creation");
        }

        String url = "https://" + this.token + "@github.com/" + this.owner + "/" + this.name + ".git";
        url = "https://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@github.com/ComputerScienceEducation/co-lab.git";    //DELETE

        String[] commands = {"git clone " + url, "git log --stat"};
        String[] locations = {"res/", "res/" + this.name};

        for (int i = 0; i < commands.length; i++){
            process = Runtime.getRuntime().exec(commands[i], null, new File(locations[i]));
            List<String> output = getOutput(process);
            List<Commit> commits = parseLog(output);
        }

        return true;
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
            counter++;

            //Get insertions, deletions
            int insertions = 0;
            int deletions = 0;
            int nrOfFilesChanged = 0;

            currLine = output.get(counter);
            while (!currLine.isEmpty()) {
                String[] elements = currLine.split("`{6}`");
                insertions += Integer.parseInt(elements[0]);
                deletions += Integer.parseInt(elements[1].trim());
                nrOfFilesChanged++;

                currLine = output.get(++counter);
            }
            counter++;

            //create Commit
            Commit currCommit = new Commit(authorName, authorEmail, dateTime, description, nrOfFilesChanged, hash, insertions, deletions, isMerge);
            commits.add(currCommit);
        }
        assert !commits.isEmpty();      //TODO: DELETE
        return commits;
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


}
