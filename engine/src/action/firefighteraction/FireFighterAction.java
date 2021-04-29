package action.firefighteraction;

import action.Action;
import agents.CS;
import agents.FireFighter;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class FireFighterAction extends Action {

    public FireFighter fireFighter;

    public FireFighterAction(FireFighter target) {
        super(target);

        fireFighter = target;
    }
}
