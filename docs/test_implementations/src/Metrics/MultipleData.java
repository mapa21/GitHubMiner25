package Metrics;
import Utilities.NameValue;

public class MultipleData extends Metric {
    protected NameValue[] data;

    public MultipleData(NameValue[] data){
        this.data = data;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();

        for (NameValue line : data) {
            result.append(line.getName());
            result.append(": ");
            result.append(line.getValue().toString());
            result.append("\n");
        }
        result.setLength(result.length() - 1); // remove last newline

        return result.toString();
    }
}
