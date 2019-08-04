package drago.rtc.shape;

import drago.rtc.*;
import drago.rtc.foundations.Intersection;
import drago.rtc.foundations.Matrix;
import drago.rtc.foundations.Ray;
import drago.rtc.foundations.Tuple;

public abstract class Shape {

    private Matrix transform = Matrix.identity(4);
    private Material material = new Material();
    private Shape parent;

    public final Matrix getTransform() {
        return transform;
    }

    final public void setTransform(Matrix transform) {
        this.transform = transform;
    }

    final public Material getMaterial() {
        return material;
    }

    final public void setMaterial(Material material) {
        this.material = material;
    }

    public final Intersection[] intersects(Ray ray) {
        Ray transformedRay = ray.transform(transform.inverse());

        return localIntersect(transformedRay);
    }

    public final Tuple normalAt(Tuple worldPoint) {

        Tuple objectPoint = worldToObject(worldPoint);
        Tuple objectNormal = localNormalAt(objectPoint);

        return normalToWorld(objectNormal);
    }

    abstract Intersection[] localIntersect(Ray transformedRay);
    abstract Tuple localNormalAt(Tuple objectPoint);
    abstract Bounds getBounds();

    Shape getParent() {
        return parent;
    }

    void setParent(Shape parent) {
        this.parent = parent;
    }

    public Tuple worldToObject(Tuple worldPoint) {
        Tuple parentPoint = worldPoint;

        if(parent != null) {
            parentPoint = parent.worldToObject(worldPoint);
        }

        Matrix transformInverse = transform.inverse();
        return transformInverse.multiplyBy(parentPoint);
    }

    Tuple normalToWorld(Tuple objectNormal) {
        Matrix transformInverse = transform.inverse();
        Tuple worldNormal = transformInverse.transpose().multiplyBy(objectNormal);
        worldNormal = Tuple.vector(worldNormal.getX(), worldNormal.getY(), worldNormal.getZ()).normalise();

        if(parent != null) {
            worldNormal = parent.normalToWorld(worldNormal);
        }

        return worldNormal;
    }
}
