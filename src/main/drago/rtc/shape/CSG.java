package drago.rtc.shape;

import drago.rtc.foundations.Intersection;
import drago.rtc.foundations.Ray;
import drago.rtc.foundations.Tuple;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSG extends Shape {


    private final Operation op;
    private final Shape left;
    private final Shape right;

    public CSG(Operation op, Shape left, Shape right) {
        this.op = op;
        this.left = left;
        this.right = right;

        this.left.setParent(this);
        this.right.setParent(this);
    }

    Shape getLeft() {
        return this.left;
    }

    Shape getRight() {
        return this.right;
    }

    Intersection[] filterIntersections(Intersection[] xs) {
        boolean insideLeft = false;
        boolean insideRight = false;

        List<Intersection> result = new ArrayList<>(xs.length);

        for (Intersection i : xs) {
            boolean leftHit = this.left.includes(i.getShape());

            if(this.op.intersectionAllowed(leftHit, insideLeft, insideRight)) {
                result.add(i);
            }

            if(leftHit) {
                insideLeft = !insideLeft;
            } else {
                insideRight = !insideRight;
            }
        }

        return result.toArray(new Intersection[0]);
    }

    @Override
    Intersection[] localIntersect(Ray transformedRay) {
        Intersection[] xsL = left.intersects(transformedRay);
        Intersection[] xsR = right.intersects(transformedRay);

        Intersection[] xs = Arrays.copyOf(xsL, xsL.length + xsR.length);
        System.arraycopy(xsR, 0, xs, xsL.length, xsR.length);
        Arrays.sort(xs);

        return filterIntersections(xs);
    }

    @Override
    Tuple localNormalAt(Tuple objectPoint, Intersection intersection) {
        throw new AbstractMethodError("CSGs do not have a local Normal");
    }

    @Override
    Bounds getBounds() {
        Bounds leftBounds = left.getBounds().transform(left.getTransform());
        Bounds rightBounds = right.getBounds().transform(right.getTransform());

        return leftBounds.combine(rightBounds);
    }

    @Override
    boolean includes(Shape other) {
        return this.left.includes(other) || this.right.includes(other);
    }

    public enum Operation {
        UNION((leftHit, insideLeft, insideRight) -> (leftHit && !insideRight) || (!leftHit && !insideLeft)),
        INTERSECTION((leftHit, insideLeft, insideRight) -> (leftHit && insideRight) || (!leftHit && insideLeft)),
        DIFFERENCE((leftHit, insideLeft, insideRight) -> (leftHit && !insideRight) || (!leftHit && insideLeft));

        private IntersectionAssertion intersectionAssertion;

        Operation(IntersectionAssertion intersectionAssertion) {
            this.intersectionAssertion = intersectionAssertion;
        }

        boolean intersectionAllowed(boolean leftHit, boolean insideLeft, boolean insideRight) {
            return intersectionAssertion.intersectionAllowed(leftHit, insideLeft, insideRight);
        }

        private interface IntersectionAssertion {
            boolean intersectionAllowed(boolean leftHit, boolean insideLeft, boolean insideRight);
        }
    }
}
