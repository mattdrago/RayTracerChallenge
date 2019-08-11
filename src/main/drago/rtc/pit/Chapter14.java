package drago.rtc.pit;

import drago.rtc.Camera;
import drago.rtc.Color;
import drago.rtc.Light;
import drago.rtc.World;
import drago.rtc.foundations.Matrix;
import drago.rtc.foundations.Tuple;
import drago.rtc.shape.Cylinder;
import drago.rtc.shape.Group;
import drago.rtc.shape.Shape;
import drago.rtc.shape.Sphere;

public class Chapter14 {
    private World world = new World();
    private Camera camera;

    private Chapter14() {
    }

    public static void main(String[] argv) {
        Chapter14 c14 = new Chapter14();

        long start = System.currentTimeMillis();
        System.out.println("Build world: 0");
        c14.buildWorld();
        System.out.println("Add a Camera: " + (System.currentTimeMillis() - start));
        c14.addCamera();

        System.out.println("Start Render:" + (System.currentTimeMillis() - start));
        c14.render("gallery/chapter14.ppm");
        System.out.println("Completed: " + (System.currentTimeMillis() - start));
    }

    private void buildWorld() {
        world.getObjects().add(hexagon());

        world.setLightSource(Light.pointLight(Tuple.point(-5, 10, -10), Color.WHITE));

    }

    private void addCamera() {
        camera = new Camera(1200, 600, Math.PI / 2);
        camera.setTransform(Matrix.viewTransform(
                Tuple.point(2, 1.5, -3),
                Tuple.point(0, 0.75, 0),
                Tuple.vector(0, 1, 0))
        );
    }

    private void render(String filename) {
        long start = System.currentTimeMillis();

        camera.render(world).save(filename);

        long end = System.currentTimeMillis();
        System.out.println("Total Render Time: " + (end - start) + "ms");
    }

    private Sphere hexagonCorner() {
        Sphere corner = new Sphere();
        corner.setTransform(Matrix.translation(0, 0, -1).multiplyBy(Matrix.scaling(0.25, 0.25, 0.25)));

        return corner;
    }

    private Cylinder hexagonEdge() {
        Cylinder edge = new Cylinder();
        edge.setMinimum(0);
        edge.setMaximum(1);
        edge.setTransform(Matrix.translation(0, 0, -1)
                .multiplyBy(Matrix.rotationY(-Math.PI / 6))
                .multiplyBy(Matrix.rotationZ(-Math.PI / 2))
                .multiplyBy(Matrix.scaling(0.25, 1, 0.25))
        );

        return edge;
    }

    private Group hexagonSide() {
        Group side = new Group();
        side.addChild(hexagonCorner());
        side.addChild(hexagonEdge());

        return side;
    }

    private Group hexagon() {
        Group hex = new Group();
        for (int i = 0; i < 6; i++) {
            Shape side = hexagonSide();
            side.setTransform(Matrix.rotationY(i * Math.PI / 3));
            hex.addChild(side);
        }

        return hex;
    }

}
