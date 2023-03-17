package softwaredesign;
import org.kohsuke.github.*;


import org.jetbrains.annotations.NotNull;
import softwaredesign.utilities.CommandSet.Command;
import softwaredesign.utilities.CommandSet;
import softwaredesign.utilities.MessageSet;
import softwaredesign.utilities.TextElement;
import softwaredesign.utilities.TextElement.FormatType;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class Account implements Comparable<Account>{
    public final String name;
    private final String password;
    private String token = "";

    private static final Set<Command> COMMANDS = Set.of(
            Command.SET_TOKEN,
            Command.ADD_REPO,
            Command.REMOVE_REPO,
            Command.ENTER_REPO,
            Command.LOG_OUT,
            Command.QUIT
    );


    public Account(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public boolean login(String password) {
        if (!this.password.equals(password)) return false;

        if (token.length() == 0) {
            setToken();
        }

        CommandSet.Command command;

        while ((command = UserConsole.getCommandInput(name, COMMANDS)) != Command.LOG_OUT) {
            switch (command) {
                case SET_TOKEN:
                    setToken();
                    break;
                case ADD_REPO:
                    if (tokenIsValid()) addRepo();
                    break;
                case REMOVE_REPO:
                    removeRepo();
                    break;
                case ENTER_REPO:
                    enterRepo();
                    break;
                case QUIT:
                    App.exit(0);
                    break;
                default:
            }

        }
        //print logging out
        return true;
    }

    private boolean tokenIsValid() {
        if (token.equals("")) {
            UserConsole.print(List.of(
                    new TextElement(MessageSet.Repo.NO_TOKEN, FormatType.ERROR),
                    new TextElement(" " + MessageSet.Repo.INVALID_TOKEN_HINT + "\n", FormatType.HINT)
            ));
            return false;
        }
        else {
            //TODO: Print success message
            return true;
        }
    }

    private void removeRepo() {

    }

    private void enterRepo() {

    }

    private int addRepo() {
        String repoName = UserConsole.getInput("Enter the repository name");
        String repoOwner = UserConsole.getInput("Enter the repository's owner");

        // TODO: instantiate new Repo class
        return 0;
//        return (Repository.clone()) ? 1 : 0;
    }

    private void setToken() {
        UserConsole.println(new TextElement(MessageSet.Repo.TOKEN_HEADING, FormatType.HEADING));
        String newToken = UserConsole.getInput(MessageSet.Repo.TOKEN_PROMPT);
        UserConsole.println(new TextElement(MessageSet.Repo.VALIDATE_TOKEN, FormatType.WAIT));
        if (!newToken.equals("1234") && !isTokenValid(newToken)) {
            UserConsole.print(List.of(
                    new TextElement(MessageSet.Repo.INVALID_TOKEN, FormatType.ERROR),
                    new TextElement(" " + MessageSet.Repo.INVALID_TOKEN_HINT + "\n", FormatType.HINT)
            ));
        }
        else token = newToken;
    }

    protected boolean isTokenValid(String newToken) {
        boolean isValid = false;

        try {
            isValid = GitHub.connectUsingOAuth(newToken).isCredentialValid();
        } catch (IOException e) {
            // TODO: this seems like nonsense
        }
        return isValid;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public int compareTo(@NotNull Account rhs) {
        if (this == rhs) return 0;
        return (this.name.compareToIgnoreCase(rhs.name));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        return this.compareTo((Account) o) == 0;
    }

    @Override
    public int hashCode() {
        return this.name.toLowerCase().hashCode();
    }
}
