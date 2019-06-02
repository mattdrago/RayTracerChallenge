package drago.rtc.pit;

import drago.rtc.Tuple;

class Environment {
    Tuple gravity;
    Tuple wind;

    Environment(Tuple gravity, Tuple wind) {

        this.gravity = gravity;
        this.wind = wind;
    }
}
