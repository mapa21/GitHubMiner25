package softwaredesign.utilities;

import softwaredesign.UserConsole;

import java.io.File;

public final class FileManager {
    private static final String WIN = "Windows";
    private static final String MAC = "Mac";
    private static final String homeDir = System.getProperty("user.home");
    private static final String APP_NAME = "GitHubMiner";
    private static final String os = System.getProperty("os.name");
    public static final String SEPARATOR = getSystemSeparator();
    private static final String source = buildPath();

    public static String getAppName(){ return APP_NAME;}
    public static String getSource(){ return source;}

    private FileManager(){ throw new IllegalStateException("Utility class"); }

    private static String buildPath(){
        String appPath = homeDir;
        if (os.contains(WIN)){
            appPath += "\\AppData\\Roaming\\";
        } else if (os.contains(MAC)){
            appPath += "/Library/Application Support/";
        }
        return appPath + APP_NAME + SEPARATOR;
    }

    private static String getSystemSeparator(){
        UserConsole.log(os);
        UserConsole.log(String.valueOf(os.contains(MAC)));
        String separator = "";
        if (os.contains(WIN)) separator = "\\";
        else if (os.contains(MAC)) {
            separator = "/";
        }
        return separator;
    }

    public static Boolean initRootFolder() {
        return createFolder("");

    }

    //TODO: return values for existing folder?
    public static Boolean createFolder(String path){

        UserConsole.log("Separator = " + SEPARATOR);
        File folder = new File(source + path);
        UserConsole.log(source + path);
        UserConsole.log(folder.toString());
        try{
            if (folder.mkdir()) {
                System.out.println("Folder created: " + folder.getName() + " in " + folder.getAbsolutePath());
            } else {
                System.out.println("Folder already exists.");
            }
            return true;
        } catch (SecurityException e) {
            System.out.println("An error occurred.");
            //e.printStackTrace();
            return false;
        }
    }
}