package drago.rtc.pattern;

import drago.rtc.Color;
import drago.rtc.foundations.Tuple;

public class StripePattern extends Pattern {
    private final Color colorA;
    private final Color colorB;

    public StripePattern(Color colorA, Color colorB) {
        this.colorA = colorA;
        this.colorB = colorB;
    }

    @Override
    Color patternAt(Tuple point) {
        int modulo = (int)(point.getX() % 2 + 2) % 2;

        if(modulo == 0) {
            return colorA;
        } else {
            return colorB;
        }
    }
}
