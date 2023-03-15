package softwaredesign;

//import org.jline.terminal.Terminal;
//import org.jline.terminal.TerminalBuilder;
//import softwaredesign.Utilitites.TextElement;
import com.sun.source.tree.Tree;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.NullCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
//import jline.console.ConsoleReader;
//import jline.console.history.History;
import org.jline.terminal.TerminalBuilder;
//import softwaredesign.Utilitites.CaseInsensitiveCompleter;

import java.io.IOException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class UserConsole {


//    public static String getInput(String prompt, Set<String> options) {
//        String line;
//
//        try (ConsoleReader console = new ConsoleReader()) {
////            console.setHistoryEnabled(true);
//
//            console.addCompleter(new CaseInsensitiveCompleter(options));
//
//            console.setPrompt(prompt + "> ");
//            while (!options.contains(line = console.readLine().toLowerCase().trim())) {
//                //TODO: print options
//                console.println("Invalid");
//            }
//
//        }
//        catch (java.io.IOException e) {
//            line = null;
//            //TODO: handle error
//        }
//        return line;
//    }

    public static String getInput(String prompt, Set<String> options) {
        String line;
        Set<String> optionsIgnoreCase = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
//        optionsIgnoreCase.addAll(options);
        try (Terminal terminal = TerminalBuilder.terminal()) {

            LineReaderBuilder builder = org.jline.reader.LineReaderBuilder.builder();
            builder.terminal(terminal);
            builder.completer(new ArgumentCompleter(new StringsCompleter(options), new NullCompleter()));
            LineReader reader = builder.build();

            //todo enable matching on case insensitive but only allow proper case to be returned

            while (!options.contains((line = reader.readLine(prompt + " > ")).trim())) {
                terminal.writer().write("Invalid\n");
            }
        }
        catch (java.io.IOException e) {
            line = null;
            //TODO: handle error
        }
        return line;

    }

}
