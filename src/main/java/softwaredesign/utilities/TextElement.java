package softwaredesign.utilities;

public class TextElement {

    public enum FormatType {
        HEADING,
        BODY,
        STATISTIC,
        DIVIDER,
        HINT,
        ERROR,
        SUCCESS,
        TITLE
    }

    public final String content;
    public final FormatType type;

    public TextElement(String content, FormatType type) {
        this.content = content;
        this.type = type;
    }
}
