package Utilities;

public final class NameValue<T> {
    public NameValue(String name, T value) {
        this.name = name;
        this.value = value;
    }
    public String getName() {
        return name;
    }
    public T getValue() {
        return value;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setValue(T value) {
        this.value = value;
    }

    private String name;
    private T value;
}
