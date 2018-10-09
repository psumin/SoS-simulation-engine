package action.ambulanceaction;

import agents.Ambulance;
import agents.Hospital;
import agents.Patient;
import agents.SafeZone;

import java.util.ArrayList;

public class AmbulanceMoveToSafeZone extends AmbulanceAction {

    SafeZone safeZone;

    public AmbulanceMoveToSafeZone(Ambulance target, SafeZone safeZone) {
        super(target);

        this.safeZone = safeZone;
    }

    @Override
    public void onUpdate() {
        ambulance.moveTo(safeZone.position);
        if(ambulance.isArrivedAt(safeZone.position)) {

            Hospital nearestHospital = (Hospital)ambulance.nearestObject(new ArrayList<>(world.hospitals));
            Patient patient = safeZone.getPatient(Patient.Status.Serious);
            if(patient == null) {
                patient = safeZone.getPatient(Patient.Status.Wounded);
            }
            if(patient == null) {
                ambulance.changeAction(new AmbulanceFree(ambulance));
                return;
            }

            safeZone.leavePatient(patient);

            ambulance.changeAction(new AmbulanceTransferToHospital(ambulance, nearestHospital, patient));
        }
    }
}
