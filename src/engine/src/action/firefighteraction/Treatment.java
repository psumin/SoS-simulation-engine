package action.firefighteraction;

import agents.FireFighter;
import agents.Patient;

// 환자 구조
// 치료 시간 딜레이
public class Treatment extends  FireFighterAction{

    int treatmentTime = 3;
    Patient targetPatient;

    public Treatment(FireFighter target, Patient targetPatient) {
        super(target);
        this.targetPatient = targetPatient;
    }

    @Override
    public void onUpdate() {
        treatmentTime--;
        if(treatmentTime == 0) {
            //targetPatient.removeFromTile();
            targetPatient.remove();
            fireFighter.patientsMemory.remove(targetPatient);
            fireFighter.changeAction(new Search(fireFighter));
        }
    }
}
