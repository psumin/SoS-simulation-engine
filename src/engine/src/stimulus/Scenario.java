package stimulus;

import core.World;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public abstract class Scenario {

    protected World world;
    public int frame;

    public Scenario(World world, int frame) {
        this.world = world;
        this.frame = frame;
    }

    public abstract void execute();
}
