package softwaredesign.extraction.metrics;

import softwaredesign.extraction.Commit;
import softwaredesign.extraction.SingleData;

import java.util.List;

public class NumberOfLinesDeleted extends SingleData {
    protected String name = "Number of Lines Deleted";
    protected String description = "Returns the number of lines deleted across all files that were ever created in the project";

    public NumberOfLinesDeleted(List<Commit> commits) {
        int value = 0;
        for (Commit commit : commits) {
            value += commit.deletions;
        }
        this.value = value;
    }
}
