package drago.rtc.foundations;

import drago.rtc.shape.Shape;

public class Computations {
    public static final double EPSILON = 0.000001;

    private final double t;
    private final Shape object;
    private final Tuple point;
    private final Tuple eyeV;
    private final Tuple normalV;
    private final Tuple reflectV;
    private final boolean isInside;
    private final Tuple overPoint;
    private final double n1;
    private final double n2;
    private Tuple underPoint;

    Computations(double t, Shape object, Tuple point, Tuple eyeV, Tuple normalV, double n1, double n2) {
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

        Tuple epsilonScaledNormal = this.normalV.scale(Computations.EPSILON);
        overPoint = point.add(epsilonScaledNormal);
        underPoint = point.subtract(epsilonScaledNormal);
        reflectV = eyeV.scale(-1).reflect(this.normalV);

        this.n1 = n1;
        this.n2 = n2;

    }

    double getT() {
        return t;
    }

    public Shape getObject() {
        return object;
    }

    Tuple getPoint() {
        return point;
    }

    public Tuple getEyeV() {
        return eyeV;
    }

    public Tuple getNormalV() {
        return normalV;
    }

    boolean isInside() {
        return isInside;
    }

    public Tuple getOverPoint() {
        return overPoint;
    }

    public Tuple getReflectV() {
        return reflectV;
    }

    public double getN1() {
        return n1;
    }

    public double getN2() {
        return n2;
    }

    public Tuple getUnderPoint() {
        return underPoint;
    }

    public double schlick() {
        double cos = eyeV.dot(normalV);

        if(n1 > n2) {
            double n = n1 / n2;
            double sin2T = n * n * (1.0 - cos * cos);

            if(sin2T > 1.0) {
                return 1.0;
            }

            cos = Math.sqrt(1 - sin2T);
        }

        double r0 = ((n1 - n2) / (n1 + n2));
        r0 *= r0;

        return r0 + (1 - r0) * Math.pow((1 - cos), 5);
    }
}
