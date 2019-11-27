package drago.rtc;

import drago.rtc.foundations.*;
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

    Color shadeHit(Computations comps, int remaining) {
        Material m = comps.getShape().getMaterial();

        boolean isShadow = isShadowed(comps.getOverPoint());

        Color surfaceColor = m.lighting(this.lightSource, comps.getShape(), comps.getOverPoint(), comps.getEyeV(), comps.getNormalV(), isShadow);
        Color reflectedColor = reflectedColor(comps, remaining);
        Color refractedColor = refractedColor(comps, remaining);

        if(comps.getShape().getMaterial().getReflective() > 0 && comps.getShape().getMaterial().getTransparency() > 0) {
            double reflectance = comps.schlick();
            return surfaceColor.add(reflectedColor.scale(reflectance)).add(refractedColor.scale(1 - reflectance));
        } else {
            return surfaceColor.add(reflectedColor).add(refractedColor);
        }
    }

    Color colorAt(Ray ray, int remaining) {

        Color color = Color.BLACK;

        Intersection[] xs = intersect(ray);
        Intersection hit = Intersection.hit(xs);

        if(hit != null) {
            color = shadeHit(hit.prepareComputations(ray, xs), remaining);
        }

        return color;
    }

    boolean isShadowed(Tuple point) {
        Tuple toLight = lightSource.getPosition().subtract(point);
        double distance = toLight.magnitude();
        Tuple direction = toLight.normalise();

        Ray ray = new Ray(point, direction);
        Intersection[] xs = intersect(ray);

        Intersection[] xsThatCastShadow = Arrays.stream(xs).filter(x -> x.getShape().isCastShadow()).toArray(Intersection[]::new);

        Intersection hit = Intersection.hit(xsThatCastShadow);

        return (hit != null && hit.getT() < distance);
    }

    Color reflectedColor(Computations comps, int remaining) {
        Color reflectedColor = Color.BLACK;

        Material m = comps.getShape().getMaterial();

        if(m.getReflective() != 0 && remaining > 0) {
            Ray reflectRay = new Ray(comps.getOverPoint(), comps.getReflectV());
            reflectedColor = colorAt(reflectRay, remaining-1).scale(m.getReflective());
        }

        return reflectedColor;
    }

    Color refractedColor(Computations comps, int remaining) {
        Color refractedColor = Color.BLACK;

        if(comps.getShape().getMaterial().getTransparency() > 0 && remaining > 0) {
            double nRatio = comps.getN1() / comps.getN2();
            double cosIncident = comps.getEyeV().dot(comps.getNormalV());

            double sin2Refracted = nRatio * nRatio * (1 - cosIncident * cosIncident);

            boolean isTotalInternalRefraction = sin2Refracted > 1.0;

            if(!isTotalInternalRefraction) {
                double cosRefracted = Math.sqrt(1.0 - sin2Refracted);

                Tuple direction = comps.getNormalV().scale(nRatio * cosIncident - cosRefracted)
                        .subtract(comps.getEyeV().scale(nRatio));

                Ray refractRay = new Ray(comps.getUnderPoint(), direction);

                refractedColor = colorAt(refractRay, remaining - 1).scale(comps.getShape().getMaterial().getTransparency());
            }

        }

        return refractedColor;
    }
}
