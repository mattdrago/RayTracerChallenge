package drago.rtc.shape;

import drago.rtc.foundations.Computations;
import drago.rtc.foundations.Intersection;
import drago.rtc.foundations.Ray;
import drago.rtc.foundations.Tuple;

import java.util.ArrayList;
import java.util.List;

public class Cylinder extends Shape {

    private double minimum = Double.NEGATIVE_INFINITY;
    private double maximum = Double.POSITIVE_INFINITY;

    double getMinimum() {
        return minimum;
    }

    public void setMinimum(double minimum) {
        this.minimum = minimum;
    }

    double getMaximum() {
        return maximum;
    }

    public void setMaximum(double maximum) {
        this.maximum = maximum;
    }

    @Override
    Intersection[] localIntersect(Ray transformedRay) {
        double a = Math.pow(transformedRay.getDirection().getX(), 2) + Math.pow(transformedRay.getDirection().getZ(), 2);

        List<Intersection> xs = new ArrayList<>();

        if(Math.abs(a) >= Computations.EPSILON) {
            double b = 2 * transformedRay.getOrigin().getX() * transformedRay.getDirection().getX()
                + 2 * transformedRay.getOrigin().getZ() * transformedRay.getDirection().getZ();
            double c = Math.pow(transformedRay.getOrigin().getX(), 2) + Math.pow(transformedRay.getOrigin().getZ(), 2) - 1;

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

        return xs.toArray(new Intersection[0]);
    }

    @Override
    Tuple localNormalAt(Tuple objectPoint) {
        return Tuple.vector(objectPoint.getX(), 0, objectPoint.getZ());
    }
}
