package drago.rtc;

import drago.rtc.foundations.Tuple;
import drago.rtc.pattern.StripePattern;
import drago.rtc.shape.Sphere;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MaterialTest {

    private Material material;
    private Tuple position;

    @BeforeEach
    void setup() {
        material = new Material();
        position = Tuple.point(0, 0, 0);
    }

    @Test
    void defaultMaterial() {
        assertEquals(new Color(1, 1, 1), material.getColor());
        assertEquals(0.1, material.getAmbient());
        assertEquals(0.9, material.getDiffuse());
        assertEquals(0.9, material.getSpecular());
        assertEquals(200.0, material.getShininess());
    }

    @Test
    void lightingWithTheEyeBetweenTheLightAndTheSurface() {
        Tuple eyeV = Tuple.vector(0, 0, -1);
        Tuple normalV = Tuple.vector(0, 0, -1);
        Light light = Light.pointLight(Tuple.point(0, 0, -10), new Color(1,1, 1));

        Color expectedColor = new Color(1.9, 1.9, 1.9);
        Color actualColor = material.lighting(light, new Sphere(), position, eyeV, normalV, false);

        assertEquals(expectedColor, actualColor);
    }

    @Test
    void lightingWithTheEyeBetweenLightAndSurfaceEyeOffset45Degrees() {
        Tuple eyeV = Tuple.vector(0, Math.sqrt(2) / 2, - Math.sqrt(2) / 2);
        Tuple normalV = Tuple.vector(0, 0, -1);
        Light light = Light.pointLight(Tuple.point(0, 0, -10), new Color(1,1, 1));

        Color expectedColor = new Color(1.0, 1.0, 1.0);
        Color actualColor = material.lighting(light, new Sphere(), position, eyeV, normalV, false);

        assertEquals(expectedColor, actualColor);
    }

    @Test
    void lightingWithEyeOppositeSurfaceLightOffset45Degrees() {
        Tuple eyeV = Tuple.vector(0, 0, -1);
        Tuple normalV = Tuple.vector(0, 0, -1);
        Light light = Light.pointLight(Tuple.point(0, 10, -10), new Color(1,1, 1));

        Color expectedColor = new Color(0.7364, 0.7364, 0.7364);
        Color actualColor = material.lighting(light, new Sphere(), position, eyeV, normalV, false);

        assertEquals(expectedColor, actualColor);
    }

    @Test
    void lightingWithEyeInThePathOfTheReflectionVector() {
        Tuple eyeV = Tuple.vector(0,  - Math.sqrt(2) / 2, - Math.sqrt(2) / 2);
        Tuple normalV = Tuple.vector(0, 0, -1);
        Light light = Light.pointLight(Tuple.point(0, 10, -10), new Color(1,1, 1));

        Color expectedColor = new Color(1.6364, 1.6364, 1.6364);
        Color actualColor = material.lighting(light, new Sphere(), position, eyeV, normalV, false);

        assertEquals(expectedColor, actualColor);
    }

    @Test
    void lightingWithTheLightBehindTheSurface() {
        Tuple eyeV = Tuple.vector(0, 0, -1);
        Tuple normalV = Tuple.vector(0, 0, -1);
        Light light = Light.pointLight(Tuple.point(0, 0, 10), new Color(1, 1, 1));

        Color expectedColor = new Color(0.1, 0.1, 0.1);
        Color actualColor = material.lighting(light, new Sphere(), position, eyeV, normalV, false);

        assertEquals(expectedColor, actualColor);
    }

    @Test
    void lightingWithTheSurfaceInShadow() {
        Tuple eyeV = Tuple.vector(0, 0, -1);
        Tuple normalV = Tuple.vector(0, 0, -1);
        Light light = Light.pointLight(Tuple.point(0, 0, -10), Color.WHITE);
        boolean inShadow = true;

        Color expected = new Color(0.1, 0.1, 0.1);
        Color actual = material.lighting(light, new Sphere(), position, eyeV, normalV, inShadow);

        assertEquals(expected, actual);
    }

    @Test
    void lightingWithAPatternApplied() {
        material.setPattern(new StripePattern(Color.WHITE, Color.BLACK));
        material.setAmbient(1);
        material.setDiffuse(0);
        material.setSpecular(0);

        Tuple eyeV = Tuple.vector(0, 0, -1);
        Tuple normalV = Tuple.vector(0, 0, -1);
        Light light = Light.pointLight(Tuple.point(0, 0, -10), Color.WHITE);

        assertEquals(Color.WHITE, material.lighting(light, new Sphere(), Tuple.point(0.9, 0, 0), eyeV, normalV, false));
        assertEquals(Color.BLACK, material.lighting(light, new Sphere(), Tuple.point(1.1, 0, 0), eyeV, normalV, false));
    }
}