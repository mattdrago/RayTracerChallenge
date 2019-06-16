package drago.rtc;

import drago.rtc.shape.Shape;
import drago.rtc.shape.Sphere;
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

        Shape s1 = w.getObjects().get(0);
        Shape s2 = w.getObjects().get(1);

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
        Shape s = w.getObjects().get(0);
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
        Shape s = w.getObjects().get(1);
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
        Shape outer = w.getObjects().get(0);
        outer.getMaterial().setAmbient(1.0);
        Shape inner = w.getObjects().get(1);
        inner.getMaterial().setAmbient(1);

        Ray r = new Ray(Tuple.point(0, 0, 0.75), Tuple.vector(0, 0, -1));

        assertEquals(inner.getMaterial().getColor(), w.colorAt(r));
    }

    @Test
    void thereIsNoShadowWhenNothingIsCollinearWithPointAndLight() {
        World w = World.defaultWorld();
        Tuple p = Tuple.point(0, 10, 0);

        assertFalse(w.isShadowed(p));
    }

    @Test
    void theShadowWhenAnObjectIsBetweenThePOintAndTheLight() {
        World w = World.defaultWorld();
        Tuple p = Tuple.point(10, -10, 10);

        assertTrue(w.isShadowed(p));
    }

    @Test
    void thereIsNoShadowWhenAnObjectIsBehindTheLight() {
        World w = World.defaultWorld();
        Tuple p = Tuple.point(-20, 20, -20);

        assertFalse(w.isShadowed(p));
    }

    @Test
    void thereIsNoShadowWhenAnObjectIsBehindThePoint() {
        World w = World.defaultWorld();
        Tuple p = Tuple.point(-2, 2, -2);

        assertFalse(w.isShadowed(p));
    }

    @Test
    void shadeHitIsGivenAnIntersectionInShadow() {
        World w = new World();
        w.setLightSource(Light.pointLight(Tuple.point(0, 0, -10), Color.WHITE));

        Sphere s1 = new Sphere();
        w.getObjects().add(s1);

        Sphere s2 = new Sphere();
        s2.setTransform(Matrix.translation(0, 0, 10));
        w.getObjects().add(s2);

        Ray r = new Ray(Tuple.point(0, 0, 5), Tuple.vector(0, 0, 1));
        Intersection i = new Intersection(4, s2);

        Computations comps = i.prepareComputations(r);
        Color c = w.shadeHit(comps);

        assertEquals(new Color(0.1, 0.1, 0.1), c);
    }
}