package drago.rtc;

import java.util.Arrays;

public class Intersection implements Comparable<Intersection> {
    private final double t;
    private final Sphere object;

    Intersection(double t, Sphere object) {
        this.t = t;
        this.object = object;
    }

    static Intersection[] intersections(Intersection ... is) {
        return is;
    }

    public static Intersection hit(Intersection[] xs) {
        Arrays.sort(xs);

        Intersection hit = null;

        for (Intersection i : xs) {
            if(i.t >= 0) {
                hit = i;
                break;
            }
        }

        return hit;
    }

    public double getT() {
        return t;
    }

    public Sphere getObject() {
        return object;
    }

    @Override
    public int compareTo(Intersection o) {
        return Double.compare(this.t, o.t);
    }
}
