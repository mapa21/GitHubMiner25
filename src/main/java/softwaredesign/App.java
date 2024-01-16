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
        exit(EXIT_CODE.SUCCESS);
    }

    /**
     * Exits the application in a controlled way with the given status. If the status is not SUCCESS, the given message will be printed. Saves the state of the active session if one exists.
     * @param status Exit code
     * @param message Error message
     */
    public static void exit(EXIT_CODE status, String message) {
        if (status != EXIT_CODE.SUCCESS) UserConsole.print(new TextElement(message, FormatType.ERROR));
        exit(status);
    }

    /**
     * Exits the application in a controlled way with the given status. Saves the state of the active session if one exists.
     * @param status Exit code
     */
    public static void exit(EXIT_CODE status) {
        UserConsole.print(MessageSet.Misc.GOODBYE);
        Session.save();
        System.exit(status.ordinal());
    }
}
