package softwaredesign;

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
import org.jline.utils.InfoCmp;
import softwaredesign.language.CommandSet;
import softwaredesign.language.MessageSet;
import softwaredesign.utilities.TextElement;
import softwaredesign.utilities.TextElement.FormatType;

import javax.naming.SizeLimitExceededException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static java.util.Map.entry;

public class UserConsole {

    private UserConsole() {}
    private static Terminal terminal;

    static {
        try {
            terminal = TerminalBuilder.builder().encoding("UTF-8").build();
            terminal.trackMouse(Terminal.MouseTracking.Off);
        } catch (IOException e) {
            System.exit(1);
        }
    }

    private static final int NO_COLOR = -1;

    private final static TextStyle DEFAULT_STYLE = new TextStyle(NO_COLOR, NO_COLOR, false ,false, false);
    private final static TextStyle GREYED_STYLE = new TextStyle(NO_COLOR, NO_COLOR, false ,false, false);

    private static Map<FormatType, TextStyle> typeToStyle = Map.ofEntries(
            entry(FormatType.TITLE, new TextStyle(true, false, true)),
            entry(FormatType.HEADING, new TextStyle(AttributedStyle.BLUE, true, false, true)),
            entry(FormatType.BODY, DEFAULT_STYLE),
            entry(FormatType.STATISTIC, DEFAULT_STYLE),
            entry(FormatType.DIVIDER, new TextStyle(AttributedStyle.BRIGHT, false, false, false)),
            entry(FormatType.ERROR, new TextStyle(AttributedStyle.RED, true, false, false)),
            entry(FormatType.SUCCESS, new TextStyle(AttributedStyle.GREEN, true, false ,false)),
            entry(FormatType.PROMPT, new TextStyle(AttributedStyle.BRIGHT, false, false, false)),
            entry(FormatType.WAIT, new TextStyle(AttributedStyle.YELLOW, false, true, false))
    );

    private static String getStyledPrompt(String prompt, String Seperator) {
        return getStyledText(prompt + Seperator, typeToStyle.getOrDefault(FormatType.PROMPT, DEFAULT_STYLE)).toAnsi();
    }

    private static String getStyledPrompt(String prompt) {
        return getStyledPrompt(prompt, MessageSet.Console.PROMPT_SEPARATOR);
    }

    public static CommandSet.Command getCommandInput(String prompt, Set<CommandSet.Command> options) {
        return CommandSet.getCommand(getInput(prompt, CommandSet.getKeywords(options)));
    }

    public static void log(String message) {
        terminal.writer().println(MessageSet.Icons.ICON_INFO + " " + message);
    }

    public static String getInput(String prompt, Set<String> options) {
        List<TextElement> errorInvalid = List.of(
                new TextElement(MessageSet.Console.INVALID_OPTION, FormatType.ERROR),
                new TextElement(" "),
                new TextElement(MessageSet.Console.OPTIONS + options + "\n")
        );

        LineReaderBuilder builder = LineReaderBuilder.builder();
        builder.terminal(terminal);
        builder.completer(new ArgumentCompleter(new StringsCompleter(options), new NullCompleter()));
        LineReader reader = builder.build();
        reader.option(LineReader.Option.ERASE_LINE_ON_FINISH, true);
            String command;
        while (!options.contains(command = reader.readLine(getStyledPrompt(prompt)).trim())
                && options.size() > 0) {
            print(errorInvalid);
            terminal.flush();
        }
        return command;
    }

    public static void printInputResult(String prompt, String result) {
        println(new TextElement(prompt + MessageSet.Console.INPUT_SET + result));
    }

    public static String getInput(String prompt, boolean allowSpaces, boolean allowEmpty) {
        LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();
        reader.option(LineReader.Option.ERASE_LINE_ON_FINISH, true);
        reader.setAutosuggestion(LineReader.SuggestionType.COMPLETER);
        String input;
        while (true) {
            input = reader.readLine(getStyledPrompt(prompt, MessageSet.Console.INPUT_SEPARATOR)).trim();
            if (!allowSpaces && input.contains(" ")) {
                println(new TextElement(prompt + MessageSet.Console.CONTAINS_SPACES_ERROR, FormatType.ERROR));
            }
            else if (!allowEmpty && input.length() == 0) {
                println(new TextElement(prompt + MessageSet.Console.IS_EMPTY_ERROR, FormatType.ERROR));
            }
            else break;
        }

        return input;
    }

    public static String getPassword(String prompt) {
        terminal.writer().print(getStyledPrompt(prompt, MessageSet.Console.INPUT_SEPARATOR));
        terminal.flush();

        Attributes attr = terminal.getAttributes();
        Attributes prevAttr = new Attributes(attr);
        attr.setLocalFlag(Attributes.LocalFlag.ECHO, false);
        attr.setLocalFlag(Attributes.LocalFlag.ECHOK, false);
        terminal.setAttributes(attr);

        int inChar = 0;
        StringBuilder readString = new StringBuilder();
        try {
            do {
                inChar = terminal.reader().read();
                readString.append((char) inChar);

            } while (inChar != '\n' && inChar != '\r');

        }
        catch (IOException e) {
            readString.append('\n');
        }

        terminal.setAttributes(prevAttr);
        terminal.writer().print('\r');
        if (readString.length() < 2) return readString.toString();
        else return readString.substring(0, readString.length() - 2).toString();

    }

    public static void print(String data) {
        print(new TextElement(data, FormatType.BODY));
    }

    public static void print(TextElement data) {
        print(List.of(data));
    }

    public static void println(TextElement data) {
        print(List.of(new TextElement(data.content + "\n", data.type, false)));
    }

    public static void println(List<TextElement> data) {
        List<TextElement> newData = new ArrayList<>();
        data.forEach(line -> {
            newData.add(new TextElement(line.content + "\n", line.type, false));
        });
        print(newData);
    }

    public static void print(List<TextElement> data) {
        AttributedStringBuilder string = new AttributedStringBuilder();

        for (TextElement element : data) {
            String content = element.content;
            if (element.type == TextElement.FormatType.DIVIDER) {
                content = MessageSet.Console.DIVIDER;
            }
            string.append(getStyledText(content, typeToStyle.getOrDefault(element.type, DEFAULT_STYLE)));
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
        terminal.puts(InfoCmp.Capability.clear_screen);

        try {
            InputStream input = ClassLoader.getSystemResourceAsStream(location);
            if (input == null) throw (new FileNotFoundException("Title file (" + location + ") not Found"));
            Scanner titleScanner = new Scanner(input, StandardCharsets.UTF_8);
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
            //TODO: Lennart make nicer
            input = ClassLoader.getSystemResourceAsStream(location);
            if (input == null) throw (new FileNotFoundException("Title file (" + location + ") not Found"));
            titleScanner = new Scanner(input, "UTF-8");

            for (int i = 0; i < rowsPre; i++) {
                String line = titleScanner.nextLine();
                terminal.writer().println(getStyledText(line, GREYED_STYLE).toAnsi());
            }
            for (int i = 0; i < rowsMain; i++) {
                String line = titleScanner.nextLine();
                terminal.writer().println(getStyledText(line, DEFAULT_STYLE).toAnsi());
            }
            for (int i = 0; i < rowsSub; i++) {
                String line = titleScanner.nextLine();
                terminal.writer().println(getStyledText(line, GREYED_STYLE).toAnsi());
            }
            titleScanner.close();
        }
        catch (FileNotFoundException | SizeLimitExceededException e) {
            terminal.writer().println(getStyledText("\n" + fallback + "\n", typeToStyle.getOrDefault(FormatType.TITLE, DEFAULT_STYLE)).toAnsi());
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

        public TextStyle(int fgColor, boolean bold, boolean italic, boolean underlined) {
            this(fgColor, NO_COLOR, bold, italic, underlined);
        }

        public TextStyle(boolean bold, boolean italic, boolean underlined) {
            this(NO_COLOR, NO_COLOR, bold, italic, underlined);
        }
    }
}
