package drago.rtc;

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
    Tuple localNormalAt(Tuple objectPoint) {
        return Tuple.vector(0,1,0);
    }
}
