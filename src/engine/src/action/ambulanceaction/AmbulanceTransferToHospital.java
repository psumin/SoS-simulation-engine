package action.ambulanceaction;

import agents.Ambulance;
import agents.Hospital;
import agents.Patient;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class AmbulanceTransferToHospital extends AmbulanceAction {

    Hospital hospital;
    Patient patient;

    public AmbulanceTransferToHospital(Ambulance target, Hospital hospital, Patient targetPatient) {
        super(target);

        this.hospital = hospital;
        this.patient = targetPatient;
        name = "Transfer To Hospital";
    }

    @Override
    public void onUpdate() {
        ambulance.moveTo(hospital.position);
        if(ambulance.isArrivedAt(hospital.position)) {
            hospital.hospitalize(patient);
            ambulance.changeAction(new AmbulanceFree(ambulance));
        }
    }
}
