package action.ambulanceaction;

import agents.Ambulance;
import agents.Hospital;
import agents.Patient;
import agents.SafeZone;
import core.Msg;

import java.util.ArrayList;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class AmbulanceSearch extends AmbulanceAction {

    ArrayList<SafeZone> safeZones;
    int destinationIndex = 0;

    public AmbulanceSearch(Ambulance target) {
        super(target);
        safeZones = target.world.safeZones;
        name = "Search";
    }

    @Override
    // When timeout occurred, ambulance change the action to "Search"
    public void onUpdate() {
        SafeZone targetSafeZone = safeZones.get(destinationIndex);      // The nearest Safe Zone

        ambulance.moveTo(targetSafeZone.position);
        if(ambulance.isArrivedAt(targetSafeZone.position)) {

            Hospital nearestHospital = (Hospital)ambulance.nearestObject(new ArrayList<>(world.hospitals));
            Patient patient = targetSafeZone.getPatient(Patient.Status.Serious);          // Serious patient first
            if(patient == null) {
                patient = targetSafeZone.getPatient(Patient.Status.Wounded);              // Wounded patient next
            }
            if(patient == null) {
                destinationIndex = (destinationIndex + 1) % safeZones.size();               // Move to the another Safe Zone
                return;
            }
            targetSafeZone.leavePatient(patient);                                           // Select the patient at the Safe Zone
            ambulance.changeAction(new AmbulanceTransferToHospital(ambulance, nearestHospital, patient));       // Transfer the patient to the hospital
        }
    }

    @Override
    public void recvMsg(Msg msg) {
        if(msg.title == "move to safezone") {
            SafeZone safeZone = (SafeZone)msg.data;
            ambulance.changeAction(new AmbulanceMoveToSafeZone(ambulance, safeZone));
        }
    }
}
