package drago.rtc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RingPatternTest {

    @Test
    void aRingShouldExtendInBothXAndZ() {
        Pattern p = Pattern.ringPattern(Color.WHITE, Color.BLACK);

        assertEquals(Color.WHITE, p.patternAt(Tuple.point(0, 0, 0)));
        assertEquals(Color.BLACK, p.patternAt(Tuple.point(1, 0, 0)));
        assertEquals(Color.BLACK, p.patternAt(Tuple.point(0, 0, 1)));
        assertEquals(Color.BLACK, p.patternAt(Tuple.point(0.708, 0, 0.708)));
    }
}
