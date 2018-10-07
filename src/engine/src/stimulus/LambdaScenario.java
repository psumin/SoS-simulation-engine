package stimulus;

import action.Func;
import core.World;

public class LambdaScenario extends Scenario {

    private Func onExecute;

    public LambdaScenario(World world, int frame, Func function) {
        super(world, frame);
        onExecute = function;
    }

    @Override
    public void execute() {
        onExecute.invoke();
    }
}
