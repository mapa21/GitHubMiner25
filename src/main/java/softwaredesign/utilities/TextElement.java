package softwaredesign.utilities;

import softwaredesign.language.MessageSet;

public class TextElement {

    public enum FormatType {
        BODY,
        ERROR,
        SUCCESS,
        WAIT,
        PROMPT,
        COMMAND,
        PAGE_TITLE,
        HINT,
        DIVIDER,
        TITLE,
        HEADING
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
                    break;
                default:
            }
            if (preText.length() > 0) preText.append(' ');
            this.content = preText + content;
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
