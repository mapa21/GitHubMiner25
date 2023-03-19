package softwaredesign.extraction;

import softwaredesign.utilities.NameValue;

import java.util.List;

public abstract class MultipleData extends Metric{
    private final List<NameValue<Integer>> data;

    protected MultipleData(String name, String description, List<NameValue<Integer>> data) {
        super(name, description);
        this.data = data;
    }

    @Override
    protected String contentToString() {
        //convert data array to string
        return null;
    }

    // TODO: remove after testing
    public List<NameValue<Integer>> getValue() {
        return data;
    }
}
