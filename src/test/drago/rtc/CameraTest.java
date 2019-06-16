package drago.rtc;

import drago.rtc.foundations.Matrix;
import drago.rtc.foundations.Ray;
import drago.rtc.foundations.Tuple;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CameraTest {

    private static final double EPSILON = 0.000001;

    @Test
    void constructingACamera() {
        int hSize = 160;
        int vSize = 120;
        double fieldOfView = Math.PI / 2;

        Camera c = new Camera(hSize, vSize, fieldOfView);

        assertEquals(hSize, c.getHSize());
        assertEquals(vSize, c.getVSize());
        assertEquals(fieldOfView, c.getFieldOfView());
        assertEquals(Matrix.identity(4), c.getTransform());
    }

    @Test
    void thePixelSizeForAHorizontalCanvas() {
        Camera c = new Camera(200, 125, Math.PI / 2);

        double expectedPixelSize = 0.01;

        assertTrue(Math.abs(expectedPixelSize - c.getPixelSize()) < EPSILON);
    }

    @Test
    void thePixelSizeForAVerticalCanvas() {
        Camera c = new Camera(125, 200, Math.PI / 2);

        double expectedPixelSize = 0.01;

        assertTrue(Math.abs(expectedPixelSize - c.getPixelSize()) < EPSILON);
    }

    @Test
    void constructingARayThroughTheCenterOfTheCanvas() {
        Camera c = new Camera(201, 101, Math.PI / 2);
        Ray r = c.rayForPixel(100, 50);

        assertEquals(Tuple.point(0, 0, 0), r.getOrigin());
        assertEquals(Tuple.vector(0, 0, -1), r.getDirection());
    }

    @Test
    void constructingARayThroughACornerOfTheCanvas() {
        Camera c = new Camera(201, 101, Math.PI / 2);
        Ray r = c.rayForPixel(0, 0);

        assertEquals(Tuple.point(0, 0, 0), r.getOrigin());
        assertEquals(Tuple.vector(0.66519, 0.33259, -0.66851), r.getDirection());
    }

    @Test
    void constructingARayWhenTheCameraIsTransformed() {
        Camera c = new Camera(201, 101, Math.PI / 2);
        c.setTransform(Matrix.rotationY(Math.PI / 4).multiplyBy(Matrix.translation(0, -2, 5)));

        Ray r = c.rayForPixel(100, 50);

        assertEquals(Tuple.point(0, 2, -5), r.getOrigin());
        assertEquals(Tuple.vector(Math.sqrt(2) / 2, 0, -Math.sqrt(2) / 2), r.getDirection());
    }

    @Test
    void renderingAWorldWithACamera() {
        World w = World.defaultWorld();
        Camera c = new Camera(11, 11, Math.PI / 2);

        Tuple from = Tuple.point(0, 0, -5);
        Tuple to = Tuple.point(0, 0, 0);
        Tuple up = Tuple.vector(0, 1, 0);

        Matrix viewTransform = Matrix.viewTransform(from, to, up);

        c.setTransform(viewTransform);

        Canvas image = c.render(w);

        assertEquals(new Color(0.38066, 0.47583, 0.2855), image.pixelAt(5, 5));
    }
}