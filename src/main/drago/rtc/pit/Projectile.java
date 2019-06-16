package drago.rtc.pit;

import drago.rtc.foundations.Tuple;

class Projectile {
    final Tuple position;
    final Tuple velocity;

    Projectile(Tuple position, Tuple velocity) {
        this.position = position;
        this.velocity = velocity;
    }
}
