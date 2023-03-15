package Metrics;

import java.util.Arrays;

public class NumberOfLinesAdded extends SingleData {
    public NumberOfLinesAdded(Commit[] commits) {
        super(commits[0].value);
        System.out.println("Constructor running. argument: ");
        System.out.println(Arrays.toString(commits));
    }

    private String description = "This is the description";
}


