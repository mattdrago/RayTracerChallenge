package drago.rtc.shape;

import drago.rtc.foundations.Computations;
import drago.rtc.foundations.Intersection;
import drago.rtc.foundations.Ray;
import drago.rtc.foundations.Tuple;

import java.beans.IntrospectionException;

public class Cone extends Shape {
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

        Intersection[] xs;

        if(Math.abs(a) < Computations.EPSILON) {
            if(Math.abs(b) >= Computations.EPSILON) {
                xs = new Intersection[] {
                        new Intersection(-c / (2 * b), this)
                };
            } else {
                xs = new Intersection[0];
            }
        } else {
            double discriminant = b*b - 4*a*c;

            if(discriminant >= 0) {
                double discriminantRoot = Math.sqrt(discriminant);

                xs = new Intersection[]{
                        new Intersection((-b - discriminantRoot) / (2 * a), this),
                        new Intersection((-b + discriminantRoot) / (2 * a), this)
                };

            } else {
                xs = new Intersection[0];
            }

        }

        return xs;
    }

    @Override
    Tuple localNormalAt(Tuple objectPoint) {
        double y = Math.sqrt(Math.pow(objectPoint.getX(), 2) + Math.pow(objectPoint.getZ(), 2));

        if(objectPoint.getY() > 0) {
            y *= -1.0;
        }

        return Tuple.vector(objectPoint.getX(), y, objectPoint.getZ());
    }
}
