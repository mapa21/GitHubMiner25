package softwaredesign.extraction.metrics;

import softwaredesign.extraction.Commit;
import softwaredesign.extraction.SingleData;

import java.util.List;

public class NumberOfFilesModified extends SingleData {
    protected String name = "Number of Files Modified";
    protected String description = "Returns the number of file renaming instances throughout the project";

    public NumberOfFilesModified(List<Commit> commits) {
        int value = 0;
        for (Commit commit : commits) {
            value += commit.filesModified;
        }
        this.value = value;
    }
}
