package softwaredesign.language;

import softwaredesign.UserConsole;
import softwaredesign.language.CommandSet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.Scanner;

public final class MessageSet {
    private MessageSet() {
        throw new IllegalStateException("Utility class");
    }
    public static final class Misc {
        private Misc() {
            throw new IllegalStateException("Utility class");
        }
        public static final String GOODBYE = "Exiting Application... Good Bye " + Icons.EMOJI_ZEN;
    }
    public static final class General {
        private General() {throw new IllegalStateException("Utility class");}
        public static final String ACTION_PROMPT = "Select Action";
    }
    public static final class Console {
        private Console() {throw new IllegalStateException("Utility class");}
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
        private App() {throw new IllegalStateException("Utility class");}
        public static final String ENTER_NAME = "Name";
        public static final String NAME_TAKEN = "This name is already taken";
        public static final String ENTER_PASSWORD = "Password";
        public static final String ENTER_PASSWORD_REPEAT = "Repeat Password";
        public static final String START_CREATION = "Create a new Account";
        public static final String NO_ACCOUNTS = "There are no accounts yet. Select " + CommandSet.getKeyword(CommandSet.Command.CREATE_ACCOUNT) + " to create a new account";
        public static final String ACCOUNTS_LIST = "Accounts:";
        public static final String CREATED = "Account Created";
        public static final String DELETE_SUCCESS = "Account deleted";
        public static final String INVALID_PASSWORD = "Invalid password";
        public static final String SELECT_ACCOUNT = "Select Account";
        public static final String PASSWORDS_NO_MATCH = "Passwords don't match";
    }

    public static final class Account {
        private Account() {
            throw new IllegalStateException("Utility class");
        }
        public static final String INVALID_TOKEN = "The entered token is invalid.";
        public static final String INVALID_TOKEN_HINT = " Select " + CommandSet.getKeyword(CommandSet.Command.SET_TOKEN) + " to try again\n";
        public static final String TOKEN_HEADING = "Add a GH access token";
        public static final String TOKEN_PROMPT = "Token";
        public static final String VALIDATE_TOKEN = "Validating Token...";
        public static final String NO_TOKEN = "You have not added a valid token yet.";
        public static final String TOKEN_SUCCESS = "Token successfully added";
        public static final String START_ADDING = "Add a new repository to be tracked";
        public static final String ENTER_REPO_NAME = "Repo Name";
        public static final String ENTER_REPO_OWNER = "Repo Owner";
        public static final String REPO_ADDED = "The repository was successfully added";
        public static final String INVALID_REPO = "The repository could not be added.";
        public static final String INVALID_REPO_HINT = " Check whether the given details are correct and the repository is accessible with the current token. If the issue persists check your network connection\n";
        public static final String REPOS_LIST = "Repositories:";
        public static final String NO_REPOS = "There are no repositories yet. Select " + CommandSet.getKeyword(CommandSet.Command.ADD_REPO) + " to add a new repository";
        public static final String SELECT_REPO = "Select Repository";
        public static final String REMOVE_SUCCESS = "Repository removed";
    }

    public static final class Repo {
        private Repo() {
            throw new IllegalStateException("Utility class");
        }
        public static final String INFO_TITLE = "Repository Info";
        public static final String INFO_NAME = "";
        public static final String INFO_OWNER = "by: ";
        public static final String INFO_LAST_UPDATED = "Last local update: ";

    }

    public static final class Icons {
        private Icons() {
            throw new IllegalStateException("Utility class");
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
