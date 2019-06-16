package drago.rtc.foundations;

import drago.rtc.shape.Shape;

import java.util.Arrays;

public class Intersection implements Comparable<Intersection> {
    private final double t;
    private final Shape object;

    public Intersection(double t, Shape object) {
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

    public Shape getObject() {
        return object;
    }

    @Override
    public int compareTo(Intersection o) {
        return Double.compare(this.t, o.t);
    }

    public Computations prepareComputations(Ray ray) {

        Tuple position = ray.position(this.t);
        Tuple eyeV = ray.getDirection().scale(-1);
        Tuple normalV = this.object.normalAt(position);

        return new Computations(this.t, this.object, position, eyeV, normalV);
    }
}
