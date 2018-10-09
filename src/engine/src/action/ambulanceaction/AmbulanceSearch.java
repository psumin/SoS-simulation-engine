package action.ambulanceaction;

import agents.Ambulance;
import agents.Hospital;
import agents.Patient;
import agents.SafeZone;
import core.Msg;

import java.util.ArrayList;

public class AmbulanceSearch extends AmbulanceAction {

    ArrayList<SafeZone> safeZones;
    int destinationIndex = 0;

    public AmbulanceSearch(Ambulance target) {
        super(target);

        safeZones = target.world.safeZones;

        name = "Search";
    }

    @Override
    public void onUpdate() {
        SafeZone targetSafeZone = safeZones.get(destinationIndex);

        ambulance.moveTo(targetSafeZone.position);
        if(ambulance.isArrivedAt(targetSafeZone.position)) {

            Hospital nearestHospital = (Hospital)ambulance.nearestObject(new ArrayList<>(world.hospitals));
            Patient patient = targetSafeZone.getPatient(Patient.Status.Serious);
            if(patient == null) {
                patient = targetSafeZone.getPatient(Patient.Status.Wounded);
            }
            if(patient == null) {
                destinationIndex = (destinationIndex + 1) % safeZones.size();
                return;
            }
            targetSafeZone.leavePatient(patient);
            ambulance.changeAction(new AmbulanceTransferToHospital(ambulance, nearestHospital, patient));
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
