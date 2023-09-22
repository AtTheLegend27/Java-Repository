
/* A doubly-linked list supporting various sorting algorithms. */
public class DLList<T extends Comparable<T>> {

    private class Node {

        T item;
        Node prev;
        Node next;

        Node(T item) {
            this.item = item;
            this.prev = this.next = null;
        }

        Node(T item, Node prev, Node next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

    /* The sentinel of this DLList. */
    Node sentinel;
    /* The number of items in this DLList. */
    int size;

    /* Creates an empty DLList. */
    public DLList() {
        sentinel = new Node(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        this.size = 0;
    }

    /* Creates a copy of DLList represented by LST. */
    public DLList(DLList<T> lst) {
        Node ptr = lst.sentinel.next;
        sentinel = new Node(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        this.size = 0;
        while (ptr != lst.sentinel) {
            addLast(ptr.item);
            ptr = ptr.next;
        }
    }

    /* Returns true if this DLList is empty. Otherwise, returns false. */
    public boolean isEmpty() {
        return size == 0;
    }

    /* Adds a new Node with item ITEM to the front of this DLList. */
    public void addFirst(T item) {
        Node newNode = new Node(item, sentinel, sentinel.next);
        sentinel.next.prev = newNode;
        sentinel.next = newNode;
        size += 1;
    }

    /* Adds a new Node with item ITEM to the end of this DLList. */
    public void addLast(T item) {
        Node newNode = new Node(item, sentinel.prev, sentinel);
        sentinel.prev.next = newNode;
        sentinel.prev = newNode;
        size += 1;
    }

    /* Removes the Node referenced by N from this DLList. */
    private void remove(Node n) {
        n.prev.next = n.next;
        n.next.prev = n.prev;
        n.next = null;
        n.prev = null;
        size -= 1;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Node ptr = sentinel.next; ptr != sentinel; ptr = ptr.next) {
            s.append(ptr.item.toString());
            s.append(" ");
        }
        return s.toString();
    }


    /* Returns a copy of this DLList sorted using merge sort. Does not modify
       the original DLList. */
    public DLList<T> mergeSort() {
        if (size <= 1) {
            return this;
        }
        DLList<T> oneHalf = new DLList<>();
        DLList<T> otherHalf = new DLList<>();
        // TODO: YOUR CODE HERE
        Node currentNode = sentinel.next;
        int middle = size / 2;
        //iterate the front end of values
        for (int i = 0; i < middle; i++) {
            oneHalf.addLast(currentNode.item);
            currentNode = currentNode.next;
        }
        //iterate the last end of values
        for (int i = size; i > middle; i--) {
            otherHalf.addFirst(currentNode.item);
            currentNode = currentNode.next;
        }
        // recursively call the merge sort in order to keep dividing the middle until can't divide by half by base condition
        return oneHalf.mergeSort().merge(otherHalf.mergeSort());
    }

    /* Returns the result of merging this DLList with LST. Does not modify the
       two DLLists. Assumes that this DLList and LST are in sorted order. */
    private DLList<T> merge(DLList<T> lst) {
        DLList toReturn = new DLList();
        Node thisPtr = sentinel.next;
        Node lstPtr = lst.sentinel.next;
        while (thisPtr != sentinel && lstPtr != lst.sentinel) {
            if (thisPtr.item.compareTo(lstPtr.item) < 0) {
                toReturn.addLast(thisPtr.item);
                thisPtr = thisPtr.next;
            } else {
                toReturn.addLast(lstPtr.item);
                lstPtr = lstPtr.next;
            }
        }
        while (thisPtr != sentinel) {
            toReturn.addLast(thisPtr.item);
            thisPtr = thisPtr.next;
        }
        while (lstPtr != lst.sentinel) {
            toReturn.addLast(lstPtr.item);
            lstPtr = lstPtr.next;
        }
        return toReturn;
    }

    /* Returns a copy of this DLList sorted using quicksort. The first element
       is used as the pivot. Does not modify the original DLList. */
    public DLList<T> quicksort() {
        if (size <= 1) {
            return this;
        }
        // Assume first element is the divider.
        DLList<T> smallElements = new DLList<>();
        DLList<T> largeElements = new DLList<>();
        T pivot = sentinel.next.item;
        // TODO: YOUR CODE HERE
        DLList<T> returnList = new DLList<>();
        DLList<T> equalsElement = new DLList<>();
        Node nextNode = sentinel.next;
        //filter the items into large elements and small elements in relation to pivot
        //utilizing the compare to method which spits 1 if bigger, 0 if equal and -1 if less
        //FIRGURED it out, literally was needing an equal condition
        while (nextNode != sentinel) {
            T temporaryItem = nextNode.item;;
            if (temporaryItem.compareTo(pivot) > 0) {
                largeElements.addLast(temporaryItem);
            } else if (temporaryItem.compareTo(pivot) == 0) {
                equalsElement.addLast(temporaryItem);
            } else {
                smallElements.addLast(temporaryItem);
            }
            nextNode = nextNode.next;
        }
//        if (smallElements.sentinel.next.item == pivot) {
//            Node equalsNode = smallElements.sentinel.next;
//            returnList.append( );
//            smallElements.remove(equalsNode);
//        }
//        //now add the elements from small elements to the large elements based upon sorting from the previous while loop
//        //for some reason, the recursive call to the quicksort isn't working
//        //Maybe becase of no parameters
//        for (int i = 0; i < smallElements.size; i++) {
//            if (smallElements.isEmpty()) {
//                break;
//            }
//            else {
//                smallElements.quicksort();
//                returnList.append(smallElements);
//            }
//        }
//        for (int i = 0; i < largeElements.size; i++) {
//            if (largeElements.isEmpty()) {
//                break;
//            }
//            else {
//                largeElements.quicksort();
//                returnList.append(largeElements);
//            }
//        }
        returnList.append(smallElements.quicksort());
        returnList.append(equalsElement);
        returnList.append(largeElements.quicksort());
        return returnList;
    }

    /* Appends LST to the end of this DLList. */
    public void append(DLList<T> lst) {
        if (lst.isEmpty()) {
            return;
        }
        if (isEmpty()) {
            sentinel = lst.sentinel;
            size = lst.size;
            return;
        }
        sentinel.prev.next = lst.sentinel.next;
        lst.sentinel.next.prev = sentinel.prev;
        sentinel.prev = lst.sentinel.prev;
        lst.sentinel.prev.next = sentinel;
        size += lst.size;
    }

    private static DLList<Integer> generateRandomIntegerDLList(int N) {
        DLList<Integer> toReturn = new DLList<>();
        for (int k = 0; k < N; k++) {
            toReturn.addLast((int) (100 * Math.random()));
        }
        return toReturn;
    }

    public static void main(String[] args) {
        DLList<Integer> values;
        DLList<Integer> sortedValues;

        System.out.print("Before merge sort: ");
        values = generateRandomIntegerDLList(10);
        System.out.println(values);
        sortedValues = values.mergeSort();
        System.out.print("After merge sort: ");
        System.out.println(sortedValues);

        System.out.print("Before quicksort: ");
        values = generateRandomIntegerDLList(10);
        System.out.println(values);
        sortedValues = values.quicksort();
        System.out.print("After quicksort: ");
        System.out.println(sortedValues);
    }
}
