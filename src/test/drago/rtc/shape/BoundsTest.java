package drago.rtc.shape;

import drago.rtc.foundations.Tuple;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoundsTest {

    @Test
    void aBoundsHasAMinimumAndAMaximum() {
        Tuple min = Tuple.point(0, 0, 0);
        Tuple max = Tuple.point(1, 1, 1);

        Bounds b = new Bounds(min, max);

        assertEquals(min, b.getMin());
        assertEquals(max, b.getMax());
    }

}