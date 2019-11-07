package drago.rtc.shape;

import drago.rtc.foundations.Computations;
import drago.rtc.foundations.Intersection;
import drago.rtc.foundations.Ray;
import drago.rtc.foundations.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SmoothTriangleTest {

    private Tuple p1;
    private Tuple p2;
    private Tuple p3;
    private Tuple n1;
    private Tuple n2;
    private Tuple n3;
    private SmoothTriangle tri;

    @BeforeEach
    void initialise() {
        p1 = Tuple.point(0, 1, 0);
        p2 = Tuple.point(-1, 0, 0);
        p3 = Tuple.point(1, 0, 0);
        n1 = Tuple.vector(0, 1, 0);
        n2 = Tuple.vector(-1, 0, 0);
        n3 = Tuple.vector(1, 0, 0);
        tri = new SmoothTriangle(p1, p2, p3, n1, n2, n3);
    }

    @Test
    void constructingASmoothTriangle() {
        assertEquals(p1, tri.getPoint1());
        assertEquals(p2, tri.getPoint2());
        assertEquals(p3, tri.getPoint3());
        assertEquals(n1, tri.getNormal1());
        assertEquals(n2, tri.getNormal2());
        assertEquals(n3, tri.getNormal3());
    }

    @Test
    void anIntersectionWithASmoothTriangleStoresUAndV() {
        Ray r = new Ray(Tuple.point(-0.2, 0.3, -2), Tuple.vector(0, 0, 1));

        Intersection[] xs = tri.localIntersect(r);

        assertTrue(Math.abs(0.45 - xs[0].getU()) < Computations.EPSILON);
        assertTrue(Math.abs(0.25 - xs[0].getV()) < Computations.EPSILON);
    }

    @Test
    void aSmoothTriangleUsesUAndVToInterpolateTheNormal() {
        Intersection i = new Intersection(1, tri, 0.45, 0.25);

        Tuple actualN = tri.normalAt(Tuple.point(0, 0, 0), i);
        Tuple expectedN = Tuple.vector(-0.5547, 0.83205, 0);

        assertEquals(expectedN, actualN);
    }

    @Test
    void preparingTheNormalOnASmoothTriangle() {
        Intersection i = new Intersection(1, tri, 0.45, 0.25);
        Ray r = new Ray(Tuple.point(-0.2, 0.3, -2), Tuple.vector(0, 0, 1));
        Intersection[] xs = {i};
        Computations comps = i.prepareComputations(r, xs);

        Tuple actualNormalV = comps.getNormalV();
        Tuple expectedNormalV = Tuple.vector(-0.5547, 0.83205, 0);

        assertEquals(expectedNormalV, actualNormalV);
    }
}
