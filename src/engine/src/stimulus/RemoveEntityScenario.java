package stimulus;

import core.World;

import java.util.function.Consumer;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class RemoveEntityScenario extends Scenario {


    private Consumer<String> onExecute;
    private String param;

    // remove
    public RemoveEntityScenario(World world, int frame, String param1, Consumer<String> function) {
        super(world, frame);
        onExecute = function;
        this.param = param1;
    }

    @Override
    public void execute() {
        if(onExecute != null) {
            onExecute.accept(param);
        }
    }
}

