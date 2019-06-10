package drago.rtc;

class Computations {
    private final double t;
    private final Sphere object;
    private final Tuple point;
    private final Tuple eyeV;
    private final Tuple normalV;
    private final boolean isInside;

    Computations(double t, Sphere object, Tuple point, Tuple eyeV, Tuple normalV) {
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
    }

    double getT() {
        return t;
    }

    Sphere getObject() {
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
}
