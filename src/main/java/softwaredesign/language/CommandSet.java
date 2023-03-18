package softwaredesign.language;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import com.google.common.collect.HashBiMap;

public class CommandSet {
    public enum Command {
        LIST_ACCOUNTS,
        ENTER_ACCOUNT,
        CREATE_ACCOUNT,
        DELETE_ACCOUNT,
        SET_TOKEN,
        ADD_REPO,
        REMOVE_REPO,
        LIST_REPOS,
        ENTER_REPO,
        LOG_OUT,
        EXIT_REPO,
        PRINT_INFO,
        PRINT_METRIC,
        UPDATE,
        QUIT,
        INVALID

    }

    private static final HashBiMap<Command, String> commandKeywordMap = HashBiMap.create();

    static {
        commandKeywordMap.put(Command.LIST_ACCOUNTS, "listAccounts");
        commandKeywordMap.put(Command.ENTER_ACCOUNT, "enterAccount");
        commandKeywordMap.put(Command.CREATE_ACCOUNT, "createAccount");
        commandKeywordMap.put(Command.DELETE_ACCOUNT, "deleteAccount");
        commandKeywordMap.put(Command.QUIT, "quit");
        commandKeywordMap.put(Command.SET_TOKEN, "setToken");
        commandKeywordMap.put(Command.ADD_REPO, "addRepo");
        commandKeywordMap.put(Command.REMOVE_REPO, "removeRepo");
        commandKeywordMap.put(Command.ENTER_REPO, "enterRepo");
        commandKeywordMap.put(Command.LIST_REPOS, "listRepos");
        commandKeywordMap.put(Command.LOG_OUT, "logOut");
        commandKeywordMap.put(Command.EXIT_REPO, "exit");
        commandKeywordMap.put(Command.PRINT_INFO, "printInfo");
        commandKeywordMap.put(Command.PRINT_METRIC, "printMetric");
        commandKeywordMap.put(Command.UPDATE, "update");
    }

    public static Set<String> getKeywords(Set<Command> commands) {
        Set<String> keywords = new TreeSet<>();
        commands.forEach(kw -> {
            if (commandKeywordMap.containsKey(kw)) {
                keywords.add(commandKeywordMap.get(kw));
            }
        });
        return keywords;
    }

    public static String getKeyword(Command command) {
        return commandKeywordMap.getOrDefault(command, "invalid");
    }
    public static Command getCommand(String name) {
        return commandKeywordMap.inverse().getOrDefault(name, Command.INVALID);
    }
}
