package softwaredesign.extraction;

public abstract class SingleData extends Metric {
    private final Integer value;

    protected SingleData(String name, String description, Integer value) {
        super(name, description);
        this.value = value;
    }

    @Override
    protected String contentToString() {
        return value.toString() + "\n";
    }
}
