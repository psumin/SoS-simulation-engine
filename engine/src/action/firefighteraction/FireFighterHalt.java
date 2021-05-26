package action.firefighteraction;

import agents.FireFighter;

public class FireFighterHalt extends FireFighterAction {
    public FireFighterHalt(FireFighter target) {
        super(target);

        name = "Halt";
    }
}
