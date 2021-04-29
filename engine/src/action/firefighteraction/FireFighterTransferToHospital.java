package action.firefighteraction;

import agents.FireFighter;
import agents.Hospital;
import agents.Patient;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class FireFighterTransferToHospital extends FireFighterAction {

    Hospital hospital;
    public Patient targetPatient;

    int prevMoveDelay;

    public FireFighterTransferToHospital(FireFighter target, Hospital hospital, Patient targetPatient) {
        super(target);

        name = "TransferToHospital";
        this.hospital = hospital;
        this.targetPatient = targetPatient;

        fireFighter.transferImage.visible(true);
        fireFighter.firstAid.visible(false);
        fireFighter.defaultImage.visible(false);


        prevMoveDelay = fireFighter.moveDelay;
        fireFighter.moveDelay = prevMoveDelay * 3;          // Reduce the speed while transferring the patient
    }

    @Override
    public void onUpdate() {
        fireFighter.observe();
        fireFighter.moveTo(hospital.position);              // Transfer the patient to the hospital
        fireFighter.markVisitedTiles();
        if(fireFighter.isArrivedAt(hospital.position)) {    // When the Firefighter arrived at the hospital
            hospital.hospitalize(targetPatient);            // Patient is hospitalized at the hospital
            //world.addChild(targetPatient);

            fireFighter.moveDelay = prevMoveDelay;
            fireFighter.changeAction(new FireFighterSearch(fireFighter));       // Change the Firefighter's action to "Search"
            fireFighter.transferImage.visible(false);
            fireFighter.defaultImage.visible(true);
//            world.rescuedPatientCount++;
            world.transferCounter++;
        }
    }
}
