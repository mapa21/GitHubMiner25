package softwaredesign;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Repository {
    public String name;
    public String owner;
    private String token;
    private Date lastUpdated;
    private String path;
    private String metricsListHash;

    public void enter(){}
    protected Boolean clone(){
        Process process;
        String[] commands = {"git clone https://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@github.com/ComputerScienceEducation/co-lab.git", "git log"};
        String[] locations = {"res/", "res/co-lab/"};
        for (int i = 0; i < commands.length; i++) {
            System.out.println(commands[i] + " at " + locations[i]);
            try {
                process = Runtime.getRuntime().exec(commands[i], null, new File(locations[i]));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            List<String> output = null;
            try {
                output = getOutput(process);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println(output);
        }
        return true;
    }

    public static List<String> getOutput(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        List<String> lines = new ArrayList<String>();
        String line = "";
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        return lines;
    }


}
