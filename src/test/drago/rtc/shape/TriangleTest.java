package drago.rtc.shape;

import drago.rtc.foundations.Intersection;
import drago.rtc.foundations.Ray;
import drago.rtc.foundations.Tuple;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TriangleTest {
    @Test
    void constructingATriangle() {
        Tuple p1 = Tuple.point(0, 1, 0);
        Tuple p2 = Tuple.point(-1, 0, 0);
        Tuple p3 = Tuple.point(1, 0, 0);

        Triangle t = new Triangle(p1, p2, p3);

        assertEquals(p1, t.getPoint1());
        assertEquals(p2, t.getPoint2());
        assertEquals(p3, t.getPoint3());

        assertEquals(Tuple.vector(-1, -1, 0), t.getEdge1());
        assertEquals(Tuple.vector(1, -1, 0), t.getEdge2());
        assertEquals(Tuple.vector(0, 0, -1), t.getNormal());
    }

    @Test
    void findingTheNormalOnATriangle() {
        Triangle t = new Triangle(Tuple.point(0, 1, 0), Tuple.point(-1, 0, 0), Tuple.point(1, 0, 0));

        Tuple expectedNormal = t.getNormal();

        assertEquals(expectedNormal, t.localNormalAt(Tuple.point(0, 0.5, 0), null));
        assertEquals(expectedNormal, t.localNormalAt(Tuple.point(-0.50, 0.75, 0), null));
        assertEquals(expectedNormal, t.localNormalAt(Tuple.point(0.5, 0.25, 0), null));
    }

    @Test
    void intersectingARayParallelToTheTriangle() {
        Triangle t = new Triangle(Tuple.point(0, 1, 0), Tuple.point(-1, 0, 0), Tuple.point(1, 0, 0));
        Ray r = new Ray(Tuple.point(0, -1, -2), Tuple.vector(0, 1, 0));

        assertEquals(0, t.localIntersect(r).length);
    }

    @Test
    void aRayMissesThePoint1ToPoint3Edge() {
        Triangle t = new Triangle(Tuple.point(0, 1, 0), Tuple.point(-1, 0, 0), Tuple.point(1, 0, 0));
        Ray r = new Ray(Tuple.point(1, 1, -2), Tuple.vector(0, 0, 1));

        assertEquals(0, t.localIntersect(r).length);
    }

    @Test
    void aRayMissesThePoint1ToPoint2Edge() {
        Triangle t = new Triangle(Tuple.point(0, 1, 0), Tuple.point(-1, 0, 0), Tuple.point(1, 0, 0));
        Ray r = new Ray(Tuple.point(-1, 1, -2), Tuple.vector(0, 0, 1));

        assertEquals(0, t.localIntersect(r).length);
    }

    @Test
    void aRayMissesThePoint2ToPoint3Edge() {
        Triangle t = new Triangle(Tuple.point(0, 1, 0), Tuple.point(-1, 0, 0), Tuple.point(1, 0, 0));
        Ray r = new Ray(Tuple.point(0, -1, -2), Tuple.vector(0, 0, 1));

        assertEquals(0, t.localIntersect(r).length);
    }

    @Test
    void aRayStrikesATriangle() {
        Triangle t = new Triangle(Tuple.point(0, 1, 0), Tuple.point(-1, 0, 0), Tuple.point(1, 0, 0));
        Ray r = new Ray(Tuple.point(0, 0.5, -2), Tuple.vector(0, 0, 1));

        Intersection[] xs = t.localIntersect(r);

        assertEquals(1, xs.length);
        assertEquals(2, xs[0].getT());
    }

    @Test
    void hasABounds() {
        Triangle t = new Triangle(Tuple.point(-1, 2, -1), Tuple.point(10, -7, 10), Tuple.point(-5, -1, 15));

        Bounds actual = t.getBounds();

        assertNotNull(actual);
        assertEquals(Tuple.point(-5, -7, -1), actual.getMin());
        assertEquals(Tuple.point(10, 2, 15), actual.getMax());
    }
}