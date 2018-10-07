package stimulus;

import agents.FireFighter;
import core.World;
import misc.Position;

public class FireFighterToPatientScenario extends Scenario {

    String fireFighterName;

    public FireFighterToPatientScenario(World world, int frame, String fireFighterName) {
        super(world, frame);
        this.fireFighterName = fireFighterName;
    }

    @Override
    public void execute() {
        FireFighter target = (FireFighter)world.findObject(fireFighterName);
        if(target == null) return;

        Position position = target.position;
        world.addPatient(position);
        world.removeChild(target);
        world.map.remove(target);
    }
}
