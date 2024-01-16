package softwaredesign.utilities;

import java.util.Objects;

public class NameValue<T extends Comparable<T>> implements Comparable<NameValue<T>> {
    public final String name;
    public final T value;
    public NameValue(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public int compareTo(NameValue<T> nameValue) {
        return this.value.compareTo(nameValue.value);
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        NameValue<?> nameValue = (NameValue<?>) o;
        return Objects.equals(name, nameValue.name) && value.equals(nameValue.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.value);
    }
}
