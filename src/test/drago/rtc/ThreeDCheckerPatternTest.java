package drago.rtc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ThreeDCheckerPatternTest {

    @Test
    void checkersShouldRepeatInX() {
        Pattern p = Pattern.checkersPattern(Color.WHITE, Color.BLACK);

        assertEquals(Color.WHITE, p.patternAt(Tuple.point(0, 0, 0)));
        assertEquals(Color.WHITE, p.patternAt(Tuple.point(0.99, 0, 0)));
        assertEquals(Color.BLACK, p.patternAt(Tuple.point(1.01, 0, 0)));
    }

    @Test
    void checkersShouldRepeatInY() {
        Pattern p = Pattern.checkersPattern(Color.WHITE, Color.BLACK);

        assertEquals(Color.WHITE, p.patternAt(Tuple.point(0, 0, 0)));
        assertEquals(Color.WHITE, p.patternAt(Tuple.point(0, 0.99, 0)));
        assertEquals(Color.BLACK, p.patternAt(Tuple.point(0, 1.01, 0)));
    }

    @Test
    void checkersShouldRepeatInZ() {
        Pattern p = Pattern.checkersPattern(Color.WHITE, Color.BLACK);

        assertEquals(Color.WHITE, p.patternAt(Tuple.point(0, 0, 0)));
        assertEquals(Color.WHITE, p.patternAt(Tuple.point(0, 0, 0.99)));
        assertEquals(Color.BLACK, p.patternAt(Tuple.point(0, 0, 1.01)));
    }
}
