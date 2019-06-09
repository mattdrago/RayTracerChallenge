package drago.rtc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LightTest {
    @Test
    void aPointLightHasAPositionAndIntensity() {
        Color intensity = new Color(1, 1, 1);
        Tuple position = Tuple.point(0, 0, 0);

        Light light = Light.pointLight(position, intensity);

        assertEquals(position, light.getPosition());
        assertEquals(intensity, light.getIntensity());
    }
}