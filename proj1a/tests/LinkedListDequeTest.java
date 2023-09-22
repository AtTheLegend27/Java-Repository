import jh61b.utils.Reflection;
import net.sf.saxon.functions.ConstantFunction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
import static org.junit.Assert.assertFalse;

/** Performs some basic linked list tests. */
public class LinkedListDequeTest {

    @Test
    @DisplayName("LinkedListDeque has no fields besides nodes and primitives")
    void noNonTrivialFields() {
        Class<?> nodeClass = NodeChecker.getNodeClass(LinkedListDeque.class, true);
        List<Field> badFields = Reflection.getFields(LinkedListDeque.class)
                .filter(f -> !(f.getType().isPrimitive() || f.getType().equals(nodeClass) || f.isSynthetic()))
                .toList();

        assertWithMessage("Found fields that are not nodes or primitives").that(badFields).isEmpty();
    }

    @Test
    /** In this test, we have three different assert statements that verify that addFirst works correctly. */
    public void addFirstTestBasic() {
        Deque<String> lld1 = new LinkedListDeque<>();

        lld1.addFirst("back"); // after this call we expect: ["back"]
        assertThat(lld1.toList()).containsExactly("back").inOrder();

        lld1.addFirst("middle"); // after this call we expect: ["middle", "back"]
        assertThat(lld1.toList()).containsExactly("middle", "back").inOrder();

        lld1.addFirst("front"); // after this call we expect: ["front", "middle", "back"]
        assertThat(lld1.toList()).containsExactly("front", "middle", "back").inOrder();

         /* Note: The first two assertThat statements aren't really necessary. For example, it's hard
            to imagine a bug in your code that would lead to ["front"] and ["front", "middle"] failing,
            but not ["front", "middle", "back"].
          */
    }

    @Test
    /** In this test, we use only one assertThat statement. IMO this test is just as good as addFirstTestBasic.
     *  In other words, the tedious work of adding the extra assertThat statements isn't worth it. */
    public void addLastTestBasic() {
        Deque<String> lld1 = new LinkedListDeque<>();

        lld1.addLast("front"); // after this call we expect: ["front"]
        lld1.addLast("middle"); // after this call we expect: ["front", "middle"]
        lld1.addLast("back"); // after this call we expect: ["front", "middle", "back"]
        assertThat(lld1.toList()).containsExactly("front", "middle", "back").inOrder();
    }

    @Test
    /** This test performs interspersed addFirst and addLast calls. */
    public void addFirstAndAddLastTest() {
        Deque<Integer> lld1 = new LinkedListDeque<>();

         /* I've decided to add in comments the state after each call for the convenience of the
            person reading this test. Some programmers might consider this excessively verbose. */
        lld1.addLast(0);   // [0]
        lld1.addLast(1);   // [0, 1]
        lld1.addFirst(-1); // [-1, 0, 1]
        lld1.addLast(2);   // [-1, 0, 1, 2]
        lld1.addFirst(-2); // [-2, -1, 0, 1, 2]

        assertThat(lld1.toList()).containsExactly(-2, -1, 0, 1, 2).inOrder();
    }

    // Below, you'll write your own tests for LinkedListDeque.
    @Test
    public void checkisEmptytrue() {
        Deque<Integer> lld1 = new LinkedListDeque<>();
        assertThat(lld1.isEmpty());
    }

    @Test
    public void checkisEmptyfalse() {
        Deque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addLast(0);
        assertThat(! lld1.isEmpty());
    }

    @Test
    public void size() {
        Deque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addLast(0);   // [0]
        lld1.addLast(1);   // [0, 1]
        lld1.addFirst(-1); // [-1, 0, 1]
        lld1.addLast(2);   // [-1, 0, 1, 2]
        lld1.addFirst(-2); // [-2, -1, 0, 1, 2]

        assertThat(lld1.size() == 5);
    }

    @Test
    public void checkSize2() {
        Deque<Integer> lld1 = new LinkedListDeque<>();
        assertThat(lld1.size() == 0);
    }

    @Test
    public void size_after_remove_from_empty(){
        Deque<Integer> lld1 = new LinkedListDeque<>();
        assertThat(lld1.size()).isEqualTo(0);
    }
    @Test
    public void size_after_remove_from_to_empty() {
        Deque<Integer> lld1 = new LinkedListDeque<>();
        assertThat(lld1.isEmpty()).isTrue();
    }

    @Test
    public void is_empty_false(){
        Deque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addLast(1);
        lld1.addLast(2);

        assertThat(!lld1.isEmpty());
    }
    @Test
    public void is_empty_true(){
        Deque<Integer> lld1 = new LinkedListDeque<>();
        assertThat(lld1.isEmpty());
    }

    @Test
    public void checktoList() {
        Deque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addLast(1);
        lld1.addLast(2);
        lld1.addFirst(3);
        lld1.addLast(4);
        lld1.addFirst(5);

        assertThat(lld1.toList()).containsExactly(5, 3, 1, 2, 4).inOrder();
    }

    @Test
    public void checkget1() {
        Deque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addLast(1); //5, 3, 1, 2, 4
        lld1.addLast(2);
        lld1.addFirst(3);
        lld1.addLast(4);
        lld1.addFirst(5);

        assertThat(lld1.toList().get(0) == 5);
        assertThat(lld1.toList().get(1) == 3);
        assertThat(lld1.toList().get(2) == 1);
        assertThat(lld1.toList().get(3) == 2);
        assertThat(lld1.toList().get(4) == 4);
    }

    @Test
    public void checkget2() {
        Deque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addLast(5);
        lld1.addLast(4);
        lld1.addFirst(3);
        lld1.addLast(2);
        lld1.addFirst(1);

        assertThat(lld1.toList().get(0) == 1);
        assertThat(lld1.toList().get(1) == 3);
        assertThat(lld1.toList().get(2) == 5);
        assertThat(lld1.toList().get(3) == 4);
        assertThat(lld1.toList().get(4) == 2);
    }

    @Test
    public void checkget3() {
        Deque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addLast(1);
        lld1.addLast(4);
        lld1.addFirst(2);
        lld1.addLast(3);
        lld1.addFirst(9);
        assertThat(lld1.toList().get(0) == 9);

    }

    @Test
    public void checkget4() {
        Deque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addLast(1);
        lld1.addLast(2);
        lld1.addFirst(3);
        lld1.addLast(4);
        lld1.addFirst(null);

        assertThat(lld1.toList().get(0) == null);
    }

    @Test
    public void get_valid() {
        Deque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addLast(1);
        lld1.addLast(1);
        lld1.addFirst(1);
        lld1.addLast(1);
        lld1.addFirst(7);

        assertThat(lld1.get(0) == 7);
    }
    @Test
    public void get_oob_neg() {
        Deque<Integer> lld1 = new LinkedListDeque<>();

        assertThat(lld1.get(-1)).isEqualTo(null);
    }
    @Test
    public void get_oob_large() {
        Deque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addLast(1);
        lld1.addLast(2);

        assertThat(lld1.get(2)).isEqualTo(null);
    }
    @Test
    public void get_recursive_valid() {
        Deque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addLast(1);
        lld1.addLast(2);
        lld1.addFirst(3);
        lld1.addLast(4);
        lld1.addFirst(5);

        assertThat(lld1.getRecursive(0) == 5);
        assertThat(lld1.getRecursive(1) == 3);
        assertThat(lld1.getRecursive(2) == 1);
        assertThat(lld1.getRecursive(3) == 2);
        assertThat(lld1.getRecursive(4) == 4);
    }

    @Test
    public void get_recursive_oob_large(){
        Deque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addLast(1);
        lld1.addLast(2);

        assertThat(lld1.getRecursive(2)).isEqualTo(null);
    }

    @Test
    public void get_recursive_oob_neg(){
        Deque<Integer> lld1 = new LinkedListDeque<>();

        assertThat(lld1.getRecursive(-1)).isEqualTo(null);
    }

    @Test
    public void checkremovefirst1() {
        Deque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addLast(1);//5, 3, 1, 2, 4
        lld1.addLast(2);
        lld1.addFirst(3);
        lld1.addLast(4);
        lld1.addFirst(5);
        lld1.removeFirst();

        assertThat(lld1.toList()).containsExactly(3, 1, 2, 4).inOrder();
    }

    @Test
    public void checkremovefirst2() {
        Deque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addLast(2);
        lld1.addLast(1);
        lld1.addLast(4);
        lld1.removeFirst();
        lld1.removeFirst();

        assertThat(lld1.toList()).containsExactly(4).inOrder();
    }

    @Test
    public void checkremovefirst3() {
        Deque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addLast(7);
        lld1.addLast(8);
        lld1.addLast(2);
        lld1.removeFirst();
        lld1.removeFirst();
        lld1.removeFirst();

        assertThat(lld1.toList()).containsExactly().inOrder();
    }

    @Test
    public void checkremovelast1() {
        Deque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addLast(1);
        lld1.addLast(2);
        lld1.addFirst(3);
        lld1.addLast(4);
        lld1.addFirst(5);
        /*lld1.removeLast();*/
        System.out.println(lld1.removeLast());

        assertThat(lld1.toList()).containsExactly(5, 3, 1, 2).inOrder();
    }

    @Test
    public void checkremovelast2() {
        Deque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addLast(7);
        lld1.addLast(6);
        lld1.removeLast();
        lld1.removeLast();

        assertThat(lld1.toList()).containsExactly().inOrder();
    }

    @Test
    public void checkaddafterremove() {
        Deque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addFirst(1);
        lld1.addLast(2);
        assertThat(lld1.toList()).containsExactly(1, 2).inOrder();
        lld1.removeLast();
        lld1.removeLast();
        assertThat(lld1.toList()).containsExactly().inOrder();
        lld1.addFirst(3);
        lld1.addLast(4);
        assertThat(lld1.toList()).containsExactly(3, 4).inOrder();
    }
}