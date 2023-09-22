import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;


public class HashMap <K, V> implements Map61BL<K, V> {

    private static int initialingSize = 16;
    private static double loadingFactor = 0.75;

    private static int initialSize = 26;
    public Item<K, V>[] hashMap;
    private int size = 0;

    private class Item<K, V> {
        private K key;
        private V val;
        private Item next;

        public Item(K k, V v) {
            this.key = k;
            this.val = v;
            this.next = null;
        }
    }

    public HashMap() {
        this(initialingSize, loadingFactor);
    }

    public HashMap(int initialSize) {
        this(initialSize, loadingFactor);
    }

    public HashMap(int initialSize, double loadFactor) {
        this.initialSize = initialSize;
        this.loadingFactor = loadFactor;
        hashMap = (Item<K, V>[]) new Item[initialSize];
    }


    private V itemHelper(K instanceKey, Item instanceItem) {
        if (instanceItem == null) {
            return null;
        } else if (instanceItem.key.equals(instanceKey)) {
            return (V) instanceItem.val;
        } else {
            return (V) itemHelper(instanceKey, instanceItem.next);
        }
    }

    private void pHelper(K instanceKey, V instanceValue, Item instanceItem) {
        if (instanceItem.key == instanceKey) {
            instanceItem.val = instanceValue;
        } else if (instanceItem.next == null) {
            instanceItem.next = new Item<>(instanceKey, instanceValue);
            size += 1;
        } else {
            pHelper(instanceKey, instanceValue, instanceItem.next);
        }
    }

    public Set<K> resizeHelper() {
        Set<K> chain = new HashSet<>();
        for (int i = 0; i < initialSize; i += 1) {
            Item spot = hashMap[i];
            while (spot != null) {
                chain.add((K) spot.key);
                spot = spot.next;
            }
        }
        return chain;
    }


    @Override
    public void clear() {
        for (int i = 0; i < initialSize; i++) {
            hashMap[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        int pointer = Math.abs(key.hashCode() % initialSize);
        return (itemHelper(key, hashMap[pointer]) != null);
    }

    @Override
    public V get(K key) {
        int pointer = Math.abs(key.hashCode() % initialSize);
        return itemHelper(key, hashMap[pointer]);
    }

    @Override
    public void put(K key, V value) {
        int pointer = Math.abs(key.hashCode() % initialSize);
        if (hashMap[pointer] == null) {
            hashMap[pointer] = new Item<>(key, value);
            size++;
        } else {
            pHelper(key, value, hashMap[pointer]);
        }

        if ((double) size / initialSize > loadingFactor) {
            resize();
        }
    }

    @Override
    public V remove(K key) {
        int follow = Math.abs(key.hashCode() % initialSize);
        Item<K, V> curr = hashMap[follow];
        Item<K, V> prev = null;

        while (curr != null) {
            if (curr.key.equals(key)) {
                if (prev == null) {
                    hashMap[follow] = curr.next;
                } else {
                    prev.next = curr.next;
                }
                size -= 1;
                return curr.val;
            }
            prev = curr;
            curr = curr.next;
        }
        return null;
    }

    @Override
    public boolean remove(K key, V value) {
        int follow = Math.abs(key.hashCode() % initialSize);
        Item<K, V> curr = hashMap[follow];
        Item<K, V> prev = null;

        while (curr != null) {
            if (curr.key.equals(key)) {
                if (prev == null) {
                    hashMap[follow] = curr.next;
                } else {
                    prev.next = curr.next;
                }
                size -= 1;
                return true;
            }
            prev = curr;
            curr = curr.next;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }


    public void resize() {
        int newResizedArray = 2*initialSize;
        Item<K, V>[] resizeArray = (Item<K, V>[]) new Item[newResizedArray];
        for (K insert: resizeHelper()){
            int followPointer = Math.abs(insert.hashCode() % newResizedArray);
            if (resizeArray[followPointer] == null){
                resizeArray[followPointer] = new Item<>(insert, get(insert));
            }
            else {
                pHelper(insert, get(insert), resizeArray[followPointer]);
            }
        }
        initialSize = newResizedArray;
        hashMap = resizeArray;
    }

    @Override
    public Iterator<K> iterator() {
        return null;
    }

    public int capacity() {
        return this.initialSize;
    }

    /* TODO: Instance variables here */

    /* TODO: Constructors here */

    /* TODO: Interface methods here */

    private static class Entry<K, V> {

        private K key;
        private V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        /* Returns true if this key matches with the OTHER's key. */
        public boolean keyEquals(Entry other) {
            return key.equals(other.key);
        }

        /* Returns true if both the KEY and the VALUE match. */
        @Override
        public boolean equals(Object other) {
            return (other instanceof Entry
                    && key.equals(((Entry) other).key)
                    && value.equals(((Entry) other).value));
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }
    }
}
