package stimulus;

import action.Func;
import core.World;

import java.util.function.Consumer;

public class NumberOfEntity extends Scenario {

    private Func onExecute;
    private Consumer<String> onExecute1;
    private String param1;

    // add
    public NumberOfEntity(World world, int frame, Func function) {
        super(world, frame);
        onExecute = function;
    }

    // remove
    public NumberOfEntity(World world, int frame, String param1, Consumer<String> function) {
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
