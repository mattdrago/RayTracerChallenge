package drago.rtc;

import drago.rtc.shape.Shape;

class Computations {
    static final double EPSILON = 0.000001;

    private final double t;
    private final Shape object;
    private final Tuple point;
    private final Tuple eyeV;
    private final Tuple normalV;
    private final boolean isInside;
    private final Tuple overPoint;

    Computations(double t, Shape object, Tuple point, Tuple eyeV, Tuple normalV) {
        this.t = t;
        this.object = object;
        this.point = point;
        this.eyeV = eyeV;

        if(normalV.dot(eyeV) < 0) {
            isInside = true;
            this.normalV = normalV.scale(-1);
        } else {
            isInside = false;
            this.normalV = normalV;
        }

        overPoint = point.add(this.normalV.scale(Computations.EPSILON));

    }

    double getT() {
        return t;
    }

    Shape getObject() {
        return object;
    }

    Tuple getPoint() {
        return point;
    }

    Tuple getEyeV() {
        return eyeV;
    }

    Tuple getNormalV() {
        return normalV;
    }

    boolean isInside() {
        return isInside;
    }

    Tuple getOverPoint() {
        return overPoint;
    }
}
