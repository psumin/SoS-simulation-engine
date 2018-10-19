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

public class AddEntityScenario extends Scenario {

    private Func onExecute;

    // add
    public AddEntityScenario(World world, int frame, Func function) {
        super(world, frame);
        onExecute = function;
    }

    @Override
    public void execute() {
        if(onExecute != null) {
            onExecute.invoke();
            return;
        }
    }
}
