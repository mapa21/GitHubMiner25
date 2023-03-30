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

    private static Session session = null;

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
            session = new Session();
        }
        catch (InstantiationException e) {
            exit(EXIT_CODE.SETUP_ERROR, e.getMessage());
        }

        session.run();

        exit(EXIT_CODE.SUCCESS);
    }


    public static void exit(EXIT_CODE status, String message) {
        if (status != EXIT_CODE.SUCCESS) UserConsole.print(new TextElement(message, FormatType.ERROR));
        exit(status);
    }

    public static void exit(EXIT_CODE status) {
        UserConsole.print(MessageSet.Misc.GOODBYE);
        if(session != null) session.saveAccounts();
        System.exit(status.ordinal());
    }

}
