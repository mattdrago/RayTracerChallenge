package drago.rtc.foundations;

import drago.rtc.foundations.Matrix;
import drago.rtc.foundations.Ray;
import drago.rtc.foundations.Tuple;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RayTest {

    @Test
    void creatingAndQueryingARay() {
        Tuple origin = Tuple.point(1, 2, 3);
        Tuple direction = Tuple.vector(4, 5, 6);

        Ray r = new Ray(origin, direction);

        assertEquals(origin, r.getOrigin());
        assertEquals(direction, r.getDirection());
    }

    @Test
    void computingAPointFromADistance() {
        Ray r = new Ray(Tuple.point(2, 3, 4), Tuple.vector(1, 0, 0));

        assertEquals(Tuple.point(2, 3, 4), r.position(0));
        assertEquals(Tuple.point(3, 3, 4), r.position(1));
        assertEquals(Tuple.point(1, 3, 4), r.position(-1));
        assertEquals(Tuple.point(4.5, 3, 4), r.position(2.5));
    }

    @Test
    void translatingARay() {
        Ray r = new Ray(Tuple.point(1, 2, 3), Tuple.vector(0, 1, 0));
        Matrix m = Matrix.translation(3, 4, 5);

        Ray rExpected = new Ray(Tuple.point(4, 6, 8), Tuple.vector(0, 1, 0));
        Ray rActual = r.transform(m);

        assertEquals(rExpected, rActual);
    }

    @Test
    void scalingARay() {
        Ray r = new Ray(Tuple.point(1, 2, 3), Tuple.vector(0, 1, 0));
        Matrix m = Matrix.scaling(2, 3, 4);

        Ray rExpected = new Ray(Tuple.point(2, 6, 12), Tuple.vector(0, 3, 0));
        Ray rActual = r.transform(m);

        assertEquals(rExpected, rActual);
    }
}