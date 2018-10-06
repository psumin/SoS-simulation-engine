package action.firefighteraction;

import agents.FireFighter;
import agents.Hospital;
import agents.Patient;
import agents.SafeZone;

public class FireFighterTransferToSafeZone extends FireFighterAction {

    SafeZone safeZone;
    Patient targetPatient;

    public FireFighterTransferToSafeZone(FireFighter target, SafeZone safeZone, Patient targetPatient) {
        super(target);

        name = "TransferToSafeZone";

        this.safeZone = safeZone;
        this.targetPatient = targetPatient;

        fireFighter.transferImage.visible(true);
        fireFighter.defaultImage.visible(false);
    }

    @Override
    public void onUpdate() {
        fireFighter.observe();
        fireFighter.moveTo(safeZone.position);
        fireFighter.markVisitedTiles();
        if(fireFighter.isArrivedAt(safeZone.position)) {
            safeZone.arrivedPatient(targetPatient);


            fireFighter.transferImage.visible(false);
            fireFighter.defaultImage.visible(true);

            fireFighter.changeAction(new FireFighterSearch(fireFighter));
        }
    }
}
