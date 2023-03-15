package Metrics;


public abstract class Metric {
    protected String description;
    protected String name;
    @Override
    public abstract String toString();
}
