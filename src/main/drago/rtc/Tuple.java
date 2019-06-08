package drago.rtc;

import java.util.Objects;

public class Tuple {
    private final double x;
    private final double y;
    private final double z;
    private final double w;

    private static final double POINT = 1.0;
    private static final double VECTOR = 0.0;

    private static final double EPSILON= 0.00001;

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

    double getW() {
        return w;
    }


    boolean isPoint() {
        return w == POINT;
    }

    boolean isVector() {
        return w == VECTOR;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple tuple = (Tuple) o;
        return equalDoubles(tuple.x, x) &&
                equalDoubles(tuple.y, y) &&
                equalDoubles(tuple.z, z) &&
                equalDoubles(tuple.w, w);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, w);
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", w=" + w +
                '}';
    }

    private boolean equalDoubles(double d1, double d2) {
        return Math.abs(d1 - d2) < EPSILON;
    }

    public Tuple add(Tuple operand) {
        return new Tuple(this.x + operand.x, this.y + operand.y, this.z + operand.z, this.w + operand.w);
    }

    public Tuple subtract(Tuple operand) {
        return new Tuple(this.x - operand.x, this.y - operand.y, this.z - operand.z, this.w - operand.w);
    }

    Tuple negate() {
        return new Tuple(-this.x, -this.y, -this.z, -this.w);
    }

    public Tuple scale(double operand) {
        return new Tuple(this.x * operand, this.y * operand, this.z * operand, this.w * operand );
    }

    Tuple divide(double operand) {
        return new Tuple(this.x / operand, this.y /operand, this.z / operand, this.w / operand );
    }

    double magnitude() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public Tuple normalise() {
        return this.divide(this.magnitude());
    }

    double dot(Tuple operand) {
        return this.x * operand.x + this.y * operand.y + this.z * operand.z + this.w * operand.w;
    }

    Tuple cross(Tuple operand) {
        return vector(this.y * operand.z - this.z * operand.y,
                this.z * operand.x - this.x * operand.z,
                this.x * operand.y - this.y * operand.x);
    }

    Tuple reflect(Tuple normal) {
        return this.subtract(normal.scale(2 * this.dot(normal)));
    }
}
