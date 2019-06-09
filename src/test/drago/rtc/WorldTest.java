package drago.rtc;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

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
}