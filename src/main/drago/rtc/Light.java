package drago.rtc;

class Light {
    private final Tuple position;
    private final Color intensity;

    private Light(Tuple position, Color intensity) {

        this.position = position;
        this.intensity = intensity;
    }


    static Light pointLight(Tuple position, Color intensity) {
        return new Light(position, intensity);
    }

    public Tuple getPosition() {
        return position;
    }

    public Color getIntensity() {
        return intensity;
    }
}
