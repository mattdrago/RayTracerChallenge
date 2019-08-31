package drago.rtc.pit;

import drago.rtc.*;
import drago.rtc.foundations.Matrix;
import drago.rtc.foundations.Tuple;
import drago.rtc.pattern.CheckersPattern;
import drago.rtc.shape.Plane;

class ChapterBase {
    World world = new World();
    private Camera camera;

    void buildWorld() {
        addWalls();

        world.setLightSource(Light.pointLight(Tuple.point(-3, 20, -5), Color.WHITE));
    }

    void addCamera() {
        camera = new Camera(1200, 600, Math.PI / 3);
        camera.setTransform(Matrix.viewTransform(
                Tuple.point(20, 5, -14),
                Tuple.point(-14, -10, 20),
                Tuple.vector(0, 1, 0))
        );
    }

    void render(String filename) {
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
}
