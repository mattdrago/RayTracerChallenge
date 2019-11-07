package drago.rtc.shape;

import drago.rtc.foundations.Intersection;
import drago.rtc.foundations.Matrix;
import drago.rtc.foundations.Ray;
import drago.rtc.foundations.Tuple;
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
        Tuple actualNormal = s.localNormalAt(Tuple.point(1, 0, 0), null);

        assertEquals(expectedNormal, actualNormal);
    }

    @Test
    void theNormalOnASphereAtAPointOnTheYAxis() {
        Sphere s = new Sphere();

        Tuple expectedNormal = Tuple.vector(0, 1, 0);
        Tuple actualNormal = s.localNormalAt(Tuple.point(0, 1, 0), null);

        assertEquals(expectedNormal, actualNormal);
    }

    @Test
    void theNormalOnASphereAtAPointOnTheZAxis() {
        Sphere s = new Sphere();

        Tuple expectedNormal = Tuple.vector(0, 0, 1);
        Tuple actualNormal = s.localNormalAt(Tuple.point(0, 0, 1), null);

        assertEquals(expectedNormal, actualNormal);
    }

    @Test
    void theNormalOnASphereAtANonAxialPoint() {
        Sphere s = new Sphere();

        Tuple expectedNormal = Tuple.vector(Math.sqrt(3) / 3, Math.sqrt(3) / 3, Math.sqrt(3) / 3);
        Tuple actualNormal = s.localNormalAt(Tuple.point(Math.sqrt(3) / 3, Math.sqrt(3) / 3, Math.sqrt(3) / 3), null);

        assertEquals(expectedNormal, actualNormal);
    }


    @Test
    void aSphereIsAShape() {
        assertTrue((new Sphere()) instanceof Shape);
    }

    @Test
    void aHelperForProducingASphereWithAGlassyMaterial() {
        Sphere s = Sphere.glassSphere();

        assertEquals(Matrix.identity(4), s.getTransform());
        assertEquals(1.0, s.getMaterial().getTransparency());
        assertEquals(1.5, s.getMaterial().getRefractiveIndex());
        assertEquals(1.0, s.getMaterial().getReflective());
        assertEquals(0.4, s.getMaterial().getDiffuse());
        assertEquals(0.1, s.getMaterial().getAmbient());
        assertEquals(1, s.getMaterial().getSpecular());
        assertEquals(300, s.getMaterial().getShininess());
    }

    @Test
    void aSphereHasABound() {
        Sphere s = new Sphere();

        Tuple expectedMin = Tuple.point(-1, -1, -1);
        Tuple expectedMax = Tuple.point(1, 1, 1);

        Bounds b = s.getBounds();

        assertNotNull(b);
        assertEquals(expectedMin, b.getMin());
        assertEquals(expectedMax, b.getMax());
    }
}