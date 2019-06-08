package drago.rtc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SphereTest {

    @Test
    void aRayIntersectsASphereAtTwoPoints() {
        Ray r = new Ray(Tuple.point(0, 0, -5), Tuple.vector(0, 0, 1));
        Sphere s = new Sphere();

        double[] ts = s.intersects(r);

        assertEquals(2, ts.length);
        assertEquals(4.0, ts[0]);
        assertEquals(6.0, ts[1]);
    }

    @Test
    void aRayIntersectsASphereAtATangent() {
        Ray r = new Ray(Tuple.point(0, 1, -5), Tuple.vector(0, 0, 1));
        Sphere s = new Sphere();

        double[] ts = s.intersects(r);

        assertEquals(2, ts.length);
        assertEquals(5.0, ts[0]);
        assertEquals(5.0, ts[1]);
    }

    @Test
    void aRayMissesASphere() {
        Ray r = new Ray(Tuple.point(0, 2, -5), Tuple.vector(0, 0, 1));
        Sphere s = new Sphere();

        assertEquals(0, s.intersects(r).length);
    }

    @Test
    void aRayOriginatesInsideASphere() {
        Ray r = new Ray(Tuple.point(0, 0, 0), Tuple.vector(0, 0, 1));
        Sphere s = new Sphere();

        double [] ts = s.intersects(r);

        assertEquals(2, ts.length);
        assertEquals(-1.0, ts[0]);
        assertEquals(1.0, ts[1]);
    }

    @Test
    void aSphereIsBehindARay() {
        Ray r = new Ray(Tuple.point(0, 0, 5), Tuple.vector(0, 0, 1));
        Sphere s = new Sphere();

        double[] ts = s.intersects(r);

        assertEquals(2, ts.length);
        assertEquals(-6.0, ts[0]);
        assertEquals(-4.0, ts[1]);
    }


}