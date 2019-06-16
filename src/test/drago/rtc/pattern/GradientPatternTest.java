package drago.rtc.pattern;

import drago.rtc.Color;
import drago.rtc.Tuple;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class GradientPatternTest {

    @Test
    void aGradientLinearlyInterpolatesBetweenColors() {
        Pattern p = new GradientPattern(Color.WHITE, Color.BLACK);

        assertEquals(Color.WHITE, p.patternAt(Tuple.point(0, 0, 0)));
        assertEquals(new Color(0.25, 0.25, 0.25), p.patternAt(Tuple.point(0.75, 0, 0)));
        assertEquals(new Color(0.5, 0.5, 0.5), p.patternAt(Tuple.point(0.5, 0, 0)));
        assertEquals(new Color(0.75, 0.75, 0.75), p.patternAt(Tuple.point(0.25, 0, 0)));
    }
}
