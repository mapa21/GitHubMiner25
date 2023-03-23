package softwaredesign;

import java.io.IOException;
import java.io.File;
import java.security.InvalidParameterException;
import java.util.*;

import lombok.Setter;
import org.kohsuke.github.GitHub;
import softwaredesign.extraction.ExtractionResult;
import softwaredesign.extraction.Metric;
import softwaredesign.extraction.Extractor;
import softwaredesign.language.CommandSet.Command;
import softwaredesign.language.MessageSet;
import softwaredesign.utilities.FileManager;
import softwaredesign.utilities.InputCancelledException;
import softwaredesign.utilities.TextElement;
import org.apache.commons.io.FileUtils;

public class Repository {
    public final String name;
    public final String owner;
    @Setter
    private String token;
    private Date lastUpdated;
    private final String parentPath;
    private final String repoPath;
    private final String repoDirName;
    private String metricsListHash;
    private Map<String, Metric> metrics = new TreeMap<>();

    private static final Set<Command> COMMANDS = Set.of(
            Command.QUIT,
            Command.PRINT_INFO,
            Command.PRINT_METRIC,
            Command.UPDATE,
            Command.PRINT_ALL_METRICS,
            Command.EXIT_REPO
    );

    public Repository(String name, String owner, String token, String accountName) throws InvalidParameterException {
        this.name = name;
        this.owner = owner;
        this.token = token;
        this.repoDirName = this.owner + ":" + this.name;
        this.parentPath = FileManager.getSource() + accountName + FileManager.SEPARATOR;
        this.repoPath = this.parentPath + this.repoDirName + FileManager.SEPARATOR;

        if (!validateRepo()) {
            throw(new InvalidParameterException("Invalid repository details or insufficient access rights with the given token."));
        }

        cloneRepo();
        getMetricsList();
    }

    public void enter(){
        if (!metricsListHash.equals(Extractor.getInstance().getListHash())) {
            UserConsole.log("Metrics Hash has changed since last time.");
            getMetricsList();
        }
        Command command;
        try {
            while ((command = UserConsole.getCommandInput(owner + '/' + name, COMMANDS)) != Command.EXIT_REPO) {
                switch (command) {
                    case PRINT_INFO:
                        printInfo();
                        break;
                    case PRINT_METRIC:
                        printMetric();
                        break;
                    case PRINT_ALL_METRICS:
                        printAllMetrics();
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
        catch (InputCancelledException ignored) {
            //
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

    private void printAllMetrics() {
        metrics.values().forEach(metric -> UserConsole.println(metric.getMetric()));
    }

    private void printMetric() {
        try {
            String choice = UserConsole.getInput("select metric", metrics.keySet());
            UserConsole.println(metrics.get(choice).getMetric());
        }
        catch (InputCancelledException ignored) {
            //
        }
    }

    private void update() {
        //TODO-Lennart: Print to user
        if (changesToPull()) {
            getMetricsList();
        }
    }

    private void getMetricsList() {
        assert this.parentPath != null;
        ExtractionResult result = Extractor.getInstance().extractMetrics(this.repoPath);
        metrics = result.metrics;
        metricsListHash = result.listHash;
    }

    private boolean changesToPull() {
        lastUpdated = new Date();
        List<String> output = Extractor.runCommand("git pull", this.repoPath);
        String noUpdate = "Already up to date.";
        return output.size() != 1 || !output.contains(noUpdate);
    }

    public void delete() {
        System.out.println("file to delete:" + this.repoPath);
        try {
            FileUtils.deleteDirectory(new File(this.repoPath));
        } catch (IOException | IllegalArgumentException e) {
            //TODO: unsuccessful delete message
            UserConsole.log(e.getMessage());
        }
    }
    private boolean validateRepo() {
        return validateRepo(this.token);
    }
    //TODO: naming?
    public boolean validateRepo(String token){
        try {
            GitHub.connectUsingOAuth(token).getRepository(this.owner + "/" + this.name);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    protected void cloneRepo(){
        String url = "https://" + this.token + "@github.com/" + this.owner + "/" + this.name + ".git";
        Extractor.runCommand("git clone " + url + " " + this.repoDirName, this.parentPath);
        lastUpdated = new Date();
    }
}
