package drago.rtc.pattern;

import drago.rtc.Color;
import drago.rtc.Tuple;

public class GradientPattern extends Pattern {

    private final Color from;
    private final Color to;

    public GradientPattern(Color from, Color to) {
        this.from = from;
        this.to = to;
    }

    @Override
    Color patternAt(Tuple point) {
        return to.subtract(from).scale(point.getX() - Math.floor(point.getX())).add(from);
    }
}
