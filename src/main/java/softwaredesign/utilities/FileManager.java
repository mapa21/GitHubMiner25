package softwaredesign.utilities;

import lombok.Getter;

import java.io.File;

public class FileManager {
    private static String homeDir;
    private static String appName;
    private static String source;

    public static String getAppName(){ return appName;}
    public static String getSource(){ return source;}

    private FileManager(){
        this.homeDir = System.getProperty("user.home");
        this.appName = "GitHubMiner";
        this.source = homeDir + appName;
    }

    //TODO: return values for existing folder?
    public static Boolean createFolder(String path){
        File folder = new File(homeDir + "\\" + path);
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
