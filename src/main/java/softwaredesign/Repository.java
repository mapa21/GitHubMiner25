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
        // TODO: how to get userName? Maybe when user is selected we run 'cd <userDir>'?
        String userName = "bob";

        // TODO: get this OS-dependent
        String parentDir = "data";

        Process process;
        String path = parentDir + "/" + userName + "/" + name;

        try {
            process = Runtime.getRuntime().exec("git clone https://" + token + "@github.com/" + owner + "/" + name + ".git " + path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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
