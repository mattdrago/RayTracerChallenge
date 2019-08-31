package drago.rtc.pit;

import drago.rtc.*;
import drago.rtc.foundations.Matrix;
import drago.rtc.foundations.Tuple;
import drago.rtc.pattern.CheckersPattern;
import drago.rtc.pattern.Pattern;
import drago.rtc.pattern.RingPattern;
import drago.rtc.shape.*;

public class Chapter15 extends ChapterBase {

    private Chapter15() {
    }

    public static void main(String[] argv) {
        Chapter15 c15 = new Chapter15();

        long start = System.currentTimeMillis();
        System.out.println("Build world: 0");
        c15.buildWorld();
        System.out.println("Add a Camera: " + (System.currentTimeMillis() - start));
        c15.addCamera();

        c15.addShapes();

        System.out.println("Start Render:" + (System.currentTimeMillis() - start));
        c15.render("gallery/chapter15.ppm");
        System.out.println("Completed: " + (System.currentTimeMillis() - start));
    }

    private void addShapes() {

        Group g = new Group();

        Triangle t = new Triangle(Tuple.point(-1, 0, 0), Tuple.point(0, 1, 0), Tuple.point(0, 0, 1));
        Material m = t.getMaterial();
        m.setColor(new Color(0.1, 0.0, 0.8));
        g.addChild(t);

        t = new Triangle(Tuple.point(-1, 0, 0), Tuple.point(0, 1, 0), Tuple.point(0, 0, -1));
        t.setMaterial(m);
        g.addChild(t);

        t = new Triangle(Tuple.point(1, 0, 0), Tuple.point(0, 1, 0), Tuple.point(0, 0, 1));
        t.setMaterial(m);
        g.addChild(t);

        t = new Triangle(Tuple.point(1, 0, 0), Tuple.point(0, 0, -1), Tuple.point(0, 1, 0));
        t.setMaterial(m);
        g.addChild(t);

        world.getObjects().add(g);
    }

}
