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

    static Pattern gradientPattern(Color a, Color b) {
        return new Pattern() {

            private Color from = a;
            private Color to = b;

            @Override
            Color patternAt(Tuple point) {
                return to.subtract(from).scale(point.getX() - Math.floor(point.getX())).add(from);
            }
        };
    }

    static Pattern ringPattern(Color a, Color b) {
        return new Pattern() {

            private Color colorA = a;
            private Color colorB = b;

            @Override
            Color patternAt(Tuple point) {

                double dist = Math.sqrt(point.getX() * point.getX() + point.getZ() * point.getZ());

                return (Math.floor(dist) % 2 == 0) ? colorA : colorB;
            }
        };
    }

    static Pattern checkersPattern(Color a, Color b) {
        return new Pattern() {

            Color colorA = a;
            Color colorB = b;

            @Override
            Color patternAt(Tuple point) {
                double dist = Math.floor(point.getX()) + Math.floor(point.getY()) + Math.floor(point.getZ());

                return ((dist % 2) == 0) ? colorA : colorB;
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
