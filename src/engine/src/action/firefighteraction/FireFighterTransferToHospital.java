package action.firefighteraction;

import agents.FireFighter;
import agents.Hospital;
import agents.Patient;

public class FireFighterTransferToHospital extends FireFighterAction {

    Hospital hospital;
    Patient targetPatient;

    int prevMoveDelay;

    public FireFighterTransferToHospital(FireFighter target, Hospital hospital, Patient targetPatient) {
        super(target);

        name = "TransferToHospital";
        this.hospital = hospital;
        this.targetPatient = targetPatient;

        prevMoveDelay = fireFighter.moveDelay;
        fireFighter.moveDelay = prevMoveDelay * 2;
    }

    @Override
    public void onUpdate() {
        fireFighter.observe();
        fireFighter.moveTo(hospital.position);
        fireFighter.markVisitedTiles();
        if(fireFighter.isArrivedAt(hospital.position)) {
            hospital.hospitalize(targetPatient);
            //world.addChild(targetPatient);
            fireFighter.moveDelay = prevMoveDelay;
            fireFighter.changeAction(new FireFighterSearch(fireFighter));
        }
    }
}
