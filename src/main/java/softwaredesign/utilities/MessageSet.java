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
    public static final class Account {
        private Account() {
            throw new IllegalStateException("Utility class");
        }
        public static final String ENTER_NAME = "Name";
        public static final String INVALID_NAME = "Name must be a non-empty string without spaces";
        public static final String NAME_TAKEN = "This name is already taken";
        public static final String ENTER_PASSWORD = "Password";
        public static final String START_CREATION = "Create a new Account";
        public static final String NO_ACCOUNTS = "There are no accounts yet. Select " + CommandSet.getKeyword(CommandSet.Command.CREATE_ACCOUNT) + " to create a new account";
        public static final String ACCOUNTS_LIST = "Accounts:";
        public static final String CREATED = Icons.ICON_CHECK + " Account Created";
    }
    public static final class Icons {
        private Icons() {
            throw new IllegalStateException("Utility class");
        }
        public static final String EMOJI_ZEN = getFileIcon("emoji_zen");
        public static final String ICON_CHECK = getFileIcon("icon_check");

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
