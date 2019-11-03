package drago.rtc.pit;

import drago.rtc.*;
import drago.rtc.foundations.Matrix;
import drago.rtc.foundations.Tuple;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class ClockFace {
    public static void main(String[] argv) {
        Tuple point = Tuple.point(0, 100, 0);
        Matrix rotationZ = Matrix.rotationZ(Math.PI / 6);

        Canvas canvas = new Canvas(250, 250);
        plot(point, canvas);

        for (int hours = 0; hours < 12; hours++) {
            point = rotationZ.multiplyBy(point);
            plot(point, canvas);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("plot.ppm"))) {
            PPMWriter ppmWriter = new PPMWriter();
            writer.write(ppmWriter.write(canvas));

            System.out.println("Plot saved to: " + Paths.get("plot.ppm"));
        } catch (IOException e) {
            System.out.println("Unable to save plot: " + e.getMessage());
        }
    }

    private static void plot(Tuple point, Canvas canvas) {
        // Convert points
        int x = (int) (point.getX() + (canvas.getWidth() / 2.0) + 0.5);
        int y = (int) (canvas.getHeight() - (point.getY() + (canvas.getHeight() / 2.0)) + 0.5);
        canvas.writePixel(x, y, new Color(255, 255, 255));
    }
}
