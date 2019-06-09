package drago.rtc;

public class Sphere {
    private Matrix transform = Matrix.identity(4);
    private Material material = new Material();

    public Intersection[] intersects(Ray ray) {
        Ray transformedRay = ray.transform(transform.inverse());
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

    Matrix getTransform() {
        return transform;
    }

    public void setTransform(Matrix transform) {
        this.transform = transform;
    }

    public Tuple normalAt(Tuple point) {
        Matrix transformInverse = transform.inverse();

        Tuple objectPoint = transformInverse.multiplyBy(point);
        Tuple objectNormal = objectPoint.subtract(Tuple.point(0, 0, 0));

        Tuple worldNormal = transformInverse.transpose().multiplyBy(objectNormal);
        worldNormal = Tuple.vector(worldNormal.getX(), worldNormal.getY(), worldNormal.getZ());

        return worldNormal.normalise();
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
}
