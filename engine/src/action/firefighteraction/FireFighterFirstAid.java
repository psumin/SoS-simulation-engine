package action.firefighteraction;

import agents.FireFighter;
import agents.Patient;
import core.ImageObject;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class FireFighterFirstAid extends FireFighterAction {

    private int firstAidTime = 10;
    public Patient targetPatient;
    private int frameCounter = firstAidTime;

    public FireFighterFirstAid(FireFighter target, Patient targetPatient) {
        super(target);

        name = "FirstAid";
        fireFighter.patientsMemory.remove(targetPatient);
        world.map.remove(targetPatient);
        fireFighter.moveToPatient.visible(false);
        fireFighter.firstAid.visible(true);
        this.targetPatient = targetPatient;
        if(targetPatient.assignedFireFighter == null) {
            targetPatient.assignedFireFighter = fireFighter;
        }
        targetPatient.isSaved = true;
    }

    @Override
    public void onUpdate() {

        if(targetPatient.assignedFireFighter != fireFighter) {          // If there is another Firefighter at the target patient, change the action to "Search"
            fireFighter.changeAction(new FireFighterSearch(fireFighter));
            fireFighter.defaultImage.visible(true);
            fireFighter.firstAid.visible(false);
            return;
        }

        if(!world.contains(targetPatient)) {                            // If there is no patient at the target point, change the action to "Search"
            fireFighter.changeAction(new FireFighterSearch(fireFighter));
            fireFighter.defaultImage.visible(true);
            fireFighter.firstAid.visible(false);
            return;
        }

        if(frameCounter <= 0) {                                         // After First Aid, transfer the patient
            world.removeChild(targetPatient);
            fireFighter.changeAction(new FireFighterSelectTransferDestination(fireFighter, targetPatient));
            fireFighter.transferImage.visible(true);
            fireFighter.firstAid.visible(false);
        }
        frameCounter--;
    }
}
