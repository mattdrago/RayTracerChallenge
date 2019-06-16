package drago.rtc.foundations;

import java.util.Objects;

public class Ray {
    private final Tuple origin;
    private final Tuple direction;

    public Ray(Tuple origin, Tuple direction) {
        this.origin = origin;
        this.direction = direction;
    }

    public Tuple getOrigin() {
        return origin;
    }

    public Tuple getDirection() {
        return direction;
    }

    Tuple position(double t) {
        return origin.add(direction.scale(t));
    }

    public Ray transform(Matrix transform) {

        return new Ray(transform.multiplyBy(origin), transform.multiplyBy(direction));
    }

    @Override
    public String toString() {
        return "Ray{" +
                "origin=" + origin +
                ", direction=" + direction +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ray ray = (Ray) o;
        return Objects.equals(origin, ray.origin) &&
                Objects.equals(direction, ray.direction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(origin, direction);
    }
}
