package softwaredesign.extraction.metrics;

import softwaredesign.utilities.NameValue;
import softwaredesign.extraction.Commit;
import softwaredesign.extraction.MultipleData;

public class NumberOfLinesAdded extends MultipleData {
    protected String name = "Number of Lines Added";
    protected String description = "Sample Description";

    protected NumberOfLinesAdded(Commit[] commits) {
        NameValue<Integer>[] data = null;
        //implementation
        this.data = data;
    }
}
