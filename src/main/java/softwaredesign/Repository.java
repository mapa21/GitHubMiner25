package softwaredesign;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidParameterException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.kohsuke.github.GHCompare;
import softwaredesign.extraction.ExtractionResult;
import softwaredesign.extraction.Metric;
import softwaredesign.extraction.Commit;
import softwaredesign.extraction.Extractor;
import softwaredesign.language.CommandSet.Command;
import softwaredesign.language.MessageSet;
import softwaredesign.utilities.FileManager;
import softwaredesign.utilities.TextElement;

public class Repository {
    public final String name;
    public final String owner;
    private String token;
    private Date lastUpdated;
    private String path;
    private String metricsListHash;
    private Map<String, Metric> metrics = new TreeMap<>();

    private static final Set<Command> COMMANDS = Set.of(
            Command.QUIT,
            Command.PRINT_INFO,
            Command.PRINT_METRIC,
            Command.UPDATE,
            Command.EXIT_REPO
    );

    public Repository(String name, String owner, String token, String accountName) throws InvalidParameterException {
        this.name = name;
        this.owner = owner;
        this.token = token;
        this.path = FileManager.getSource() + accountName;
        if (!cloneRepo()) {
            throw(new InvalidParameterException("Invalid repository details or insufficient access rights with the given token"));
        }
        getMetricsList();
    }

    public void enter(){
        if (!metricsListHash.equals(Extractor.get().getListHash())) {
            getMetricsList();
        }
        Command command;
        while ((command = UserConsole.getCommandInput(owner + '/' + name, COMMANDS)) != Command.EXIT_REPO) {
            switch (command) {
                case PRINT_INFO:
                    printInfo();
                    break;
                case PRINT_METRIC:
                    printMetric();
                    break;
                case UPDATE:
                    update();
                    break;
                case QUIT:
                    App.exit(0);
                    break;
                default:
            }
        }
    }

    private void printInfo() {
        UserConsole.println(List.of (
                new TextElement(MessageSet.Repo.INFO_TITLE, TextElement.FormatType.TITLE),
                //TODO: add collaborators?
                new TextElement(MessageSet.Repo.INFO_NAME + name),
                new TextElement(MessageSet.Repo.INFO_OWNER + owner),
                new TextElement(MessageSet.Repo.INFO_LAST_UPDATED + lastUpdated)
        ));
    }

    private void printMetric() {
        String choice = UserConsole.getInput("select metric", metrics.keySet());
        UserConsole.println(metrics.get(choice).getMetric());
    }

    private void update() {
        //TODO: Print to user
        if (pullChanges()) {
            getMetricsList();
        }
    }

    private void getMetricsList() {
        assert this.path != null;
        ExtractionResult result = Extractor.get().extractMetrics(this.path);
        metrics = result.metrics;
        metricsListHash = result.listHash;
    }

    private boolean pullChanges() {
        lastUpdated = new Date();

        return true;
    }

    public void delete() {
        //TODO: delete files
    }

    protected boolean cloneRepo(){
        Process process;
        String url = "https://" + this.token + "@github.com/" + this.owner + "/" + this.name + ".git";
        try {
            process = Runtime.getRuntime().exec("git clone " + url, null, new File(this.path));
        } catch (IOException e) {
            return false;
        }
        lastUpdated = new Date();
        return true;
//
//        Process process;
//        //create res folder
//        File file = new File("res");
//        if (file.mkdir()) {
//            System.out.println("dir created successfully");
//        } else {
//            System.out.println("Unsuccessful dir creation");
//        }
//
//        String url = "https://" + this.token + "@github.com/" + this.owner + "/" + this.name + ".git";
//        url = "https://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@github.com/ComputerScienceEducation/co-lab.git";    //DELETE
//
//        String[] commands = {"git clone " + url, "git log --stat"};
//        String[] locations = {"res/", "res/" + this.name};
//
//        for (int i = 0; i < commands.length; i++){
//            try {
//                process = Runtime.getRuntime().exec(commands[i], null, new File(locations[i]));
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            List<String> output = null;
//            try {
//                output = getOutput(process);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            List<Commit> commits = Extractor.parseLog(output);
//        }
//        Extractor.get().extractMetrics(commits);
//
//        return true;
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
