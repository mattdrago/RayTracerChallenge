package drago.rtc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntersectionTest {

    @Test
    void anIntersectionEncapsulatesTAndObject() {
        Sphere s = new Sphere();

        Intersection i = new Intersection(3.5, s);

        assertEquals(3.5, i.getT());
        assertEquals(s, i.getObject());
    }

    @Test
    void aggregatingIntersections() {
        Sphere s = new Sphere();
        Intersection i1 = new Intersection(1, s);
        Intersection i2 = new Intersection(2, s);

        Intersection[] xs = Intersection.intersections(i1, i2);

        assertEquals(2, xs.length);
        assertEquals(1, xs[0].getT());
        assertEquals(2, xs[1].getT());
    }

    @Test
    void hitWhenAllIntersectionHavePositiveT() {
        Sphere s = new Sphere();
        Intersection i1 = new Intersection(1, s);
        Intersection i2 = new Intersection(2, s);
        Intersection[] xs = Intersection.intersections(i1, i2);

        assertEquals(i1, Intersection.hit(xs));
    }

    @Test
    void hitWhenSomeIntersectionsHaveNegativeT() {
        Sphere s = new Sphere();
        Intersection i1 = new Intersection(-1, s);
        Intersection i2 = new Intersection(1, s);
        Intersection[] xs = Intersection.intersections(i1, i2);

        assertEquals(i2, Intersection.hit(xs));
    }

    @Test
    void hitWhenAllIntersectionsHaveNegativeT() {
        Sphere s = new Sphere();
        Intersection i1 = new Intersection(-2, s);
        Intersection i2 = new Intersection(-1, s);
        Intersection[] xs = Intersection.intersections(i1, i2);

        assertNull(Intersection.hit(xs));
    }

    @Test
    void hitIsAlwaysTheLowestNonNegativeIntersection() {
        Sphere s = new Sphere();
        Intersection i1 = new Intersection(5, s);
        Intersection i2 = new Intersection(7, s);
        Intersection i3 = new Intersection(-3, s);
        Intersection i4 = new Intersection(2, s);

        Intersection[] xs = Intersection.intersections(i1, i2, i3, i4);

        assertEquals(i4, Intersection.hit(xs));
    }
}