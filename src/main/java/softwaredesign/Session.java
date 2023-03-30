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

    public Session() throws InstantiationException {
        try {
            Extractor.getInstance();
            FileManager.initRootFolder();
            accounts = FileManager.initAccounts();
        } catch (IOException e) {
            throw new InstantiationException(e.getMessage());
        }
    }

    public void saveAccounts() {
        FileManager.saveAccounts(accounts);
    }

    public void run() {
        try {
            UserConsole.printTitle(TITLE_FILE, TITLE_FALLBACK);
            showHelp();
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

        if (Boolean.TRUE.equals(FileManager.createFolder(name))){
            accounts.put(name, new Account(name, password));
            UserConsole.print(MessageSet.App.CREATED);
        } else{
            UserConsole.print(MessageSet.App.NOT_CREATED);
        }
    }

    private void deleteAccount() {
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

    private void enterAccount() {
        try {
            if (listAccounts()
                    && Boolean.FALSE.equals(accounts.get(getAccountChoice()).login(UserConsole.getHiddenInput(MessageSet.App.PASSWORD_PROMPT)))) {
                UserConsole.print(MessageSet.App.INVALID_PASSWORD);
            }
            else {
                showHelp();
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
            UserConsole.println(new TextElement(accounts.keySet().toString(), TextElement.FormatType.BODY));
            return true;
        }
    }

    private void showHelp() {
        UserConsole.clearScreen();
        UserConsole.print(MessageSet.App.HELP_PAGE);
    }

}
