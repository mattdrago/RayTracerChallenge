package drago.rtc.pit;

import drago.rtc.Tuple;

class Projectile {
    Tuple position;
    Tuple velocity;

    Projectile(Tuple position, Tuple velocity) {
        this.position = position;
        this.velocity = velocity;
    }
}
