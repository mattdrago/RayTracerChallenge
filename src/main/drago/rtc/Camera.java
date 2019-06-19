package drago.rtc;

import drago.rtc.foundations.Matrix;
import drago.rtc.foundations.Ray;
import drago.rtc.foundations.Tuple;

public class Camera {
    private static final int REFLECTION_MAX_DEPTH = 5;
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

        for (int y = 0; y < vSize; y++) {
            for (int x = 0; x < hSize; x++) {
                Ray ray = rayForPixel(x, y);
                Color color = world.colorAt(ray, REFLECTION_MAX_DEPTH);
                image.writePixel(x, y, color);
            }
        }

        return image;
    }
}
