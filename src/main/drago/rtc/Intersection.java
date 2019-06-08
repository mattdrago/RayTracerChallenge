package drago.rtc;

import java.util.Arrays;

class Intersection implements Comparable<Intersection> {
    private final double t;
    private final Object object;

    Intersection(double t, Object object) {
        this.t = t;
        this.object = object;
    }

    static Intersection[] intersections(Intersection ... is) {
        return is;
    }

    static Intersection hit(Intersection[] xs) {
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

    double getT() {
        return t;
    }

    Object getObject() {
        return object;
    }

    @Override
    public int compareTo(Intersection o) {
        int compare = Double.compare(this.t, o.t);
        return compare;
    }
}
