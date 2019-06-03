package drago.rtc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PPMWriterTest {
    @Test
    void constructingThePPMHeader() {
        Canvas c = new Canvas(5, 3);
        PPMWriter ppmWriter = new PPMWriter();

        String expected = "P3\n5 3\n255\n";
        String actual = ppmWriter.write(c).substring(0, "P3\n5 3\n255\n".length());

        assertEquals(expected, actual);
    }

    @Test
    void constructingThePPMPixelData() {
        Canvas canvas = new Canvas(5, 3);
        PPMWriter ppmWriter = new PPMWriter();

        Color c1 = new Color(1.5, 0, 0);
        Color c2 = new Color(0, 0.5, 0);
        Color c3 = new Color(-0.5, 0, 1);

        canvas.writePixel(0, 0, c1);
        canvas.writePixel(2, 1, c2);
        canvas.writePixel(4, 2, c3);

        String expected = "255 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n" +
                "0 0 0 0 0 0 0 128 0 0 0 0 0 0 0\n" +
                "0 0 0 0 0 0 0 0 0 0 0 0 0 0 255\n" ;

        String ppm = ppmWriter.write(canvas);
        String actual = ppm.substring("P3\n5 3\n255\n".length());

        assertEquals(expected, actual);
    }

    @Test
    void splittingLongLinesInPPMFiles() {
        Canvas canvas = new Canvas(10, 2);
        PPMWriter ppmWriter = new PPMWriter();
        Color c1 = new Color(1, 0.8, 0.6);

        for (int wi = 0; wi < canvas.getWidth(); wi++) {
            for (int hi = 0; hi < canvas.getHeight(); hi++) {
                canvas.writePixel(wi, hi, c1);
            }
        }

        String expected = "255 204 153 255 204 153 255 204 153 255 204 153 255 204 153 255 204\n"
                + "153 255 204 153 255 204 153 255 204 153 255 204 153\n"
                + "255 204 153 255 204 153 255 204 153 255 204 153 255 204 153 255 204\n"
                + "153 255 204 153 255 204 153 255 204 153 255 204 153\n";

        String ppm = ppmWriter.write(canvas);
        String actual = ppm.substring("P3\n10 2\n255\n".length());

        assertEquals(expected, actual);
    }

    @Test
    void ppmFilesAreTerminatedByANewlineCharacter() {
        Canvas canvas = new Canvas (5, 3);
        PPMWriter ppmWriter = new PPMWriter();

        assertTrue(ppmWriter.write(canvas).endsWith("\n"));
    }
}