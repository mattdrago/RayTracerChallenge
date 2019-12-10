package drago.rtc;

import drago.rtc.foundations.Matrix;
import drago.rtc.foundations.Ray;
import drago.rtc.foundations.Tuple;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Camera {
    private static final int REFLECTION_MAX_DEPTH = 5;
    private static final int CHUNK_SIZE = 4;
    private static final int NUM_RENDERER_THREADS = 1;

    private int hSize;
    private int vSize;
    private double fieldOfView;
    private Matrix transform;

    private double halfWidth;
    private double halfHeight;
    private double pixelSize;

    public Camera(int hSize, int vSize, double fieldOfView) {
        this.hSize = hSize;
        this.vSize = vSize;
        this.fieldOfView = fieldOfView;

        this.transform = Matrix.identity(4);

        calculatePixelSize();
    }

    int getHSize() {
        return hSize;
    }

    void setHSize(int hSize) {
        this.hSize = hSize;
        calculatePixelSize();
    }

    int getVSize() {
        return vSize;
    }

    void setVSize(int vSize) {
        this.vSize = vSize;
        calculatePixelSize();
    }

    double getFieldOfView() {
        return fieldOfView;
    }

    void setFieldOfView(double fieldOfView) {
        this.fieldOfView = fieldOfView;
        calculatePixelSize();
    }

    Matrix getTransform() {
        return transform;
    }

    public void setTransform(Matrix transform) {
        this.transform = transform;
    }

    double getPixelSize() {
        return this.pixelSize;
    }

    private void calculatePixelSize() {
        double halfView = Math.tan(fieldOfView / 2);
        double aspect = (double)hSize / (double)vSize;

        if (aspect >= 1) {
            this.halfWidth = halfView;
            this.halfHeight = halfView / aspect;
        } else {
            this.halfWidth = halfView * aspect;
            this.halfHeight = halfView;
        }

        this.pixelSize = (this.halfWidth * 2) / hSize;
    }

    Ray rayForPixel(int px, int py) {

        final double pixelCenter = 0.5;

        double xOffset = ((double)px + pixelCenter) * this.pixelSize;
        double yOffset = ((double)py + pixelCenter) * this.pixelSize;

        double worldX = this.halfWidth - xOffset;
        double worldY = this.halfHeight - yOffset;

        Matrix invTransform = transform.inverse();

        Tuple origin = invTransform.multiplyBy(Tuple.point(0, 0, 0));
        Tuple pixel = invTransform.multiplyBy(Tuple.point(worldX, worldY, -1));
        Tuple direction = pixel.subtract(origin).normalise();

        return new Ray(origin, direction);
    }

    public Canvas render(World world) {
        Canvas image = new Canvas(hSize, vSize);
        Queue<Chunk> chunks = prepareChunks();

        Set<Thread> rendererThreads = new HashSet<>();
        for (int i = 0; i < NUM_RENDERER_THREADS; i++) {
            Renderer r = new Renderer(chunks, world, image);
            Thread t = new Thread(r);
            rendererThreads.add(t);
            t.start();
        }

        for (Thread t : rendererThreads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                System.out.println("Interrupted whilst rendering: " + e.getLocalizedMessage());
                Thread.currentThread().interrupt();
            }
        }

        return image;
    }

//    public Canvas render(World world) {
//        Canvas image = new Canvas(hSize, vSize);
//
//        for (int y = 0; y < vSize; y++) {
//            for (int x = 0; x < hSize; x++) {
//                Ray ray = rayForPixel(x, y);
//                Color color = world.colorAt(ray, REFLECTION_MAX_DEPTH);
//                image.writePixel(x, y, color);
//            }
//        }
//
//        return image;
//    }

    public Queue<Chunk> prepareChunks() {
        Queue<Chunk> chunks = new ConcurrentLinkedQueue<>();

        for (int x = 0; x < hSize; x += CHUNK_SIZE) {
            for (int y = 0; y < vSize; y += CHUNK_SIZE) {
                int toX = Math.min(hSize, (x + CHUNK_SIZE));
                int toY = Math.min(vSize, (y + CHUNK_SIZE));
                chunks.offer(new Chunk (x, y, toX, toY));
            }
        }

        return chunks;
    }

    class Chunk {
        private final int fromX;
        private final int fromY;
        private final int toX;
        private final int toY;

        Chunk(int fromX, int fromY, int toX, int toY) {

            this.fromX = fromX;
            this.fromY = fromY;
            this.toX = toX;
            this.toY = toY;
        }

        public int getFromX() {
            return fromX;
        }

        public int getFromY() {
            return fromY;
        }

        public int getToX() {
            return toX;
        }

        public int getToY() {
            return toY;
        }
    }

    private class Renderer implements Runnable {

        private final Queue<Chunk> chunks;
        private final World world;
        private final Canvas image;

        public Renderer(Queue<Chunk> chunks, World world, Canvas image) {

            this.chunks = chunks;
            this.world = world;
            this.image = image;
        }

        @Override
        public void run() {

            Chunk chunk;
            while((chunk = chunks.poll()) != null) {
                for (int y = chunk.getFromY(); y < chunk.getToY(); y++) {
                    for (int x = chunk.getFromX(); x < chunk.getToX(); x++) {
                        Ray ray = rayForPixel(x, y);
                        Color color = world.colorAt(ray, REFLECTION_MAX_DEPTH);
                        image.writePixel(x, y, color);
                    }
                }
            }
        }
    }
}
