package drago.rtc.pit;

import drago.rtc.*;
import drago.rtc.foundations.Matrix;
import drago.rtc.foundations.Tuple;
import drago.rtc.pattern.CheckersPattern;
import drago.rtc.shape.Plane;

abstract class ChapterBase {
    World world = new World();
    private Camera camera;

    private void buildWorld() {
        addWalls();

        world.setLightSource(Light.pointLight(Tuple.point(-3, 20, -5), Color.WHITE));
    }

    Camera createCamera() {
        Camera defaultCamera = new Camera(1200, 600, Math.PI / 3);
        defaultCamera.setTransform(Matrix.viewTransform(
                Tuple.point(20, 5, -14),
                Tuple.point(-14, -10, 20),
                Tuple.vector(0, 1, 0))
        );

        return defaultCamera;
    }

    private void render(String filename) {
        long start = System.currentTimeMillis();

        camera.render(world).save(filename);

        long end = System.currentTimeMillis();
        System.out.println("Total Render Time: " + (end - start) + "ms");
    }

    private void addWalls() {
        Material m = new Material();
        double light = 0.93;
        double dark = 0.89;
        m.setPattern(new CheckersPattern(new Color(dark, dark, dark), new Color(light, light, light)));

        Plane floor = new Plane();
        floor.setMaterial(m);
        world.getObjects().add(floor);

        Plane wall = new Plane();
        wall.setMaterial(m);
        wall.setTransform(Matrix.translation(1, 0, 10).multiplyBy(Matrix.rotationX(Math.PI / 2)));
        world.getObjects().add(wall);

        wall = new Plane();
        wall.setMaterial(m);
        wall.setTransform(Matrix.translation(-10, 0, 0).multiplyBy(Matrix.rotationZ(Math.PI / 2)));
        world.getObjects().add(wall);
    }

    void renderWorld(String filename) {
        long start = System.currentTimeMillis();
        System.out.println("Build world: 0");
        buildWorld();
        System.out.println("Add a Camera: " + (System.currentTimeMillis() - start));
        camera = createCamera();

        System.out.println("Add other shapes: " + (System.currentTimeMillis() - start));
        addShapes();

        System.out.println("Start Render:" + (System.currentTimeMillis() - start));
        render(filename);
        System.out.println("Completed: " + (System.currentTimeMillis() - start));
    }

    abstract void addShapes();
}
