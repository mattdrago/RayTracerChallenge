package drago.rtc;

import java.util.Objects;

public class Tuple {
    private final double x;
    private final double y;
    private final double z;
    private final double w;

    private static final double POINT = 1.0;
    private static final double VECTOR = 0.0;

    public Tuple(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public static Tuple point(double x, double y, double z) {
        return new Tuple(x, y, z, POINT);
    }

    public static Tuple vector(double x, double y, double z) {
        return new Tuple(x, y, z, VECTOR);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getW() {
        return w;
    }


    public boolean isPoint() {
        return w == POINT;
    }

    public boolean isVector() {
        return w == VECTOR;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple tuple = (Tuple) o;
        return Double.compare(tuple.x, x) == 0 &&
                Double.compare(tuple.y, y) == 0 &&
                Double.compare(tuple.z, z) == 0 &&
                Double.compare(tuple.w, w) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, w);
    }

    public Tuple add(Tuple operand) {
        return new Tuple(this.x + operand.x, this.y + operand.y, this.z + operand.z, this.w + operand.w);
    }

    public Tuple subtract(Tuple operand) {
        return new Tuple(this.x - operand.x, this.y - operand.y, this.z - operand.z, this.w - operand.w);
    }
}
