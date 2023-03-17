package softwaredesign;

import org.checkerframework.checker.units.qual.A;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.NullCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Attributes;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.w3c.dom.Attr;
import softwaredesign.utilities.CommandSet;
import softwaredesign.utilities.TextElement;

import javax.naming.SizeLimitExceededException;
import java.io.*;
import java.util.*;

import static java.util.Map.entry;

public class UserConsole {
    private UserConsole() {}
    private static Terminal terminal;

    static {
        try {
            terminal = TerminalBuilder.builder().encoding("UTF-8").build();
        } catch (IOException e) {
            System.exit(1);
        }
    }

    private static final int NO_COLOR = -1;

    private final static class Messages {
        static final String INVALID_OPTION = "Invalid Option.";
        static final String OPTIONS = "Options are: ";
        static final String DIVIDER = "---------------";
        static final String PROMPT_SEPARATOR = " > ";
        static final String INPUT_SEPARATOR = ": ";
    }

    private static class TextStyles {
        static final TextStyle ERROR = new TextStyle(AttributedStyle.RED, NO_COLOR, true, false, false);
        static final TextStyle SUCCESS = new TextStyle(AttributedStyle.GREEN, NO_COLOR, true, false ,false);
        static final TextStyle DEFAULT = new TextStyle(NO_COLOR, NO_COLOR, false ,false, false);
        static final TextStyle HEADING = new TextStyle(AttributedStyle.BLUE, NO_COLOR, true, false, true);
        static final TextStyle GREYED = new TextStyle(AttributedStyle.BRIGHT, NO_COLOR, false, false, false);
        static final TextStyle BOLD_UNDERLINED = new TextStyle(NO_COLOR, NO_COLOR, true, false, true);
        static final TextStyle PROMPT = new TextStyle(AttributedStyle.BRIGHT, NO_COLOR, false, false, false);
    }

    private static Map<TextElement.FormatType, TextStyle> typeToStyle = Map.ofEntries(
            entry(TextElement.FormatType.HEADING, TextStyles.HEADING),
            entry(TextElement.FormatType.BODY, TextStyles.DEFAULT),
            entry(TextElement.FormatType.STATISTIC, TextStyles.DEFAULT),
            entry(TextElement.FormatType.DIVIDER, TextStyles.GREYED),
            entry(TextElement.FormatType.ERROR, TextStyles.ERROR),
            entry(TextElement.FormatType.SUCCESS, TextStyles.SUCCESS)
    );

    private static String getStyledPrompt(String prompt, String Seperator) {
        return getStyledText(prompt + Seperator, TextStyles.PROMPT).toAnsi();
    }

    private static String getStyledPrompt(String prompt) {
        return getStyledPrompt(prompt, Messages.PROMPT_SEPARATOR);
    }

    //Separate methods for getting strings and getting keywords?
    public static CommandSet.Command getCommandInput(String prompt, Set<CommandSet.Command> options) {
        return CommandSet.getCommand(getInput(prompt, CommandSet.getKeywords(options)));
    }

    public static String getInput(String prompt, Set<String> options) {
        AttributedStringBuilder errorInvalid = getStyledText(Messages.INVALID_OPTION, TextStyles.ERROR)
                .append(getStyledText(" ", TextStyles.DEFAULT))
                .append(getStyledText(Messages.OPTIONS + options, TextStyles.DEFAULT));

        LineReaderBuilder builder = LineReaderBuilder.builder();
        builder.terminal(terminal);
        builder.completer(new ArgumentCompleter(new StringsCompleter(options), new NullCompleter()));
        LineReader reader = builder.build();
        reader.option(LineReader.Option.ERASE_LINE_ON_FINISH, true);
        String command;
        while (!options.contains(command = reader.readLine(getStyledPrompt(prompt)).trim())
                && options.size() > 0) {
            terminal.writer().println(errorInvalid.toAnsi());
            terminal.flush();
        }
        return command;
    }

    public static String getInput(String prompt) {
        Attributes test = new Attributes(terminal.getAttributes());
//        test.setLocalFlag(Attributes.LocalFlag.ECHO, false);
//        test.setLocalFlag(Attributes.LocalFlag.ECHOE, true);
////        test.setLocalFlag(Attributes.LocalFlag.ICANON, false);
////        test.setLocalFlag(Attributes.LocalFlag.ECHOCTL, true);
//
//        terminal.setAttributes(test);
//        try {
//            Integer test2 = terminal.reader().read();
//            return test2.toString();
//        }
//        catch (IOException e) {
//            //
//        }
//
//        return "no";
        LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();
        reader.option(LineReader.Option.ERASE_LINE_ON_FINISH, true);
        return reader.readLine(getStyledPrompt(prompt, Messages.INPUT_SEPARATOR)).trim();

    }

    public static void print(TextElement data) {
        print(List.of(data));
    }

    public static void println(TextElement data) {
        print(List.of(new TextElement(data.content + "\n", data.type)));
    }

    public static void println(List<TextElement> data) {
        List<TextElement> newData = new ArrayList<>();
        data.forEach(line -> {
            newData.add(new TextElement(line.content + "\n", line.type));
        });
        print(newData);
    }
    public static void print(List<TextElement> data) {
        AttributedStringBuilder string = new AttributedStringBuilder();

        for (TextElement element : data) {
            String content = element.content;
            if (element.type == TextElement.FormatType.DIVIDER) {
                content = Messages.DIVIDER;
            }
            string.append(getStyledText(content, typeToStyle.getOrDefault(element.type, TextStyles.DEFAULT)));
        }

        terminal.writer().print(string.toAnsi());
        terminal.flush();
    }

    private static AttributedStringBuilder getStyledText(String text, TextStyle style) {
        AttributedStringBuilder string = new AttributedStringBuilder();
        string.style(AttributedStyle.DEFAULT);
        if (style.fgColor != NO_COLOR) {
            string.style(string.style().foreground(style.fgColor));
        }
        if (style.bgColor != NO_COLOR) {
            string.style(string.style().background(style.bgColor));
        }
        if (style.bold) {
            string.style(string.style().bold());
        }
        if (style.italic) {
            string.style(string.style().italic());
        }
        if (style.underlined) {
            string.style(string.style().underline());
        }
        string.append(text);
        string.style(AttributedStyle.DEFAULT);
        return string;
    }

    public static void printTitle(String location, int rowsPre, int rowsMain, int rowsSub, String fallback) {
        try {
            File titleFile = new File(location);
            Scanner titleScanner = new Scanner(titleFile, "UTF-8");
            int maxLength = 0;
            while (titleScanner.hasNext()) {
                String line = titleScanner.nextLine();
                if (line.length() > maxLength) {
                    maxLength = line.length();
                }
            }

            if (maxLength > terminal.getWidth()) {
                throw new SizeLimitExceededException();
            }

            titleScanner = new Scanner(titleFile, "UTF-8");

            for (int i = 0; i < rowsPre; i++) {
                String line = titleScanner.nextLine();
                terminal.writer().println(getStyledText(line, TextStyles.GREYED).toAnsi());
            }
            for (int i = 0; i < rowsMain; i++) {
                String line = titleScanner.nextLine();
                terminal.writer().println(getStyledText(line, TextStyles.DEFAULT).toAnsi());
            }
            for (int i = 0; i < rowsSub; i++) {
                String line = titleScanner.nextLine();
                terminal.writer().println(getStyledText(line, TextStyles.GREYED).toAnsi());
            }
            titleScanner.close();
        }
        catch (FileNotFoundException | SizeLimitExceededException e) {
            terminal.writer().println(getStyledText("\n" + fallback + "\n", TextStyles.BOLD_UNDERLINED).toAnsi());
        }
        catch (NoSuchElementException e) {
            //TODO catch properly
        }

        terminal.flush();
    }

    private static final class TextStyle {
        public final int fgColor;
        public final int bgColor;
        public final boolean bold;
        public final boolean italic;
        public final boolean underlined;

        public TextStyle(int fgColor, int bgColor, boolean bold, boolean italic, boolean underlined) {
            this.fgColor = fgColor;
            this.bgColor = bgColor;
            this.bold = bold;
            this.italic = italic;
            this.underlined = underlined;
        }
    }
}
