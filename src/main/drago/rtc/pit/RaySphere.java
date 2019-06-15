package drago.rtc.pit;

import drago.rtc.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class RaySphere {

    private World world = new World();
    private Camera camera;

    private RaySphere() {
    }

    public static void main(String[] argv) {
        RaySphere rs = new RaySphere();

        rs.buildWorld();
        rs.addCamera();

        long start = System.currentTimeMillis();
        rs.render("gallery/chapter9.ppm");
        long end = System.currentTimeMillis();
        System.out.println("Total Render Time: " + (end - start) + "ms");
    }

    private void render(String imageFileName) {
        Canvas image = camera.render(world);
        save(image, imageFileName);
    }

    private void addCamera() {
        camera = new Camera(1200, 600, Math.PI / 3);
        camera.setTransform(Matrix.viewTransform(
                Tuple.point(0, 1.5, -5),
                Tuple.point(0, 1, 0),
                Tuple.vector(0, 1, 0))
        );
    }

    private void buildWorld() {
        world.getObjects().add(floor());
        world.getObjects().add(leftWall());
        world.getObjects().add(rightWall());
        world.getObjects().add(largeMiddle());
        world.getObjects().add(smallLeft());
        world.getObjects().add(smallRight());

        world.setLightSource(Light.pointLight(Tuple.point(-5, 10, -10), Color.WHITE));
    }

    private Plane floor() {
        Plane floor = new Plane();

        floor.getMaterial().setColor(new Color(1, 0.9, 0.9));
        floor.getMaterial().setSpecular(0);
        floor.getMaterial().setAmbient(0.2);
        floor.getMaterial().setDiffuse(0.8);
        floor.getMaterial().setPattern(Pattern.stripePattern(Color.WHITE, Color.BLACK));

        return floor;
    }

    private Plane leftWall() {
        Plane wall = new Plane();

        wall.setTransform(Matrix.translation(-11, 0, 0).multiplyBy(Matrix.rotationY(- Math.PI / 2)).multiplyBy(Matrix.rotationX(Math.PI / 2)));
        wall.getMaterial().setColor(new Color(1, 0.9, 0.9));
        wall.getMaterial().setSpecular(0);
        wall.getMaterial().setAmbient(0.2);
        wall.getMaterial().setDiffuse(0.8);
        wall.getMaterial().setPattern(Pattern.stripePattern(Color.WHITE, Color.BLACK));

        return wall;
    }

    private Plane rightWall() {
        Plane wall = new Plane();

        wall.setTransform(Matrix.translation(11, 0, 0).multiplyBy(Matrix.rotationY(Math.PI / 2)).multiplyBy(Matrix.rotationX(Math.PI / 2)));
        wall.getMaterial().setColor(new Color(1, 0.9, 0.9));
        wall.getMaterial().setSpecular(0);
        wall.getMaterial().setAmbient(0.2);
        wall.getMaterial().setDiffuse(0.8);
        wall.getMaterial().setPattern(Pattern.stripePattern(Color.WHITE, Color.BLACK));

        return wall;
    }

    private Sphere largeMiddle() {
        Sphere s = new Sphere();
        s.setTransform(Matrix.translation(-0.5, 1, 0.5));
        s.getMaterial().setColor(new Color(0.1, 1, 0.5));
        s.getMaterial().setDiffuse(0.7);
        s.getMaterial().setSpecular(0.3);

        Pattern p = Pattern.stripePattern(new Color(0.1, 1, 0.5), new Color(0.9, 0, 0.5));
        p.setTransformation(Matrix.rotationY(Math.PI / 7).multiplyBy(Matrix.rotationZ(Math.PI / 5)).multiplyBy(Matrix.scaling(0.1, 1, 1)));
        s.getMaterial().setPattern(p);


        return s;
    }

    private Sphere smallRight() {
        Sphere s = new Sphere();
        s.setTransform(
                Matrix.translation(1.5, 0.5, -0.5)
                .multiplyBy(Matrix.scaling(0.5, 0.5, 0.5))
        );

        s.getMaterial().setColor(new Color(0.5, 1, 0.1));
        s.getMaterial().setDiffuse(0.7);
        s.getMaterial().setSpecular(0.3);

        return s;
    }

    private Sphere smallLeft() {
        Sphere s = new Sphere();
        s.setTransform(
                Matrix.translation(-1.5, 0.33, -0.75)
                .multiplyBy(Matrix.scaling(0.33, 0.33, 0.33))
        );

        s.getMaterial().setColor(new Color(1, 0.8, 0.1));
        s.getMaterial().setDiffuse(0.7);
        s.getMaterial().setSpecular(0.3);

        return s;
    }

    private void save(Canvas image, String imageFileName) {
        PPMWriter ppmWriter = new PPMWriter();

        try(BufferedWriter bw = Files.newBufferedWriter(Paths.get(imageFileName))) {
            bw.write(ppmWriter.write(image));
        } catch (IOException e) {
            System.out.println("Unable to save RaySphere: " + e.getMessage());
        }
    }
}
