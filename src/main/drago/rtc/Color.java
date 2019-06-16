package drago.rtc;

import drago.rtc.foundations.Tuple;

public class Color extends Tuple {

    public static final Color BLACK = new Color(0, 0, 0);
    public static final Color WHITE = new Color(1, 1, 1);

    public Color(double red, double green, double blue) {
        super(red, green, blue, 0.0);
    }

    double getRed() {
        return super.getX();
    }

    double getGreen() {
        return super.getY();
    }

    double getBlue() {
        return super.getZ();
    }

    public Color add(Color operand) {

        return fromTuple(super.add(operand));
    }

    public Color subtract(Color operand) {

        return fromTuple(super.subtract(operand));
    }

    public Color scale(double operand) {
        return fromTuple(super.scale(operand));
    }

    private Color fromTuple(Tuple result) {
        return new Color(result.getX(), result.getY(), result.getZ());
    }

    Color hadamardProduct(Color operand) {
        return new Color(this.getRed() * operand.getRed(),
                this.getGreen() * operand.getGreen(),
                this.getBlue() * operand.getBlue());
    }
}
