package softwaredesign;

import softwaredesign.utilities.CommandSet;
import softwaredesign.utilities.CommandSet.Command;
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

    private static final class Messages {
        public final static String NO_ACCOUNTS = "There are no accounts yet. Select " + CommandSet.getKeyword(Command.CREATE_ACCOUNT) + " to create a new account";
    }

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
                    //TODO: create Account
                    break;
                case DELETE_ACCOUNT:
                    //delete
                    break;
            }
        }
    }

    private static void createAccount() {

    }

    private static void deleteAccount() {

    }

    private static void enterAccount() {

    }

    private static void listAccounts() {
        if (accounts.isEmpty()) {
            UserConsole.print(new TextElement(Messages.NO_ACCOUNTS, FormatType.HINT));
        } else {
            UserConsole.print(new TextElement(accounts.toString(), FormatType.BODY));
        }
    }
}
