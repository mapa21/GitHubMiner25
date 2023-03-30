package softwaredesign.utilities;

import softwaredesign.UserConsole;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CommandLineManager {
    private CommandLineManager() {throw new IllegalStateException("Static Class");}

    /** Executes a terminal command in a given location and returns the output
     * @param command Terminal command to execute
     * @param path Location where the command should be executed
     * @return List of output lines produced by the command
     */
    public static List<String> runCommand(String command, String path) {
        UserConsole.log("Running command \"" + command + "\" at \"" + path + "\"" );
        Process process;
        try {
            process = Runtime.getRuntime().exec(command, null, new File(path));
            assert process != null;
            return getOutput(process);
        }
        catch (IOException e) {
            UserConsole.log("Running command failed due to IOException: " + e.getMessage());
            Thread.currentThread().interrupt();
            return List.of();
        }
    }

    private static List<String> getOutput(Process process) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        List<String> lines = new ArrayList<>();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        catch (IOException e) {
            UserConsole.log(e.getMessage());
        }
        return lines;
    }
}
