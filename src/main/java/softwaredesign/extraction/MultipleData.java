package softwaredesign.extraction;

import softwaredesign.utilities.NameValue;

import java.util.List;

public abstract class MultipleData extends Metric{
    protected List<NameValue<Integer>> data;
//    protected MultipleData(List<NameValue<Integer>> data) { //not sure if this is needed
//        this.data = data;
//    }
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
