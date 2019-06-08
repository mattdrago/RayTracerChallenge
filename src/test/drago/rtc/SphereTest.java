package drago.rtc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SphereTest {

    @Test
    void aRayIntersectsASphereAtTwoPoints() {
        Ray r = new Ray(Tuple.point(0, 0, -5), Tuple.vector(0, 0, 1));
        Sphere s = new Sphere();

        Intersection[] ts = s.intersects(r);

        assertEquals(2, ts.length);
        assertEquals(4.0, ts[0].getT());
        assertEquals(6.0, ts[1].getT());
    }

    @Test
    void aRayIntersectsASphereAtATangent() {
        Ray r = new Ray(Tuple.point(0, 1, -5), Tuple.vector(0, 0, 1));
        Sphere s = new Sphere();

        Intersection[] ts = s.intersects(r);

        assertEquals(2, ts.length);
        assertEquals(5.0, ts[0].getT());
        assertEquals(5.0, ts[1].getT());
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

        Intersection [] ts = s.intersects(r);

        assertEquals(2, ts.length);
        assertEquals(-1.0, ts[0].getT());
        assertEquals(1.0, ts[1].getT());
    }

    @Test
    void aSphereIsBehindARay() {
        Ray r = new Ray(Tuple.point(0, 0, 5), Tuple.vector(0, 0, 1));
        Sphere s = new Sphere();

        Intersection[] ts = s.intersects(r);

        assertEquals(2, ts.length);
        assertEquals(-6.0, ts[0].getT());
        assertEquals(-4.0, ts[1].getT());
    }

    @Test
    void intersectSetsTheObjectOnTheIntersection() {
        Ray r = new Ray(Tuple.point(0, 0, -5), Tuple.vector(0, 0, 1));
        Sphere s = new Sphere();

        Intersection[] xs = s.intersects(r);

        assertEquals(s, xs[0].getObject());
        assertEquals(s, xs[1].getObject());
    }

    @Test
    void aSpheresDefaultTransformation() {
        Sphere s = new Sphere();

        assertEquals(Matrix.identity(4), s.getTransform());
    }

    @Test
    void changingASpheresTransformation() {
        Sphere s = new Sphere();
        Matrix t = Matrix.translation(2, 3, 4);

        s.setTransform(t);

        assertEquals(t, s.getTransform());
    }

    @Test
    void intersectingAScaledSphereWithARay() {
        Ray r = new Ray(Tuple.point(0, 0, -5), Tuple.vector(0, 0, 1));
        Sphere s = new Sphere();
        s.setTransform(Matrix.scaling(2, 2, 2));

        Intersection[] xs = s.intersects(r);

        assertEquals(2, xs.length);
        assertEquals(3.0, xs[0].getT());
        assertEquals(7.0, xs[1].getT());
    }

    @Test
    void intersectingATranslatedSPhereWithARay() {
        Ray r = new Ray(Tuple.point(0, 0, -5), Tuple.vector(0, 0, 1));
        Sphere s = new Sphere();
        s.setTransform(Matrix.translation(5, 0, 0));

        Intersection[] xs = s.intersects(r);

        assertEquals(0, xs.length);
    }

    @Test
    void theNormalOnASphereAtAPointOnTheXAxis() {
        Sphere s = new Sphere();

        Tuple expectedNormal = Tuple.vector(1, 0, 0);
        Tuple actualNormal = s.normalAt(Tuple.point(1, 0, 0));

        assertEquals(expectedNormal, actualNormal);
    }

    @Test
    void theNormalOnASphereAtAPointOnTheYAxis() {
        Sphere s = new Sphere();

        Tuple expectedNormal = Tuple.vector(0, 1, 0);
        Tuple actualNormal = s.normalAt(Tuple.point(0, 1, 0));

        assertEquals(expectedNormal, actualNormal);
    }

    @Test
    void theNormalOnASphereAtAPointOnTheZAxis() {
        Sphere s = new Sphere();

        Tuple expectedNormal = Tuple.vector(0, 0, 1);
        Tuple actualNormal = s.normalAt(Tuple.point(0, 0, 1));

        assertEquals(expectedNormal, actualNormal);
    }

    @Test
    void theNormalOnASphereAtANonAxialPoint() {
        Sphere s = new Sphere();

        Tuple expectedNormal = Tuple.vector(Math.sqrt(3) / 3, Math.sqrt(3) / 3, Math.sqrt(3) / 3);
        Tuple actualNormal = s.normalAt(Tuple.point(Math.sqrt(3) / 3, Math.sqrt(3) / 3, Math.sqrt(3) / 3));

        assertEquals(expectedNormal, actualNormal);
    }

    @Test
    void theNormalIsANormalisedVector() {
        Sphere s = new Sphere();
        Tuple normal = s.normalAt(Tuple.point(Math.sqrt(3) / 3, Math.sqrt(3) / 3, Math.sqrt(3) / 3));

        assertEquals(normal, normal.normalise());
    }

    @Test
    void computingTheNormalOnATranslatedSphere() {
        Sphere s = new Sphere();
        s.setTransform(Matrix.translation(0, 1, 0));

        Tuple expectedNormal = Tuple.vector(0, 0.70711, -0.70711);
        Tuple actualNormal = s.normalAt(Tuple.point(0,1.70711, -0.70711));

        assertEquals(expectedNormal, actualNormal);
    }

    @Test
    void computingTheNormailOnATransformedSphere() {
        Sphere s = new Sphere();
        s.setTransform(Matrix.scaling(1, 0.5, 1).multiplyBy(Matrix.rotationZ(Math.PI / 5)));

        Tuple expectedNormal = Tuple.vector(0, 0.97014, -0.24254);
        Tuple actualNormal =  s.normalAt(Tuple.point(0, Math.sqrt(2) / 2, -Math.sqrt(2) / 2));

        assertEquals(expectedNormal, actualNormal);
    }
}