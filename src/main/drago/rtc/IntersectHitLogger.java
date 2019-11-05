package drago.rtc;

import drago.rtc.foundations.Intersection;
import drago.rtc.shape.Shape;

import java.util.HashMap;
import java.util.Map;

public class IntersectHitLogger {

    public static final boolean ENABLED = false;
    static private final Map<String, ShapeHitCount> hitCounts = new HashMap<>();

    public static void log(Shape s, Intersection[] xs) {
        String key = s.toString();

        if(!hitCounts.containsKey(key)) {
            hitCounts.put(key, new ShapeHitCount(key, s.getParent() != null));
        }
        ShapeHitCount shc = hitCounts.get(key);
        shc.count(xs);
    }

    public static String report() {
        StringBuilder sb = new StringBuilder();

        for (ShapeHitCount shc: hitCounts.values()) {
            sb.append(shc.toString()).append("\n");
        }

        return sb.toString();
    }

    static void reset() {
        hitCounts.clear();
    }

    static private class ShapeHitCount {

        private final String key;
        private final boolean hasParent;
        private long calls;
        private long hits;

        ShapeHitCount(String key, boolean hasParent) {
            this.key = key;
            this.hasParent = hasParent;
        }

        void count(Intersection[] xs) {
            this.calls++;

            if(Intersection.hit(xs) != null) {
                this.hits++;
            }
        }

        public String toString() {
            return String.join(",", new String[] {key, Long.toString(calls), Long.toString(hits), Boolean.toString(hasParent)});
        }
    }
}
