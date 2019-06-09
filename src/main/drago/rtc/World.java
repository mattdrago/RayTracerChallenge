package drago.rtc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class World {
    private Light lightSource = null;
    private List<Sphere> objects = new ArrayList<Sphere>();

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

    List<Sphere> getObjects() {
        return this.objects;
    }

    Light getLightSource() {
        return this.lightSource;
    }

    Intersection[] intersect(Ray ray) {
        List<Intersection> xsList = new ArrayList<>();

        for (Sphere s : objects) {
            xsList.addAll(Arrays.asList(s.intersects(ray)));
        }

        Intersection[] xs = xsList.toArray(new Intersection[0]);
        Arrays.sort(xs);
        return xs;
    }
}
