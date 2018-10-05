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
    }

    @Override
    public void onUpdate() {
        fireFighter.observe();
        fireFighter.moveTo(safeZone.position);
        fireFighter.markVisitedTiles();
        if(fireFighter.isArrivedAt(safeZone.position)) {
            safeZone.arrivedPatient(targetPatient);
            world.addChild(targetPatient);
            fireFighter.changeAction(new FireFighterSearch(fireFighter));
        }
    }
}
