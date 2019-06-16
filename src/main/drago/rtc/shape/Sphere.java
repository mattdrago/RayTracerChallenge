package drago.rtc.shape;

import drago.rtc.foundations.Intersection;
import drago.rtc.foundations.Ray;
import drago.rtc.foundations.Tuple;

public class Sphere extends Shape {
    @Override
    Intersection[] localIntersect(Ray transformedRay) {
        // Calculate the Discriminant
        Tuple sphereToRay = transformedRay.getOrigin().subtract(Tuple.point(0, 0, 0));

        double a = transformedRay.getDirection().dot(transformedRay.getDirection());
        double b = 2.0 * transformedRay.getDirection().dot(sphereToRay);
        double c = sphereToRay.dot(sphereToRay) - 1.0;

        double discriminant = b * b - 4 * a * c;

        Intersection[] ts = {};

        if(discriminant >= 0) {
            ts = new Intersection[2];

            double discriminantRoot = Math.sqrt(discriminant);

            ts[0] = new Intersection((-b - discriminantRoot) / (2 * a), this);
            ts[1] = new Intersection((-b + discriminantRoot) / (2 * a), this);
        }

        return ts;
    }

    @Override
    Tuple localNormalAt(Tuple objectPoint) {
        return objectPoint.subtract(Tuple.point(0, 0, 0));
    }
}
