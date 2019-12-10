package drago.rtc.pit;

import drago.rtc.Color;
import drago.rtc.Material;
import drago.rtc.foundations.Matrix;
import drago.rtc.shape.CSG;
import drago.rtc.shape.Cube;
import drago.rtc.shape.Sphere;

public class Chapter16 extends ChapterBase {
    @Override
    void addShapes() {
        Sphere s = new Sphere();
        s.getMaterial().setColor(new Color(1.0, 0.1, 0.05));
//        s.setTransform(
//                Matrix.rotationY(Math.PI / 5)
//                        .multiplyBy(Matrix.translation(0, 2, 0))
//        );

        Cube c = new Cube();
        c.setTransform(
                Matrix.rotationY(Math.PI / 4)
                .multiplyBy(Matrix.rotationX(-Math.PI / 4))
                .multiplyBy(Matrix.translation(1, 0, 1))
                .multiplyBy(Matrix.scaling(1, 1, 10))
//                .multiplyBy(Matrix.rotationY(Math.PI / 2))
        );
        c.getMaterial().setTransparency(1.0);
        c.setCastShadow(false);
        c.getMaterial().setColor(Color.BLACK);

        CSG csg = new CSG(CSG.Operation.DIFFERENCE, s, c);
        csg.setTransform(
                Matrix.rotationY(Math.PI / 5)
                .multiplyBy(Matrix.translation(0, 2, 0))
        );
        world.getObjects().add(csg);
    }

    public static void main(String[] args) {
        new Chapter16().renderWorld("gallery/chapter16.ppm");
    }
}
