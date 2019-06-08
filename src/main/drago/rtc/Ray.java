package drago.rtc;

class Ray {
    private final Tuple origin;
    private final Tuple direction;

    Ray(Tuple origin, Tuple direction) {
        this.origin = origin;
        this.direction = direction;
    }

    public Tuple getOrigin() {
        return origin;
    }

    public Tuple getDirection() {
        return direction;
    }

    public Tuple position(double t) {
        return origin.add(direction.scale(t));
    }
}
