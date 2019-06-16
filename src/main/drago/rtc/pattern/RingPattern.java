package drago.rtc.pattern;

import drago.rtc.Color;
import drago.rtc.Tuple;

public class RingPattern extends Pattern {

    private final Color colorA;
    private final Color colorB;

    public RingPattern(Color colorA, Color colorB) {
        this.colorA = colorA;
        this.colorB = colorB;
    }

    @Override
    Color patternAt(Tuple point) {

        double dist = Math.sqrt(point.getX() * point.getX() + point.getZ() * point.getZ());

        return (Math.floor(dist) % 2 == 0) ? colorA : colorB;
    }
}
