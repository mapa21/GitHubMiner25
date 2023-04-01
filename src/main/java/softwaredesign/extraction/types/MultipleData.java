package softwaredesign.extraction.types;

import softwaredesign.extraction.Metric;
import softwaredesign.utilities.NameValue;

import java.util.List;

import static java.lang.Math.max;

public abstract class MultipleData<T extends Comparable<T>> extends Metric {
    private final List<NameValue<T>> data;

    protected MultipleData(String name, String description, List<NameValue<T>> data) {
        super(name, description);
        this.data = data;
    }

    @Override
    protected final String contentToString() {
        int maxNameLength = 0;
        for (NameValue<?> dataElement : data) {
            maxNameLength = max(maxNameLength, dataElement.name.length());
        }

        StringBuilder stringContent = new StringBuilder();
        for (NameValue<?> element : data) {
            String stringSeparator = ": " + " ".repeat(maxNameLength - element.name.length());
            stringContent.append(element.name).append(stringSeparator).append(element.value.toString()).append("\n");
        }
        return stringContent.toString();
    }
}
