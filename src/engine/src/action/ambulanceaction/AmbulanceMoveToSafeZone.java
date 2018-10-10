package action.ambulanceaction;

import agents.Ambulance;
import agents.Hospital;
import agents.Patient;
import agents.SafeZone;

import java.util.ArrayList;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class AmbulanceMoveToSafeZone extends AmbulanceAction {

    SafeZone safeZone;

    public AmbulanceMoveToSafeZone(Ambulance target, SafeZone safeZone) {
        super(target);

        this.safeZone = safeZone;
        name = "Move To SafeZone";
    }

    @Override
    // Move to the Safe Zone
    public void onUpdate() {
        ambulance.moveTo(safeZone.position);
        if(ambulance.isArrivedAt(safeZone.position)) {

            Hospital nearestHospital = (Hospital)ambulance.nearestObject(new ArrayList<>(world.hospitals));
            Patient patient = safeZone.getPatient(Patient.Status.Serious);          // Serious patient first
            if(patient == null) {
                patient = safeZone.getPatient(Patient.Status.Wounded);              // Wounded patient next
            }
            if(patient == null) {
                ambulance.changeAction(new AmbulanceFree(ambulance));               // When there is no patient, "Free"
                return;
            }

            safeZone.leavePatient(patient);                                           // Select the patient at the Safe Zone

            ambulance.changeAction(new AmbulanceTransferToHospital(ambulance, nearestHospital, patient));       // Transfer the patient to the hospital
        }
    }
}
