package drago.rtc;

public class Pattern {

    private final Color colorA;
    private final Color colorB;
    private Matrix transformation = Matrix.identity(4);

    private Pattern(Color colorA, Color colorB) {

        this.colorA = colorA;
        this.colorB = colorB;
    }

    public static Pattern stripePattern(Color colorA, Color colorB) {
        return new Pattern(colorA, colorB);
    }

    Color getColorA() {
        return colorA;
    }

    Color getColorB() {
        return colorB;
    }

    Color colorAt(Tuple point) {

        int modulo = (int)(point.getX() % 2 + 2) % 2;

        if(modulo == 0) {
            return colorA;
        } else {
            return colorB;
        }
    }

    public void setTransformation(Matrix transformation) {
        this.transformation = transformation;
    }

    Color colorAtObject(Shape object, Tuple point) {
        Tuple objectPoint = object.getTransform().inverse().multiplyBy(point);
        Tuple patternPoint = transformation.inverse().multiplyBy(objectPoint);

        return colorAt(patternPoint);
    }
}
