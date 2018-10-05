package action.firefighteraction;

import agents.FireFighter;
import agents.Hospital;
import agents.Patient;
import agents.SafeZone;
import core.SoSObject;

import java.util.ArrayList;

public class FireFighterSelectTransferDestination extends FireFighterAction {

    private Patient targetPatient;

    public FireFighterSelectTransferDestination(FireFighter target, Patient targetPatient) {
        super(target);

        name = "SelectTransferDestination";
        this.targetPatient = targetPatient;
    }

    @Override
    public void onUpdate() {
        ArrayList<SoSObject> hospitalAndSafeZones = new ArrayList<>(world.hospitals);
        hospitalAndSafeZones.addAll(world.safeZones);
        SoSObject nearest =  fireFighter.nearestObject(hospitalAndSafeZones);

        if(nearest instanceof Hospital) {
            fireFighter.changeAction(new FireFighterTransferToHospital(fireFighter, (Hospital)nearest, targetPatient));
        } else {
            fireFighter.changeAction(new FireFighterTransferToSafeZone(fireFighter, (SafeZone)nearest, targetPatient));
        }
    }
}
