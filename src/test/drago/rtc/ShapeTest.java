package drago.rtc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShapeTest {

    @Test
    void theDefaultTransformation() {
        Shape s = testShape();

        assertEquals(Matrix.identity(4), s.getTransform());
    }

    @Test
    void assigningATransformation() {
        Shape s = testShape();
        Matrix t = Matrix.translation(2, 3, 4);

        s.setTransform(t);

        assertEquals(t, s.getTransform());
    }

    @Test
    void aShapeHasADefaultMaterial() {
        Shape s = testShape();

        Material m = new Material();

        assertEquals(m, s.getMaterial());
    }

    @Test
    void aShapeMayBeAssignedAMaterial() {
        Shape s = testShape();

        Material m = new Material();
        m.setAmbient(1.0);

        s.setMaterial(m);

        assertEquals(m, s.getMaterial());
    }

    @Test
    void intersectingAScaledShapeWithARay() {
        Ray r = new Ray(Tuple.point(0, 0, -5), Tuple.vector(0, 0, 1));
        TestShape s = testShape();

        s.setTransform(Matrix.scaling(2, 2, 2));
        Intersection[] xs = s.intersects(r);

        assertEquals(Tuple.point(0, 0, -2.5), s.savedRay.getOrigin());
        assertEquals(Tuple.vector(0, 0, 0.5), s.savedRay.getDirection());
    }

    @Test
    void intersectingATranslatedShapeWithARay() {
        Ray r = new Ray(Tuple.point(0, 0, -5), Tuple.vector(0, 0, 1));
        TestShape s = testShape();

        s.setTransform(Matrix.translation(5, 0, 0));
        Intersection[] xs = s.intersects(r);

        assertEquals(Tuple.point(-5, 0, -5), s.savedRay.getOrigin());
        assertEquals(Tuple.vector(0, 0, 1), s.savedRay.getDirection());
    }

    @Test
    void computingTheNormalOnaATranslatedShape() {
        Shape s = testShape();
        s.setTransform(Matrix.translation(0, 1, 0));

        Tuple expectedNormal = Tuple.vector(0, 0.70711, -0.70711);
        Tuple actualNormal = s.normalAt(Tuple.point(0, 1.70711, -0.70711));

        assertEquals(expectedNormal, actualNormal);
    }

    @Test
    void computingTheNormalOnATransformedShape() {
        Shape s = testShape();
        s.setTransform(Matrix.scaling(1, 0.5, 1).multiplyBy(Matrix.rotationZ(Math.PI / 5)));

        Tuple expectedNormal = Tuple.vector(0, 0.97014, -0.24254);
        Tuple actualNormal = s.normalAt(Tuple.point(0, Math.sqrt(2) / 2, - Math.sqrt(2) / 2));

        assertEquals(expectedNormal, actualNormal);
    }


    @Test
    void theNormalIsANormalisedVector() {
        Shape s = testShape();
        Tuple normal = s.normalAt(Tuple.point(1, 2, 3));

        assertEquals(normal.normalise(), normal);
    }

    private TestShape testShape() {
        return new TestShape();
    }

    private class TestShape extends Shape {
        Ray savedRay;

        @Override
        Intersection[] localIntersect(Ray ray) {
            this.savedRay = ray;

            return new Intersection[0];
        }

        @Override
        Tuple localNormalAt(Tuple point) {
            return Tuple.vector(point.getX(), point.getY(), point.getZ());
        }
    }
}