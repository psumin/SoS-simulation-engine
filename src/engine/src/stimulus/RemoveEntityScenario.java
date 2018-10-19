package stimulus;

import action.Func;
import core.World;

import java.util.function.Consumer;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class RemoveEntityScenario extends NumberOfEntityScenario {
    public RemoveEntityScenario(World world, int frame, String param1, Consumer<String> function) {
        super(world,frame, param1, function);
    }
}

