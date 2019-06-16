package drago.rtc;

public abstract class Pattern {

    private Matrix transform = Matrix.identity(4);

    Pattern() {
    }

    public static Pattern stripePattern(Color a, Color b) {
        return new Pattern() {
            private Color colorA = a;
            private Color colorB = b;

            @Override
            Color patternAt(Tuple point) {
                int modulo = (int)(point.getX() % 2 + 2) % 2;

                if(modulo == 0) {
                    return colorA;
                } else {
                    return colorB;
                }
            }
        };
    }


    public void setTransform(Matrix transform) {
        this.transform = transform;
    }

    Matrix getTransform() {
        return transform;
    }

    Color patternAtShape(Shape shape, Tuple point) {
        Tuple objectPoint = shape.getTransform().inverse().multiplyBy(point);
        Tuple patternPoint = transform.inverse().multiplyBy(objectPoint);

        return patternAt(patternPoint);
    }

    abstract Color patternAt(Tuple point);
}
