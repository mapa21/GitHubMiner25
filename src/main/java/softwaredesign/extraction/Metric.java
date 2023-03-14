package softwaredesign.extraction;

import softwaredesign.Utilitites.TextElement;

public abstract class Metric {
    protected String description;
    protected String name;

    String getName() { //TODO: needed?
        return name;
    }

    protected abstract String contentToString();
    public TextElement[] getMetric() {
        //combine name, description, and contentToString
        return null;
    }
}
