package action.ambulanceaction;

import action.Action;
import agents.Ambulance;
import agents.CS;

public class AmbulanceAction extends Action {

    protected Ambulance ambulance;

    public AmbulanceAction(Ambulance target) {
        super(target);
        ambulance = target;
    }
}
