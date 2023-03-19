package softwaredesign.extraction.metrics;

import softwaredesign.extraction.Commit;
import softwaredesign.extraction.SingleData;

import java.util.List;

public class NumberOfFilesModified extends SingleData {

    public NumberOfFilesModified(List<Commit> commits) {
        super(
                "Number of Files Modified",
                "Returns the number of file renaming instances throughout the project",
                extract(commits)
        );
    }

    private static Integer extract(List<Commit> commits) {
        int value = 0;
        for (Commit commit : commits) {
            value += commit.filesModified;
        }
        return value;
    }
}
