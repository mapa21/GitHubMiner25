package softwaredesign;

import softwaredesign.extraction.Extractor;
import softwaredesign.language.CommandSet.Command;
import softwaredesign.language.MessageSet;
import softwaredesign.utilities.FileManager;
import softwaredesign.utilities.InputCancelledException;
import softwaredesign.utilities.TextElement;
import softwaredesign.utilities.TextElement.FormatType;
import sun.misc.Signal;

import java.util.*;

public class App {

    private static final Map<String, Account> accounts = FileManager.initAccounts();

    private static final Set<Command> COMMANDS = Set.of(
            Command.QUIT,
            Command.CREATE_ACCOUNT,
            Command.DELETE_ACCOUNT,
            Command.ENTER_ACCOUNT,
            Command.LIST_ACCOUNTS
    );

    public static void main (String[] args) {
        //TODO: handle return
        FileManager.initRootFolder();
        try {
            Extractor.getInstance(); //eager evaluation of instance does not seem to work
        } catch (Exception e) { //TODO: add proper error handling
            exit(1);
        }

        try {
            Signal.handle(new Signal("INT"), signal -> exit(0));
            UserConsole.printTitle("title.txt", 5, 6, 3, "Welcome to GitHubMiner (by Pirates)");
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

        exit(0);
    }

    private static void createAccount() {
        String name = null;
        String password = null;
        try {
            UserConsole.println(new TextElement(MessageSet.App.START_CREATION, FormatType.HEADING));

            do {
                if (name != null) {
                    UserConsole.println(new TextElement(MessageSet.App.NAME_TAKEN, FormatType.ERROR));
                }
                name = UserConsole.getInput(MessageSet.App.ENTER_NAME, false, false);
            } while (accounts.containsKey(name));

            UserConsole.printInputResult(MessageSet.App.ENTER_NAME, name);

            do {
                if (password != null) {
                    UserConsole.println(new TextElement(MessageSet.App.PASSWORDS_NO_MATCH, FormatType.ERROR));
                }
                password = UserConsole.getPassword(MessageSet.App.ENTER_PASSWORD);
            } while (!password.equals(UserConsole.getPassword(MessageSet.App.ENTER_PASSWORD_REPEAT)));

        }
        catch (InputCancelledException ignored){
            return;
        }

        accounts.put(name, new Account(name, password));
        UserConsole.println(new TextElement(MessageSet.App.CREATED, FormatType.SUCCESS));

        //TODO: handle return
        FileManager.createFolder(name);
    }

    private static void deleteAccount() {
        try {
            if (listAccounts()) {
                accounts.remove(getAccountChoice());
                UserConsole.println(new TextElement(MessageSet.App.DELETE_SUCCESS, FormatType.SUCCESS));
            }
        }
        catch (InputCancelledException ignored) {
            //cancel
        }

    }

    private static void enterAccount() {
        try {
            if (listAccounts()
                    && Boolean.FALSE.equals(accounts.get(getAccountChoice()).login(UserConsole.getPassword(MessageSet.App.ENTER_PASSWORD)))) {
                UserConsole.println(new TextElement(MessageSet.App.INVALID_PASSWORD, FormatType.ERROR));
            }
        }
        catch (InputCancelledException ignored) {
            //cancel
        }

    }

    private static String getAccountChoice() throws InputCancelledException {
        return UserConsole.getInput(MessageSet.App.SELECT_ACCOUNT, new TreeSet<>(accounts.keySet()));
    }

    private static boolean listAccounts() {
        UserConsole.print(new TextElement(MessageSet.App.ACCOUNTS_LIST, FormatType.HEADING));

        if (accounts.isEmpty()) {
            UserConsole.println(new TextElement(" " + MessageSet.App.NO_ACCOUNTS, FormatType.HINT));
            return false;
        } else {
            UserConsole.println(new TextElement(" " + accounts.keySet(), FormatType.BODY));
            return true;
        }
    }

    public static void exit(int status) {
        UserConsole.println(new TextElement(MessageSet.Misc.GOODBYE, FormatType.BODY));
        FileManager.saveAccounts(accounts);
        System.exit(status);
    }
}
