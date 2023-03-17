package softwaredesign.utilities;

import java.io.File;
import java.io.IOException;
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
    public static final class Account {
        private Account() {throw new IllegalStateException("Utility class");}
        public static final String ENTER_NAME = "Name";
        public static final String INVALID_NAME = "Name must be a non-empty string without spaces";
        public static final String NAME_TAKEN = "This name is already taken";
        public static final String ENTER_PASSWORD = "Password";
        public static final String START_CREATION = "Create a new Account";
        public static final String NO_ACCOUNTS = "There are no accounts yet. Select " + CommandSet.getKeyword(CommandSet.Command.CREATE_ACCOUNT) + " to create a new account";
        public static final String ACCOUNTS_LIST = "Accounts:";
        public static final String CREATED = "Account Created";
        public static final String DELETE_SUCCESS = "Account deleted";
        public static final String INVALID_PASSWORD = "Invalid password";
        public static final String SELECT_ACCOUNT = "Select Account";
    }

    public static final class Repo {
        private Repo() {
            throw new IllegalStateException("Utility class");
        }
        public static final String INVALID_TOKEN = "The entered token is invalid.";
        public static final String INVALID_TOKEN_HINT = "Select " + CommandSet.getKeyword(CommandSet.Command.SET_TOKEN) + " to try again";
        public static final String TOKEN_HEADING = "Add a GH access token";
        public static final String TOKEN_PROMPT = "Token";
        public static final String VALIDATE_TOKEN = "Validating Token...";
        public static final String NO_TOKEN = "You have not added a valid token yet.";
    }

    public static final class Icons {
        private Icons() {
            throw new IllegalStateException("Utility class");
        }
        public static final String EMOJI_ZEN = getFileIcon("emoji_zen");
        public static final String ICON_CHECK = getFileIcon("icon_check");
        public static final String ICON_CROSS = getFileIcon("icon_cross");
        public static final String ICON_CLOCK = getFileIcon("icon_clock");

        private static final String FOLDER_PATH = "res/icons/";
        private static String getFileIcon(String path) {
            try (Scanner scanner = new Scanner(new File(FOLDER_PATH + path + ".txt"), StandardCharsets.UTF_8)) {
                return scanner.nextLine();
            } catch (IOException | NoSuchElementException e) {
                return "";
            }
        }
    }
}
