package softwaredesign.utilities;

import softwaredesign.language.MessageSet;

public class TextElement {

    public enum FormatType {
        HEADING,
        BODY,
        STATISTIC,
        DIVIDER,
        COMMAND,
        HINT,
        ERROR,
        SUCCESS,
        PROMPT,
        PAGE_TITLE,
        WAIT,
        TITLE
    }

    public final String content;
    public final FormatType type;


    public TextElement(String content, FormatType type, boolean addStyling) {
        if (addStyling) {
            StringBuilder preText = new StringBuilder();
            switch (type) {
                case ERROR:
                    preText.append(MessageSet.Icons.ICON_CROSS);
                    break;
                case SUCCESS:
                    preText.append(MessageSet.Icons.ICON_CHECK);
                    break;
                case WAIT:
                    preText.append(MessageSet.Icons.ICON_CLOCK);
                default:

            }
            if (preText.length() > 0) preText.append(' ');
            this.content = preText.toString() + content;
        }
        else this.content = content;
        this.type = type;

    }

    public TextElement(String content, FormatType type) {
         this(content, type, true);
    }

    public TextElement(String content) {
        this(content, FormatType.BODY);
    }

}
