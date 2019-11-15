package drago.rtc.shape;

import drago.rtc.foundations.Intersection;
import drago.rtc.foundations.Matrix;
import drago.rtc.foundations.Ray;
import drago.rtc.foundations.Tuple;
import org.junit.jupiter.api.Test;

import java.util.List;

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
            Tuple localNormalAt(Tuple objectPoint, Intersection intersection) {
                return null;
            }

            @Override
            Bounds getBounds() {
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

        assertEquals(s2, xs[0].getShape());
        assertEquals(s2, xs[1].getShape());
        assertEquals(s1, xs[2].getShape());
        assertEquals(s1, xs[3].getShape());
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

    @Test
    void childrenBoundsAreTransformedIntoGroupSpace() {
        Sphere s = new Sphere();
        s.setTransform(Matrix.scaling(2, 2, 2));

        Group g = new Group();
        g.addChild(s);

        Bounds actual = g.getBounds();
        Bounds expected = new Bounds(Tuple.point(-2, -2, -2), Tuple.point(2, 2, 2));

        assertEquals(expected.getMin(), actual.getMin());
        assertEquals(expected.getMax(), actual.getMax());
    }

    @Test
    void allChildrenContributeToBounds() {
        Sphere s1 = new Sphere();
        s1.setTransform(Matrix.translation(10, 10, 10));

        Sphere s2 = new Sphere();
        s2.setTransform(Matrix.translation(-5, -4, 5));

        Group g = new Group();
        g.addChild(s1);
        g.addChild(s2);

        Bounds actual = g.getBounds();
        Bounds expected = new Bounds(Tuple.point(-6, -5, 4), Tuple.point(11, 11, 11));

        assertEquals(expected.getMin(), actual.getMin());
        assertEquals(expected.getMax(), actual.getMax());
    }

    @Test
    void putsShapesIntoSubGroups() {
        Group g = new Group();

        Sphere s1 = new Sphere();
        s1.setTransform(Matrix.translation(-2, -2, -2));
        g.addChild(s1);

        Sphere s2 = new Sphere();
        s2.setTransform(Matrix.translation(2, -2, -2));
        g.addChild(s2);

        Sphere s3 = new Sphere();
        s3.setTransform(Matrix.translation(-2, 2, -2));
        g.addChild(s3);

        Sphere s4 = new Sphere();
        s4.setTransform(Matrix.translation(-2, -2, 2));
        g.addChild(s4);

        Sphere s5 = new Sphere();
        s5.setTransform(Matrix.translation(2, 2, -2));
        g.addChild(s5);

        Sphere s6 = new Sphere();
        s6.setTransform(Matrix.translation(2, -2, 2));
        g.addChild(s6);

        Sphere s7 = new Sphere();
        s7.setTransform(Matrix.translation(-2, 2, 2));
        g.addChild(s7);

        Sphere s8 = new Sphere();
        s8.setTransform(Matrix.translation(2, 2, 2));
        g.addChild(s8);

        g.subDivide();

        assertNotEquals(g, s1.getParent());
        assertNotEquals(g, s2.getParent());
        assertNotEquals(g, s3.getParent());
        assertNotEquals(g, s4.getParent());
        assertNotEquals(g, s5.getParent());
        assertNotEquals(g, s6.getParent());
        assertNotEquals(g, s7.getParent());
        assertNotEquals(g, s8.getParent());

        List<Shape> children = g.getChildren();

        assertEquals(8, children.size());

        for (Shape child: children) {
            assertTrue(child instanceof Group);
            assertEquals(1, ((Group)child).getChildren().size());
        }
    }

    @Test
    void shapesOverlappingSubGroupsStayInParentGroup() {
        Group g = new Group();

        Sphere s1 = new Sphere();
        s1.setTransform(Matrix.translation(-2, -2, -2));
        g.addChild(s1);

        Sphere s2 = new Sphere();
        s2.setTransform(Matrix.translation(2, -2, -2));
        g.addChild(s2);

        Sphere s3 = new Sphere();
        s3.setTransform(Matrix.translation(-2, 2, -2));
        g.addChild(s3);

        Sphere s4 = new Sphere();
        s4.setTransform(Matrix.translation(-2, -2, 2));
        g.addChild(s4);

        Sphere s5 = new Sphere();
        s5.setTransform(Matrix.translation(2, 2, -2));
        g.addChild(s5);

        Sphere s6 = new Sphere();
        s6.setTransform(Matrix.translation(2, -2, 2));
        g.addChild(s6);

        Sphere s7 = new Sphere();
        s7.setTransform(Matrix.translation(-2, 2, 2));
        g.addChild(s7);

        Sphere s8 = new Sphere();
        s8.setTransform(Matrix.translation(2, 2, 2));
        g.addChild(s8);

        Sphere s9 = new Sphere();
        g.addChild(s9);

        g.subDivide();

        assertEquals(g, s9.getParent());
        assertEquals(9, g.getChildren().size());
    }

    @Test
    void emptySubGroupsDoNotExist() {
        Group g = new Group();

        Sphere s1 = new Sphere();
        s1.setTransform(Matrix.translation(-2, -2, -2));
        g.addChild(s1);

        Sphere s2 = new Sphere();
        s2.setTransform(Matrix.translation(2, -2, -2));
        g.addChild(s2);

        Sphere s3 = new Sphere();
        s3.setTransform(Matrix.translation(-2, 2, -2));
        g.addChild(s3);

        Sphere s4 = new Sphere();
        s4.setTransform(Matrix.translation(-2, -2, 2));
        g.addChild(s4);

        Sphere s5 = new Sphere();
        s5.setTransform(Matrix.translation(2, 2, -2));
        g.addChild(s5);

        Sphere s6 = new Sphere();
        s6.setTransform(Matrix.translation(2, -2, 2));
        g.addChild(s6);

        Sphere s7 = new Sphere();
        s7.setTransform(Matrix.translation(-2, 2, 2));
        g.addChild(s7);

        g.subDivide();

        List<Shape> children = g.getChildren();

        assertEquals(7, children.size());

        for (Shape child: children) {
            assertTrue(child instanceof Group);
            assertEquals(1, ((Group)child).getChildren().size());
        }
    }
}
