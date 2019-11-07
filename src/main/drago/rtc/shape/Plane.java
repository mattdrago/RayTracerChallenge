package drago.rtc.shape;

import drago.rtc.foundations.Intersection;
import drago.rtc.foundations.Ray;
import drago.rtc.foundations.Tuple;

public class Plane extends Shape {

    private static final double EPSILON = 0.000001;

    @Override
    Intersection[] localIntersect(Ray transformedRay) {
        Intersection[] xs;

        if(Math.abs(transformedRay.getDirection().getY()) < EPSILON) {
            xs = new Intersection[0];
        } else {
            xs = new Intersection[] {
                new Intersection(-transformedRay.getOrigin().getY() / transformedRay.getDirection().getY(), this)
            };
        }

        return xs;
    }

    @Override
    Tuple localNormalAt(Tuple objectPoint, Intersection intersection) {
        return Tuple.vector(0,1,0);
    }

    @Override
    Bounds getBounds() {
        return new Bounds(Tuple.point(Double.NEGATIVE_INFINITY, 0, Double.NEGATIVE_INFINITY),
                Tuple.point(Double.POSITIVE_INFINITY, 0, Double.POSITIVE_INFINITY));
    }
}
