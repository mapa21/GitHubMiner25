package softwaredesign.utilities;

import lombok.Getter;

import java.io.File;

public final class FileManager {
    private static final String WIN = "Windows";
    private static final String MAC = "Mac";
    private static final String homeDir = System.getProperty("user.home");
    private static final String appName = "GitHubMiner";
    private static final String os = System.getProperty("os.name");
    private static final String source = buildPath();
    private static final String separator = getSystemSeparator();

    public static String getAppName(){ return appName;}
    public static String getSource(){ return source;}

    private FileManager(){ throw new IllegalStateException("Utility class"); }

    private static String buildPath(){
        String appPath = homeDir;
        if (os.contains(WIN)){
            appPath += "\\AppData\\Roaming\\";
        } else if (os.contains(MAC)){
            appPath += "/Library/Application Support/";
        }
        return appPath;
    }

    private static String getSystemSeparator(){
        String separator = "";
        if (os.contains(WIN)) separator = "\\";
        else if (os.contains(MAC)) separator = "/";
        return separator;
    }

    //TODO: return values for existing folder?
    public static Boolean createFolder(String path){
        File folder = new File(source + getSystemSeparator() + path);
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
