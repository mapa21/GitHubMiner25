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
            UserConsole.print(MessageSet.Account.NO_TOKEN);
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
                UserConsole.println(MessageSet.Account.REMOVE_SUCCESS);
            }
        }
        catch (InputCancelledException ignored) {
            //
        }
    }

    private String getRepoChoice() throws InputCancelledException {
        return UserConsole.getInput(MessageSet.Account.SELECT_REPO_PROMPT, new TreeSet<>(repositories.keySet()));
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
        UserConsole.print(MessageSet.Account.REPOS_LIST);

        if (repositories.isEmpty()) {
            UserConsole.print(MessageSet.Account.NO_REPOS);
            return false;
        } else {
            UserConsole.println(new TextElement(repositories.keySet().toString()));
            return true;
        }
    }

    private void addRepo() {
        try {
            UserConsole.print(MessageSet.Account.START_ADDING);

            String repoName = UserConsole.getInput(MessageSet.Account.REPO_NAME_PROMPT, false, false);
            UserConsole.printInputResult(MessageSet.Account.REPO_NAME_PROMPT, repoName);
            String repoOwner = UserConsole.getInput(MessageSet.Account.REPO_OWNER_PROMPT, false, false);
            UserConsole.printInputResult(MessageSet.Account.REPO_OWNER_PROMPT, repoOwner);

            String id = repoOwner + "/" + repoName;
            if (!repositories.containsKey(id)) {
                Repository repo = new Repository(repoName, repoOwner, token, this.name);
                repositories.put(id, repo);
                UserConsole.print(MessageSet.Account.REPO_ADDED);
            }
        }
        catch (InvalidParameterException e) {
            UserConsole.print(MessageSet.Account.INVALID_REPO);
        }
        catch (InputCancelledException ignored){
            //cancel
        }

    }

    private void setToken() {
        try {
            UserConsole.print(MessageSet.Account.TOKEN_HEADING);
            String newToken = UserConsole.getInput(MessageSet.Account.TOKEN_PROMPT,false, false);
            UserConsole.print(MessageSet.Account.VALIDATE_TOKEN);


            if (!newToken.equals("1234") && !isTokenValid(newToken)) {
                UserConsole.print(MessageSet.Account.INVALID_TOKEN);
            }
            else if (!tokenValidForAddedRepos(newToken)) {
                UserConsole.print(MessageSet.Account.INSUFFICIENT_TOKEN);
            }
            else {
                UserConsole.print(MessageSet.Account.TOKEN_SUCCESS);
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
            return GitHub.connectUsingOAuth(newToken).isCredentialValid();
        } catch (IOException e) {
            return false;
        }
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
