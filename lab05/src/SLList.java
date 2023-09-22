import org.w3c.dom.Node;

/** A data structure to represent a Linked List of Integers.
 * Each SLList represents one node in the overall Linked List.
 */

public class SLList<T> {

    /** The object stored by this node. */
    public T item;
    /** The next node in this SLList. */
    public SLList<T> next;

    /** Constructs an SLList storing ITEM and next node NEXT. */
    public SLList(T item, SLList<T> next) {
        this.item = item;
        this.next = next;
    }

    /** Constructs an SLList storing ITEM and no next node. */
    public SLList(T item) {
        this(item, null);
    }

    /**
     * Returns [position]th item in this list. Throws IllegalArgumentException
     * if index out of bounds.
     *
     * @param position, the position of element.
     * @return The element at [position]
     */
    public int CountingPosition(SLList countertest){
        int counter = 0;
        while (countertest.next != null){
            countertest = countertest.next;
            counter += 1;
        }
        return counter;
    }

    public T get(int position) {
        SLList<T> MagicTest = this;
        if (position > CountingPosition(this)||position < 0){
            throw new IllegalArgumentException("YOUR MESSAGE HERE");
        }
        else{
            for (int i = 0; i < position; i++ ){
              MagicTest = MagicTest.next;
                }

            }
        return MagicTest.item;
        }


    /**
     * Returns the string representation of the list. For the list (1, 2, 3),
     * returns "1 2 3".
     *
     * @return The String representation of the list.
     */
    public String toString() {
        SLList<T> RealMagicTest = this;
        String Emptystring = "";
        while (RealMagicTest.next != null){
            Emptystring = Emptystring + RealMagicTest.item + " ";
            RealMagicTest = RealMagicTest.next;
        }
        Emptystring = Emptystring + RealMagicTest.item;
        return Emptystring;
    }

    /**
     * Returns whether this and the given list or object are equal.
     *
     * NOTE: A full implementation of equals requires checking if the
     * object passed in is of the correct type, as the parameter is of
     * type Object. This also requires we convert the Object to an
     * SLList<T>, if that is legal. The operation we use to do this is called
     * casting, and it is done by specifying the desired type in
     * parenthesis. This has already been implemented for you.
     *
     * @param obj, another list (object)
     * @return Whether the two lists are equal.
     */

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof SLList)) {
            return false;
        }
        SLList<T> otherLst = (SLList<T>) obj;

        SLList<T> CurrentList = this;
        SLList<T> OtherList = otherLst;

        while (CurrentList.next != null && OtherList.next != null) {
            if (!(CurrentList.item.equals(OtherList.item))) {
                return false;
            }
        CurrentList = CurrentList.next;
        OtherList = OtherList.next;
    }
        if (!(CurrentList.item.equals(OtherList.item))) {
            return false;
        }
        CurrentList = CurrentList.next;
        OtherList = OtherList.next;
        return CurrentList == null && OtherList == null;
    }


    public void add(T value) {
        //TODO: your code here!
        SLList<T> AddRealMagicTest = this;
        SLList<T> ExtraRealMagicTest = new SLList<T>(value, null);
        while (AddRealMagicTest.next != null){
            AddRealMagicTest = AddRealMagicTest.next;
        }
        AddRealMagicTest.next = ExtraRealMagicTest;
    }
}
