package action.ambulanceaction;

import agents.Ambulance;
import agents.Bridgehead;
import agents.Hospital;
import agents.Patient;
import core.Msg;

import java.util.ArrayList;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class AmbulanceSearch extends AmbulanceAction {

    ArrayList<Bridgehead> bridgeheads;
    Bridgehead targetBridgehead;

    public AmbulanceSearch(Ambulance target) {
        super(target);
        bridgeheads = new ArrayList<>(target.world.bridgeheads);
        name = "Search";
    }

    @Override
    // When timeout occurred, ambulance change the action to "Search"
    public void onUpdate() {

        if(targetBridgehead == null) {
            targetBridgehead = (Bridgehead)ambulance.nearestObject(new ArrayList<>(bridgeheads));
            bridgeheads.remove(targetBridgehead);
        }

        ambulance.moveTo(targetBridgehead.position);
        if(ambulance.isArrivedAt(targetBridgehead.position)) {

            Hospital nearestHospital = (Hospital)ambulance.nearestObject(new ArrayList<>(world.hospitals));
            Patient patient = targetBridgehead.getPatient(Patient.Status.Serious);          // Serious patient first
            if(patient == null) {
                patient = targetBridgehead.getPatient(Patient.Status.Wounded);              // Wounded patient next
            }
            if(patient == null) {

                if(bridgeheads.isEmpty()) {
                    bridgeheads = new ArrayList<>(world.bridgeheads);
                }

                // 다른 Bridgehead 찾기
                targetBridgehead = (Bridgehead)ambulance.nearestObject(new ArrayList<>(bridgeheads));
                bridgeheads.remove(targetBridgehead);

                return;
            }
            targetBridgehead.leavePatient(patient);                                           // Select the patient at the Bridgehead
            ambulance.changeAction(new AmbulanceTransferToHospital(ambulance, nearestHospital, patient));       // Transfer the patient to the hospital
        }
    }

    @Override
    public void recvMsg(Msg msg) {
        if(msg.title == "move to bridgehead") {
            Bridgehead bridgehead = (Bridgehead)msg.data;
            ambulance.changeAction(new AmbulanceMoveToBridgehead(ambulance, bridgehead));
        }
    }
}
