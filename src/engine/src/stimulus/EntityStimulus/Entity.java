package stimulus.EntityStimulus;

import action.Func;
import core.World;
import stimulus.Stimulus;

import java.util.function.Consumer;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class Entity extends Stimulus {

    private Func onExecute;
    private Consumer<String> onExecute1;
    private String param1;

    // add
    public Entity(World world, int frame, Func function) {
        super(world, frame);
        onExecute = function;
    }

    // remove
    public Entity(World world, int frame, String param1, Consumer<String> function) {
        super(world, frame);
        onExecute1 = function;
        this.param1 = param1;
    }

    @Override
    public void execute() {
        if (onExecute != null) {
            onExecute.invoke();
            return;
        }
        if (onExecute1 != null) {
            onExecute1.accept(param1);
        }
    }
}
