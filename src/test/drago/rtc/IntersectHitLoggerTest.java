package drago.rtc;

import drago.rtc.foundations.Intersection;
import drago.rtc.shape.Group;
import drago.rtc.shape.Shape;
import drago.rtc.shape.Sphere;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntersectHitLoggerTest {

    @BeforeEach
    void resetLogger() {
        IntersectHitLogger.reset();
    }

    @Test
    void logsAHitWithNoParent() {
        Shape s = new Sphere();
        Intersection[] xs = {
                new Intersection(2, s),
        };

        IntersectHitLogger.log(s, xs);

        String report = IntersectHitLogger.report();
        String expected = String.join(",", new String[] {
                ((Object)s).toString(),
                "1", "1", Boolean.FALSE.toString()
        }) + "\n";

        assertEquals(expected, report);
    }

    @Test
    void logsAHitWithParent() {
        Shape s = new Sphere();
        Group g = new Group();
        g.addChild(s);

        Intersection[] xs = {
                new Intersection(2, s),
        };

        IntersectHitLogger.log(s, xs);

        String report = IntersectHitLogger.report();
        String expected = String.join(",", new String[] {
                ((Object)s).toString(),
                "1", "1", Boolean.TRUE.toString()
        }) + "\n";

        assertEquals(expected, report);
    }

    @Test
    void logsMultipleHits() {
        Shape s = new Sphere();
        Intersection[] xs = {
                new Intersection(2, s),
        };

        IntersectHitLogger.log(s, xs);
        IntersectHitLogger.log(s, xs);
        IntersectHitLogger.log(s, xs);

        String report = IntersectHitLogger.report();
        String expected = String.join(",", new String[] {
                ((Object)s).toString(),
                "3", "3", Boolean.FALSE.toString()
        }) + "\n";

        assertEquals(expected, report);
    }

    @Test
    void logsHitsAndMisses() {
        Shape s = new Sphere();
        Intersection[] xs = {
                new Intersection(2, s),
        };

        IntersectHitLogger.log(s, xs);
        IntersectHitLogger.log(s, xs);
        IntersectHitLogger.log(s, xs);

        xs = new Intersection[0];
        IntersectHitLogger.log(s, xs);
        IntersectHitLogger.log(s, xs);

        String report = IntersectHitLogger.report();
        String expected = String.join(",", new String[] {
                ((Object)s).toString(),
                "5", "3", Boolean.FALSE.toString()
        }) + "\n";

        assertEquals(expected, report);
    }
}