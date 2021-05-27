package action.firefighteraction;

import agents.FireFighter;
import agents.Patient;
import core.ImageObject;
import core.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static Logger LOGGER = LoggerFactory.getLogger(FireFighterFirstAid.class);

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
        LOGGER.info(target.name + "performed first aid on " + targetPatient.name);
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
