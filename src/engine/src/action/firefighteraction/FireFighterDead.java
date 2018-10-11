package action.firefighteraction;

import agents.FireFighter;

public class FireFighterDead extends FireFighterAction {
    public FireFighterDead(FireFighter target) {
        super(target);

        name = "Dead";
    }
}
