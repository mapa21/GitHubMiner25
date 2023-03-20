package softwaredesign.extraction;

import softwaredesign.utilities.NameValue;

import java.util.List;

import static java.lang.Math.max;

public abstract class MultipleData extends Metric{
    private final List<NameValue<Integer>> data;

    protected MultipleData(String name, String description, List<NameValue<Integer>> data) {
        super(name, description);
        this.data = data;
    }

    @Override
    protected String contentToString() {
        int maxNameLength = 0;
        for (NameValue<Integer> dataElement : data) {
            maxNameLength = max(maxNameLength, dataElement.name.length());
        }

        StringBuilder stringContent = new StringBuilder();
        for (NameValue<Integer> element : data) {
            String stringSeparator = ": " + " ".repeat(maxNameLength - element.name.length());
            stringContent.append(element.name).append(stringSeparator).append(element.value).append("\n");
        }
        return stringContent.toString();
    }
}
