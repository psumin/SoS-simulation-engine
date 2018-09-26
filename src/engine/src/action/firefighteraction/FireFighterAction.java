package action.firefighteraction;

import action.Action;
import agents.CS;
import agents.FireFighter;

public class FireFighterAction extends Action {

    public FireFighter fireFighter;

    public FireFighterAction(FireFighter target) {
        super(target);

        fireFighter = target;
    }
}
