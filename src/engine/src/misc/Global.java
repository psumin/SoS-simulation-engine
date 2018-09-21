package misc;

import java.util.Random;

public class Global {
    private static Random random = new Random(1);
    public static Random getRandom() {
        return random;
    }
}
