package stimulus.StateStimulus;

import action.firefighteraction.FireFighterHalt;
import agents.FireFighter;
import core.World;
import misc.Position;
import stimulus.Stimulus;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class State extends Stimulus {

    String fireFighterName;

    public State(World world, int frame, String fireFighterName) {
        super(world, frame);
        this.fireFighterName = fireFighterName;
    }

    @Override
    public void execute() {
        FireFighter target = (FireFighter)world.findObject(fireFighterName);
        if(target == null) return;

        Position position = target.position;
        world.addPatient(position);
        target.changeAction(new FireFighterHalt(target));
        world.removeChild(target);
        world.map.remove(target);
    }
}
