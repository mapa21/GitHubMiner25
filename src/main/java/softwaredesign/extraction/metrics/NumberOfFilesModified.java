package softwaredesign.extraction.metrics;

import softwaredesign.extraction.Commit;
import softwaredesign.extraction.SingleData;

import java.util.List;

public class NumberOfFilesModified extends SingleData {

    public NumberOfFilesModified(List<Commit> commits) {
        super(
                "Number of Files Modified",
                "This is number of file modification instances throughout the project",
                extract(commits)
        );
    }

    private static Integer extract(List<Commit> commits) {
        int value = 0;
        for (Commit commit : commits) {
            value += commit.fileStats.getFilesModified();
        }
        return value;
    }
}
