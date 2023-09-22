import java.util.ArrayList;
import java.util.List;

public class ArrayDeque<T> implements Deque<T> {
    private T[] item;
    private int size;
    private int nextitem;
    private int lastitem;
    private final int arrayCapacity = 8;

    private static int sixteen = 8 * 2;

    public ArrayDeque() {
        item = (T[]) new Object[arrayCapacity];
        size = 0;
        lastitem = 4;
        nextitem = 5;
    }
    public void resizeHelperF(int maxsize) {
        T[] first = (T[]) new Object[maxsize];
        int placeholder = (lastitem + 1) % item.length;
        for (int j = 0; j < size; j++) {
            first[j] = item[placeholder];
            placeholder = (placeholder + 1) % item.length;
        }
        item = first;
        lastitem = item.length - 1;
        nextitem = size;
    }


    @Override
    public void addFirst(T x) {
        if (size == item.length) {
            resizeHelperF(2 * size);
        }
        item[lastitem] = x;
        size += 1;
        lastitem = (item.length - 1 + lastitem) % item.length;
    }

    @Override
    public void addLast(T x) {
        if (size == item.length) {
            resizeHelperF(2 * size);
        }
        item[nextitem] = x;
        size += 1;
        nextitem = (nextitem + 1) % item.length;
    }

    @Override
    public List<T> toList() {
        List<T> returnList = new ArrayList<>();
        int i;
        if (size == 0) {
            return returnList;
        }
        if (lastitem + 1 == item.length) {
            i = 0;
        } else {
            i = lastitem + 1;
        }
        while (item[i] != null) {
            returnList.add(item[i]);
            if (i + 1 == item.length) {
                i = 0;
            } else {
                i++;
            }
        }
        return returnList;
    }

    @Override
    public boolean isEmpty() {
        return (size == 0);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        lastitem = (lastitem + 1) % item.length;
        T spotter = item[lastitem];
        item[lastitem] = null;
        size = size - 1;
        if ((item.length >= sixteen) && (size * 4 < item.length)) {
            resizeHelperF(item.length / 2);
        }
        return spotter;
    }


    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        nextitem = (nextitem - 1 + item.length) % item.length;
        T spotter = item[nextitem];
        item[nextitem] = null;
        size = size - 1;
        if ((item.length >= sixteen) && (size * 4 < item.length)) {
            resizeHelperF(item.length / 2);
        }
        return spotter;
    }


    @Override
    public T get(int index) {
        if (index > size) {
            return null;
        }
        if (index < 0) {
            return null;
        }
        return item[(((lastitem + 1) % item.length) + index) % item.length];
    }
}
