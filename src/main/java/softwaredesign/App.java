package softwaredesign;

import softwaredesign.utilities.CommandSet;
import softwaredesign.utilities.CommandSet.Command;
import softwaredesign.utilities.MessageSet;
import softwaredesign.utilities.TextElement;
import softwaredesign.utilities.TextElement.FormatType;

import java.util.*;

public class App {

    private static final Set<Account> accounts = new TreeSet<>();
    private static final String prompt = "Enter Action";

    private static final Set<Command> COMMANDS = Set.of(
            Command.QUIT,
            Command.CREATE_ACCOUNT,
            Command.DELETE_ACCOUNT,
            Command.ENTER_ACCOUNT,
            Command.LIST_ACCOUNTS
    );

    //TODO add method to get all values out of map

    public static void main (String[] args) {
        UserConsole.printTitle("res/title.txt", 5, 6, 3, "Welcome to GitHubMiner (by Pirates)");
        Command command;

        while ((command = UserConsole.getCommandInput(prompt, COMMANDS)) != Command.QUIT) {
            switch (command) {
                case LIST_ACCOUNTS:
                    listAccounts();
                    break;
                case ENTER_ACCOUNT:
                    //TODO: enterfdsa
                    break;
                case CREATE_ACCOUNT:
                    createAccount();
                    break;
                case DELETE_ACCOUNT:
                    //delete
                    break;
            }
        }

        UserConsole.println(new TextElement(MessageSet.Misc.GOODBYE, FormatType.BODY));
    }

    private static void createAccount() {
        UserConsole.println(new TextElement(MessageSet.Account.START_CREATION, FormatType.HEADING));
        String name;
        while (true) {
            name = UserConsole.getInput(MessageSet.Account.ENTER_NAME);
            if (name.equals("") || name.contains(" ")) {
                UserConsole.println(new TextElement(MessageSet.Account.INVALID_NAME, FormatType.ERROR));
            }
            else if (accounts.contains(new Account(name, ""))) {
                UserConsole.println(new TextElement(MessageSet.Account.NAME_TAKEN, FormatType.ERROR));
            }
            else break;
        }
        UserConsole.println(new TextElement(MessageSet.Account.ENTER_NAME + ": " + name, FormatType.BODY));
        String password = UserConsole.getInput(MessageSet.Account.ENTER_PASSWORD);
        accounts.add(new Account(name, password));
        UserConsole.println(new TextElement(MessageSet.Account.CREATED, FormatType.SUCCESS));
    }

    private static void deleteAccount() {

    }

    private static void enterAccount() {

    }

    private static void listAccounts() {
        UserConsole.print(new TextElement(MessageSet.Account.ACCOUNTS_LIST, FormatType.HEADING));

        if (accounts.isEmpty()) {
            UserConsole.println(new TextElement(" " + MessageSet.Account.NO_ACCOUNTS, FormatType.HINT));
        } else {
            UserConsole.println(new TextElement(" " + accounts, FormatType.BODY));
        }
    }
}
