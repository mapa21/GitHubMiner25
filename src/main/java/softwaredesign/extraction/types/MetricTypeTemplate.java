package softwaredesign.extraction.types;

import softwaredesign.extraction.Metric;

@SuppressWarnings("unused")
public abstract class MetricTypeTemplate extends Metric {

    private final Object data;

    protected MetricTypeTemplate(String name, String description, Object data) {
        super(name, description);
        this.data = data;
    }

    @Override
    protected final String contentToString() {

        // Implement creation of string representation of the metric type here.

        return data.toString();
    }
}
