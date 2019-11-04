package drago.rtc.shape;

import drago.rtc.foundations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

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

    public Bounds transform(Matrix transform) {
        Tuple[] boundsPoints = {
                transform.multiplyBy(Tuple.point(min.getX(), max.getY(), min.getZ())),
                transform.multiplyBy(Tuple.point(min.getX(), max.getY(), max.getZ())),
                transform.multiplyBy(Tuple.point(max.getX(), max.getY(), min.getZ())),
                transform.multiplyBy(Tuple.point(max.getX(), max.getY(), max.getZ())),
                transform.multiplyBy(Tuple.point(min.getX(), min.getY(), min.getZ())),
                transform.multiplyBy(Tuple.point(min.getX(), min.getY(), max.getZ())),
                transform.multiplyBy(Tuple.point(max.getX(), min.getY(), min.getZ())),
                transform.multiplyBy(Tuple.point(max.getX(), min.getY(), max.getZ()))
        };

        Tuple min = findBound(boundsPoints, Math::min, Double.POSITIVE_INFINITY);
        Tuple max = findBound(boundsPoints, Math::max, Double.NEGATIVE_INFINITY);

        return new Bounds(min, max);
    }

    private Tuple findBound(Tuple[] boundsPoints, BiFunction<Double, Double, Double> op, final double init) {
        double x = init;
        double y = init;
        double z = init;

        for (Tuple p : boundsPoints) {
            x = op.apply(x, p.getX());
            y = op.apply(y, p.getY());
            z = op.apply(z, p.getZ());
        }

        return Tuple.point(x, y, z);
    }

    Bounds combine(Bounds other) {

        if(other == null) {
            return this;
        }

        Tuple[] extremes = {
                this.getMin(),
                this.getMax(),
                other.getMin(),
                other.getMax()
        };

        Tuple min = findBound(extremes, Math::min, Double.POSITIVE_INFINITY);
        Tuple max = findBound(extremes, Math::max, Double.NEGATIVE_INFINITY);

        return new Bounds(min, max);
    }

    boolean intersected(Ray ray) {
        double[] xt = checkAxis(this.getMin().getX(), this.getMax().getX(), ray.getOrigin().getX(), ray.getDirection().getX());
        double[] yt = checkAxis(this.getMin().getY(), this.getMax().getY(), ray.getOrigin().getY(), ray.getDirection().getY());
        double[] zt = checkAxis(this.getMin().getZ(), this.getMax().getZ(), ray.getOrigin().getZ(), ray.getDirection().getZ());

        double tMin = Math.max(Math.max(xt[0], yt[0]), zt[0]);
        double tMax = Math.min(Math.min(xt[1], yt[1]), zt[1]);

        return (tMin <= tMax);
    }

    private double[] checkAxis(double minLoc, double maxLoc, double origin, double direction) {
        double tMinNumerator = minLoc - origin;
        double tMaxNumerator = maxLoc - origin;

        double t1, t2;

        if(Math.abs(direction) >= Computations.EPSILON) {
            t1 = tMinNumerator / direction;
            t2 = tMaxNumerator / direction;
        } else {
            t1 = tMinNumerator * Double.POSITIVE_INFINITY;
            t2 = tMaxNumerator * Double.POSITIVE_INFINITY;
        }

        double[] ts = new double[2];
        ts[0] = Math.min(t1, t2);
        ts[1] = Math.max(t1, t2);

        return ts;
    }

    boolean contains(Bounds other) {
        return (min.getX() < other.min.getX() && min.getY() < other.min.getY() && min.getZ() < other.min.getY()) &&
                (max.getX() > other.max.getX() && max.getY() > other.max.getY() && max.getZ() > other.max.getZ());
    }

    List<Bounds> divide() {

        Tuple center = Tuple.point(
                (min.getX() + max.getX()) / 2.0,
                (min.getY() + max.getZ()) / 2.0,
                (min.getZ() + max.getZ()) / 2.0
        );

        List<Bounds> bounds = new ArrayList<>(8);
        bounds.add(new Bounds(min, center));

        bounds.add(new Bounds(Tuple.point(center.getX(), min.getY(), min.getZ()), Tuple.point(max.getX(), center.getY(), center.getZ())));
        bounds.add(new Bounds(Tuple.point(min.getX(), center.getY(), min.getZ()), Tuple.point(center.getX(), max.getY(), center.getZ())));
        bounds.add(new Bounds(Tuple.point(min.getX(), min.getY(), center.getZ()), Tuple.point(center.getX(), center.getY(), max.getZ())));

        bounds.add(new Bounds(Tuple.point(center.getX(), center.getY(), min.getZ()), Tuple.point(max.getX(), max.getY(), center.getZ())));
        bounds.add(new Bounds(Tuple.point(center.getX(), min.getY(), center.getZ()), Tuple.point(max.getX(), center.getY(), max.getZ())));
        bounds.add(new Bounds(Tuple.point(min.getX(), center.getY(), center.getZ()), Tuple.point(center.getX(), max.getY(), max.getZ())));

        bounds.add(new Bounds(center, max));

        return bounds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bounds bounds = (Bounds) o;
        return Objects.equals(min, bounds.min) &&
                Objects.equals(max, bounds.max);
    }

    @Override
    public int hashCode() {
        return Objects.hash(min, max);
    }
}
