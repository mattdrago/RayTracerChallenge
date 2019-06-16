package drago.rtc.pattern;

import drago.rtc.*;
import drago.rtc.foundations.Matrix;
import drago.rtc.foundations.Tuple;
import drago.rtc.shape.Shape;
import drago.rtc.shape.Sphere;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PatternTest {

    private Pattern testPattern() {
        return new Pattern() {

            @Override
            Color patternAt(Tuple point) {
                return new Color(point.getX(), point.getY(), point.getZ());
            }
        };
    }

    @Test
    void theDefaultPatternTransformation() {
        Pattern p = testPattern();

        assertEquals(Matrix.identity(4), p.getTransform());
    }

    @Test
    void assigningATransformation() {
        Pattern p = testPattern();
        Matrix transform = Matrix.scaling(1, 2, 3);

        p.setTransform(transform);

        assertEquals(transform, p.getTransform());
    }

    @Test
    void aPatternWithAnObjectTransform() {
        Shape s = new Sphere();
        s.setTransform(Matrix.scaling(2, 2, 2));

        Pattern p = testPattern();

        Color expected = new Color(1, 1.5, 2);

        assertEquals(expected, p.patternAtShape(s, Tuple.point(2, 3, 4)));
    }

    @Test
    void aPatternWithAPatternTransformation() {
        Shape s = new Sphere();
        Pattern p = testPattern();
        p.setTransform(Matrix.scaling(2, 2, 2));

        Color expected = new Color(1, 1.5, 2);

        assertEquals(expected, p.patternAtShape(s, Tuple.point(2, 3, 4)));
    }

    @Test
    void aPatternWithBothAnObjectAndAPatternTransform() {
        Shape s = new Sphere();
        s.setTransform(Matrix.scaling(2, 2, 2));

        Pattern p = testPattern();
        p.setTransform(Matrix.translation(0.5, 1, 1.5));

        Color expected = new Color(0.75, 0.5, 0.25);

        assertEquals(expected, p.patternAtShape(s, Tuple.point(2.5, 3, 3.5)));
    }
}
