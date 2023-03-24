package softwaredesign;

import softwaredesign.extraction.Extractor;
import softwaredesign.language.CommandSet.Command;
import softwaredesign.language.MessageSet;
import softwaredesign.utilities.FileManager;
import softwaredesign.utilities.InputCancelledException;
import softwaredesign.utilities.TextElement;
import softwaredesign.utilities.TextElement.FormatType;

import java.io.IOException;
import java.util.*;

public class App {

    private static final String TITLE_FILE_LOCATION = "title.txt";

    public enum EXIT_CODES {
        SUCCESS,
        UNKNOWN_ERROR
    }

    private static final Set<Command> COMMANDS = Set.of(
            Command.QUIT,
            Command.CREATE_ACCOUNT,
            Command.DELETE_ACCOUNT,
            Command.ENTER_ACCOUNT,
            Command.LIST_ACCOUNTS
    );

    private static final Map<String, Account> accounts = FileManager.initAccounts();

    /**
     * Application entry point
     * @param args Command line arguments
     */
    public static void main (String[] args) {
        for (String arg : args) {
            if (arg.equals("-d")) {
                UserConsole.setDebug(true);
                break;
            }
        }

        if (Boolean.FALSE.equals(FileManager.initRootFolder())) exit(EXIT_CODES.UNKNOWN_ERROR);
        try {
            Extractor.getInstance();
        } catch (IOException e) {
            exit(EXIT_CODES.UNKNOWN_ERROR, e.toString());
        }

        try {
            UserConsole.printTitle(TITLE_FILE_LOCATION, "Welcome to GitHubMiner (by Pirates)");
            UserConsole.print(MessageSet.App.HELP_PAGE);
            Command command;

            while ((command = UserConsole.getCommandInput("", COMMANDS)) != Command.QUIT) {
                switch (command) {
                    case LIST_ACCOUNTS:
                        listAccounts();
                        break;
                    case ENTER_ACCOUNT:
                        enterAccount();
                        break;
                    case CREATE_ACCOUNT:
                        createAccount();
                        break;
                    case DELETE_ACCOUNT:
                        deleteAccount();
                        break;
                    default:
                }
            }
        }
        catch (org.jline.reader.UserInterruptException | InputCancelledException ignored) {
            //
        }

        exit(EXIT_CODES.SUCCESS);
    }

    public static void exit(EXIT_CODES status, String message) {
        if (status != EXIT_CODES.SUCCESS) UserConsole.print(new TextElement(message, FormatType.ERROR));
        exit(status);
    }


    public static void exit(EXIT_CODES status) {
        UserConsole.print(MessageSet.Misc.GOODBYE);
        FileManager.saveAccounts(accounts);
        System.exit(status.ordinal());
    }

    private static void createAccount() {
        String name = null;
        String password = null;
        try {
            UserConsole.print(MessageSet.App.START_CREATION);

            do {
                if (name != null) {
                    UserConsole.print(MessageSet.App.NAME_TAKEN);
                }
                name = UserConsole.getInput(MessageSet.App.NAME_PROMPT, false, false);
            } while (accounts.containsKey(name));

            UserConsole.printInputResult(MessageSet.App.NAME_PROMPT, name);

            do {
                if (password != null) {
                    UserConsole.print(MessageSet.App.PASSWORDS_NO_MATCH);
                }
                password = UserConsole.getPassword(MessageSet.App.PASSWORD_PROMPT);
            } while (!password.equals(UserConsole.getPassword(MessageSet.App.PASSWORD_REPEAT_PROMPT)));

        }
        catch (InputCancelledException ignored){
            return;
        }

        if (Boolean.TRUE.equals(FileManager.createFolder(name))){
            accounts.put(name, new Account(name, password));
            UserConsole.print(MessageSet.App.CREATED);
        } else{
            UserConsole.print(MessageSet.App.NOT_CREATED);
        }
    }

    private static void deleteAccount() {
        try {
            if (listAccounts()) {
                accounts.remove(getAccountChoice());
                UserConsole.println(MessageSet.App.DELETE_SUCCESS);
            }
        }
        catch (InputCancelledException ignored) {
            //cancel
        }

    }

    private static void enterAccount() {
        try {
            if (listAccounts()
                    && Boolean.FALSE.equals(accounts.get(getAccountChoice()).login(UserConsole.getPassword(MessageSet.App.PASSWORD_PROMPT)))) {
                UserConsole.print(MessageSet.App.INVALID_PASSWORD);
            }
            else {
                UserConsole.print(MessageSet.App.HELP_PAGE);
            }
        }
        catch (InputCancelledException ignored) {
            //cancel
        }

    }

    private static String getAccountChoice() throws InputCancelledException {
        return UserConsole.getInput(MessageSet.App.ACCOUNT_PROMPT, new TreeSet<>(accounts.keySet()));
    }

    private static boolean listAccounts() {
        UserConsole.print(MessageSet.App.ACCOUNTS_LIST);

        if (accounts.isEmpty()) {
            UserConsole.print(MessageSet.App.NO_ACCOUNTS);
            return false;
        } else {
            UserConsole.println(new TextElement(accounts.keySet().toString(), FormatType.BODY));
            return true;
        }
    }

}
