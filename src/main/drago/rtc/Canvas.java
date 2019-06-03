package drago.rtc;

import java.util.HashMap;

class Canvas {

    private final int width;
    private final int height;

    private final HashMap<String, Color> pixels = new HashMap<>();

    private static final Color DEFAULT_PIXEL = new Color(0, 0, 0);

    Canvas(int width, int height) {
        this.width = width;
        this.height = height;
    }

    int getWidth() {
        return width;
    }

    int getHeight() {
        return height;
    }

    Color pixelAt(int x, int y) {
        Color c = pixels.get(pixelKey(x, y));

        if(c == null) {
            c = DEFAULT_PIXEL;
        }

        return c;
    }

    private String pixelKey(int x, int y) {
        return x + "_" + y;
    }

    void writePixel(int x, int y, Color color) {
        pixels.put(pixelKey(x, y), color);
    }
}
