package core;

import misc.Position;

import java.util.Random;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class GlobalRandom {
    private static Random random = new Random(1);

    public static void setSeed(int seed) {
        random.setSeed(seed);
    }

    // 그때 그때 사용하는 랜덤 기능 추가
    public static Position nextPosition(int boundX, int boundY) {
        return new Position(random.nextInt(boundX), random.nextInt(boundY));
    }

    public static int nextInt(int bound) {
        return random.nextInt(bound);
    }
}
