package softwaredesign.utilities;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.HashBiMap;

public class CommandSet {
    public enum Command {
        LIST_ACCOUNTS,
        ENTER_ACCOUNT,
        CREATE_ACCOUNT,
        DELETE_ACCOUNT,
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
    }

    public static Set<String> getKeywords(Set<Command> commands) {
        Set<String> keywords = new HashSet<>();
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
