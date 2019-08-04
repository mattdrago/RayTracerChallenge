package drago.rtc.shape;

import drago.rtc.foundations.Tuple;

class Bounds {
    private final Tuple min;
    private final Tuple max;

    Bounds(Tuple min, Tuple max) {
        this.min = min;
        this.max = max;
    }

    Tuple getMin() {
        return min;
    }

    Tuple getMax() {
        return max;
    }
}
