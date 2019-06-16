package drago.rtc;

import drago.rtc.foundations.Tuple;

import java.util.Objects;

public class Light {
    private final Tuple position;
    private final Color intensity;

    private Light(Tuple position, Color intensity) {

        this.position = position;
        this.intensity = intensity;
    }


    public static Light pointLight(Tuple position, Color intensity) {
        return new Light(position, intensity);
    }

    Tuple getPosition() {
        return position;
    }

    Color getIntensity() {
        return intensity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Light light = (Light) o;
        return Objects.equals(position, light.position) &&
                Objects.equals(intensity, light.intensity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, intensity);
    }

    @Override
    public String toString() {
        return "Light{" +
                "position=" + position +
                ", intensity=" + intensity +
                '}';
    }
}
