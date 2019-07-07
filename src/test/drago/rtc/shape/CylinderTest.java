package drago.rtc.shape;

import drago.rtc.foundations.Computations;
import drago.rtc.foundations.Intersection;
import drago.rtc.foundations.Ray;
import drago.rtc.foundations.Tuple;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CylinderTest {

    @Test
    void aRayMissesACylinder() {
        Cylinder cyl = new Cylinder();

        Ray[] rs = {
                new Ray(Tuple.point(1, 0, 0), Tuple.vector(0, 1, 0)),
                new Ray(Tuple.point(0, 0, 0), Tuple.vector(0, 1, 0)),
                new Ray(Tuple.point(0, 0, -5), Tuple.vector(1, 1, 1).normalise())
        };

        for (Ray r : rs) {
            Intersection[] xs = cyl.localIntersect(r);

            assertEquals(0, xs.length);
        }
    }

    @Test
    void aRayStrikesACylinder() {
        Cylinder cyl = new Cylinder();

        Ray[] rs = {
                new Ray(Tuple.point(1, 0, -5), Tuple.vector(0, 0, 1)),
                new Ray(Tuple.point(0, 0, -5), Tuple.vector(0, 0, 1)),
                new Ray(Tuple.point(0.5, 0, -5), Tuple.vector(0.1, 1, 1).normalise())
        };

        double[][] expectedTs = {
                {5, 5},
                {4, 6},
                {6.807981, 7.088723}
        };

        for (int i = 0; i < rs.length; i++) {
            Intersection[] xs = cyl.localIntersect(rs[i]);

            assertEquals(2, xs.length);
            assertTrue(Math.abs(expectedTs[i][0] - xs[0].getT()) < Computations.EPSILON);
            assertTrue(Math.abs(expectedTs[i][1] - xs[1].getT()) < Computations.EPSILON);
        }
    }

    @Test
    void normalVectorOnACylinder() {
        Cylinder cyl = new Cylinder();

        Tuple[] ps = {
                Tuple.point(1, 0, 0),
                Tuple.point(0, 5, -1),
                Tuple.point(0, -2, 1),
                Tuple.point(-1, 1, 0)
        };

        Tuple[] expectedNormals = {
                Tuple.vector(1, 0, 0),
                Tuple.vector(0, 0, -1),
                Tuple.vector(0, 0, 1),
                Tuple.vector(-1, 0, 0)
        };

        for (int i = 0; i < ps.length; i++) {
            Tuple actualNormal = cyl.localNormalAt(ps[i]);

            assertEquals(expectedNormals[i], actualNormal);
        }
    }

    @Test
    void theDefaultMinimumAndMaximumForACylinder() {
        Cylinder cyl = new Cylinder();

        assertEquals(Double.NEGATIVE_INFINITY, cyl.getMinimum());
        assertEquals(Double.POSITIVE_INFINITY, cyl.getMaximum());
    }

    @Test
    void intersectingAConstrainedCylinder() {
        Cylinder cyl = new Cylinder();
        cyl.setMinimum(1);
        cyl.setMaximum(2);

        Ray[] rays = {
                new Ray(Tuple.point(0, 1.5, 0), Tuple.vector(0.1, 1, 0).normalise()),
                new Ray(Tuple.point(0, 3, -5), Tuple.vector(0, 0, 1)),
                new Ray(Tuple.point(0, 0, -5), Tuple.vector(0, 0, 1)),
                new Ray(Tuple.point(0, 2, -5), Tuple.vector(0, 0, 1)),
                new Ray(Tuple.point(0, 1, -5), Tuple.vector(0, 0, 1)),
                new Ray(Tuple.point(0, 1.5, -5), Tuple.vector(0, 0, 1))
        };

        int[] expectedIntersectionCount = {0, 0, 0, 0, 0, 2};

        for (int i = 0; i < rays.length; i++) {
            Intersection[] xs = cyl.localIntersect(rays[i]);

            assertEquals(expectedIntersectionCount[i], xs.length);
        }
    }

    @Test
    void theDefaultClosedValueForACylinder() {
        assertFalse((new Cylinder().isClosed()));
    }

    @Test
    void intersectingTheCapsOfAClosedCylinder() {
        Cylinder cyl = new Cylinder();
        cyl.setMinimum(1);
        cyl.setMaximum(2);
        cyl.setClosed(true);

        Ray[] rays = {
                new Ray(Tuple.point(0, 3, 0), Tuple.vector(0, -1, 0).normalise()),
                new Ray(Tuple.point(0, 3, -2), Tuple.vector(0, -1, 2).normalise()),
                new Ray(Tuple.point(0, 4, -2), Tuple.vector(0, -1, 1).normalise()),
                new Ray(Tuple.point(0, 0, -2), Tuple.vector(0, 1, 2).normalise()),
                new Ray(Tuple.point(0, -1, -2), Tuple.vector(0, 1, 1).normalise())
        };

        int[] expectedIntersectionCount = {2, 2, 2, 2, 2};

        for (int i = 0; i < rays.length; i++) {
            Intersection[] xs = cyl.localIntersect(rays[i]);

            assertEquals(expectedIntersectionCount[i], xs.length);
        }

    }

    @Test
    void theNormalVectorOnACylindersEndCaps() {
        Cylinder cyl = new Cylinder();
        cyl.setMinimum(1);
        cyl.setMaximum(2);
        cyl.setClosed(true);

        Tuple[] points = {
                Tuple.point(0, 1, 0),
                Tuple.point(0.5, 1, 0),
                Tuple.point(0, 1, 0.5),
                Tuple.point(0, 2, 0),
                Tuple.point(0.5, 2, 0),
                Tuple.point(0, 2, 0.5)
        };

        Tuple[] normals = {
                Tuple.vector(0, -1, 0),
                Tuple.vector(0, -1, 0),
                Tuple.vector(0, -1, 0),
                Tuple.vector(0, 1, 0),
                Tuple.vector(0, 1, 0),
                Tuple.vector(0, 1, 0)
        };

        for (int i = 0; i < points.length; i++) {
            assertEquals(normals[i], cyl.localNormalAt(points[i]));
        }
    }
}
