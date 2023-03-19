package softwaredesign;
import org.kohsuke.github.*;


import org.jetbrains.annotations.NotNull;
import softwaredesign.language.CommandSet;
import softwaredesign.language.CommandSet.Command;
import softwaredesign.language.MessageSet;
import softwaredesign.utilities.TextElement;
import softwaredesign.utilities.TextElement.FormatType;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.*;

public class Account implements Comparable<Account>{
    public final String name;
    private final String password;
    private String token = "";
    private final Map<String, Repository> repositories = new TreeMap<>();

    private static final Set<Command> COMMANDS = Set.of(
            Command.SET_TOKEN,
            Command.ADD_REPO,
            Command.REMOVE_REPO,
            Command.ENTER_REPO,
            Command.LIST_REPOS,
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
                case LIST_REPOS:
                    listRepos();
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
                    new TextElement(MessageSet.Account.NO_TOKEN, FormatType.ERROR),
                    new TextElement(MessageSet.Account.INVALID_TOKEN_HINT, FormatType.HINT)
            ));
            return false;
        }
        else {
            return true;
        }
    }

    private void removeRepo() {
        if (listRepos()) {
            String id = getRepoChoice();
            repositories.get(id).delete();  //getRepoChoice only returns existing repos, so default is not needed
            repositories.remove(id);
            UserConsole.println(new TextElement(MessageSet.Account.REMOVE_SUCCESS, FormatType.SUCCESS));
        }
    }

    private String getRepoChoice() {
        return UserConsole.getInput(MessageSet.Account.SELECT_REPO, new TreeSet<>(repositories.keySet()));
    }

    private void enterRepo() {
        if(listRepos()) {
            repositories.get(getRepoChoice()).enter();
        }
    }

    private boolean listRepos() {
        UserConsole.print(new TextElement(MessageSet.Account.REPOS_LIST, FormatType.HEADING));

        if (repositories.isEmpty()) {
            UserConsole.println(new TextElement(" " + MessageSet.Account.NO_REPOS, FormatType.HINT));
            return false;
        } else {
            UserConsole.println(new TextElement(" " + repositories.keySet(), FormatType.BODY));
            return true;
        }
    }

    private void addRepo() {
        UserConsole.println(new TextElement(MessageSet.Account.START_ADDING, FormatType.HEADING));

        String repoName = UserConsole.getInput(MessageSet.Account.ENTER_REPO_NAME, false, false);
        UserConsole.printInputResult(MessageSet.Account.ENTER_REPO_NAME, repoName);
        String repoOwner = UserConsole.getInput(MessageSet.Account.ENTER_REPO_OWNER, false, false);
        UserConsole.printInputResult(MessageSet.Account.ENTER_REPO_OWNER, repoOwner);

        String id = repoOwner + "/" + repoName;
        if (!repositories.containsKey(id)) {
            try {
                Repository repo = new Repository(repoName, repoOwner, token);
                repositories.put(id, repo);
                UserConsole.println(new TextElement(MessageSet.Account.REPO_ADDED, FormatType.SUCCESS));
            }
            catch (InvalidParameterException e) {
                UserConsole.print(List.of(
                        new TextElement(MessageSet.Account.INVALID_REPO, FormatType.ERROR),
                        new TextElement(MessageSet.Account.INVALID_REPO_HINT, FormatType.HINT)
                ));
            }
        }
    }

    private void setToken() {
        UserConsole.println(new TextElement(MessageSet.Account.TOKEN_HEADING, FormatType.HEADING));
        String newToken = UserConsole.getInput(MessageSet.Account.TOKEN_PROMPT,false, false);
        UserConsole.println(new TextElement(MessageSet.Account.VALIDATE_TOKEN, FormatType.WAIT));
        if (!newToken.equals("1234") && !isTokenValid(newToken)) {
            UserConsole.print(List.of(
                    new TextElement(MessageSet.Account.INVALID_TOKEN, FormatType.ERROR),
                    new TextElement(MessageSet.Account.INVALID_TOKEN_HINT, FormatType.HINT)
            ));
        }
        else {
            UserConsole.println(new TextElement(MessageSet.Account.TOKEN_SUCCESS, FormatType.SUCCESS));
            token = newToken;
        }
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
