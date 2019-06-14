package drago.rtc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlaneTest {

    @Test
    void theNormaltOfAPlaneIsConstantEverywhere() {
        Plane p = new Plane();

        Tuple expected = Tuple.vector(0, 1, 0);

        assertEquals(expected, p.localNormalAt(Tuple.point(0, 0, 0)));
        assertEquals(expected, p.localNormalAt(Tuple.point(10, 0, -10)));
        assertEquals(expected, p.localNormalAt(Tuple.point(-5, 0, 150)));

    }

    @Test
    void intersectWithARayParallelToThePlane() {
        Plane p = new Plane();
        Ray r = new Ray(Tuple.point(0, 10, 0), Tuple.vector(0, 0, 1));

        assertEquals(0, p.localIntersect(r).length);
    }

    @Test
    void intersectWithACoPlanarRay() {
        Plane p = new Plane();
        Ray r = new Ray(Tuple.point(0, 0, 0), Tuple.vector(0, 0, 1));

        assertEquals(0, p.localIntersect(r).length);
    }

    @Test
    void aRayIntersectingAPlaneFromAbove() {
        Plane p = new Plane();
        Ray r = new Ray(Tuple.point(0, 1, 0), Tuple.vector(0, -1, 0));

        Intersection[] xs = p.localIntersect(r);

        assertEquals(1, xs.length);
        assertEquals(1, xs[0].getT());
        assertEquals(p, xs[0].getObject());
    }

    @Test
    void aRayIntersectingAPlaneFromBelow() {
        Plane p = new Plane();
        Ray r = new Ray(Tuple.point(0, -1, 0), Tuple.vector(0, 1, 0));

        Intersection[] xs = p.localIntersect(r);

        assertEquals(1, xs.length);
        assertEquals(1, xs[0].getT());
        assertEquals(p, xs[0].getObject());
    }

    @Test
    void aRayIntersectingAPlaneBehindOrigin() {
        Plane p = new Plane();
        Ray r = new Ray(Tuple.point(0, 1, 0), Tuple.vector(0, 1, 0));

        Intersection[] xs = p.localIntersect(r);

        assertEquals(1, xs.length);
        assertEquals(-1, xs[0].getT());
        assertEquals(p, xs[0].getObject());
    }
}