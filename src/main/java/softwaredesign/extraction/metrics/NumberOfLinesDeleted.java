package softwaredesign.extraction.metrics;

import softwaredesign.extraction.Commit;
import softwaredesign.extraction.SingleData;

import java.util.List;

public class NumberOfLinesDeleted extends SingleData {

    public NumberOfLinesDeleted(List<Commit> commits) {
        super(
                "Number of Lines Deleted",
                "Returns the number of lines deleted across all files that were ever created in the project",
                extract(commits)
        );
    }

    private static Integer extract(List<Commit> commits) {
        int value = 0;
        for (Commit commit : commits) {
            value += commit.deletions;
        }
        return value;
    }
}
