import java.util.ArrayList;
import java.util.List;

public class LinkedListDeque<T> implements Deque<T> {
    private class Node {
        private T item;
        private Node previous;
        private Node next;
        private Node(T itm, Node prev, Node nxt) {
            item = itm;
            previous = prev;
            next = nxt;
        }
    }
    private Node sentinel;
    private int size;
    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        sentinel.next = sentinel;
        sentinel.previous = sentinel;
        size = 0;
    }

    @Override
    public void addFirst(T x) {
        if (sentinel.next == sentinel) {
            sentinel.next = new Node(x, sentinel, sentinel);
            sentinel.previous = sentinel.next;
        } else {
            Node temp = sentinel.next;
            sentinel.next = new Node(x, sentinel, temp);
            temp.previous = sentinel.next;
        }
        size++;
    }

    @Override
    public void addLast(T x) {
        if (sentinel.next == sentinel) {
            sentinel.previous = new Node(x, sentinel, sentinel);
            sentinel.next = sentinel.previous;
        } else {
            Node temp = sentinel.previous;
            sentinel.previous = new Node(x, temp, sentinel);
            temp.next = sentinel.previous;
        }
        size++;
    }

    @Override
    public List<T> toList() {
        Node listtwo = sentinel.next;
        List<T> returnList = new ArrayList<>();
        while (listtwo != sentinel) {
            returnList.add(listtwo.item);
            listtwo = listtwo.next;
        }
        return returnList;
    }
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T removeFirst() {
        if (this.isEmpty()) {
            return null;
        } else {
            size--;
            Node temp = sentinel.next.next;
            T removedItem = sentinel.next.item;
            temp.previous = sentinel;
            sentinel.next = temp;
            return removedItem;
        }
    }

    @Override
    public T removeLast() {
        if (this.isEmpty()) {
            return null;
        } else {
            size--;
            Node temp = sentinel.previous.previous;
            T removedItem = sentinel.previous.item;
            temp.next = sentinel;
            sentinel.previous = temp;
            return removedItem;
        }
    }

    @Override
    public T get(int index) {
        if (index < 0) {
            return null;
        } else if (index >= size) {
            return null;
        } else {
            int tracker = 0;
            Node sentineltracker = sentinel;
            while (sentineltracker.next != sentinel) {
                sentineltracker = sentineltracker.next;
                if (tracker == index) {
                    break;
                }
                tracker += 1;
            }
            return sentineltracker.item;
        }
    }
    @Override
    public T getRecursive(int index) {
        if (index >= size || index < 0) {
            return null;
        }
        return getRecursiveHelper(index, sentinel);
    }
    public T getRecursiveHelper(int index, Node tracker) {
        if (index == 0) {
            return tracker.next.item;
        }
        return getRecursiveHelper(index - 1, tracker.next);
    }

}
