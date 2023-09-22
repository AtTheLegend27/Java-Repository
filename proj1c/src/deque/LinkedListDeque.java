package deque;

import java.util.ArrayList;
import java.util.Iterator;
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

    public Iterator<T> iterator() {
        return new LinkedListIterator<>();
    }
    public class LinkedListIterator<T> implements Iterator<T> {
        int current = 0;

        @Override
        public boolean hasNext() {
            if (current <= size - 1) {
                return true;
            }
            return false;
        }

        @Override
        public T next() {
            T after = (T) get(current);
            current += 1;
            return after;
        }
    }
    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object instanceof Deque) {
            Deque place = (Deque) object;
            if (this.size() != place.size()) {
                return false;
            }
            for (int x = 0; this.size() > x; x++) {
                if (!place.get(x).equals(this.get(x))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    @Override
    public String toString() {
        return this.toList().toString();
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
            sentinel.next = new Node(x, sentinel, sentinel);
            sentinel.previous = sentinel.next;
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
        return 0 == size;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T removeFirst() {
        if (0 == size) {
            return null;
        } else {
            size -= 1;
            T resolution = sentinel.next.item;
            sentinel.next = sentinel.next.next;
            sentinel.next.previous = sentinel;
            return resolution;
        }
    }

    @Override
    public T removeLast() {
        if (0 == size) {
            return null;
        } else {
            size -= 1;
            T resolution = sentinel.previous.item;
            sentinel.previous.previous.next = sentinel;
            sentinel.previous = sentinel.previous.previous;
            return resolution;
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
                    return sentineltracker.item;
                }
                tracker += 1;
            }
        }
        return null;
    }

    public T rechelperMethod(int index, int counter, Node newtracker) {
        if (index == counter) {
            return newtracker.item;
        }
        return rechelperMethod(index, counter + 1, newtracker.next);
    }

    @Override
    public T getRecursive(int index) {
        return get(index);
    }
}

