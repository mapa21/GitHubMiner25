package softwaredesign;

import softwaredesign.extraction.Commit;
import softwaredesign.extraction.Metric;

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
