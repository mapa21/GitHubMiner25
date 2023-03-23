package softwaredesign.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.kohsuke.github.GHCompare;
import softwaredesign.Account;
import softwaredesign.UserConsole;
import softwaredesign.extraction.Metric;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.Collectors;

public final class FileManager {
    private static final String WIN = "Windows";
    private static final String MAC = "Mac";
    private static final String HOME_DIR = System.getProperty("user.home");
    private static final String APP_NAME = "GitHubMiner";
    private static final String OS = System.getProperty("os.name");
    public static final String SEPARATOR = getSystemSeparator();
    private static final String SOURCE = buildPath();
    private static final String JSON_FILE = "data.json";

    public static String getSource(){ return SOURCE;}

    private FileManager(){ throw new IllegalStateException("Utility class"); }

    private static String buildPath(){
        String appPath = HOME_DIR;
        if (OS.contains(WIN)){
            appPath += "\\AppData\\Roaming\\";
        } else if (OS.contains(MAC)){
            appPath += "/Library/Application Support/";
        }
        return appPath + APP_NAME + SEPARATOR;
    }

    private static String getSystemSeparator(){
        UserConsole.log(OS);
        UserConsole.log(String.valueOf(OS.contains(MAC)));
        String separator = "";
        if (OS.contains(WIN)) separator = "\\";
        else if (OS.contains(MAC)) {
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
        File folder = new File(SOURCE + path);
        UserConsole.log(SOURCE + path);
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
            return false;
        }
    }

    public static void saveAccounts(Map<String, Account> accounts) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        builder.registerTypeAdapter(Metric.class, new AbstractMetricsAdapter());
        Gson gson = builder.create();
        saveJsonStringToFile(gson.toJson(accounts.values().toArray(new Account[0])));
    }

    public static Map<String, Account> initAccounts() {
        String jsonString = FileManager.getJsonStringFromFile();

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        builder.registerTypeAdapter(Metric.class, new AbstractMetricsAdapter());
        Gson gson = builder.create();

        Account[] accountsArr = gson.fromJson(jsonString, Account[].class);

        if (accountsArr == null) return new TreeMap<>(); // empty map is returned

        return Arrays.stream(accountsArr).collect(Collectors.toMap(account -> account.name, account -> account, (a, b) -> b, TreeMap::new));
    }

    public static String getJsonStringFromFile() {
        String filePath = SOURCE + JSON_FILE;

        if (!(new File(filePath).exists())) {
            return "";
        }

        try (BufferedReader in = new BufferedReader(new FileReader(filePath))){
            StringBuilder jsonString = new StringBuilder();
            String tmp;

            while ((tmp = in.readLine()) != null ) {
                jsonString.append(tmp);
            }

            return jsonString.toString();
        } catch (Exception e) {
            UserConsole.log("File read has failed. Data is lost.");
            return "";
        }
    }

    public static void saveJsonStringToFile(String jsonString) {
        String filePath = SOURCE + JSON_FILE;
        try {
            Files.write(Paths.get(filePath), jsonString.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}