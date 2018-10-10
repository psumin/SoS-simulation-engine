package action.ambulanceaction;

import agents.Ambulance;
import agents.Hospital;
import agents.Patient;
import agents.SafeZone;
import core.Msg;
import core.SoSObject;

import java.util.ArrayList;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class AmbulanceSearch extends AmbulanceAction {

    ArrayList<SafeZone> safeZones;
    SafeZone targetSafeZone;

    public AmbulanceSearch(Ambulance target) {
        super(target);
        safeZones = new ArrayList<>(target.world.safeZones);
        name = "Search";
    }

    @Override
    // When timeout occurred, ambulance change the action to "Search"
    public void onUpdate() {

        if(targetSafeZone == null) {
            targetSafeZone = (SafeZone)ambulance.nearestObject(new ArrayList<>(safeZones));
            safeZones.remove(targetSafeZone);
        }

        ambulance.moveTo(targetSafeZone.position);
        if(ambulance.isArrivedAt(targetSafeZone.position)) {

            Hospital nearestHospital = (Hospital)ambulance.nearestObject(new ArrayList<>(world.hospitals));
            Patient patient = targetSafeZone.getPatient(Patient.Status.Serious);          // Serious patient first
            if(patient == null) {
                patient = targetSafeZone.getPatient(Patient.Status.Wounded);              // Wounded patient next
            }
            if(patient == null) {

                if(safeZones.isEmpty()) {
                    safeZones = new ArrayList<>(world.safeZones);
                }

                // 다른 SafeZone 찾기
                targetSafeZone = (SafeZone)ambulance.nearestObject(new ArrayList<>(safeZones));
                safeZones.remove(targetSafeZone);

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
