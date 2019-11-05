package drago.rtc.shape;

import drago.rtc.foundations.Matrix;
import drago.rtc.foundations.Ray;
import drago.rtc.foundations.Tuple;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoundsTest {

    @Test
    void aBoundsHasAMinimumAndAMaximum() {
        Tuple min = Tuple.point(0, 0, 0);
        Tuple max = Tuple.point(1, 1, 1);

        Bounds b = new Bounds(min, max);

        assertEquals(min, b.getMin());
        assertEquals(max, b.getMax());
    }

    @Test
    void aTransformedBoundsIsAxisALigned() {
        Bounds b = new Bounds(Tuple.point(-1, -1, -1), Tuple.point(1, 1, 1));
        Matrix transform = Matrix.rotationY(Math.PI / 4);

        Bounds actual = b.transform(transform);
        Tuple expectedMin = Tuple.point(-Math.sqrt(2), -1, -Math.sqrt(2));
        Tuple expectedMax = Tuple.point(Math.sqrt(2), 1, Math.sqrt(2));

        assertEquals(expectedMin, actual.getMin());
        assertEquals(expectedMax, actual.getMax());
    }

    @Test
    void combineWithNothing() {
        Bounds b = new Bounds(Tuple.point(-1, -1, -1), Tuple.point(1, 1, 1));

        Bounds actual = b.combine(null);

        assertNotNull(actual);
        assertEquals(b.getMin(), actual.getMin());
        assertEquals(b.getMax(), actual.getMax());

    }

    @Test
    void combineFullContainedBounds() {
        Bounds outer = new Bounds(Tuple.point(-2, -2, -2), Tuple.point(1, 1, 1));
        Bounds inner = new Bounds(Tuple.point(-1, -1, -1), Tuple.point(0, 0, 0));

        Bounds actual = outer.combine(inner);

        assertEquals(outer.getMin(), actual.getMin());
        assertEquals(outer.getMax(), actual.getMax());
    }

    @Test
    void combineSideBySideBounds() {
        Bounds b1 = new Bounds(Tuple.point(-1, -1, -1), Tuple.point(1, 1, 1));
        Bounds b2 = new Bounds(Tuple.point(-4, -1, -4), Tuple.point(-2, 1, -2));

        Bounds actual = b1.combine(b2);

        Tuple expectedMin = Tuple.point(-4, -1, -4);
        Tuple expectedMax = Tuple.point(1, 1, 1);

        assertEquals(expectedMin, actual.getMin());
        assertEquals(expectedMax, actual.getMax());
    }

    @Test
    void combineIsCommutative() {
        Bounds b1 = new Bounds(Tuple.point(-1, -1, -1), Tuple.point(1, 1, 1));
        Bounds b2 = new Bounds(Tuple.point(-4, -1, -4), Tuple.point(-2, 1, -2));

        Bounds actual1Then2 = b1.combine(b2);
        Bounds actual2Then1 = b2.combine(b1);


        assertEquals(actual1Then2.getMin(), actual2Then1.getMin());
        assertEquals(actual1Then2.getMax(), actual2Then1.getMax());
    }

    @Test
    void aRayIntersectsABounds() {
        Bounds b = new Bounds(Tuple.point(2, 2, 2), Tuple.point(4, 4, 4));
        Ray[] rs = {
                new Ray(Tuple.point(8, 3.5, 3), Tuple.vector(-1, 0, 0)), // +x
                new Ray(Tuple.point(-2, 3.5, 3), Tuple.vector(1, 0, 0)), // -x
                new Ray(Tuple.point(3.5, 8, 3), Tuple.vector(0, -1, 0)), // +y
                new Ray(Tuple.point(3.5, -2, 3), Tuple.vector(0, 1, 0)), // -y
                new Ray(Tuple.point(3.5, 3, 8), Tuple.vector(0, 0, -1)), // +z
                new Ray(Tuple.point(3.5, 3, -2), Tuple.vector(0, 0, 1)), // -z
                new Ray(Tuple.point(3, 3.5, 3), Tuple.vector(0, 0, 1)), // inside
        };

        for (Ray r : rs) {
            assertTrue(b.intersected(r));
        }
    }

    @Test
    void aRayMissesABounds() {
        Bounds b = new Bounds(Tuple.point(-1, -1, -1), Tuple.point(1, 1, 1));
        Ray[] rs = {
                new Ray(Tuple.point(-2, 0, 0), Tuple.vector(0.2673, 0.5345, 0.8018)),
                new Ray(Tuple.point(0, -2, 0), Tuple.vector(0.8018, 0.2673, 0.5345)),
                new Ray(Tuple.point(0, 0, -2), Tuple.vector(0.5345, 0.8018, 0.2673)),
                new Ray(Tuple.point(2, 0, 2), Tuple.vector(0, 0, -1)),
                new Ray(Tuple.point(0, 2, 2), Tuple.vector(0, -1, 0)),
                new Ray(Tuple.point(2, 2, 0), Tuple.vector(-1, 0, 0))
        };

        for (Ray r : rs) {
            assertFalse(b.intersected(r));

        }
    }

    @Test
    void aBoundThatOverlapsAnotherIsNotContainedInIt() {
        Bounds b = new Bounds(Tuple.point(-1, -1, -1), Tuple.point(1, 1, 1));
        Bounds c = new Bounds(Tuple.point(0, 0, 0), Tuple.point(2, 2, 2));

        assertFalse(b.contains(c));
    }

    @Test
    void aBoundThatIsOutsideAnotherBoundIsNotContainedInIt() {
        Bounds b = new Bounds(Tuple.point(-1, -1, -1), Tuple.point(1, 1, 1));
        Bounds c = new Bounds(Tuple.point(2, 2, 2), Tuple.point(3, 4, 5));

        assertFalse(b.contains(c));
    }

    @Test
    void aBoundThatIsCompletelyInsideAnotherBoundIsContainedInIt() {
        Bounds b = new Bounds(Tuple.point(-1, -1, -1), Tuple.point(1, 1, 1));
        Bounds c = new Bounds(Tuple.point(0, 0, 0), Tuple.point(0.5, 0.5, 0.5));

        assertTrue(b.contains(c));
    }

    @Test
    void aBoundShouldContainItself() {
        Bounds b = new Bounds(Tuple.point(-1, -2, -0.5), Tuple.point(1, 2, 3));

        assertTrue(b.contains(b));
    }

    @Test
    void dividingABoundsCreates8Bounds() {
        Bounds b = new Bounds(Tuple.point(-1, -1, -1), Tuple.point(1, 1, 1));

        List<Bounds> bounds = b.divide();

        assertEquals(8, bounds.size());
    }

    @Test
    void dividingABoundsCreatesSmallerBounds() {
        Bounds b = new Bounds(Tuple.point(-1, -1, -1), Tuple.point(1, 1, 1));

        List<Bounds> bounds = b.divide();

        assertTrue(bounds.contains(new Bounds(Tuple.point(-1, -1, -1), Tuple.point(0, 0, 0))));
        assertTrue(bounds.contains(new Bounds(Tuple.point( 0, -1, -1), Tuple.point(1, 0, 0))));
        assertTrue(bounds.contains(new Bounds(Tuple.point(-1,  0, -1), Tuple.point(0, 1, 0))));
        assertTrue(bounds.contains(new Bounds(Tuple.point(-1, -1,  0), Tuple.point(0, 0, 1))));
        assertTrue(bounds.contains(new Bounds(Tuple.point( 0,  0, -1), Tuple.point(1, 1, 0))));
        assertTrue(bounds.contains(new Bounds(Tuple.point( 0, -1,  0), Tuple.point(1, 0, 1))));
        assertTrue(bounds.contains(new Bounds(Tuple.point(-1,  0,  0), Tuple.point(0, 1, 1))));
        assertTrue(bounds.contains(new Bounds(Tuple.point( 0,  0,  0), Tuple.point(1, 1, 1))));
    }

}