package drago.rtc.shape;

import drago.rtc.foundations.Intersection;
import drago.rtc.foundations.Matrix;
import drago.rtc.foundations.Ray;
import drago.rtc.foundations.Tuple;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class GroupTest {

    @Test
    void creatingANewGroup() {
        Group g = new Group();

        assertEquals(Matrix.identity(4), g.getTransform());
        assertEquals(0, g.getChildren().size());
    }

    @Test
    void addingAChildToAGroup() {
        Group g = new Group();
        Shape s = testShape();

        g.addChild(s);

        assertTrue(g.hasChildren());
        assertTrue(g.getChildren().contains(s));
        assertEquals(g, s.getParent());
    }

    private Shape testShape() {
        return new Shape() {
            @Override
            Intersection[] localIntersect(Ray transformedRay) {
                return new Intersection[0];
            }

            @Override
            Tuple localNormalAt(Tuple objectPoint) {
                return null;
            }
        };
    }

    @Test
    void intersectingARayWithAnEmptyGroup() {
        Group g = new Group();
        Ray r = new Ray(Tuple.point(0, 0, 0), Tuple.vector(0, 0, 1));

        Intersection[] xs = g.localIntersect(r);

        assertEquals(0, xs.length);
    }

    @Test
    void intersectingARayWithANonEmptyGroup() {
        Group g = new Group();

        Sphere s1 = new Sphere();

        Sphere s2 = new Sphere();
        s2.setTransform(Matrix.translation(0, 0, -3));

        Sphere s3 = new Sphere();
        s3.setTransform(Matrix.translation(5, 0, 0));

        g.addChild(s1);
        g.addChild(s2);
        g.addChild(s3);

        Ray r = new Ray(Tuple.point(0, 0, -5), Tuple.vector(0, 0, 1));

        Intersection[] xs = g.localIntersect(r);

        assertEquals(4, xs.length);

        assertEquals(s2, xs[0].getObject());
        assertEquals(s2, xs[1].getObject());
        assertEquals(s1, xs[2].getObject());
        assertEquals(s1, xs[3].getObject());
    }

    @Test
    void intersectingATransformedGroup() {
        Group g = new Group();
        g.setTransform(Matrix.scaling(2, 2, 2));

        Sphere s = new Sphere();
        s.setTransform(Matrix.translation(5, 0, 0));
        g.addChild(s);

        Ray r = new Ray(Tuple.point(10, 0, -10), Tuple.vector(0, 0, 1));

        Intersection[] xs = g.intersects(r);

        assertEquals(2, xs.length);
    }
}
