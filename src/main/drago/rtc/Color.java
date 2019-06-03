package drago.rtc;

class Color extends Tuple {

    Color(double red, double green, double blue) {
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

    Color add(Color operand) {

        return fromTuple(super.add(operand));
    }

    Color subtract(Color operand) {

        return fromTuple(super.subtract(operand));
    }

    public Color scale(double operand) {
        return fromTuple(super.scale(operand));
    }

    private Color fromTuple(Tuple result) {
        return new Color(result.getX(), result.getY(), result.getZ());
    }

    public Color hadamardProduct(Color operand) {
        return new Color(this.getRed() * operand.getRed(),
                this.getGreen() * operand.getGreen(),
                this.getBlue() * operand.getBlue());
    }
}
