package drago.rtc.pit;

import drago.rtc.Camera;
import drago.rtc.foundations.Matrix;
import drago.rtc.foundations.Tuple;
import drago.rtc.shape.OBJReader;

import java.io.FileReader;
import java.io.IOException;

class Teapot extends ChapterBase {
    private Teapot() {

    }

    public static void main(String[] args) {
        Teapot t = new Teapot();
        t.renderWorld("gallery/teapot-smooth.ppm");

    }

    @Override
    Camera createCamera() {
        Camera camera = new Camera(1200, 600, Math.PI / 3);
        camera.setTransform(Matrix.viewTransform(
                Tuple.point(10, 5, -7),
                Tuple.point(-7, -2.5, 10),
                Tuple.vector(0, 1, 0))
        );

        return camera;
    }

    @Override
    void addShapes() {
        try(FileReader fr = new FileReader("models/teapot-smooth.obj")) {
            OBJReader objReader = new OBJReader(fr);
            objReader.parse();
            world.getObjects().add(objReader.toGroup(true));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
