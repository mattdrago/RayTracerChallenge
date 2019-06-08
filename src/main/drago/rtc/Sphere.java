package drago.rtc;

class Sphere {
    double[] intersects(Ray r) {
        // Calculate the Discriminant
        Tuple sphereToRay = r.getOrigin().subtract(Tuple.point(0, 0, 0));

        double a = r.getDirection().dot(r.getDirection());
        double b = 2.0 * r.getDirection().dot(sphereToRay);
        double c = sphereToRay.dot(sphereToRay) - 1.0;

        double discriminant = b * b - 4 * a * c;

        double[] ts = {};

        if(discriminant >= 0) {
            ts = new double[2];

            double discriminantRoot = Math.sqrt(discriminant);

            ts[0] = (-b - discriminantRoot) / (2 * a);
            ts[1] = (-b + discriminantRoot) / (2 * a);
        }

        return ts;
    }

}
