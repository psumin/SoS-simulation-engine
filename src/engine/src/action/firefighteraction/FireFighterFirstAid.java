package action.firefighteraction;

import action.Action;
import agents.CS;
import agents.FireFighter;
import agents.Patient;

public class FireFighterFirstAid extends FireFighterAction {

    private int firstAidTime = 10;
    private Patient targetPatient;
    private int frameCounter = firstAidTime;

    public FireFighterFirstAid(FireFighter target, Patient targetPatient) {
        super(target);

        name = "FirstAid";
        world.map.remove(targetPatient);
        this.targetPatient = targetPatient;
        if(targetPatient.assignedFireFighter == null) {
            targetPatient.assignedFireFighter = fireFighter;
        }
    }

    @Override
    public void onUpdate() {

        if(targetPatient.assignedFireFighter != fireFighter) {
            fireFighter.changeAction(new FireFighterSearch(fireFighter));
            return;
        }

        if(!world.contains(targetPatient)) {
            fireFighter.changeAction(new FireFighterSearch(fireFighter));
            return;
        }

        if(frameCounter <= 0) {
            world.removeChild(targetPatient);
            fireFighter.changeAction(new FireFighterSelectTransferDestination(fireFighter, targetPatient));
        }
        frameCounter--;
    }
}
