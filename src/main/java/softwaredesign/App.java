package softwaredesign;

import softwaredesign.utilities.CommandSet;
import softwaredesign.utilities.CommandSet.Command;
import softwaredesign.utilities.MessageSet;
import softwaredesign.utilities.TextElement;
import softwaredesign.utilities.TextElement.FormatType;

import java.util.*;

public class App {

    private static final Map<String, Account> accounts = new TreeMap<>();

    private static final Set<Command> COMMANDS = Set.of(
            Command.QUIT,
            Command.CREATE_ACCOUNT,
            Command.DELETE_ACCOUNT,
            Command.ENTER_ACCOUNT,
            Command.LIST_ACCOUNTS
    );

    public static void main (String[] args) {
        UserConsole.printTitle("res/title.txt", 5, 6, 3, "Welcome to GitHubMiner (by Pirates)");
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
            }
        }

        exit(0);
    }

    private static void createAccount() {
        UserConsole.println(new TextElement(MessageSet.Account.START_CREATION, FormatType.HEADING));
        String name;
        while (true) {
            name = UserConsole.getInput(MessageSet.Account.ENTER_NAME);
            if (name.equals("") || name.contains(" ")) {
                UserConsole.println(new TextElement(MessageSet.Account.INVALID_NAME, FormatType.ERROR));
            }
            else if (accounts.containsKey(name)) {
                UserConsole.println(new TextElement(MessageSet.Account.NAME_TAKEN, FormatType.ERROR));
            }
            else break;
        }
        UserConsole.println(new TextElement(MessageSet.Account.ENTER_NAME + ": " + name, FormatType.BODY));
        String password = UserConsole.getPassword(MessageSet.Account.ENTER_PASSWORD);
        accounts.put(name, new Account(name, password));
        UserConsole.println(new TextElement(MessageSet.Account.CREATED, FormatType.SUCCESS));
    }

    private static void deleteAccount() {
        if (listAccounts()) {
            accounts.remove(getAccountChoice());
            UserConsole.println(new TextElement(MessageSet.Account.DELETE_SUCCESS, FormatType.SUCCESS));
        }
    }

    private static void enterAccount() {
        if (listAccounts() && Boolean.FALSE.equals(accounts.get(getAccountChoice()).login(UserConsole.getPassword(MessageSet.Account.ENTER_PASSWORD))))
            UserConsole.println(new TextElement(MessageSet.Account.INVALID_PASSWORD, FormatType.ERROR));
    }

    private static String getAccountChoice() {
        return UserConsole.getInput(MessageSet.Account.SELECT_ACCOUNT, new TreeSet<>(accounts.keySet()));
    }

    private static boolean listAccounts() {
        UserConsole.print(new TextElement(MessageSet.Account.ACCOUNTS_LIST, FormatType.HEADING));

        if (accounts.isEmpty()) {
            UserConsole.println(new TextElement(" " + MessageSet.Account.NO_ACCOUNTS, FormatType.HINT));
            return false;
        } else {
            UserConsole.println(new TextElement(" " + accounts.keySet(), FormatType.BODY));
            return true;
        }
    }

    public static void exit(int status) {
        UserConsole.println(new TextElement(MessageSet.Misc.GOODBYE, FormatType.BODY));
        //save data
        System.exit(0);
    }
}
