package drago.rtc;

import drago.rtc.foundations.*;
import drago.rtc.pattern.PatternTest;
import drago.rtc.shape.Plane;
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

        Computations comps = i.prepareComputations(r, new Intersection[] {i});

        Color expectedColor = new Color(0.38066, 0.47583, 0.2855);
        Color actualColor = w.shadeHit(comps, 1);

        assertEquals(expectedColor, actualColor);

    }

    @Test
    void shadingAnIntersectionFromTheInside() {
        World w = World.defaultWorld();
        w.setLightSource(Light.pointLight(Tuple.point(0, 0.25, 0), Color.WHITE));
        Ray r = new Ray(Tuple.point(0, 0, 0), Tuple.vector(0, 0, 1));
        Shape s = w.getObjects().get(1);
        Intersection i = new Intersection(0.5, s);

        Computations comps = i.prepareComputations(r, new Intersection[] {i});

        Color expectedColor = new Color(0.90498, 0.90498, 0.90498);
        Color actualColor = w.shadeHit(comps, 1);

        assertEquals(expectedColor, actualColor);
    }

    @Test
    void theColorWhenARayMisses() {
        World w = World.defaultWorld();
        Ray r = new Ray(Tuple.point(0, 0, -5), Tuple.vector(0, 1, 0));

        Color result = w.colorAt(r, 1);

        assertEquals(Color.BLACK, result);
    }

    @Test
    void theColorWhenARayHits() {
        World w = World.defaultWorld();
        Ray r = new Ray(Tuple.point(0, 0, -5), Tuple.vector(0, 0, 1));

        Color expectedColor = new Color(0.38066, 0.47583, 0.2855);
        Color actualColor = w.colorAt(r, 1);

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

        assertEquals(inner.getMaterial().getColor(), w.colorAt(r, 1));
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

        Computations comps = i.prepareComputations(r, new Intersection[] {i});
        Color c = w.shadeHit(comps, 1);

        assertEquals(new Color(0.1, 0.1, 0.1), c);
    }

    @Test
    void theReflectedColorForANonReflectiveMaterial() {
        World w = World.defaultWorld();
        Ray r = new Ray(Tuple.point(0, 0, 0), Tuple.vector(0, 0, 1));

        Shape s = w.getObjects().get(1);
        s.getMaterial().setAmbient(1);

        Intersection i = new Intersection(1, s);

        Computations comps = i.prepareComputations(r, new Intersection[] {i});

        Color expected = Color.BLACK;
        Color actual = w.reflectedColor(comps, 0);

        assertEquals(expected, actual);
    }

    @Test
    void theReflectedColorForAReflectiveMaterial() {
        World w = World.defaultWorld();

        Shape plane = new Plane();
        plane.getMaterial().setReflective(0.5);
        plane.setTransform(Matrix.translation(0, -1, 0));

        w.getObjects().add(plane);

        Ray r = new Ray(Tuple.point(0, 0, -3), Tuple.vector(0, -Math.sqrt(2) / 2, Math.sqrt(2) / 2));
        Intersection i = new Intersection(Math.sqrt(2), plane);

        Computations comps = i.prepareComputations(r, new Intersection[] {i});

        // Numbers here are more exact than in the book
        Color expectedReflected = new Color(0.19033, 0.23791, 0.14274);

        assertEquals(expectedReflected, w.reflectedColor(comps, 1));

        // Numbers here do not match exactly in the book.
        Color expectedShade = new Color(0.87676, 0.92434, 0.82917);
        assertEquals(expectedShade, w.shadeHit(comps, 1));
    }

    @Test
    void colorAtWithMutuallyReflectiveSurfaces() {
        World w = new World();
        w.setLightSource(Light.pointLight(Tuple.point(0, 0, 0), Color.WHITE));

        Shape lower = new Plane();
        lower.getMaterial().setReflective(1);
        lower.setTransform(Matrix.translation(0, -1, 0));
        w.getObjects().add(lower);

        Shape upper = new Plane();
        upper.getMaterial().setReflective(1);
        upper.setTransform(Matrix.translation(0, 1, 0));
        w.getObjects().add(upper);

        Ray r = new Ray(Tuple.point(0, 0, 0), Tuple.vector(0, 1, 0));

        w.colorAt(r, 10);
    }

    @Test
    void theReflectedColorForAReflectiveMaterialAtTheMaximumRecursiveDepth() {
        World w = World.defaultWorld();

        Shape plane = new Plane();
        plane.getMaterial().setReflective(0.5);
        plane.setTransform(Matrix.translation(0, -1, 0));

        w.getObjects().add(plane);

        Ray r = new Ray(Tuple.point(0, 0, -3), Tuple.vector(0, -Math.sqrt(2) / 2, Math.sqrt(2) / 2));
        Intersection i = new Intersection(Math.sqrt(2), plane);

        Computations comps = i.prepareComputations(r, new Intersection[] {i});

        assertEquals(Color.BLACK, w.reflectedColor(comps, 0));
    }

    @Test
    void theRefractedColorWithAnOpaqueSurface() {
        World w = World.defaultWorld();
        Shape s = w.getObjects().get(0);
        Ray r = new Ray(Tuple.point(0, 0, -5), Tuple.vector(0, 0, 1));
        Intersection[] xs = {
                new Intersection(4, s),
                new Intersection(6, s)
        };

        Computations comps = xs[0].prepareComputations(r, xs);

        assertEquals(Color.BLACK, w.refractedColor(comps, 5));
    }

    @Test
    void theRefractedColorAtTheMaximumRecursiveDepth() {
        World w = World.defaultWorld();

        Shape s = w.getObjects().get(0);
        s.getMaterial().setTransparency(1.0);
        s.getMaterial().setRefractiveIndex(1.5);

        Ray r = new Ray(Tuple.point(0, 0, -5), Tuple.vector(0, 0, 1));
        Intersection[] xs = {
                new Intersection(4, s),
                new Intersection(6, s)
        };

        Computations comps = xs[0].prepareComputations(r, xs);

        assertEquals(Color.BLACK, w.refractedColor(comps, 0));
    }

    @Test
    void theRefractedColorUnderTotalInternalReflection() {
        World w = World.defaultWorld();

        Shape s = w.getObjects().get(0);
        s.getMaterial().setTransparency(1.0);
        s.getMaterial().setRefractiveIndex(1.5);

        Ray r = new Ray(Tuple.point(0, 0, Math.sqrt(2) / 2), Tuple.vector(0, 1, 0));
        Intersection[] xs = {
                new Intersection(-Math.sqrt(2) / 2, s),
                new Intersection(Math.sqrt(2) / 2, s)
        };

        // NOTE: this time the ray origin is in the sphere, so the test needs to use the second intersection
        Computations comps = xs[1].prepareComputations(r, xs);

        assertEquals(Color.BLACK, w.refractedColor(comps, 5));
    }

    @Test
    void theRefractedColorWithARefractedRay() {
        World w = World.defaultWorld();

        Shape a = w.getObjects().get(0);
        a.getMaterial().setAmbient(1);
        a.getMaterial().setPattern(PatternTest.testPattern());

        Shape b = w.getObjects().get(1);
        b.getMaterial().setTransparency(1.0);
        b.getMaterial().setRefractiveIndex(1.5);

        Ray r = new Ray(Tuple.point(0, 0, 0.1), Tuple.vector(0, 1, 0));
        Intersection[] xs = {
                new Intersection(-0.9899, a),
                new Intersection(-0.4899, b),
                new Intersection(0.4899, b),
                new Intersection(0.9899, a)
        };

        Computations comps = xs[2].prepareComputations(r, xs);

        assertEquals(new Color(0, 0.99888, 0.04722), w.refractedColor(comps, 5));
    }

    @Test
    void shadeHitWithATransparentMaterial() {
        World w = World.defaultWorld();

        Plane floor = new Plane();
        floor.setTransform(Matrix.translation(0, -1, 0));
        floor.getMaterial().setTransparency(0.5);
        floor.getMaterial().setRefractiveIndex(1.5);
        w.getObjects().add(floor);

        Sphere ball = new Sphere();
        ball.getMaterial().setColor(new Color(1, 0, 0));
        ball.getMaterial().setAmbient(0.5);
        ball.setTransform(Matrix.translation(0, -3.5, -0.5));
        w.getObjects().add(ball);

        Ray r = new Ray(Tuple.point(0, 0, -3), Tuple.vector(0, -Math.sqrt(2) / 2, Math.sqrt(2) / 2));
        Intersection[] xs = {
                new Intersection(Math.sqrt(2), floor)
        };

        Computations comps = xs[0].prepareComputations(r, xs);

        Color expectedColor = new Color(0.93642, 0.68642, 0.68642);
        Color actualColor = w.shadeHit(comps, 5);

        assertEquals(expectedColor, actualColor);
    }

    @Test
    void shadeHitWithAReflectiveTransparentMaterial() {
        World w = World.defaultWorld();
        Ray r = new Ray(Tuple.point(0, 0, -3), Tuple.vector(0, -Math.sqrt(2) / 2, Math.sqrt(2) / 2));

        Plane floor = new Plane();
        floor.setTransform(Matrix.translation(0, -1, 0));
        floor.getMaterial().setReflective(0.5);
        floor.getMaterial().setTransparency(0.5);
        floor.getMaterial().setRefractiveIndex(1.5);
        w.getObjects().add(floor);

        Shape ball = new Sphere();
        ball.setTransform(Matrix.translation(0, -3.5, -0.5));
        ball.getMaterial().setColor(new Color(1, 0, 0));
        ball.getMaterial().setAmbient(0.5);
        w.getObjects().add(ball);

        Intersection[] xs = {
                new Intersection(Math.sqrt(2), floor)
        };

        Computations comps = xs[0].prepareComputations(r, xs);

        Color expected = new Color(0.93391, 0.69643, 0.69243);
        Color actual = w.shadeHit(comps, 5);

        assertEquals(expected, actual);
    }
}