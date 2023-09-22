import com.google.common.truth.Subject;
import org.junit.Rule;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
public abstract class TriangleTest {

    /** For autograding purposes; do not change this line. */
    abstract Triangle getNewTriangle();

    /* ***** TESTS ***** */

    // FIXME: Add additional tests for Triangle.java here that pass on a
    //  correct Triangle implementation and fail on buggy Triangle implementations.


    @Test
    public void test1() {
        Triangle t = getNewTriangle();
        assertThat(t.squaredHypotenuse(4,4)).isEqualTo(16+16);
        assertThat(t.squaredHypotenuse(-4,-4)).isEqualTo(32);

        assertThat(t.sidesFormTriangle(4, 4, 1)).isTrue();
        assertThat(t.sidesFormTriangle(4, 4, 8)).isFalse();

        assertThat(t.triangleType(3, 4, 5)).isEqualTo("Scalene");
        assertThat(t.triangleType(3, 3, 2)).isEqualTo("Isosceles");
        assertThat(t.triangleType(3, 3, 3)).isEqualTo("Equilateral");

        assertThat(t.pointsFormTriangle(0, 0, 2, 0, 1, 1)).isTrue();
        assertThat(t.pointsFormTriangle(0, 0, 1, 0, 1, 0)).isFalse();

        // remember that you'll have to call on Triangle methods like
        // t.functionName(arguments), where t is a Triangle object

    }



}
