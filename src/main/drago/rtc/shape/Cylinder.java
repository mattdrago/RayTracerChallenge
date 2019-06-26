package drago.rtc.shape;

import drago.rtc.foundations.Computations;
import drago.rtc.foundations.Intersection;
import drago.rtc.foundations.Ray;
import drago.rtc.foundations.Tuple;

public class Cylinder extends Shape {
    @Override
    Intersection[] localIntersect(Ray transformedRay) {
        double a = Math.pow(transformedRay.getDirection().getX(), 2) + Math.pow(transformedRay.getDirection().getZ(), 2);

        Intersection[] xs;

        if(Math.abs(a) < Computations.EPSILON) {
            xs = new Intersection[0];
        } else {
            double b = 2 * transformedRay.getOrigin().getX() * transformedRay.getDirection().getX()
                + 2 * transformedRay.getOrigin().getZ() * transformedRay.getDirection().getZ();
            double c = Math.pow(transformedRay.getOrigin().getX(), 2) + Math.pow(transformedRay.getOrigin().getZ(), 2) - 1;

            double discriminant = b*b - 4*a*c;

            if(discriminant < 0) {
                xs = new Intersection[0];
            } else {
                double discriminantRoot = Math.sqrt(discriminant);

                xs = new Intersection[] {
                        new Intersection((-b - discriminantRoot) / (2 * a), this),
                        new Intersection((-b + discriminantRoot) / (2 * a), this),
                };
            }
        }

        return xs;
    }

    @Override
    Tuple localNormalAt(Tuple objectPoint) {
        return Tuple.vector(objectPoint.getX(), 0, objectPoint.getZ());
    }
}
