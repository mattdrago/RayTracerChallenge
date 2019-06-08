package drago.rtc;

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

}