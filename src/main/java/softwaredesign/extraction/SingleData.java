package softwaredesign.extraction;

public abstract class SingleData extends Metric {
    protected Integer value;
//    protected SingleData(Integer value) { //not sure if this is needed
//        this.value = value;
//    }
    @Override
    protected String contentToString() {

        return null;
    }

    // TODO: remove after testing
    public Integer getValue() {
        return value;
    }
}
