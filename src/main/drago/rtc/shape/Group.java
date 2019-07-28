package drago.rtc.shape;

import drago.rtc.foundations.Intersection;
import drago.rtc.foundations.Ray;
import drago.rtc.foundations.Tuple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Group extends Shape {

    private List<Shape> children = new ArrayList<>();

    @Override
    Intersection[] localIntersect(Ray transformedRay) {
        List<Intersection> allXsList = new ArrayList<>();

        for(Shape s: children) {
            Intersection[] xs = s.intersects(transformedRay);
            allXsList.addAll(Arrays.asList(xs));
        }

        Intersection[] allXs = allXsList.toArray(new Intersection[0]);
        Arrays.sort(allXs);
        return allXs;
    }

    @Override
    Tuple localNormalAt(Tuple objectPoint) {
        throw new AbstractMethodError("Groups do not have a local Normal");
    }

    boolean hasChildren() {
        return children.size() > 0;
    }

    List<Shape> getChildren() {
        return children;
    }

    public void addChild(Shape s) {
        children.add(s);
        s.setParent(this);
    }
}
