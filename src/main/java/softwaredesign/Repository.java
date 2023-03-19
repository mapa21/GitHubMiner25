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
        this.path = accountName + FileManager.SEPARATOR + owner + FileManager.SEPARATOR;
        FileManager.createFolder(this.path);
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
        ExtractionResult result = Extractor.get().extractMetrics(FileManager.getSource() + this.path + this.name);
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
            process = Runtime.getRuntime().exec("git clone " + url, null, new File(FileManager.getSource() + this.path));
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            UserConsole.log(e.getMessage());
            return false;
        }
        lastUpdated = new Date();
        return true;
    }
}
