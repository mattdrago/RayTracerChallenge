package drago.rtc.pit;

import drago.rtc.foundations.Tuple;

class Environment {
    final Tuple gravity;
    final Tuple wind;

    Environment(Tuple gravity, Tuple wind) {

        this.gravity = gravity;
        this.wind = wind;
    }
}
