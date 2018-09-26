package action.firefighteraction;

import agents.FireFighter;
import agents.Patient;
import core.Tile;

// 환자 구조
// 치료 시간 딜레이
public class Treatment extends  FireFighterAction{

    int treatmentTime = 10;
    Patient targetPatient;

    public Treatment(FireFighter target, Patient targetPatient) {
        super(target);
        this.targetPatient = targetPatient;
    }

    @Override
    public void onUpdate() {

        Tile tile = fireFighter.world.getMap().getTile(targetPatient.position.x, targetPatient.position.y);
        if(tile.contain(targetPatient) == false) {
            treatmentPatient();
            return;
        }

        if(targetPatient.fireFighter != null && targetPatient.fireFighter != fireFighter) {
            fireFighter.patientsMemory.remove(targetPatient);
            fireFighter.changeAction(new Search(fireFighter));
        } else {
            targetPatient.fireFighter = fireFighter;
        }

        treatmentTime--;
        if(treatmentTime == 0) {
            assert targetPatient.fireFighter == null : "아니면 다시 생각해보자";
            targetPatient.fireFighter = fireFighter;
            treatmentPatient();
        }
    }

    void treatmentPatient() {
        targetPatient.remove();
        fireFighter.patientsMemory.remove(targetPatient);
        fireFighter.changeAction(new Search(fireFighter));
    }
}
