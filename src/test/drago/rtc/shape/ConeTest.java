package drago.rtc.shape;

import drago.rtc.foundations.Computations;
import drago.rtc.foundations.Intersection;
import drago.rtc.foundations.Ray;
import drago.rtc.foundations.Tuple;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConeTest {

    @Test
    void aRayMissesACone() {
        Cone c = new Cone();
        Ray r = new Ray(Tuple.point(0, 0, 0), Tuple.vector(1, 1, 0).normalise());

        Intersection[] xs = c.localIntersect(r);
        assertEquals(0, xs.length);

    }

    @Test
    void aRayHitsACone() {
        Cone c = new Cone();

        Ray[] rays = {
                new Ray(Tuple.point(0, 0, -5), Tuple.vector(0, 0, 1).normalise()),
                new Ray(Tuple.point(0, 0, -5), Tuple.vector(1, 1, 1).normalise()),
                new Ray(Tuple.point(1, 1, -5), Tuple.vector(-0.5, -1, 1).normalise())
        };

        double[][] ts = {
                {5, 5},
                {8.660254, 8.660254},
                {4.550056, 49.449944}
        };

        for (int i = 0; i < rays.length; i++) {
            Intersection[] xs = c.localIntersect(rays[i]);

            assertEquals(2, xs.length);
            assertTrue(Math.abs(ts[i][0] - xs[0].getT()) < Computations.EPSILON);
            assertTrue(Math.abs(ts[i][1] - xs[1].getT()) < Computations.EPSILON);

        }
    }

    @Test
    void intersectingAConeWithARayParallelToOneOfItsHalves() {
        Cone c = new Cone();
        Ray r = new Ray(Tuple.point(0, 0, -1), Tuple.vector(0, 1, 1).normalise());

        Intersection[] xs = c.localIntersect(r);

        assertEquals(1, xs.length);
        assertTrue(Math.abs(0.353553 - xs[0].getT()) < Computations.EPSILON);
    }

    @Test
    void computingTheNormalVectorOnACone() {
        Cone c = new Cone();

        Tuple[] points = {
                Tuple.point(0, 0, 0),
                Tuple.point(1, 1, 1),
                Tuple.point(-1, -1, 0)
        };

        Tuple[] normals = {
                Tuple.vector(0, 0, 0),
                Tuple.vector(1, -Math.sqrt(2), 1),
                Tuple.vector(-1, 1, 0)
        };

        for (int i = 0; i < points.length; i++) {
            assertEquals(normals[i], c.localNormalAt(points[i]));
        }
    }
}
