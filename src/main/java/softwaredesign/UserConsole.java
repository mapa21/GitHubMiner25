package softwaredesign;

import lombok.Setter;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
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
import softwaredesign.utilities.InputCancelledException;
import softwaredesign.utilities.TextElement;
import softwaredesign.utilities.TextElement.FormatType;

import javax.naming.SizeLimitExceededException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static java.util.Map.entry;

public class UserConsole {
    private UserConsole() {throw new IllegalStateException("Static Class");}

    private static Terminal terminal;
    @Setter
    private static boolean debug = false;

    static {
        try {
            terminal = TerminalBuilder.builder().encoding("UTF-8").build();
            terminal.trackMouse(Terminal.MouseTracking.Off);
        } catch (IOException e) {
            System.exit(1);
        }
    }

    private static final int NO_COLOR = -1;

    private static final TextStyle DEFAULT_STYLE = new TextStyle(false, false, false);
    private static final TextStyle GREYED_STYLE = new TextStyle(AttributedStyle.BRIGHT, NO_COLOR, false, false, false);

    private static Map<FormatType, TextStyle> typeToStyle = Map.ofEntries(
            entry(FormatType.PAGE_TITLE, new TextStyle(true, false, true)),
            entry(FormatType.TITLE, new TextStyle(true, false, true)),
            entry(FormatType.HEADING, new TextStyle(AttributedStyle.MAGENTA, true, false, true)),
            entry(FormatType.HINT, new TextStyle(false, true, false)),
            entry(FormatType.COMMAND, new TextStyle(AttributedStyle.CYAN, false, false, true)),
            entry(FormatType.DIVIDER, new TextStyle(AttributedStyle.BRIGHT, false, false, false)),
            entry(FormatType.ERROR, new TextStyle(AttributedStyle.RED, true, false, false)),
            entry(FormatType.SUCCESS, new TextStyle(AttributedStyle.GREEN, true, false, false)),
            entry(FormatType.PROMPT, new TextStyle(AttributedStyle.BRIGHT, false, false, false)),
            entry(FormatType.WAIT, new TextStyle(AttributedStyle.YELLOW, false, true, false))
    );

    /**
     * Prints the given string if debug is active.
     *
     * @param message String to print
     */
    public static void log(String message) {
        if (debug) {
            terminal.writer().println(MessageSet.Icons.ICON_INFO + " " + message);
            terminal.writer().flush();
        }
    }

    /**
     * Prints the given TextElement. No newline is added.
     *
     * @param data TextElement to print
     */
    public static void print(TextElement data) {
        print(List.of(data));
    }

    /**
     * Prints the given list of TextElements, no newlines are added.
     *
     * @param data List of TextElements to print
     */
    public static void print(List<TextElement> data) {
        AttributedStringBuilder string = new AttributedStringBuilder();
        data.forEach(e ->
            string.append(getStyledText(e.content, getStyle(e.type)))
        );
        terminal.writer().print(string.toAnsi());
        terminal.flush();
    }


    /**
     * Prints the given TextElement and terminates with a newline.
     *
     * @param data TextElement to print
     */
    public static void println(TextElement data) {
        print(List.of(new TextElement(data.content + "\n", data.type, false)));
    }

    /**
     * Prints the given list of TextElements with a newline after each element.
     *
     * @param data List of TextElements to print
     */
    public static void println(List<TextElement> data) {
        List<TextElement> newData = new ArrayList<>();
        data.forEach(line -> newData.add(
                new TextElement(line.content + "\n", line.type, false))
        );
        print(newData);
    }

    /**
     * Simple utility method to print the result of previously attained input with the INPUT_SET separator.
     *
     * @param prompt Prompt to print
     * @param result Result to print
     */
    public static void printInputResult(String prompt, String result) {
        println(new TextElement(prompt + MessageSet.Console.INPUT_SET + result));
    }

    public static void clearScreen() {
        terminal.puts(InfoCmp.Capability.clear_screen);
        terminal.writer().flush();
    }

    /**
     * Prints the prompt, waits for input, and returns the given input after a newline
     * or carriage return character has been received. Leading and trailing whitespace is trimmed.
     * If allowSpaces or allowEmpty are false, input containing spaces or empty input will not be accepted respectively.
     *
     * @param prompt      Prompt to print
     * @param allowSpaces Specifier for whether spaces are allowed in the input
     * @param allowEmpty  Specifier for whether empty input is allows
     * @return Entered input trimmed of leading and trailing whitespace
     * @throws InputCancelledException
     */
    public static String getInput(String prompt, boolean allowSpaces, boolean allowEmpty) throws InputCancelledException {
        try {
            LineReader reader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .build()
                    .option(LineReader.Option.ERASE_LINE_ON_FINISH, true);
            String input;
            while (true) {
                input = reader.readLine(getStyledPrompt(prompt, MessageSet.Console.INPUT_SEPARATOR)).trim();
                if (!allowSpaces && input.contains(" ")) {
                    print(MessageSet.Console.getNoSpacesError(prompt));
                } else if (!allowEmpty && input.length() == 0) {
                    print(MessageSet.Console.getNonEmptyError(prompt));
                } else break;
            }

            return input;
        } catch (UserInterruptException e) {
            throw new InputCancelledException();
        }
    }

    /**
     * Prints the prompt, waits for input, and returns the given input.
     * If a non-empty set of options is given, only a choice of those options will be accepted and returned.
     *
     * @param prompt  Prompt to print
     * @param options Set of options (can be empty)
     * @return Entered String
     * @throws InputCancelledException
     */
    public static String getInput(String prompt, Set<String> options) throws InputCancelledException {
        try {
            LineReader reader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .completer(new ArgumentCompleter(new StringsCompleter(options), new NullCompleter()))
                    .build().option(LineReader.Option.ERASE_LINE_ON_FINISH, true);

            String command;
            while (!options.contains(command = reader.readLine(getStyledPrompt(prompt)).trim())
                    && !options.isEmpty()) {
                print(MessageSet.Console.getInvalidOptionText(options));
                terminal.flush();
            }
            return command;
        } catch (UserInterruptException e) {
            throw (new InputCancelledException());
        }

    }

    /**
     * Similar to the getInput method, but takes a set of commands, and returns the chosen command
     *
     * @param prompt  Prompt to print
     * @param options Set of options
     * @return Entered option
     * @throws InputCancelledException
     */
    public static CommandSet.Command getCommandInput(String prompt, Set<CommandSet.Command> options) throws InputCancelledException {
        if (options == null || options.isEmpty()) return CommandSet.Command.INVALID;
        return CommandSet.getCommand(getInput(prompt, CommandSet.getKeywords(options)));
    }

    /**
     * Prints the prompt, waits for input, and returns the given input after a newline
     * or carriage return character has been received.
     * The entered keys are not shown, making this method mainly useful for passwords.
     *
     * @param prompt Prompt to print
     * @return String of entered characters
     */
    public static String getHiddenInput(String prompt) {
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

        } catch (IOException e) {
            readString.append('\n');
        }

        terminal.setAttributes(prevAttr);
        clearLine();
        if (readString.length() < 2) return readString.toString();
        else return readString.substring(0, readString.length() - 2);
    }

    /**
     * Prints the title located at the given location with different styling for the pre, main, and sub part of the title.
     * The length of the parts needs to be specified in the first 3 lines of the file.
     * If the title can't be found or is too wide for the terminal window, the fallback string will be printed.
     *
     * @param location Path of the title file (relative to the resources folder)
     * @param fallback Fallback string to print
     */
    public static void printTitle(String location, String fallback) {
        terminal.puts(InfoCmp.Capability.clear_screen);

        try (InputStream input = ClassLoader.getSystemResourceAsStream(location);) {
            if (input == null) throw (new FileNotFoundException("Title file (" + location + ") not Found"));
            List<String> lines = new ArrayList<>();
            int maxLength = 0;
            Scanner inputScanner = new Scanner(input, StandardCharsets.UTF_8);

            while (inputScanner.hasNext()) {
                String line = inputScanner.nextLine();
                if (line.length() > maxLength) maxLength = line.length();
                lines.add(line);
            }

            if (maxLength > terminal.getWidth()) {
                throw new SizeLimitExceededException();
            }

            int rowsPre = Integer.parseInt(lines.get(0));
            int rowsMain = Integer.parseInt(lines.get(1));
            int rowsSub = Integer.parseInt(lines.get(2));

            lines = lines.subList(3, lines.size());

            for (String line : lines.subList(0, rowsPre)) {
                terminal.writer().println(getStyledText(line, GREYED_STYLE).toAnsi());
            }
            lines = lines.subList(rowsPre, lines.size());
            for (String line : lines.subList(0, rowsMain)) {
                terminal.writer().println(getStyledText(line, DEFAULT_STYLE).toAnsi());
            }
            lines = lines.subList(rowsMain, lines.size());
            for (String line : lines.subList(0, rowsSub)) {
                terminal.writer().println(getStyledText(line, GREYED_STYLE).toAnsi());
            }
        } catch (IOException | SizeLimitExceededException | NumberFormatException e) {
            terminal.writer().println(getStyledText("\n" + fallback + "\n", getStyle(FormatType.TITLE)).toAnsi());
        }
        terminal.flush();
    }

    // INTERNAL HELPER METHODS:

    private static TextStyle getStyle(FormatType type) {
        return typeToStyle.getOrDefault(type, DEFAULT_STYLE);
    }

    private static void clearLine() {
        terminal.writer().print('\r');
        for (int i = 0; i < terminal.getWidth(); i++) {
            terminal.writer().print(' ');
        }
        terminal.writer().print('\r');
        terminal.writer().flush();
    }

    private static String getStyledPrompt(String prompt, String separator) {
        return getStyledText(prompt + separator, getStyle(FormatType.PROMPT)).toAnsi();
    }

    private static String getStyledPrompt(String prompt) {
        return getStyledPrompt(prompt, MessageSet.Console.PROMPT_SEPARATOR);
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
