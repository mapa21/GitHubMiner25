package softwaredesign;

import softwaredesign.extraction.Extractor;
import softwaredesign.language.CommandSet;
import softwaredesign.language.MessageSet;
import softwaredesign.utilities.FileManager;
import softwaredesign.utilities.InputCancelledException;
import softwaredesign.utilities.TextElement;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Session {
    private static final String TITLE_FILE = "title.txt";
    private static final String TITLE_FALLBACK = "Welcome to GitHubMiner (by Pirates)";

    private static final Set<CommandSet.Command> COMMANDS = Set.of(
            CommandSet.Command.QUIT,
            CommandSet.Command.CREATE_ACCOUNT,
            CommandSet.Command.DELETE_ACCOUNT,
            CommandSet.Command.ENTER_ACCOUNT,
            CommandSet.Command.LIST_ACCOUNTS
    );

    private final Map<String, Account> accounts;
    private static Session instance = null;

    public static void create() throws InstantiationException {
        if (instance == null) instance = new Session();
    }

    public static void save() {
        if (instance != null) FileManager.saveAccounts(instance.accounts);
    }

    private Session() throws InstantiationException {
        try {
            Extractor.getInstance();
            FileManager.initRootFolder();
            accounts = FileManager.loadAccounts();
        } catch (IOException e) {
            throw new InstantiationException(e.getMessage());
        }
    }

    public static void run() {
        if (instance != null) instance.runInstance();
    }

    private void runInstance() {
        try {
            UserConsole.printTitle(TITLE_FILE, TITLE_FALLBACK);
            showHelp(false);
            CommandSet.Command command;

            while ((command = UserConsole.getCommandInput("", COMMANDS)) != CommandSet.Command.QUIT) {
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
    }


    private void createAccount() {
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
                password = UserConsole.getHiddenInput(MessageSet.App.PASSWORD_PROMPT);
            } while (!password.equals(UserConsole.getHiddenInput(MessageSet.App.PASSWORD_REPEAT_PROMPT)));

        }
        catch (InputCancelledException ignored){
            return;
        }

        try {
            FileManager.createFolder(name);
            accounts.put(name, new Account(name, password));
            UserConsole.print(MessageSet.App.CREATED);
        }
        catch (IOException e) {
            UserConsole.print(MessageSet.App.NOT_CREATED);
            UserConsole.log(e.getMessage());
        }
    }

    private void deleteAccount() {
        try {
            if (listAccounts()) {
                String account = getAccountChoice();
                if (accounts.get(account).delete()) {
                    accounts.remove(account);
                    UserConsole.print(MessageSet.App.DELETE_SUCCESS);
                }
                else {
                    UserConsole.print(MessageSet.App.DELETE_ERROR);
                }
            }
        }
        catch (InputCancelledException ignored) {
            //cancel
        }
    }

    private void enterAccount() {
        try {
            if (listAccounts()
                    && !accounts.get(getAccountChoice()).login(UserConsole.getHiddenInput(MessageSet.App.PASSWORD_PROMPT))) {
                UserConsole.print(MessageSet.App.INVALID_PASSWORD);
            }
            else {
                showHelp(true);
            }
        }
        catch (InputCancelledException ignored) {
            //cancel
        }

    }

    private String getAccountChoice() throws InputCancelledException {
        return UserConsole.getInput(MessageSet.App.ACCOUNT_PROMPT, new TreeSet<>(accounts.keySet()));
    }

    private boolean listAccounts() {
        UserConsole.print(MessageSet.App.ACCOUNTS_LIST);

        if (accounts.isEmpty()) {
            UserConsole.print(MessageSet.App.NO_ACCOUNTS);
            return false;
        } else {
            UserConsole.println(new TextElement(accounts.keySet().toString()));
            return true;
        }
    }

    private void showHelp(boolean clearScreen) {
        if (clearScreen) UserConsole.clearScreen();
        UserConsole.print(MessageSet.App.HELP_PAGE);
    }

}
