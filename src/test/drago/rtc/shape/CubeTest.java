package drago.rtc.shape;

import drago.rtc.foundations.Intersection;
import drago.rtc.foundations.Ray;
import drago.rtc.foundations.Tuple;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CubeTest {


    @Test
    void aCubeIsAShape() {
        assertTrue((new Cube()) instanceof Shape);
    }

    @Test
    void aRayIntersectsACube() {
        Cube c = new Cube();
        Ray[] rs = {
                new Ray(Tuple.point(5, 0.5, 0), Tuple.vector(-1, 0, 0)), // +x
                new Ray(Tuple.point(-5, 0.5, 0), Tuple.vector(1, 0, 0)), // -x
                new Ray(Tuple.point(0.5, 5, 0), Tuple.vector(0, -1, 0)), // +y
                new Ray(Tuple.point(0.5, -5, 0), Tuple.vector(0, 1, 0)), // -y
                new Ray(Tuple.point(0.5, 0, 5), Tuple.vector(0, 0, -1)), // +z
                new Ray(Tuple.point(0.5, 0, -5), Tuple.vector(0, 0, 1)), // -z
                new Ray(Tuple.point(0, 0.5, 0), Tuple.vector(0, 0, 1)), // inside
        };

        double[] t1s = {4, 4, 4, 4, 4, 4, -1};
        double[] t2s = {6, 6, 6, 6, 6, 6, 1};

        for (int i = 0; i < rs.length; i++) {
            Intersection[] xs = c.localIntersect(rs[i]);

            assertEquals(2, xs.length);
            assertEquals(t1s[i], xs[0].getT());
            assertEquals(t2s[i], xs[1].getT());
        }
    }

    @Test
    void aRayMissesACube() {
        Cube c = new Cube();
        Ray[] rs = {
                new Ray(Tuple.point(-2, 0, 0), Tuple.vector(0.2673, 0.5345, 0.8018)),
                new Ray(Tuple.point(0, -2, 0), Tuple.vector(0.8018, 0.2673, 0.5345)),
                new Ray(Tuple.point(0, 0, -2), Tuple.vector(0.5345, 0.8018, 0.2673)),
                new Ray(Tuple.point(2, 0, 2), Tuple.vector(0, 0, -1)),
                new Ray(Tuple.point(0, 2, 2), Tuple.vector(0, -1, 0)),
                new Ray(Tuple.point(2, 2, 0), Tuple.vector(-1, 0, 0))
        };

        for (Ray r : rs) {
            Intersection[] xs = c.localIntersect(r);
            assertEquals(0, xs.length);

        }
    }

    @Test
    void theNormalOnTheSurfaceOfACube() {
        Cube c = new Cube();
        Tuple[] ps = {
             Tuple.point(1, 0.5, -0.8),
             Tuple.point(-1, -0.2, 0.9),
             Tuple.point(-0.4, 1, -0.1),
             Tuple.point(0.3, -1, -0.7),
             Tuple.point(-0.6, 0.3, 1),
             Tuple.point(0.4, 0.4, -1),
             Tuple.point(1, 1, 1),
             Tuple.point(-1, -1, -1)
        };

        Tuple[] normals = {
                Tuple.vector(1, 0, 0),
                Tuple.vector(-1, 0, 0),
                Tuple.vector(0, 1, 0),
                Tuple.vector(0, -1, 0),
                Tuple.vector(0, 0, 1),
                Tuple.vector(0, 0, -1),
                Tuple.vector(1, 0, 0),
                Tuple.vector(-1, 0, 0)
        };

        for (int i = 0; i < ps.length; i++) {
            assertEquals(normals[i], c.localNormalAt(ps[i]));
        }
    }

    @Test
    void aCubeHasABound() {
        Cube c = new Cube();

        Tuple expectedMin = Tuple.point(-1, -1, -1);
        Tuple expectedMax = Tuple.point(1, 1, 1);

        Bounds b = c.getBounds();

        assertNotNull(b);
        assertEquals(expectedMin, b.getMin());
        assertEquals(expectedMax, b.getMax());
    }
}
