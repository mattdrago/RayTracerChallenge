package drago.rtc.shape;

import drago.rtc.foundations.Intersection;
import drago.rtc.foundations.Ray;
import drago.rtc.foundations.Tuple;

import java.util.*;

public class Group extends Shape {

    private final List<Shape> children = new ArrayList<>();
    private Bounds bounds = null;

    @Override
    Intersection[] localIntersect(Ray transformedRay) {
        Intersection[] allXs = {};

        if(hasChildren() && getBounds().intersected(transformedRay)) {
            List<Intersection> allXsList = new ArrayList<>();

            for (Shape s : children) {
                Intersection[] xs = s.intersects(transformedRay);
                allXsList.addAll(Arrays.asList(xs));
            }

            allXs = allXsList.toArray(new Intersection[0]);
            Arrays.sort(allXs);
        }

        return allXs;
    }

    @Override
    Tuple localNormalAt(Tuple objectPoint, Intersection intersection) {
        throw new AbstractMethodError("Groups do not have a local Normal");
    }

    @Override
    Bounds getBounds() {
        if(bounds == null) {
            bounds = calculateBounds();
        }

        return bounds;
    }

    private Bounds calculateBounds() {
        Bounds b = null;

        for (Shape child: children) {
            Bounds childBounds = child.getBounds();
            if(childBounds != null) {
                b = childBounds.transform(child.getTransform()).combine(b);
            }
        }

        return b;
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

    @Override
    boolean includes(Shape other) {
        for(Shape s : children) {
            if(s.includes(other)) {
                return true;
            }
        }
        return false;
    }


    void subDivide() {
        List<Bounds> subBounds = calculateBounds().divide();
        Map<Bounds, Group> subGroups = createSubGroups(subBounds);

        for(Iterator<Shape> iter = children.iterator(); iter.hasNext(); ) {
            Shape child = iter.next();
            Bounds childBounds = child.getBounds().transform(child.getTransform());
            for(Bounds bound : subBounds) {
                if(bound.contains(childBounds)) {
                    subGroups.get(bound).addChild(child);
                    iter.remove();
                    break;
                }
            }
        }

        for(Group subGroup : subGroups.values()) {
            if(subGroup.hasChildren()) {
                addChild(subGroup);
            }
        }
    }

    private Map<Bounds, Group> createSubGroups(List<Bounds> subBounds) {
        Map<Bounds, Group> subGroupMap = new HashMap<>();
        for(Bounds bound : subBounds) {
            subGroupMap.put(bound, new Group());
        }

        return subGroupMap;
    }
}
