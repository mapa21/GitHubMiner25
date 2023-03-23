package softwaredesign.language;

import softwaredesign.UserConsole;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;
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
        public static final String ACTION_PROMPT = "Select Action";
    }
    public static final class Console {
        private Console() {throw new IllegalStateException(UTIL_CLASS);}
        public static final String INVALID_OPTION = "Invalid Option.";
        public static final String OPTIONS = "Options are: ";
        public static final String DIVIDER = "---------------";
        public static final String PROMPT_SEPARATOR = " > ";
        public static final String INPUT_SEPARATOR = ": ";
        public static final String INPUT_SET = " = ";
        public static final String CONTAINS_SPACES_ERROR = " cannot contain any spaces";
        public static final String IS_EMPTY_ERROR = " cannot be empty";
    }

    public static final class App {
        private App() {throw new IllegalStateException(UTIL_CLASS);}
        public static final String NAME_PROMPT = "Name";
        public static final String PASSWORD_PROMPT = "Password";
        public static final String PASSWORD_REPEAT_PROMPT = "Repeat Password";
        public static final String ACCOUNT_PROMPT = "Select Account";
        public static final String NOT_CREATED = "Account NOT Created";
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
        public static final TextElement CREATED = new TextElement("Account Created\n");
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
        public static final String INFO_TITLE = "Repository Info";
        public static final String INFO_NAME = "";
        public static final String INFO_OWNER = "by: ";
        public static final String INFO_LAST_UPDATED = "Last local update: ";
        public static final String UPDATING = "Updating Repo";
        public static final String UPDATED = "Updated";
        public static final String GETTING_METRICS = "Getting new metrics";
        public static final String REMOVE_UNSUCCESSFUL = "Unsuccessful folder delete";

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
