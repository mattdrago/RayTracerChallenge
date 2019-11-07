package drago.rtc.shape;

import drago.rtc.*;
import drago.rtc.foundations.Intersection;
import drago.rtc.foundations.Matrix;
import drago.rtc.foundations.Ray;
import drago.rtc.foundations.Tuple;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShapeTest {

    @Test
    void theDefaultTransformation() {
        Shape s = new TestShape();

        Assertions.assertEquals(Matrix.identity(4), s.getTransform());
    }

    @Test
    void assigningATransformation() {
        Shape s = new TestShape();
        Matrix t = Matrix.translation(2, 3, 4);

        s.setTransform(t);

        assertEquals(t, s.getTransform());
    }

    @Test
    void aShapeHasADefaultMaterial() {
        Shape s = new TestShape();

        Material m = new Material();

        assertEquals(m, s.getMaterial());
    }

    @Test
    void aShapeMayBeAssignedAMaterial() {
        Shape s = new TestShape();

        Material m = new Material();
        m.setAmbient(1.0);

        s.setMaterial(m);

        assertEquals(m, s.getMaterial());
    }

    @Test
    void intersectingAScaledShapeWithARay() {
        Ray r = new Ray(Tuple.point(0, 0, -5), Tuple.vector(0, 0, 1));
        TestShape s = new TestShape();

        s.setTransform(Matrix.scaling(2, 2, 2));
        s.intersects(r);

        assertEquals(Tuple.point(0, 0, -2.5), s.savedRay.getOrigin());
        assertEquals(Tuple.vector(0, 0, 0.5), s.savedRay.getDirection());
    }

    @Test
    void intersectingATranslatedShapeWithARay() {
        Ray r = new Ray(Tuple.point(0, 0, -5), Tuple.vector(0, 0, 1));
        TestShape s = new TestShape();

        s.setTransform(Matrix.translation(5, 0, 0));
        s.intersects(r);

        assertEquals(Tuple.point(-5, 0, -5), s.savedRay.getOrigin());
        assertEquals(Tuple.vector(0, 0, 1), s.savedRay.getDirection());
    }

    @Test
    void computingTheNormalOnaATranslatedShape() {
        Shape s = new TestShape();
        s.setTransform(Matrix.translation(0, 1, 0));

        Tuple expectedNormal = Tuple.vector(0, 0.70711, -0.70711);
        Tuple actualNormal = s.normalAt(Tuple.point(0, 1.70711, -0.70711), null);

        assertEquals(expectedNormal, actualNormal);
    }

    @Test
    void computingTheNormalOnATransformedShape() {
        Shape s = new TestShape();
        s.setTransform(Matrix.scaling(1, 0.5, 1).multiplyBy(Matrix.rotationZ(Math.PI / 5)));

        Tuple expectedNormal = Tuple.vector(0, 0.97014, -0.24254);
        Tuple actualNormal = s.normalAt(Tuple.point(0, Math.sqrt(2) / 2, - Math.sqrt(2) / 2), null);

        assertEquals(expectedNormal, actualNormal);
    }


    @Test
    void theNormalIsANormalisedVector() {
        Shape s = new TestShape();
        Tuple normal = s.normalAt(Tuple.point(1, 2, 3), null);

        assertEquals(normal.normalise(), normal);
    }

    @Test
    void aShapeHasAParentAttribute() {
        Shape s = new TestShape();

        assertNull(s.getParent());
    }

    @Test
    void convertingAPointFromWorldToObjectSpace() {
        Group g1 = new Group();
        g1.setTransform(Matrix.rotationY(Math.PI / 2.0));

        Group g2 = new Group();
        g2.setTransform(Matrix.scaling(2, 2, 2));

        g1.addChild(g2);

        Sphere s = new Sphere();
        s.setTransform(Matrix.translation(5, 0, 0));

        g2.addChild(s);

        Tuple calculatedPoint = s.worldToObject(Tuple.point(-2, 0, -10));
        Tuple expectedPoint = Tuple.point(0, 0, -1);

        assertEquals(expectedPoint, calculatedPoint);
    }

    @Test
    void convertingANormalFromObjectToWorldSpace() {
        Group g1 = new Group();
        g1.setTransform(Matrix.rotationY(Math.PI / 2.0));

        Group g2 = new Group();
        g2.setTransform(Matrix.scaling(1, 2, 3));
        g1.addChild(g2);

        Sphere s = new Sphere();
        s.setTransform(Matrix.translation(5, 0, 0));
        g2.addChild(s);

        double val = Math.sqrt(3) / 3;
        Tuple calculatedNormal = s.normalToWorld(Tuple.vector(val, val, val));
        Tuple expectedNormal = Tuple.vector(0.28571, 0.42857, -0.85714);

        assertEquals(expectedNormal, calculatedNormal);
    }

    @Test
    void findingTheNormalOnAChildObject() {
        Group g1 = new Group();
        g1.setTransform(Matrix.rotationY(Math.PI / 2.0));

        Group g2 = new Group();
        g2.setTransform(Matrix.scaling(1, 2, 3));
        g1.addChild(g2);

        Sphere s = new Sphere();
        s.setTransform(Matrix.translation(5, 0,0));
        g2.addChild(s);

        Tuple calculatedNormal = s.normalAt(Tuple.point(1.7321, 1.1547, -5.5774), null);
        Tuple expectedNormal = Tuple.vector(0.28570, 0.42854, -0.85716);

        assertEquals(expectedNormal, calculatedNormal);
    }

    private static class TestShape extends Shape {
        Ray savedRay;

        @Override
        Intersection[] localIntersect(Ray ray) {
            this.savedRay = ray;

            return new Intersection[0];
        }

        @Override
        Tuple localNormalAt(Tuple point, Intersection intersection) {
            return Tuple.vector(point.getX(), point.getY(), point.getZ());
        }

        @Override
        Bounds getBounds() {
            return null;
        }
    }
}