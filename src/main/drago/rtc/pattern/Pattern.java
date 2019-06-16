package drago.rtc.pattern;

import drago.rtc.Color;
import drago.rtc.Matrix;
import drago.rtc.Shape;
import drago.rtc.Tuple;

public abstract class Pattern {

    private Matrix transform = Matrix.identity(4);

    Pattern() {
    }

    public void setTransform(Matrix transform) {
        this.transform = transform;
    }

    Matrix getTransform() {
        return transform;
    }

    public final Color patternAtShape(Shape shape, Tuple point) {
        Tuple objectPoint = shape.getTransform().inverse().multiplyBy(point);
        Tuple patternPoint = transform.inverse().multiplyBy(objectPoint);

        return patternAt(patternPoint);
    }

    abstract Color patternAt(Tuple point);
}