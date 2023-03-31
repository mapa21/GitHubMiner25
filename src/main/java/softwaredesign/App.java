package softwaredesign;

import softwaredesign.language.MessageSet;
import softwaredesign.utilities.TextElement;
import softwaredesign.utilities.TextElement.FormatType;

public class App {

    public enum EXIT_CODE {
        SUCCESS,
        UNKNOWN_ERROR,
        SETUP_ERROR,
    }

    /**
     * Application entry point
     * @param args Command line arguments
     */
    public static void main (String[] args) {

        for (String arg : args) {
            if (arg.equals("-d")) {
                UserConsole.setDebug(true);
                break;
            }
        }

        try {
            Session.create();
            Session.run();
        }
        catch (InstantiationException e) {
            exit(EXIT_CODE.SETUP_ERROR, e.getMessage());
        }
    }

    public static void exit(EXIT_CODE status, String message) {
        if (status != EXIT_CODE.SUCCESS) UserConsole.print(new TextElement(message, FormatType.ERROR));
        exit(status);
    }

    public static void exit(EXIT_CODE status) {
        UserConsole.print(MessageSet.Misc.GOODBYE);
        Session.save();
        System.exit(status.ordinal());
    }
}
