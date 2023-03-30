package softwaredesign.language;

import softwaredesign.UserConsole;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import softwaredesign.utilities.TextElement;
import static softwaredesign.utilities.TextElement.FormatType.*;

public final class MessageSet {
    private static final String UTIL_CLASS = "Utility Class";
    private MessageSet() {
        throw new IllegalStateException(UTIL_CLASS);
    }
    public static final class Misc {
        private Misc() {throw new IllegalStateException(UTIL_CLASS);}
        public static final TextElement GOODBYE = new TextElement("Exiting Application... Good Bye " + Icons.EMOJI_ZEN + "\n");
    }

    public static final class General {
        private General() {throw new IllegalStateException(UTIL_CLASS);}
        public static final TextElement WELCOME_HELP = new TextElement("Enter a command or press TAB to see all available commands\n", HINT);
    }

    public static final class Console {
        private Console() {throw new IllegalStateException(UTIL_CLASS);}
        public static List<TextElement> getInvalidOptionText(Set<String> options) {
            return List.of(
                    new TextElement("Invalid Option", ERROR),
                    new TextElement(" Options are: " + options + "\n")
            );
        }
        public static final String PROMPT_SEPARATOR = " > ";
        public static final String INPUT_SEPARATOR = ": ";
        public static final String INPUT_SET = " = ";
        public static TextElement getNoSpacesError(String prompt) {
            return new TextElement(prompt +" cannot contain any spaces\n", ERROR);
        }
        public static TextElement getNonEmptyError(String prompt) {
            return new TextElement(prompt + " cannot be empty\n", ERROR);
        }
        public static final TextElement DIVIDER = new TextElement("---------------", TextElement.FormatType.DIVIDER);
    }

    public static final class App {
        private App() {throw new IllegalStateException(UTIL_CLASS);}
        public static final String NAME_PROMPT = "Name";
        public static final String PASSWORD_PROMPT = "Password";
        public static final String PASSWORD_REPEAT_PROMPT = "Repeat Password";
        public static final String ACCOUNT_PROMPT = "Select Account";
        public static final List<TextElement> HELP_PAGE = List.of(
                new TextElement(Icons.ICON_HOME + " "),
                new TextElement("Home\n", PAGE_TITLE),
                new TextElement("From your home screen, you can create new accounts, " +
                        "list and enter existing accounts, or delete accounts that are no longer needed.\n"),
                new TextElement("Each account will be a home for repositories to " +
                        "be added and needs a valid GitHub access token to enable functionality.\n"),
                new TextElement("\n"),
                General.WELCOME_HELP
        );

        public static final TextElement NOT_CREATED = new TextElement(
                "Filesystem error - Account not created\n", ERROR);
        public static final TextElement NAME_TAKEN = new TextElement(
                "This name is already taken\n", TextElement.FormatType.ERROR);
        public static final TextElement START_CREATION = new TextElement(
                "Create a new Account\n", HEADING);
        public static final List<TextElement> NO_ACCOUNTS = List.of(
                new TextElement("There are no accounts yet. Select "),
                new TextElement(CommandSet.getKeyword(CommandSet.Command.CREATE_ACCOUNT), COMMAND),
                new TextElement(" to create a new account\n")
        );
        public static final List<TextElement> ACCOUNTS_LIST = List.of(
                new TextElement("Accounts:", HEADING),
                new TextElement(" ")
        );
        public static final TextElement CREATED = new TextElement("Account Created\n", SUCCESS);
        public static final TextElement DELETE_SUCCESS = new TextElement("Account deleted", SUCCESS);
        public static final TextElement INVALID_PASSWORD = new TextElement("Invalid password\n", ERROR);
        public static final TextElement PASSWORDS_NO_MATCH = new TextElement("Passwords don't match\n", ERROR);

    }

    public static final class Account {
        private Account() {
            throw new IllegalStateException(UTIL_CLASS);
        }

        private static final String SELECT = "Select ";
        private static final String TRY_AGAIN = " to try again\n";

        public static final String REPO_NAME_PROMPT = "Repo Name";
        public static final String REPO_OWNER_PROMPT = "Repo Owner";
        public static final String SELECT_REPO_PROMPT = "Select Repository";
        public static final String TOKEN_PROMPT = "Token";
        public static final TextElement TOKEN_HELP = new TextElement(
                "In order for GitHubMiner to access your repositories you need to provide a valid GitHub access token with at least \"repo\" rights.\n", HINT);
        public static List<TextElement> getHelpPage(String accountName) {
            return List.of(
                    new TextElement(Icons.ICON_ACCOUNT + " "),
                    new TextElement("Welcome, " + accountName + "\n", PAGE_TITLE),
                    new TextElement("From your account screen, you can add new repositories to be tracked, " +
                            "list and enter existing repositories, or remove repositories that are no longer needed.\n"),
                    new TextElement("\n"),
                    General.WELCOME_HELP
            );
        }

        public static final List<TextElement> INVALID_TOKEN = List.of(
                new TextElement("The entered token is invalid.\n", ERROR),
                new TextElement(SELECT, HINT),
                new TextElement(CommandSet.getKeyword(CommandSet.Command.SET_TOKEN), COMMAND),
                new TextElement(TRY_AGAIN, HINT)
        );
        public static final TextElement VALIDATE_TOKEN = new TextElement("Validating Token...\n", WAIT);
        public static final TextElement TOKEN_HEADING = new TextElement("Add a GH access token\n", HEADING);
        public static final List<TextElement> NO_TOKEN = List.of(
                new TextElement("You have not added a valid token yet.\n", ERROR),
                new TextElement(SELECT, HINT),
                new TextElement(CommandSet.getKeyword(CommandSet.Command.SET_TOKEN), COMMAND),
                new TextElement(TRY_AGAIN, HINT)
        );
        public static final TextElement TOKEN_SUCCESS = new TextElement("Token successfully added\n", SUCCESS);
        public static final TextElement START_ADDING = new TextElement("Add a new repository to be tracked\n", HEADING);
        public static final TextElement ADDING_REPO = new TextElement("Adding repository...\n", WAIT);
        public static final TextElement REPO_ADDED = new TextElement("The repository was successfully added\n", SUCCESS);
        public static final List<TextElement> INVALID_REPO = List.of(
                new TextElement("The repository could not be added\n", ERROR),
                new TextElement("Check whether the given details are correct and the repository is accessible with the current token. " +
                        "If the issue persists check your network connection\n", HINT)
        );
        public static final List<TextElement> REPOS_LIST = List.of(
                new TextElement("Repositories:", HEADING),
                new TextElement(" ")
        );
        public static final List<TextElement> NO_REPOS = List.of(
                new TextElement("There are no repositories yet\n"),
                new TextElement(SELECT, HINT),
                new TextElement(CommandSet.getKeyword(CommandSet.Command.ADD_REPO), COMMAND),
                new TextElement(" to add a new repository\n", HINT)
        );
        public static final TextElement REMOVE_SUCCESS = new TextElement("Repository removed\n", SUCCESS);
        public static final List<TextElement> INSUFFICIENT_TOKEN = List.of(
                new TextElement("The entered token does not have sufficient access rights for your repositories\n", ERROR),
                new TextElement("Try again with a token that has at least the access rights of the previous token. The previous token has not been replaced\n", HINT)
        );
    }

    public static final class Repo {
        private Repo() {
            throw new IllegalStateException(UTIL_CLASS);
        }
        public static final String SELECT_METRIC_PROMPT = "Select Metric";
        public static List<TextElement> getHelpPage(String name, String owner, String lastUpdate) {
            return List.of(
                    new TextElement(Icons.ICON_REPO + " "),
                    new TextElement(owner + "/" + name + "\n", PAGE_TITLE),
                    new TextElement("From the repository screen, you can print metrics of the repo or select to update the local data.\n"),
                    new TextElement("Last local update: " + lastUpdate + "\n"),
                    new TextElement("\n"),
                    General.WELCOME_HELP
            );
        }

        public static List<TextElement> getRepoInfoText(String name, String owner, String lastUpdated) {
            return List.of(
                    new TextElement("Repository Info\n", TITLE),
                    new TextElement(name + "\n"),
                    new TextElement("by: " + owner + "\n"),
                    new TextElement("Last local update: " + lastUpdated + "\n")
            );
        }
        public static final TextElement UPDATING = new TextElement("Updating Repo\n", WAIT);
        public static final TextElement UPDATED = new TextElement("Updated\n", SUCCESS);
        public static final TextElement GETTING_METRICS = new TextElement("Getting new metrics\n", WAIT);
        public static final TextElement REMOVE_UNSUCCESSFUL = new TextElement("Filesystem error while removing user folder - Account not removed", ERROR);

    }

    public static final class Icons {
        private Icons() {
            throw new IllegalStateException(UTIL_CLASS);
        }
        public static final String EMOJI_ZEN = getFileIcon("emoji_zen");
        public static final String ICON_CHECK = getFileIcon("icon_check");
        public static final String ICON_CROSS = getFileIcon("icon_cross");
        public static final String ICON_CLOCK = getFileIcon("icon_clock");
        public static final String ICON_INFO = getFileIcon("icon_info");
        public static final String ICON_HOME = getFileIcon("icon_home");
        public static final String ICON_ACCOUNT = getFileIcon("icon_account");
        public static final String ICON_REPO = getFileIcon("icon_repo");

        private static final String FOLDER_PATH = "icons/";

        private static String getFileIcon(String path) {
            try {
                InputStream input = ClassLoader.getSystemResourceAsStream(FOLDER_PATH + path + ".txt");
                if (input == null) throw (new FileNotFoundException(path + " does not exist at " + FOLDER_PATH));

                Scanner scanner = new Scanner(input, StandardCharsets.UTF_8);
                return scanner.nextLine();
            }
            catch (FileNotFoundException e) {
                UserConsole.log(e.getMessage());
                return "";
            }
        }
    }
}
