package softwaredesign.extraction;

import softwaredesign.utilities.TextElement;

import java.util.List;

public abstract class Metric {
    private final String description;
    private final String name;

    String getName() { //TODO: needed?
        return name;
    }
    protected Metric(String name, String description) {
        this.name = name;
        this.description = description;
    }
    protected abstract String contentToString();
    public List<TextElement> getMetric() {
        //combine name, description, and contentToString
        return List.of(new TextElement(this.name + " " + this.description));
    }
}
