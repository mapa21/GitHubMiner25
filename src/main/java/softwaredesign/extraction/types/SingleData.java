package softwaredesign.extraction.types;

import softwaredesign.extraction.Metric;

public abstract class SingleData<T> extends Metric {
    private final T value;

    protected SingleData(String name, String description, T value) {
        super(name, description);
        this.value = value;
    }

    @Override
    protected final String contentToString() {
        return value.toString() + "\n";
    }
}
