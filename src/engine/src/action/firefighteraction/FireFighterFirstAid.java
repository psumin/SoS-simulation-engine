package action.firefighteraction;

import action.Action;
import agents.CS;
import agents.FireFighter;
import agents.Patient;

public class FireFighterFirstAid extends FireFighterAction {

    private int firstAidTime = 5;
    private Patient targetPatient;
    private int frameCounter = firstAidTime;

    public FireFighterFirstAid(FireFighter target, Patient targetPatient) {
        super(target);

        name = "FirstAid";
        this.targetPatient = targetPatient;
    }

    @Override
    public void onUpdate() {
        if(frameCounter <= 0) {
            world.removeChild(targetPatient);
            fireFighter.changeAction(new FireFighterSelectTransferDestination(fireFighter, targetPatient));
        }
        frameCounter--;
    }
}
