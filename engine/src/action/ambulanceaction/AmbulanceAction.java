package action.ambulanceaction;

import action.Action;
import agents.Ambulance;
import agents.CS;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class AmbulanceAction extends Action {

    protected Ambulance ambulance;

    public AmbulanceAction(Ambulance target) {
        super(target);
        ambulance = target;
    }
}
