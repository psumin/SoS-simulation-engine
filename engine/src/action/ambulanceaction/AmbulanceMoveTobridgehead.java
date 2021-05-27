package action.ambulanceaction;

import action.firefighteraction.FireFighterTransferToHospital;
import agents.Ambulance;
import agents.Bridgehead;
import agents.Hospital;
import agents.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class AmbulanceMoveTobridgehead extends AmbulanceAction {

    Bridgehead bridgehead;
    private static Logger LOGGER = LoggerFactory.getLogger(AmbulanceMoveTobridgehead.class);


    public AmbulanceMoveTobridgehead(Ambulance target, Bridgehead bridgehead) {
        super(target);

        this.bridgehead = bridgehead;
        name = "Move To Bridgehead";
    }

    @Override
    // Move to the Bridgehead
    public void onUpdate() {
        ambulance.moveTo(bridgehead.position);
        if(ambulance.isArrivedAt(bridgehead.position)) {

            Hospital nearestHospital = (Hospital)ambulance.nearestObject(new ArrayList<>(world.hospitals));
            Patient patient = bridgehead.getPatient(Patient.Status.Serious);          // Serious patient first
            if(patient == null) {
                patient = bridgehead.getPatient(Patient.Status.Wounded);              // Wounded patient next
            }
            if(patient == null) {
                ambulance.changeAction(new AmbulanceFree(ambulance));               // When there is no patient, "Free"
                return;
            }

            bridgehead.leavePatient(patient);                                           // Select the patient at the Bridgehead
            LOGGER.info(ambulance.name + " moving " + patient.name + " with status:" + patient.getStatus().name() + " to " + nearestHospital.name);

            ambulance.changeAction(new AmbulanceTransferToHospital(ambulance, nearestHospital, patient));       // Transfer the patient to the hospital
        }
    }
}
