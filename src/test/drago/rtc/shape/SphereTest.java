package drago.rtc.shape;

import drago.rtc.Intersection;
import drago.rtc.Ray;
import drago.rtc.Tuple;
import drago.rtc.shape.Shape;
import drago.rtc.shape.Sphere;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SphereTest {

    @Test
    void aRayIntersectsASphereAtTwoPoints() {
        Ray r = new Ray(Tuple.point(0, 0, -5), Tuple.vector(0, 0, 1));
        Sphere s = new Sphere();

        Intersection[] ts = s.localIntersect(r);

        assertEquals(2, ts.length);
        assertEquals(4.0, ts[0].getT());
        assertEquals(6.0, ts[1].getT());
    }

    @Test
    void aRayIntersectsASphereAtATangent() {
        Ray r = new Ray(Tuple.point(0, 1, -5), Tuple.vector(0, 0, 1));
        Sphere s = new Sphere();

        Intersection[] ts = s.localIntersect(r);

        assertEquals(2, ts.length);
        assertEquals(5.0, ts[0].getT());
        assertEquals(5.0, ts[1].getT());
    }

    @Test
    void aRayMissesASphere() {
        Ray r = new Ray(Tuple.point(0, 2, -5), Tuple.vector(0, 0, 1));
        Sphere s = new Sphere();

        assertEquals(0, s.localIntersect(r).length);
    }

    @Test
    void aRayOriginatesInsideASphere() {
        Ray r = new Ray(Tuple.point(0, 0, 0), Tuple.vector(0, 0, 1));
        Sphere s = new Sphere();

        Intersection [] ts = s.localIntersect(r);

        assertEquals(2, ts.length);
        assertEquals(-1.0, ts[0].getT());
        assertEquals(1.0, ts[1].getT());
    }

    @Test
    void aSphereIsBehindARay() {
        Ray r = new Ray(Tuple.point(0, 0, 5), Tuple.vector(0, 0, 1));
        Sphere s = new Sphere();

        Intersection[] ts = s.localIntersect(r);

        assertEquals(2, ts.length);
        assertEquals(-6.0, ts[0].getT());
        assertEquals(-4.0, ts[1].getT());
    }

    @Test
    void intersectSetsTheObjectOnTheIntersection() {
        Ray r = new Ray(Tuple.point(0, 0, -5), Tuple.vector(0, 0, 1));
        Sphere s = new Sphere();

        Intersection[] xs = s.localIntersect(r);

        assertEquals(s, xs[0].getObject());
        assertEquals(s, xs[1].getObject());
    }


    @Test
    void theNormalOnASphereAtAPointOnTheXAxis() {
        Sphere s = new Sphere();

        Tuple expectedNormal = Tuple.vector(1, 0, 0);
        Tuple actualNormal = s.localNormalAt(Tuple.point(1, 0, 0));

        assertEquals(expectedNormal, actualNormal);
    }

    @Test
    void theNormalOnASphereAtAPointOnTheYAxis() {
        Sphere s = new Sphere();

        Tuple expectedNormal = Tuple.vector(0, 1, 0);
        Tuple actualNormal = s.localNormalAt(Tuple.point(0, 1, 0));

        assertEquals(expectedNormal, actualNormal);
    }

    @Test
    void theNormalOnASphereAtAPointOnTheZAxis() {
        Sphere s = new Sphere();

        Tuple expectedNormal = Tuple.vector(0, 0, 1);
        Tuple actualNormal = s.localNormalAt(Tuple.point(0, 0, 1));

        assertEquals(expectedNormal, actualNormal);
    }

    @Test
    void theNormalOnASphereAtANonAxialPoint() {
        Sphere s = new Sphere();

        Tuple expectedNormal = Tuple.vector(Math.sqrt(3) / 3, Math.sqrt(3) / 3, Math.sqrt(3) / 3);
        Tuple actualNormal = s.localNormalAt(Tuple.point(Math.sqrt(3) / 3, Math.sqrt(3) / 3, Math.sqrt(3) / 3));

        assertEquals(expectedNormal, actualNormal);
    }


    @Test
    void aSphereIsAShape() {
        assertTrue((new Sphere()) instanceof Shape);
    }
}