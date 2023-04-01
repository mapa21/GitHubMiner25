package softwaredesign.extraction;

import softwaredesign.language.MessageSet;
import softwaredesign.utilities.TextElement;

import java.util.List;

public abstract class Metric {
    private final String description;
    private final String name;

    public String getCommand() {
        boolean nextUppercase = false;
        StringBuilder command = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            if (name.charAt(i) == ' ') {
                nextUppercase = true;
            }
            else {
                char thisChar = name.charAt(i);
                if (nextUppercase) thisChar = Character.toUpperCase(thisChar);
                else thisChar = Character.toLowerCase(thisChar);
                command.append(thisChar);
                nextUppercase = false;
            }
        }
        return command.toString();
    }

    protected Metric(String name, String description) {
        this.name = name;
        this.description = description;
    }
    protected abstract String contentToString();
    public final List<TextElement> getMetric() {
        return List.of(
                new TextElement(name, TextElement.FormatType.TITLE),
                new TextElement(description, TextElement.FormatType.HINT),
                MessageSet.Console.DIVIDER,
                new TextElement(contentToString(), TextElement.FormatType.STATISTIC)
        );
    }
}