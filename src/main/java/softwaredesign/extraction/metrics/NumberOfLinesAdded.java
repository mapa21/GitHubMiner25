package softwaredesign.extraction.metrics;

import softwaredesign.extraction.Commit;
import softwaredesign.extraction.SingleData;

import java.util.List;

public class NumberOfLinesAdded extends SingleData {
    protected String name = "Number of Lines Added";
    protected String description = "Returns the number of lines added across all files that were ever created in the project";

    public NumberOfLinesAdded(List<Commit> commits) {
        int value = 0;
        for (Commit commit : commits) {
            value += commit.insertions;
        }
        this.value = value;
    }
}
