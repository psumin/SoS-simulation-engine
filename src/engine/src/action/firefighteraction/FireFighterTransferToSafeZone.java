package action.firefighteraction;

import agents.FireFighter;
import agents.Patient;
import agents.SafeZone;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class FireFighterTransferToSafeZone extends FireFighterAction {

    SafeZone safeZone;
    Patient targetPatient;

    int prevMoveDelay;

    public FireFighterTransferToSafeZone(FireFighter target, SafeZone safeZone, Patient targetPatient) {
        super(target);

        name = "TransferToSafeZone";

        this.safeZone = safeZone;
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
        fireFighter.moveTo(safeZone.position);              // Transfer the patient to the Safe Zone
        fireFighter.markVisitedTiles();
        if(fireFighter.isArrivedAt(safeZone.position)) {    // When the Firefighter arrived at the Safe Zone
            safeZone.arrivedPatient(targetPatient);

            fireFighter.moveDelay = prevMoveDelay;
            fireFighter.changeAction(new FireFighterSearch(fireFighter));       // Change the Firefighter's action to "Search"
            fireFighter.transferImage.visible(false);
            fireFighter.defaultImage.visible(true);
            world.rescuedPatientCount++;
        }
    }
}
