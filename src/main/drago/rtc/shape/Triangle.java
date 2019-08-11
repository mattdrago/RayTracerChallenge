package drago.rtc.shape;

import drago.rtc.foundations.Computations;
import drago.rtc.foundations.Intersection;
import drago.rtc.foundations.Ray;
import drago.rtc.foundations.Tuple;

class Triangle extends Shape {
    private final Tuple point1;
    private final Tuple point2;
    private final Tuple point3;

    private final Tuple edge1;
    private final Tuple edge2;
    private final Tuple normal;

    Triangle(Tuple point1, Tuple point2, Tuple point3) {

        this.point1 = point1;
        this.point2 = point2;
        this.point3 = point3;

        edge1 = point2.subtract(point1);
        edge2 = point3.subtract(point1);
        normal = edge2.cross(edge1).normalise();
    }

    Tuple getPoint1() {
        return point1;
    }

    Tuple getPoint2() {
        return point2;
    }

    Tuple getPoint3() {
        return point3;
    }

    Tuple getEdge1() {
        return edge1;
    }

    Tuple getEdge2() {
        return edge2;
    }

    Tuple getNormal() {
        return normal;
    }

    @Override
    Intersection[] localIntersect(Ray transformedRay) {
        Intersection[] xs = {};

        Tuple dirCrossEdge2 = transformedRay.getDirection().cross(edge2);
        double determinant = edge1.dot(dirCrossEdge2);

        if(Math.abs(determinant) > Computations.EPSILON) {

            double f = 1.0 / determinant;
            Tuple p1ToOrigin = transformedRay.getOrigin().subtract(point1);
            double u = f * p1ToOrigin.dot(dirCrossEdge2);

            if(u>= 0 && u <= 1) {

                Tuple originCrossEdge1 = p1ToOrigin.cross(edge1);
                double v = f * transformedRay.getDirection().dot(originCrossEdge1);

                if(v >= 0 && (u + v) <= 1.0) {
                    double t = f * edge2.dot(originCrossEdge1);
                    xs = new Intersection[] {
                            new Intersection(t, this)
                    };
                }
            }
        }


        return xs;
    }

    @Override
    Tuple localNormalAt(Tuple objectPoint) {
        return normal;
    }

    @Override
    Bounds getBounds() {
        Tuple min = Tuple.point(
                Math.min(Math.min(point1.getX(), point2.getX()), point3.getX()),
                Math.min(Math.min(point1.getY(), point2.getY()), point3.getY()),
                Math.min(Math.min(point1.getZ(), point2.getZ()), point3.getZ())
        );

        Tuple max = Tuple.point(
                Math.max(Math.max(point1.getX(), point2.getX()), point3.getX()),
                Math.max(Math.max(point1.getY(), point2.getY()), point3.getY()),
                Math.max(Math.max(point1.getZ(), point2.getZ()), point3.getZ())
        );

        return new Bounds(min, max);
    }
}
