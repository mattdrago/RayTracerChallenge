package drago.rtc;

import drago.rtc.shape.Shape;
import drago.rtc.shape.Sphere;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class World {
    private Light lightSource = null;
    private final List<Shape> objects = new ArrayList<>();

    static World defaultWorld() {
        World w = new World();

        w.lightSource = Light.pointLight(Tuple.point(-10, 10, -10), Color.WHITE);

        Sphere s1 = new Sphere();
        Material m1 = new Material();
        m1.setColor(new Color(0.8, 1.0, 0.6));
        m1.setDiffuse(0.7);
        m1.setSpecular(0.2);
        s1.setMaterial(m1);

        w.objects.add(s1);

        Sphere s2 = new Sphere();
        s2.setTransform(Matrix.scaling(0.5, 0.5, 0.5));

        w.objects.add(s2);

        return w;
    }

    public List<Shape> getObjects() {
        return this.objects;
    }

    public void setLightSource(Light lightSource) {
        this.lightSource = lightSource;
    }

    Light getLightSource() {
        return this.lightSource;
    }

    Intersection[] intersect(Ray ray) {
        List<Intersection> xsList = new ArrayList<>();

        for (Shape s : objects) {
            xsList.addAll(Arrays.asList(s.intersects(ray)));
        }

        Intersection[] xs = xsList.toArray(new Intersection[0]);
        Arrays.sort(xs);
        return xs;
    }

    Color shadeHit(Computations comps) {
        Material m = comps.getObject().getMaterial();

        boolean isShadow = isShadowed(comps.getOverPoint());

        return m.lighting(this.lightSource, comps.getObject(), comps.getOverPoint(), comps.getEyeV(), comps.getNormalV(), isShadow);
    }

    Color colorAt(Ray ray) {

        Color color = Color.BLACK;

        Intersection hit = Intersection.hit(intersect(ray));

        if(hit != null) {
            color = shadeHit(hit.prepareComputations(ray));
        }

        return color;
    }

    boolean isShadowed(Tuple point) {
        Tuple toLight = lightSource.getPosition().subtract(point);
        double distance = toLight.magnitude();
        Tuple direction = toLight.normalise();

        Ray ray = new Ray(point, direction);
        Intersection[] xs = intersect(ray);

        Intersection hit = Intersection.hit(xs);

        return (hit != null && hit.getT() < distance);
    }
}
