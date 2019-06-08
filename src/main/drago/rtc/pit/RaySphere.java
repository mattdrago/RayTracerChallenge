package drago.rtc.pit;

import drago.rtc.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class RaySphere {

    private final Tuple rayOrigin;
    private Canvas c;
    private Color color = new Color(1.0, 0.0, 0.0);
    private Sphere s;
    private double wallZ;

    private RaySphere(int size) {
        c = new Canvas(size, size);

        wallZ = size;
        rayOrigin = Tuple.point(0, 0, -wallZ);
    }

    public static void main(String[] argv) {
        RaySphere rs = new RaySphere(100);

        rs.addSphere(20);

        rs.paint();
        rs.save();
    }

    private void addSphere(double size) {
        s = new Sphere();
        s.setTransform(Matrix.scaling(size, size, size));
    }

    private void save() {
        PPMWriter ppmWriter = new PPMWriter();

        try(BufferedWriter bw = Files.newBufferedWriter(Paths.get("raySphere.ppm"))) {
            bw.write(ppmWriter.write(c));
        } catch (IOException e) {
            System.out.println("Unable to save RaySphere: " + e.getMessage());
        }
    }

    private void paint() {
        for (int x = 0; x < c.getWidth(); x++) {
            for (int y = 0; y < c.getHeight(); y++) {

                Tuple position = Tuple.point(canvasXToWorld(x), canvasYToWorld(y), wallZ);

                Ray r = new Ray(rayOrigin, position.subtract(rayOrigin).normalise());
                Intersection i = Intersection.hit(s.intersects(r));

                if(i != null) {
                    c.writePixel(x, y, color);
                }
            }
        }
    }

    private double canvasYToWorld(int y) {
        return (c.getHeight() / 2.0) - y;
    }

    private double canvasXToWorld(int x) {
        return x - c.getWidth()/2.0;
    }
}
