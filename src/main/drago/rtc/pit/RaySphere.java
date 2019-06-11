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
        rs.render("gallery/chapter7.ppm");

    }

    private void render(String imageFileName) {
        Canvas image = camera.render(world);
        save(image, imageFileName);
    }

    private void addCamera() {
        camera = new Camera(1000, 500, Math.PI / 3);
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

        world.setLightSource(Light.pointLight(Tuple.point(-10, 10, -10), Color.WHITE));
    }

    private Sphere floor() {
        Sphere floor = new Sphere();
        floor.setTransform(Matrix.scaling(10, 0.01, 10));
        floor.getMaterial().setColor(new Color(1, 0.9, 0.9));
        floor.getMaterial().setSpecular(0);

        return floor;
    }

    private Sphere leftWall() {
        Sphere leftWall = new Sphere();
        leftWall.setTransform(
                Matrix.translation(0, 0, 5)
                        .multiplyBy(Matrix.rotationY(- Math.PI / 4))
                        .multiplyBy(Matrix.rotationX(Math.PI / 2))
                        .multiplyBy(Matrix.scaling(10, 0.001, 10))
        );
        leftWall.getMaterial().setColor(new Color(1, 0.9, 0.9));
        leftWall.getMaterial().setSpecular(0);

        return leftWall;
    }

    private Sphere rightWall() {
        Sphere rightWall = new Sphere();
        rightWall.setTransform(
                Matrix.translation(0, 0, 5)
                        .multiplyBy(Matrix.rotationY(Math.PI / 4))
                        .multiplyBy(Matrix.rotationX(Math.PI / 2))
                        .multiplyBy(Matrix.scaling(10, 0.001, 10))
        );
        rightWall.getMaterial().setColor(new Color(1, 0.9, 0.9));
        rightWall.getMaterial().setSpecular(0);

        return rightWall;

    }

    private Sphere largeMiddle() {
        Sphere s = new Sphere();
        s.setTransform(Matrix.translation(-0.5, 1, 0.5));
        s.getMaterial().setColor(new Color(0.1, 1, 0.5));
        s.getMaterial().setDiffuse(0.7);
        s.getMaterial().setSpecular(0.3);

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
