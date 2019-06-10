package drago.rtc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WorldTest {

    @Test
    void creatingAWorld() {
        World w = new World();

        assertTrue(w.getObjects().isEmpty());
        assertNull(w.getLightSource());
    }

    @Test
    void theDefaultWorld() {
        Light l = Light.pointLight(Tuple.point(-10, 10, -10), Color.WHITE);

        Matrix t = Matrix.scaling(0.5, 0.5, 0.5);

        Material m = new Material();
        m.setColor(new Color(0.8, 1.0, 0.6));
        m.setDiffuse(0.7);
        m.setSpecular(0.2);

        World w = World.defaultWorld();

        assertEquals(l, w.getLightSource());
        assertEquals(2, w.getObjects().size());

        Sphere s1 = w.getObjects().get(0);
        Sphere s2 = w.getObjects().get(1);

        assertTrue((
                    s1.getMaterial().equals(m) &&
                    s1.getTransform().equals(Matrix.identity(4)) &&
                    s2.getTransform().equals(t)
                ) || (
                    s1.getTransform().equals(t) &&
                    s2.getMaterial().equals(m) &&
                    s2.getTransform().equals(Matrix.identity(4))
                )
        );
    }

    @Test
    void intersectAWorldWithARay() {
        World w = World.defaultWorld();
        Ray r = new Ray(Tuple.point(0, 0, -5), Tuple.vector(0, 0, 1));

        Intersection[] xs = w.intersect(r);

        assertEquals(4, xs.length);

        assertEquals(4, xs[0].getT());
        assertEquals(4.5, xs[1].getT());
        assertEquals(5.5, xs[2].getT());
        assertEquals(6, xs[3].getT());
    }

    @Test
    void shadingAnIntersection() {
        World w = World.defaultWorld();
        Ray r = new Ray(Tuple.point(0, 0, -5), Tuple.vector(0, 0, 1));
        Sphere s = w.getObjects().get(0);
        Intersection i = new Intersection(4, s);

        Computations comps = i.prepareComputations(r);

        Color expectedColor = new Color(0.38066, 0.47583, 0.2855);
        Color actualColor = w.shadeHit(comps);

        assertEquals(expectedColor, actualColor);

    }

    @Test
    void shadingAnIntersectionFromTheInside() {
        World w = World.defaultWorld();
        w.setLightSource(Light.pointLight(Tuple.point(0, 0.25, 0), Color.WHITE));
        Ray r = new Ray(Tuple.point(0, 0, 0), Tuple.vector(0, 0, 1));
        Sphere s = w.getObjects().get(1);
        Intersection i = new Intersection(0.5, s);

        Computations comps = i.prepareComputations(r);

        Color expectedColor = new Color(0.90498, 0.90498, 0.90498);
        Color actualColor = w.shadeHit(comps);

        assertEquals(expectedColor, actualColor);
    }

    @Test
    void theColorWhenARayMisses() {
        World w = World.defaultWorld();
        Ray r = new Ray(Tuple.point(0, 0, -5), Tuple.vector(0, 1, 0));

        Color result = w.colorAt(r);

        assertEquals(Color.BLACK, result);
    }

    @Test
    void theColorWhenARayHits() {
        World w = World.defaultWorld();
        Ray r = new Ray(Tuple.point(0, 0, -5), Tuple.vector(0, 0, 1));

        Color expectedColor = new Color(0.38066, 0.47583, 0.2855);
        Color actualColor = w.colorAt(r);

        assertEquals(expectedColor, actualColor);
    }

    @Test
    void theColorWithAnIntersectionBehindTheRay() {
        World w = World.defaultWorld();
        Sphere outer = w.getObjects().get(0);
        outer.getMaterial().setAmbient(1.0);
        Sphere inner = w.getObjects().get(1);
        inner.getMaterial().setAmbient(1);

        Ray r = new Ray(Tuple.point(0, 0, 0.75), Tuple.vector(0, 0, -1));

        assertEquals(inner.getMaterial().getColor(), w.colorAt(r));
    }
}