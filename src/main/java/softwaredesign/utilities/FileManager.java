package softwaredesign.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import softwaredesign.Account;
import softwaredesign.UserConsole;
import softwaredesign.extraction.AbstractMetricsAdapter;
import softwaredesign.extraction.Metric;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.TreeMap;

public final class FileManager {
    private static final String WIN = "Windows";
    private static final String MAC = "Mac";
    private static final String HOME_DIR = System.getProperty("user.home");
    private static final String APP_NAME = "GitHubMiner";
    private static final String OS = System.getProperty("os.name");
    public static final String SEPARATOR = getSystemSeparator();
    public static final String SOURCE = buildPath(); // /Library/Application Support/GitHubMiner/
    private static final String JSON_FILE = "data.json";

    private FileManager(){ throw new IllegalStateException("Static class"); }

    private static String buildPath(){
        String appPath = HOME_DIR;
        if (OS.contains(WIN)){
            appPath += "\\AppData\\Roaming\\";
        } else if (OS.contains(MAC)){
            appPath += "/Library/Application Support/";
        } else if (OS.contains("Linux")){
            appPath = "/tmp/"; // it's not permanent anyway. Not adding it in user dir is intentional (missing "+")
        }
        return appPath + APP_NAME + SEPARATOR;
    }

    private static String getSystemSeparator(){
        String separator = "";
        if (OS.contains(WIN)) separator = "\\";
        else separator = "/";

        return separator;
    }

    public static void initRootFolder() throws IOException {
        createFolder("");
    }

    public static void createFolder(String path) throws IOException {
        File folder = new File(SOURCE + path);
        try{
            if (folder.mkdir()) {
                UserConsole.log("Folder created: " + folder.getName() + " in " + folder.getAbsolutePath());
            } else {
                UserConsole.log("Folder already exists.");
            }
        } catch (SecurityException e) {
            throw new IOException("Can't create folder \"" + SOURCE + path + "\" - " + e.getMessage());
        }
    }

    public static void saveAccounts(Map<String, Account> accounts) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        builder.registerTypeAdapter(Metric.class, new AbstractMetricsAdapter());
        Gson gson = builder.create();
        saveJsonStringToFile(gson.toJson(accounts.values().toArray(new Account[0])));
    }

    public static Map<String, Account> loadAccounts() {
        String jsonString = FileManager.getJsonStringFromFile();

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        builder.registerTypeAdapter(Metric.class, new AbstractMetricsAdapter());
        Gson gson = builder.create();

        Account[] accountsArr = gson.fromJson(jsonString, Account[].class);

        if (accountsArr == null) return new TreeMap<>(); // empty map is returned
        Map<String, Account> accounts = new TreeMap<>();

        for (Account account : accountsArr) {
            if (account != null) accounts.put(account.name, account);
        }
        return accounts;
    }

    public static void deleteFolder(String path) throws IOException {
        try {
            FileUtils.deleteDirectory(new File(SOURCE + path));
        }
        catch (IOException e) {
            throw new IOException("Could not delete folder \"" + path + "\" - " + e.getMessage());
        }
    }

    private static String getJsonStringFromFile() {
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

    private static void saveJsonStringToFile(String jsonString) {
        String filePath = SOURCE + JSON_FILE;
        try {
            Files.write(Paths.get(filePath), jsonString.getBytes());
        } catch (IOException e) {
            UserConsole.log("Error while writing to json: " + e.getMessage());
        }
    }
}