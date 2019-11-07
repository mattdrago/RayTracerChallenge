package drago.rtc.shape;

import drago.rtc.foundations.Intersection;
import drago.rtc.foundations.Tuple;

class SmoothTriangle extends Triangle {

    private final Tuple normal1;
    private final Tuple normal2;
    private final Tuple normal3;

    SmoothTriangle(Tuple point1, Tuple point2, Tuple point3, Tuple normal1, Tuple normal2, Tuple normal3) {
        super(point1, point2, point3);
        this.normal1 = normal1;
        this.normal2 = normal2;
        this.normal3 = normal3;
    }

    @Override
    Tuple localNormalAt(Tuple objectPoint, Intersection intersection) {
        return normal2.scale(intersection.getU())
                .add(normal3.scale(intersection.getV()))
                .add(normal1.scale(1 - intersection.getU() - intersection.getV()));
    }

    @Override
    Intersection createIntersection(double t, double u, double v) {
        return new Intersection(t, this, u, v);
    }

    Tuple getNormal1() {
        return normal1;
    }

    Tuple getNormal2() {
        return normal2;
    }

    Tuple getNormal3() {
        return normal3;
    }
}
