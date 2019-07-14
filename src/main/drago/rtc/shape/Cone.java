package drago.rtc.shape;

import drago.rtc.foundations.Computations;
import drago.rtc.foundations.Intersection;
import drago.rtc.foundations.Ray;
import drago.rtc.foundations.Tuple;

import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.List;

public class Cone extends Shape {
    private double minimum = Double.NEGATIVE_INFINITY;
    private double maximum = Double.POSITIVE_INFINITY;
    private boolean closed;

    @Override
    Intersection[] localIntersect(Ray transformedRay) {

        double a = Math.pow(transformedRay.getDirection().getX(), 2)
                - Math.pow(transformedRay.getDirection().getY(), 2)
                + Math.pow(transformedRay.getDirection().getZ(), 2);

        double b = 2 * transformedRay.getOrigin().getX() * transformedRay.getDirection().getX()
                - 2 * transformedRay.getOrigin().getY() * transformedRay.getDirection().getY()
                + 2 * transformedRay.getOrigin().getZ() * transformedRay.getDirection().getZ();

        double c = Math.pow(transformedRay.getOrigin().getX(), 2)
                - Math.pow(transformedRay.getOrigin().getY(), 2)
                + Math.pow(transformedRay.getOrigin().getZ(), 2);

        List<Intersection> xs = new ArrayList<>();

        if(Math.abs(a) < Computations.EPSILON) {
            if(Math.abs(b) >= Computations.EPSILON) {
                xs.add(new Intersection(-c / (2 * b), this));
            }
        } else {
            double discriminant = b*b - 4*a*c;

            if(discriminant >= 0) {
                double discriminantRoot = Math.sqrt(discriminant);

                double[] ts = {
                        (-b - discriminantRoot) / (2 * a),
                        (-b + discriminantRoot) / (2 * a)
                };

                for (double t: ts) {
                    double y = transformedRay.getOrigin().getY() + t * transformedRay.getDirection().getY();

                    if(y > minimum && y < maximum) {
                        xs.add(new Intersection(t, this));
                    }
                }
            }
        }

        intersectCaps(transformedRay, xs);
        return xs.toArray(new Intersection[0]);
    }

    private boolean checkCap(Ray r, double t, double y) {
        double x = r.getOrigin().getX() + t * r.getDirection().getX();
        double z = r.getOrigin().getZ() + t * r.getDirection().getZ();

        return (x*x + z*z) <= y*y;
    }

    private void intersectCaps(Ray transformedRay, List<Intersection> xs) {
        if(closed && Math.abs(transformedRay.getDirection().getY()) > Computations.EPSILON) {
            double t = (minimum - transformedRay.getOrigin().getY()) / transformedRay.getDirection().getY();
            if(checkCap(transformedRay, t, minimum)) {
                xs.add(new Intersection(t, this));
            }

            t = (maximum - transformedRay.getOrigin().getY()) / transformedRay.getDirection().getY();
            if(checkCap(transformedRay, t, maximum)) {
                xs.add(new Intersection(t, this));
            }
        }
    }

    @Override
    Tuple localNormalAt(Tuple objectPoint) {
        Tuple normal;

        double dist = Math.pow(objectPoint.getX(), 2) + Math.pow(objectPoint.getZ(), 2);
        double radiusSq = Math.pow(objectPoint.getY(), 2);

        if(dist < radiusSq && objectPoint.getY() >= maximum - Computations.EPSILON) {
            normal = Tuple.vector(0, 1, 0);
        } else if (dist < radiusSq && objectPoint.getY() <= minimum + Computations.EPSILON) {
            normal = Tuple.vector(0, -1, 0);
        } else {
            double y = Math.sqrt(dist);

            if(objectPoint.getY() > 0) {
                y *= -1.0;
            }

            normal = Tuple.vector(objectPoint.getX(), y, objectPoint.getZ());
        }

        return normal;


    }

    public void setMinimum(double minimum) {
        this.minimum = minimum;
    }

    double getMinimum() {
        return minimum;
    }

    public void setMaximum(double maximum) {
        this.maximum = maximum;
    }

    double getMaximum() {
        return maximum;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    boolean isClosed() {
        return closed;
    }
}
