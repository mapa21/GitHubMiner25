package Metrics;

public abstract class SingleData extends Metric {
    protected Integer value;

    protected SingleData(Integer value) {
        this.value = value;
    }

    public String toString() {
        return value.toString();
    }
}
