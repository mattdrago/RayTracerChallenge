package drago.rtc;

abstract class Shape {

    private Matrix transform = Matrix.identity(4);
    private Material material = new Material();

    final Matrix getTransform() {
        return transform;
    }

    final public void setTransform(Matrix transform) {
        this.transform = transform;
    }

    final public Material getMaterial() {
        return material;
    }

    final void setMaterial(Material material) {
        this.material = material;
    }

    final Intersection[] intersects(Ray ray) {
        Ray transformedRay = ray.transform(transform.inverse());

        return localIntersect(transformedRay);
    }

    final Tuple normalAt(Tuple point) {
        Matrix transformInverse = transform.inverse();

        Tuple objectPoint = transformInverse.multiplyBy(point);

        Tuple objectNormal = localNormalAt(objectPoint);

        Tuple worldNormal = transformInverse.transpose().multiplyBy(objectNormal);
        worldNormal = Tuple.vector(worldNormal.getX(), worldNormal.getY(), worldNormal.getZ());

        return worldNormal.normalise();
    }

    abstract Intersection[] localIntersect(Ray transformedRay);
    abstract Tuple localNormalAt(Tuple objectPoint);
}