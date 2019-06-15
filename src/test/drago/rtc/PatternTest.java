package drago.rtc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PatternTest {

    @Test
    void creatingAStripePattern() {
        Pattern p = Pattern.stripePattern(Color.WHITE, Color.BLACK);

        assertEquals(Color.WHITE, p.getColorA());
        assertEquals(Color.BLACK, p.getColorB());
    }

    @Test
    void aStripePatternIsConstantInY() {
        Pattern p = Pattern.stripePattern(Color.WHITE, Color.BLACK);

        assertEquals(Color.WHITE, p.colorAt(Tuple.point(0, 0, 0)));
        assertEquals(Color.WHITE, p.colorAt(Tuple.point(0, 1, 0)));
        assertEquals(Color.WHITE, p.colorAt(Tuple.point(0, 2, 0)));
    }

    @Test
    void aStripePatternIsConstantInZ() {
        Pattern p = Pattern.stripePattern(Color.WHITE, Color.BLACK);

        assertEquals(Color.WHITE, p.colorAt(Tuple.point(0, 0, 0)));
        assertEquals(Color.WHITE, p.colorAt(Tuple.point(0, 0, 1)));
        assertEquals(Color.WHITE, p.colorAt(Tuple.point(0, 0, 2)));
    }

    @Test
    void aStripePatternAlternatesInX() {
        Pattern p = Pattern.stripePattern(Color.WHITE, Color.BLACK);

        assertEquals(Color.WHITE, p.colorAt(Tuple.point(0, 0, 0)));
        assertEquals(Color.WHITE, p.colorAt(Tuple.point(0.9, 0, 0)));
        assertEquals(Color.BLACK, p.colorAt(Tuple.point(1, 0, 0)));
        assertEquals(Color.BLACK, p.colorAt(Tuple.point(1.1, 0, 0)));
        assertEquals(Color.BLACK, p.colorAt(Tuple.point(-0.1, 0, 0)));
        assertEquals(Color.BLACK, p.colorAt(Tuple.point(-1, 0, 0)));
        assertEquals(Color.WHITE, p.colorAt(Tuple.point(-1.1, 0, 0)));
    }

    @Test
    void stripesWithAnObjectTransformation() {
        Shape object = new Sphere();
        object.setTransform(Matrix.scaling(2, 2, 2));

        Pattern p = Pattern.stripePattern(Color.WHITE, Color.BLACK);

        assertEquals(Color.WHITE, p.colorAtObject(object, Tuple.point(1.5, 0, 0)));
    }

    @Test
    void stripesWithAPatternTransformation() {
        Shape object = new Sphere();
        Pattern p = Pattern.stripePattern(Color.WHITE, Color.BLACK);
        p.setTransformation(Matrix.scaling(2, 2, 2));

        assertEquals(Color.WHITE, p.colorAtObject(object, Tuple.point(1.5, 0, 0)));
    }

    @Test
    void stripesWithBothAnObjectAndAPatternTransformation() {
        Shape object = new Sphere();
        object.setTransform(Matrix.scaling(2, 2, 2));

        Pattern p = Pattern.stripePattern(Color.WHITE, Color.BLACK);
        p.setTransformation(Matrix.translation(0.5, 0, 0));

        assertEquals(Color.WHITE, p.colorAtObject(object, Tuple.point(2.5, 0, 0)));
    }
}