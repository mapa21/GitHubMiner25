package softwaredesign.extraction.metrics;

import softwaredesign.extraction.Commit;
import softwaredesign.extraction.SingleData;

import java.util.List;

public class NumberOfMerges extends SingleData {

    public NumberOfMerges(List<Commit> commits) {
        super(
                "Number of Merges",
                "This is the number of branch merges",
                extract(commits)
        );
    }

    private static Integer extract(List<Commit> commits) {
        int value = 0;
        for (Commit commit : commits) {
            if (commit.isMerge == Boolean.TRUE) {
                value++;
            }
        }
        return value;
    }
}
