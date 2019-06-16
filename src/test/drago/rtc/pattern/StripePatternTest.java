package drago.rtc.pattern;

import drago.rtc.Color;
import drago.rtc.foundations.Tuple;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StripePatternTest {

    @Test
    void aStripePatternIsConstantInY() {
        Pattern p = new StripePattern(Color.WHITE, Color.BLACK);

        assertEquals(Color.WHITE, p.patternAt(Tuple.point(0, 0, 0)));
        assertEquals(Color.WHITE, p.patternAt(Tuple.point(0, 1, 0)));
        assertEquals(Color.WHITE, p.patternAt(Tuple.point(0, 2, 0)));
    }

    @Test
    void aStripePatternIsConstantInZ() {
        Pattern p = new StripePattern(Color.WHITE, Color.BLACK);

        assertEquals(Color.WHITE, p.patternAt(Tuple.point(0, 0, 0)));
        assertEquals(Color.WHITE, p.patternAt(Tuple.point(0, 0, 1)));
        assertEquals(Color.WHITE, p.patternAt(Tuple.point(0, 0, 2)));
    }

    @Test
    void aStripePatternAlternatesInX() {
        Pattern p = new StripePattern(Color.WHITE, Color.BLACK);

        assertEquals(Color.WHITE, p.patternAt(Tuple.point(0, 0, 0)));
        assertEquals(Color.WHITE, p.patternAt(Tuple.point(0.9, 0, 0)));
        assertEquals(Color.BLACK, p.patternAt(Tuple.point(1, 0, 0)));
        assertEquals(Color.BLACK, p.patternAt(Tuple.point(1.1, 0, 0)));
        assertEquals(Color.BLACK, p.patternAt(Tuple.point(-0.1, 0, 0)));
        assertEquals(Color.BLACK, p.patternAt(Tuple.point(-1, 0, 0)));
        assertEquals(Color.WHITE, p.patternAt(Tuple.point(-1.1, 0, 0)));
    }
}