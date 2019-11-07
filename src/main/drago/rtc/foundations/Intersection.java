package drago.rtc.foundations;

import drago.rtc.shape.Shape;

import java.util.ArrayList;
import java.util.Arrays;

public class Intersection implements Comparable<Intersection> {
    private final double t;
    private final Shape object;
    private final double u;
    private final double v;

    public Intersection(double t, Shape object) {
        this(t, object, 0, 0);
    }

    public Intersection(double t, Shape object, double u, double v) {
        this.t = t;
        this.object = object;
        this.u = u;
        this.v = v;
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

    public Computations prepareComputations(Ray ray, Intersection[] xs) {

        Tuple position = ray.position(this.t);
        Tuple eyeV = ray.getDirection().scale(-1);
        Tuple normalV = this.object.normalAt(position, this);

        double n1 = 0, n2 = 0;
        ArrayList<Shape> containers = new ArrayList<>();

        for (Intersection x : xs) {
            if (x.equals(this)) {
                if (containers.isEmpty()) {
                    n1 = 1.0;
                } else {
                    n1 = containers.get(containers.size() - 1).getMaterial().getRefractiveIndex();
                }
            }

            if (containers.indexOf(x.getObject()) >= 0) {
                containers.remove(x.getObject());
            } else {
                containers.add(x.getObject());
            }

            if (x.equals(this)) {
                if (containers.isEmpty()) {
                    n2 = 1.0;
                } else {
                    n2 = containers.get(containers.size() - 1).getMaterial().getRefractiveIndex();
                }

                break;
            }
        }

        return new Computations(this.t, this.object, position, eyeV, normalV, n1, n2);
    }

    public double getU() {
        return u;
    }

    public double getV() {
        return v;
    }
}
