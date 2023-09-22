package deque;
import java.util.*;
public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> helperCompare;
    public MaxArrayDeque(Comparator<T> c) {
        helperCompare = c;
    }

    public T max() {
        if (isEmpty()) {
            return null;
        }
        T otherValue = get(0);
        for (int i = 1; i < size(); i++) {
            if (helperCompare.compare(get(i), otherValue) > 0) {
                otherValue = get(i);
            }
        }
        return otherValue;
    }
    public T max(Comparator<T> c) {
        if (isEmpty()) {
            return null;
        }
        helperCompare = c;
        T otherValue = get(0);
        for (int i = 1; i < size(); i++) {
            if (helperCompare.compare(get(i), otherValue) > 0) {
                otherValue = get(i);
            }
        }
        return otherValue;
    }
}
