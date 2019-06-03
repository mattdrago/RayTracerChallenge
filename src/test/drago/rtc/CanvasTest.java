package drago.rtc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CanvasTest {

    @Test
    void createACanvas() {
        Canvas c = new Canvas(10, 20);
        Color black = new Color(0, 0, 0);

        assertEquals(10, c.getWidth());
        assertEquals(20, c.getHeight());

        for (int wi = 0; wi < c.getWidth(); ++wi) {
            for(int hi = 0; hi < c.getHeight(); ++hi) {
                assertEquals(black, c.pixelAt(wi, hi));
            }
        }
    }

    @Test
    void writingPixelsToACanvas() {
        Canvas c = new Canvas(10, 20);
        Color red = new Color(1, 0, 0);

        c.writePixel(2, 3, red);

        assertEquals(red, c.pixelAt(2, 3));
    }
}