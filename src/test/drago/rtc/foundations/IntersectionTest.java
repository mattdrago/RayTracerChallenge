package drago.rtc.foundations;

import drago.rtc.foundations.*;
import drago.rtc.shape.Plane;
import drago.rtc.shape.Shape;
import drago.rtc.shape.Sphere;
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

    @Test
    void precomputingTheStateOfAnIntersection() {
        Ray r = new Ray(Tuple.point(0, 0, -5), Tuple.vector(0, 0, 1));
        Sphere s = new Sphere();
        Intersection i = new Intersection(4, s);

        Computations comps = i.prepareComputations(r);

        assertEquals(i.getT(), comps.getT());
        assertEquals(i.getObject(), comps.getObject());
        assertEquals(Tuple.point(0, 0, -1), comps.getPoint());
        assertEquals(Tuple.vector(0, 0, -1), comps.getEyeV());
        assertEquals(Tuple.vector(0, 0, -1), comps.getNormalV());
    }

    @Test
    void theHitWhenAnIntersectionOccursOnTheOutside() {
        Ray r = new Ray(Tuple.point(0, 0, -5), Tuple.vector(0, 0, 1));
        Sphere s = new Sphere();
        Intersection i = new Intersection(4, s);

        Computations comps = i.prepareComputations(r);

        assertFalse(comps.isInside());
    }

    @Test
    void theHitWhenAnIntersectionOccursOnTheInside() {
        Ray r = new Ray(Tuple.point(0, 0, 0), Tuple.vector(0, 0, 1));
        Sphere s = new Sphere();
        Intersection i = new Intersection(1, s);

        Computations comps = i.prepareComputations(r);

        assertEquals(Tuple.point(0, 0, 1), comps.getPoint());
        assertEquals(Tuple.vector(0, 0, -1), comps.getEyeV());
        assertTrue(comps.isInside());
        assertEquals(Tuple.vector(0, 0, -1), comps.getNormalV());
    }

    @Test
    void theHitShouldOffsetThePoint() {
        Ray r = new Ray(Tuple.point(0, 0, -5), Tuple.vector(0, 0, 1));
        Sphere s = new Sphere();
        s.setTransform(Matrix.translation(0, 0, 1));
        Intersection i = new Intersection(5, s);

        Computations comps = i.prepareComputations(r);

        assertTrue(comps.getOverPoint().getZ() < -Computations.EPSILON / 2);
        assertTrue(comps.getPoint().getZ() > comps.getOverPoint().getZ());
    }

    @Test
    void precomputeTheReflectionVector() {
        Shape s = new Plane();
        Ray r = new Ray(Tuple.point(0, 1, -1), Tuple.vector(0, -Math.sqrt(2) / 2, Math.sqrt(2) / 2));
        Intersection i = new Intersection(Math.sqrt(2), s);

        Computations c = i.prepareComputations(r);

        assertEquals(Tuple.vector(0, Math.sqrt(2) / 2, Math.sqrt(2) / 2), c.getReflectV());
    }
}