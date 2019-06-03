package drago.rtc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CanvasTest {

    public static final Color BLACK = new Color(0, 0, 0);
    public static final Color RED = new Color(1, 0, 0);

    @Test
    void createACanvas() {
        Canvas c = new Canvas(10, 20);

        assertEquals(10, c.getWidth());
        assertEquals(20, c.getHeight());

        for (int wi = 0; wi < c.getWidth(); ++wi) {
            for(int hi = 0; hi < c.getHeight(); ++hi) {
                assertEquals(BLACK, c.pixelAt(wi, hi));
            }
        }
    }

    @Test
    void writingPixelsToACanvas() {
        Canvas c = new Canvas(10, 20);

        c.writePixel(2, 3, RED);

        assertEquals(RED, c.pixelAt(2, 3));
    }

    @Test
    void writingPixelsBelowCanvasDoesNothingToTheCanvas() {
        Canvas c = new Canvas(5, 3);

        c.writePixel(-2, -3, RED);

        assertEquals(BLACK, c.pixelAt(-2, -3));
    }

    @Test
    void writingPixelsAboveCanvasDoesNothingToTheCanvas() {
        Canvas c = new Canvas(5, 3);

        c.writePixel(10, 8, RED);

        assertEquals(BLACK, c.pixelAt(10, 8));
    }
}