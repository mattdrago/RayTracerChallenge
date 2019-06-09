package drago.rtc.pit;

import drago.rtc.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class RaySphere {

    private final Tuple rayOrigin = Tuple.point(0, 0, -5);
    private Canvas c = new Canvas(500, 500);
    private Sphere s;
    private Light light = Light.pointLight(Tuple.point(-10, 10, -10), new Color(1, 1, 1));

    private double wallSize = 7.0;
    private double wallZ = 10;

    private double pixelSize = wallSize / c.getWidth();

    private RaySphere() {
    }

    public static void main(String[] argv) {
        RaySphere rs = new RaySphere();

        rs.addSphere();

        rs.paint();
        rs.save();
    }

    private void addSphere() {
        s = new Sphere();

        Material m = new Material();
        m.setColor(new Color(1, 0.2, 1));
        s.setMaterial(m);
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
        double half = wallSize / 2.0;

        for (int y = 0; y < c.getHeight(); y++) {
            double worldY = half - pixelSize * y;

            for (int x = 0; x < c.getWidth(); x++) {
                double worldX = - half + pixelSize * x;

                Tuple position = Tuple.point(worldX, worldY, wallZ);

                Ray r = new Ray(rayOrigin, position.subtract(rayOrigin).normalise());
                Intersection i = Intersection.hit(s.intersects(r));

                if(i != null) {
                    Tuple point = r.position(i.getT());
                    Tuple normalAt = i.getObject().normalAt(point);

                    Tuple eyeV = r.getDirection().scale(-1);


                    Color color = i.getObject().getMaterial().lighting(light, point, eyeV, normalAt);

                    c.writePixel(x, y, color);
                }
            }
        }
    }
}
