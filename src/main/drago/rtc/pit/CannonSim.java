package drago.rtc.pit;

import drago.rtc.Tuple;

public class CannonSim {
    private Projectile projectile;
    private Environment environment;

    private CannonSim(Projectile projectile, Environment environment) {

        this.projectile = projectile;
        this.environment = environment;
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
            System.out.println(String.format("Current Position @ %d: %f, %f, %f", tickCount, pos.getX(), pos.getY(), pos.getZ()));
        }
    }

    public static void main(String[] argv) {
        Projectile projectile = new Projectile(Tuple.point(0, 1, 0), Tuple.vector(1, 1, 0).normalise());
        Environment environment = new Environment(Tuple.vector(0, -0.1, 0), Tuple.vector(-0.01, 0, 0));

        CannonSim sim = new CannonSim(projectile, environment);
        sim.run();
    }
}
