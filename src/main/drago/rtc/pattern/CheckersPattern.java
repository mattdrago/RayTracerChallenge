package drago.rtc.pattern;

import drago.rtc.Color;
import drago.rtc.foundations.Tuple;

public class CheckersPattern extends Pattern {

    private final Color colorA;
    private final Color colorB;

    public CheckersPattern(Color colorA, Color colorB) {
        this.colorA = colorA;
        this.colorB = colorB;
    }

    @Override
    Color patternAt(Tuple point) {
        double dist = Math.floor(point.getX()) + Math.floor(point.getY()) + Math.floor(point.getZ());

        return ((dist % 2) == 0) ? colorA : colorB;
    }
}
