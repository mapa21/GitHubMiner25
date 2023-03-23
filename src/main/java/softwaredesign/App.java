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
    public static boolean debug = false;
    private static final Map<String, Account> accounts = FileManager.initAccounts();

    private static final Set<Command> COMMANDS = Set.of(
            Command.QUIT,
            Command.CREATE_ACCOUNT,
            Command.DELETE_ACCOUNT,
            Command.ENTER_ACCOUNT,
            Command.LIST_ACCOUNTS
    );

    public static void main (String[] args) {
        for (String arg : args) {
            if (arg.equals("-d")) {
                debug = true;
                break;
            }
        }

        if (Boolean.FALSE.equals(FileManager.initRootFolder())) exit(-1);
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

        accounts.put(name, new Account(name, password));
        if (Boolean.TRUE.equals(FileManager.createFolder(name))){
            UserConsole.print(MessageSet.App.CREATED);
        } else{
            UserConsole.println(new TextElement(MessageSet.App.NOT_CREATED, FormatType.ERROR));
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
                UserConsole.println(MessageSet.App.INVALID_PASSWORD);
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

    public static void exit(int status) {
        UserConsole.print(MessageSet.Misc.GOODBYE);
        FileManager.saveAccounts(accounts);
        System.exit(status);
    }
}
