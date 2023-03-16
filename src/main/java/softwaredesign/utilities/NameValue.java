package softwaredesign.utilities;

public class NameValue<T> {
    public String name;
    public T value;
    public NameValue(String name, T value) {
        this.name = name;
        this.value = value;
    }
}
