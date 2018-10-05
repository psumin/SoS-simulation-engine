package action.firefighteraction;

import agents.FireFighter;
import agents.Hospital;
import agents.Patient;

public class FireFighterTransferToHospital extends FireFighterAction {

    Hospital hospital;
    Patient targetPatient;

    public FireFighterTransferToHospital(FireFighter target, Hospital hospital, Patient targetPatient) {
        super(target);

        name = "TransferToHospital";
        this.hospital = hospital;
        this.targetPatient = targetPatient;
    }

    @Override
    public void onUpdate() {
        fireFighter.observe();
        fireFighter.moveTo(hospital.position);
        fireFighter.markVisitedTiles();
        if(fireFighter.isArrivedAt(hospital.position)) {
            hospital.hospitalize(targetPatient);
            //world.addChild(targetPatient);
            fireFighter.changeAction(new FireFighterSearch(fireFighter));
        }
    }
}
