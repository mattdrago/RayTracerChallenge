package drago.rtc.shape;

import drago.rtc.foundations.Computations;
import drago.rtc.foundations.Intersection;
import drago.rtc.foundations.Ray;
import drago.rtc.foundations.Tuple;

public class Cube extends Shape {
    private static final int MIN = 0;
    private static final int MAX = 1;

    public Intersection[] localIntersect(Ray ray) {

        double[] xt = checkAxis(ray.getOrigin().getX(), ray.getDirection().getX());
        double[] yt = checkAxis(ray.getOrigin().getY(), ray.getDirection().getY());
        double[] zt = checkAxis(ray.getOrigin().getZ(), ray.getDirection().getZ());

        double tMin = Math.max(Math.max(xt[MIN], yt[MIN]), zt[MIN]);
        double tMax = Math.min(Math.min(xt[MAX], yt[MAX]), zt[MAX]);

        if(tMin > tMax) {
            return new Intersection[0];
        } else {
            return new Intersection[]{
                    new Intersection(tMin, this),
                    new Intersection(tMax, this)
            };
        }
    }

    @Override
    Tuple localNormalAt(Tuple objectPoint) {
        Tuple normal;

        double x = Math.abs(objectPoint.getX());
        double y = Math.abs(objectPoint.getY());
        double z = Math.abs(objectPoint.getZ());

        double maxComponent = Math.max(Math.max(x, y), z);

        if(maxComponent == x) {
            normal = Tuple.vector(objectPoint.getX(), 0, 0);
        } else if(maxComponent == y) {
            normal = Tuple.vector(0, objectPoint.getY(), 0);
        } else {
            normal = Tuple.vector(0, 0, objectPoint.getZ());
        }

        return normal;
    }

    private double[] checkAxis(double origin, double direction) {
        double tMinNumerator = -1 - origin;
        double tMaxNumerator = 1 - origin;

        double t1, t2;

        if(Math.abs(direction) >= Computations.EPSILON) {
            t1 = tMinNumerator / direction;
            t2 = tMaxNumerator / direction;
        } else {
            t1 = tMinNumerator * Double.POSITIVE_INFINITY;
            t2 = tMaxNumerator * Double.POSITIVE_INFINITY;
        }

        double[] ts = new double[2];
        ts[MIN] = Math.min(t1, t2);
        ts[MAX] = Math.max(t1, t2);

        return ts;
    }
}
