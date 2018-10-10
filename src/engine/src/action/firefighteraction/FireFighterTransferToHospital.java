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
    Patient targetPatient;

    int prevMoveDelay;

    public FireFighterTransferToHospital(FireFighter target, Hospital hospital, Patient targetPatient) {
        super(target);

        name = "TransferToHospital";
        this.hospital = hospital;
        this.targetPatient = targetPatient;

        fireFighter.transferImage.visible(true);
        fireFighter.defaultImage.visible(false);

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

            fireFighter.transferImage.visible(false);
            fireFighter.defaultImage.visible(true);

            fireFighter.moveDelay = prevMoveDelay;
            fireFighter.changeAction(new FireFighterSearch(fireFighter));
        }
    }
}
