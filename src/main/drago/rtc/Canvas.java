package drago.rtc;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Canvas {

    private final int width;
    private final int height;

    private final Color[][] pixels;

    private static final Color DEFAULT_PIXEL = new Color(0, 0, 0);

    public Canvas(int width, int height) {
        this.width = width;
        this.height = height;

        pixels = new Color[width][height];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    Color pixelAt(int x, int y) {
        Color c = null;

        if(withinCanvasBounds(x, y)) {
            c = pixels[x][y];
        }

        if(c == null) {
            c = DEFAULT_PIXEL;
        }

        return c;
    }

    public void writePixel(int x, int y, Color color) {
        if(withinCanvasBounds(x, y)) {
            pixels[x][y] = color;
        }
    }

    private boolean withinCanvasBounds(int x, int y) {
        return x >= 0
            && x < width
            && y >=0
            && y < height;
    }

    public void save(String imageFileName) {
        PPMWriter ppmWriter = new PPMWriter();

        try(BufferedWriter bw = Files.newBufferedWriter(Paths.get(imageFileName))) {
            bw.write(ppmWriter.write(this));
        } catch (IOException e) {
            System.out.println("Unable to save " + imageFileName + ": " + e.getMessage());
        }
    }

}
