package drago.rtc.shape;

import drago.rtc.foundations.Intersection;
import drago.rtc.foundations.Matrix;
import drago.rtc.foundations.Ray;
import drago.rtc.foundations.Tuple;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSGTest {

    @Test
    void csgIsCreatedWithAnOperationAndTwoShapes() {
        Shape s1 = new Sphere();
        Shape s2 = new Cube();

        CSG c = new CSG(CSG.Operation.UNION, s1, s2);

        assertEquals(s1, c.getLeft());
        assertEquals(s2, c.getRight());
        assertEquals(c, s1.getParent());
        assertEquals(c, s2.getParent());
    }

    @Test
    void evaluatingTheRuleForACsgOperationUnion() {
        boolean [][] inputs = {
                {true, true, true},
                {true, true, false},
                {true, false, true},
                {true, false, false},
                {false, true, true},
                {false, true, false},
                {false, false, true},
                {false, false, false}
        };

        boolean [] results = {
                false,
                true,
                false,
                true,
                false,
                false,
                true,
                true
        };

        for (int index = 0; index < inputs.length; index++) {
            boolean[] args = inputs[index];
            assertEquals(results[index], CSG.Operation.UNION.intersectionAllowed(args[0], args[1], args[2]));
        }
    }

    @Test
    void evaluatingTheRuleForACsgOperationIntersection() {
        boolean [][] inputs = {
                {true, true, true},
                {true, true, false},
                {true, false, true},
                {true, false, false},
                {false, true, true},
                {false, true, false},
                {false, false, true},
                {false, false, false}
        };

        boolean [] results = {
                true,
                false,
                true,
                false,
                true,
                true,
                false,
                false
        };

        for (int index = 0; index < inputs.length; index++) {
            boolean[] args = inputs[index];
            assertEquals(results[index], CSG.Operation.INTERSECTION.intersectionAllowed(args[0], args[1], args[2]));
        }
    }

    @Test
    void evaluatingTheRuleForACsgOperationDifference() {
        boolean [][] inputs = {
                {true, true, true},
                {true, true, false},
                {true, false, true},
                {true, false, false},
                {false, true, true},
                {false, true, false},
                {false, false, true},
                {false, false, false}
        };

        boolean [] results = {
                false,
                true,
                false,
                true,
                true,
                true,
                false,
                false
        };

        for (int index = 0; index < inputs.length; index++) {
            boolean[] args = inputs[index];
            assertEquals(results[index], CSG.Operation.DIFFERENCE.intersectionAllowed(args[0], args[1], args[2]));
        }
    }

    @Test
    void filteringAListOfIntersections() {
        Shape s1 = new Sphere();
        Shape s2 = new Cube();

        Intersection[] xs = {
                new Intersection(1, s1),
                new Intersection(2, s2),
                new Intersection(3, s1),
                new Intersection(4, s2)
        };

        CSG.Operation[] operations = {
                CSG.Operation.UNION,
                CSG.Operation.INTERSECTION,
                CSG.Operation.DIFFERENCE,
        };

        Intersection[][] results = {
                {xs[0], xs[3]},
                {xs[1], xs[2]},
                {xs[0], xs[1]}
        };

        for (int index = 0; index < operations.length; index++) {
            CSG c = new CSG(operations[index], s1, s2);

            Intersection[] actual = c.filterIntersections(xs);

            assertEquals(2, actual.length);
            assertEquals(results[index][0], actual[0]);
            assertEquals(results[index][1], actual[1]);
        }
    }

    @Test
    void aCSGIncludesAddedShapes() {
        Shape s1 = new Sphere();
        Shape s2 = new Sphere();

        CSG c = new CSG(CSG.Operation.UNION, s1, s2);

        assertTrue(c.includes(s1));
        assertTrue(c.includes(s2));
    }

    @Test
    void aCSGDoesNotIncludeAShapeNotAdded() {
        Shape s = new Sphere();

        CSG c = new CSG(CSG.Operation.UNION, new Sphere(), new Sphere());

        assertFalse(c.includes(s));
    }

    @Test
    void aCSGIncludesShapesAddedToGroupOrCSGShapes() {
        Shape s1 = new Sphere();
        Shape s2 = new Cube();
        Shape s3 = new Cone();

        Group g = new Group();
        g.addChild(s1);

        CSG c1 = new CSG(CSG.Operation.UNION, s2, s3);

        CSG c2 = new CSG(CSG.Operation.UNION, c1, g);

        assertTrue(c2.includes(s1));
        assertTrue(c2.includes(s2));
        assertTrue(c2.includes(s3));
    }

    @Test
    void aRayMissesACsgObject() {
        CSG c = new CSG(CSG.Operation.UNION, new Sphere(), new Cube());
        Ray r = new Ray(Tuple.point(0, 2, -5), Tuple.vector(0, 0, 1));

        Intersection[] xs = c.localIntersect(r);

        assertEquals(0, xs.length);
    }

    @Test
    void aRayHitsACsgObject() {
        Sphere s1 = new Sphere();
        Sphere s2 = new Sphere();
        s2.setTransform(Matrix.translation(0, 0, 0.5));

        CSG csg = new CSG(CSG.Operation.UNION, s1, s2);
        Ray r = new Ray(Tuple.point(0, 0, -5), Tuple.vector(0, 0, 1));

        Intersection[] xs = csg.localIntersect(r);

        assertEquals(2, xs.length);

        assertEquals(4, xs[0].getT());
        assertEquals(s1, xs[0].getShape());

        assertEquals(6.5, xs[1].getT());
        assertEquals(s2, xs[1].getShape());
    }

    @Test
    void naiveBounds() {
        Sphere s1 = new Sphere();
        Sphere s2 = new Sphere();
        s2.setTransform(Matrix.translation(0.5, 0.5, 0.5));
        CSG csg = new CSG(CSG.Operation.UNION, s1, s2);

        Bounds expected = new Bounds(Tuple.point(-1, -1, -1), Tuple.point(1.5, 1.5, 1.5));
        Bounds actual = csg.getBounds();

        assertEquals(expected, actual);
    }
}
