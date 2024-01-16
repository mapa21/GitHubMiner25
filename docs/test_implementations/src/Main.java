import Metrics.ExtractionResult;
import Metrics.Extractor;
import Metrics.Metric;
import Metrics.NumberOfLinesAdded;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        Process process;
        String[] commands = {"git clone https://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@github.com/ComputerScienceEducation/co-lab.git", "git log"};
        String[] locations = {"res/", "res/co-lab/"};
        for (int i = 0; i < commands.length; i++) {
            System.out.println(commands[i] + " at " + locations[i]);
            process = Runtime.getRuntime().exec(commands[i], null, new File(locations[i]));
            List<String> output = getOutput(process);
            System.out.println(output);
        }

        Extractor extr = new Extractor();
        ExtractionResult test = extr.extractMetrics("that");
        System.out.println(test.metrics);

        Test test5 = new Test2();
        System.out.println(test5.getTest());
        System.out.println(test5.getRealTest());


    }
    public static void printResults(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = "";
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }
    public static List<String> getOutput(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        List<String> lines = new ArrayList<String>();
        String line = "";
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        return lines;
    }

}




