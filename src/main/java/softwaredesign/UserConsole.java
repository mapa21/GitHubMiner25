package softwaredesign;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.NullCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import softwaredesign.utilities.TextElement;

import javax.naming.SizeLimitExceededException;
import java.io.*;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static java.util.Map.entry;

public class UserConsole {

    private UserConsole() {}
    private static Terminal terminal;

    static {
        try {
            terminal = TerminalBuilder.builder().encoding("UTF-8").build();
        } catch (IOException e) {
            //TODO: handle exception;
        }
    }

    private static final int NO_COLOR = -1;

    private static class Messages {
        static final String INVALID_OPTION = "Invalid Option.";
        static final String OPTIONS = "Options are: ";
        static final String DIVIDER = "---------------";
    }

    private static class TextStyles {
        static final TextStyle ERROR = new TextStyle(AttributedStyle.RED, NO_COLOR, true, false, false);
        static final TextStyle SUCCESS = new TextStyle(AttributedStyle.GREEN, NO_COLOR, true, false ,false);
        static final TextStyle DEFAULT = new TextStyle(NO_COLOR, NO_COLOR, false ,false, false);
        static final TextStyle HEADING = new TextStyle(AttributedStyle.BLUE, NO_COLOR, true, false, true);
        static final TextStyle GREYED = new TextStyle(AttributedStyle.BRIGHT, NO_COLOR, false, false, false);
        static final TextStyle BOLD_UNDERLINED = new TextStyle(NO_COLOR, NO_COLOR, true, false, true);
    }

    private static Map<TextElement.FormatType, TextStyle> typeToStyle = Map.ofEntries(
            entry(TextElement.FormatType.HEADING, TextStyles.HEADING),
            entry(TextElement.FormatType.BODY, TextStyles.DEFAULT),
            entry(TextElement.FormatType.STATISTIC, TextStyles.DEFAULT),
            entry(TextElement.FormatType.DIVIDER, TextStyles.GREYED)
    );

    public static String getInput(String prompt, Set<String> options) {

        AttributedStringBuilder errorInvalid = getStyledText(Messages.INVALID_OPTION, TextStyles.ERROR)
                .append(getStyledText(" ", TextStyles.DEFAULT))
                .append(getStyledText(Messages.OPTIONS + options, TextStyles.DEFAULT));

        LineReaderBuilder builder = org.jline.reader.LineReaderBuilder.builder();
        builder.terminal(terminal);
        builder.completer(new ArgumentCompleter(new StringsCompleter(options), new NullCompleter()));
        LineReader reader = builder.build();

        String line;
        while (!options.contains((line = reader.readLine(prompt + " > ")).trim())) {
            terminal.writer().println(errorInvalid.toAnsi());
            terminal.flush();
        }
        return line;
    }

    public static void print(List<TextElement> data) {
        AttributedStringBuilder string = new AttributedStringBuilder();

        for (TextElement element : data) {
            String content = element.content;
            if (element.type == TextElement.FormatType.DIVIDER) {
                content = Messages.DIVIDER;
            }
            string.append(getStyledText(content + "\n", typeToStyle.get(element.type)));
        }

        System.out.println(terminal);

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
//        string.style(AttributedStyle.DEFAULT);
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
            System.out.println("Error: " + e);
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
