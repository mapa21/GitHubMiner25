package softwaredesign.extraction.metrics;

import softwaredesign.extraction.Commit;
import softwaredesign.extraction.SingleData;

import java.util.List;

public class NumberOfMerges extends SingleData {
    protected String name = "Number of Merges";
    protected String description = "Returns the number of branch merges";

    public NumberOfMerges(List<Commit> commits) {
        int value = 0;
        for (Commit commit : commits) {
            if (commit.isMerge == Boolean.TRUE) {
                value++;
            }
        }
        this.value = value;
    }
}
