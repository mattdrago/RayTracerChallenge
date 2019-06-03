package drago.rtc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColorTest {

    @Test
    void colorsAreRedGreenBlueTuples() {
        Color c = new Color(-0.5, 0.4, 1.7);

        assertEquals(-0.5, c.getRed());
        assertEquals(0.4, c.getGreen());
        assertEquals(1.7, c.getBlue());
    }

    @Test
    void addingColors() {
        Color c1 = new Color(0.9, 0.6, 0.75);
        Color c2 = new Color(0.7, 0.1, 0.25);

        Color expected = new Color(1.6, 0.7, 1.0);
        Color actual = c1.add(c2);

        assertEquals(expected, actual);
    }

    @Test
    void subtractingColors() {
        Color c1 = new Color(0.9, 0.6, 0.75);
        Color c2 = new Color(0.7, 0.1, 0.25);

        Color expected = new Color(0.2, 0.5, 0.5);
        Color actual = c1.subtract(c2);

        assertEquals(expected, actual);
    }

    @Test
    void mulitplyingColorByAScalar() {
        Color c = new Color(0.2, 0.3, 0.4);

        Color expected = new Color(0.4, 0.6, 0.8);
        Color actual = c.scale(2);

        assertEquals(expected, actual);
    }

    @Test
    void multiplyingColors() {
        Color c1 = new Color(1, 0.2, 0.4);
        Color c2 = new Color(0.9, 1, 0.1);

        Color expected = new Color(0.9, 0.2, 0.04);
        Color actual = c1.hadamardProduct(c2);

        assertEquals(expected, actual);
    }
}