package drago.rtc.pattern;

import drago.rtc.Color;
import drago.rtc.foundations.Tuple;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ThreeDCheckerPatternTest {

    @Test
    void checkersShouldRepeatInX() {
        Pattern p = new CheckersPattern(Color.WHITE, Color.BLACK);

        assertEquals(Color.WHITE, p.patternAt(Tuple.point(0, 0, 0)));
        assertEquals(Color.WHITE, p.patternAt(Tuple.point(0.99, 0, 0)));
        assertEquals(Color.BLACK, p.patternAt(Tuple.point(1.01, 0, 0)));
    }

    @Test
    void checkersShouldRepeatInY() {
        Pattern p = new CheckersPattern(Color.WHITE, Color.BLACK);

        assertEquals(Color.WHITE, p.patternAt(Tuple.point(0, 0, 0)));
        assertEquals(Color.WHITE, p.patternAt(Tuple.point(0, 0.99, 0)));
        assertEquals(Color.BLACK, p.patternAt(Tuple.point(0, 1.01, 0)));
    }

    @Test
    void checkersShouldRepeatInZ() {
        Pattern p = new CheckersPattern(Color.WHITE, Color.BLACK);

        assertEquals(Color.WHITE, p.patternAt(Tuple.point(0, 0, 0)));
        assertEquals(Color.WHITE, p.patternAt(Tuple.point(0, 0, 0.99)));
        assertEquals(Color.BLACK, p.patternAt(Tuple.point(0, 0, 1.01)));
    }
}
