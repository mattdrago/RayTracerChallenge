package drago.rtc.pit;

import drago.rtc.Canvas;
import drago.rtc.Color;
import drago.rtc.PPMWriter;
import drago.rtc.foundations.Tuple;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class CannonSim {
    private Projectile projectile;
    private final Environment environment;
    private final Canvas canvas;

    private static final Color PLOT_COLOR = new Color(0.9, 0.2, 0.1);

    private CannonSim(Projectile projectile, Environment environment, Canvas canvas) {

        this.projectile = projectile;
        this.environment = environment;
        this.canvas = canvas;
    }

    private void tick() {
        this.projectile = new Projectile(projectile.position.add(projectile.velocity),
                projectile.velocity.add(environment.gravity).add(environment.wind));
    }

    private void run() {
        int tickCount = 0;
        while(projectile.position.getY() > 0) {
            tick();
            tickCount++;
            Tuple pos = projectile.position;
            plot(pos);
            System.out.println(String.format("Current Position @ %d: %f, %f, %f", tickCount, pos.getX(), pos.getY(), pos.getZ()));
        }
    }

    private void plot(Tuple pos) {
        int x = (int) (pos.getX() + 0.5);
        int y = (int) (pos.getY() + 0.5);
        y = canvas.getHeight() - y;
        canvas.writePixel(x, y, PLOT_COLOR);
    }

    public static void main(String[] argv) {
        Tuple start = Tuple.point(0, 1, 0);
        Tuple velocity = Tuple.vector(1, 1.8, 0).normalise().scale(11.25);
        Projectile projectile = new Projectile(start, velocity);

        Tuple gravity = Tuple.vector(0, -0.1, 0);
        Tuple wind = Tuple.vector(-0.01, 0, 0);
        Environment environment = new Environment(gravity, wind);

        Canvas canvas = new Canvas(900, 550);

        CannonSim sim = new CannonSim(projectile, environment, canvas);
        sim.run();

        sim.savePlot();
    }

    private void savePlot() {

        Path outPath = Paths.get("plot.ppm");

        try (BufferedWriter writer = Files.newBufferedWriter(outPath)) {
            PPMWriter ppmWriter = new PPMWriter();
            writer.write(ppmWriter.write(canvas));

            System.out.println("Plot saved to: " + outPath);
        } catch (IOException e) {
            System.out.println("Unable to save plot: " + e.getMessage());
        }
    }
}
