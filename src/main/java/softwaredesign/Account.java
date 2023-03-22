package softwaredesign;
import org.kohsuke.github.*;


import org.jetbrains.annotations.NotNull;
import softwaredesign.language.CommandSet;
import softwaredesign.language.CommandSet.Command;
import softwaredesign.language.MessageSet;
import softwaredesign.utilities.InputCancelledException;
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
        try {
            while ((command = UserConsole.getCommandInput(name, COMMANDS)) != Command.LOG_OUT) {
                switch (command) {
                    case SET_TOKEN:
                        setToken();
                        break;
                    case ADD_REPO:
                        if (tokenIsAdded()) addRepo();
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
        }
        catch (InputCancelledException ignored){
            //
        }

        //print logging out
        return true;
    }

    private boolean tokenIsAdded() {
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
        try {
            if (listRepos()) {
                String id = getRepoChoice();
                repositories.get(id).delete();  //getRepoChoice only returns existing repos, so default is not needed
                repositories.remove(id);
                UserConsole.println(new TextElement(MessageSet.Account.REMOVE_SUCCESS, FormatType.SUCCESS));
            }
        }
        catch (InputCancelledException ignored) {
            //
        }
    }

    private String getRepoChoice() throws InputCancelledException {
        return UserConsole.getInput(MessageSet.Account.SELECT_REPO, new TreeSet<>(repositories.keySet()));
    }

    private void enterRepo() {
        try {
            if(listRepos()) repositories.get(getRepoChoice()).enter();
        }
        catch (InputCancelledException ignored) {
            //
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
        try {
            UserConsole.println(new TextElement(MessageSet.Account.START_ADDING, FormatType.HEADING));

            String repoName = UserConsole.getInput(MessageSet.Account.ENTER_REPO_NAME, false, false);
            UserConsole.printInputResult(MessageSet.Account.ENTER_REPO_NAME, repoName);
            String repoOwner = UserConsole.getInput(MessageSet.Account.ENTER_REPO_OWNER, false, false);
            UserConsole.printInputResult(MessageSet.Account.ENTER_REPO_OWNER, repoOwner);

            String id = repoOwner + "/" + repoName;
            if (!repositories.containsKey(id)) {
                Repository repo = new Repository(repoName, repoOwner, token, this.name);
                repositories.put(id, repo);
                UserConsole.println(new TextElement(MessageSet.Account.REPO_ADDED, FormatType.SUCCESS));
            }
        }
        catch (InvalidParameterException e) {
            UserConsole.print(List.of(
                    new TextElement(MessageSet.Account.INVALID_REPO, FormatType.ERROR),
                    new TextElement(MessageSet.Account.INVALID_REPO_HINT, FormatType.HINT)
            ));
        }
        catch (InputCancelledException ignored){
            //cancel
        }

    }

    private void setToken() {
        try {
            UserConsole.println(new TextElement(MessageSet.Account.TOKEN_HEADING, FormatType.HEADING));
            String newToken = UserConsole.getInput(MessageSet.Account.TOKEN_PROMPT,false, false);
            UserConsole.println(new TextElement(MessageSet.Account.VALIDATE_TOKEN, FormatType.WAIT));


            if (!newToken.equals("1234") && !isTokenValid(newToken)) {
                UserConsole.print(List.of(
                        new TextElement(MessageSet.Account.INVALID_TOKEN, FormatType.ERROR),
                        new TextElement(MessageSet.Account.INVALID_TOKEN_HINT, FormatType.HINT)
                ));
            }
            else if (!tokenValidForAddedRepos(newToken)) {
                UserConsole.print(List.of(
                        new TextElement(MessageSet.Account.INSUFFICIENT_TOKEN, FormatType.ERROR),
                        new TextElement(MessageSet.Account.INSUFFICIENT_TOKEN_HINT, FormatType.HINT)
                ));
            }
            else {
                UserConsole.println(new TextElement(MessageSet.Account.TOKEN_SUCCESS, FormatType.SUCCESS));
                token = newToken;
                repositories.values().forEach(repo -> repo.setToken(newToken));
            }
        }
        catch (InputCancelledException ignored) {
            //
        }
    }

    private boolean tokenValidForAddedRepos(String token) {
        for (Repository repo : repositories.values()) {
            if (!repo.validateRepo(token))  return false;
        }
        return true;
    }

    protected boolean isTokenValid(String newToken) {
        try {
            GitHub.connectUsingOAuth(newToken).isCredentialValid();
        } catch (IOException e) {
            return false;
        }
        return true;
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
