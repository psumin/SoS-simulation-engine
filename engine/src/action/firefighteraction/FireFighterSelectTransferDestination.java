package action.firefighteraction;

import agents.FireFighter;
import agents.Hospital;
import agents.Patient;
import agents.Bridgehead;
import core.Msg;

import java.util.ArrayList;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class FireFighterSelectTransferDestination extends FireFighterAction {

    public Patient targetPatient;
    private int timeout = 30;           // Timeout duration
    private int frameCounter = timeout;

    public FireFighterSelectTransferDestination(FireFighter target, Patient targetPatient) {
        super(target);

        name = "SelectTransferDestination";
        this.targetPatient = targetPatient;
    }

    @Override
    public void onUpdate() {
        // Get an nearest hospital's location from the Organization
        if(frameCounter == timeout) {
            router.route(new Msg()
                    .setFrom(fireFighter.name)
                    .setTo("Organization")
                    .setTitle("nearest hospital")
                    .setData(fireFighter));
        } else if(frameCounter <= 0) {                                                                                      // If timeout
            Bridgehead nearestBridgehead = (Bridgehead)fireFighter.nearestObject(new ArrayList<>(world.bridgeheads));               // Select the nearest Bridgehead
            fireFighter.changeAction(new FireFighterTransferToBridgehead(fireFighter, nearestBridgehead, targetPatient));       // transfer the patient to the nearest Bridgehead
        }
        frameCounter--;
    }

    @Override
    // If the Firefighter get a nearest hospital's location from the Organization
    public void recvMsg(Msg msg) {
        if(msg.from == "Organization" && msg.title == "nearest hospital") {
            Hospital nearestHospital = (Hospital)msg.data;
            Bridgehead nearestBridgehead = (Bridgehead)fireFighter.nearestObject(new ArrayList<>(world.bridgeheads));

//            if(fireFighter.distantTo(nearestHospital) < fireFighter.distantTo(nearestBridgehead)) {                           // Calculate the distance between nearest Hospital and nearest Bridgehead
            if(fireFighter.distantTo(nearestHospital) <= 8) {
                fireFighter.changeAction(new FireFighterTransferToHospital(fireFighter, nearestHospital, targetPatient));   // transfer the patient to the hospital
            } else {
                fireFighter.changeAction(new FireFighterTransferToBridgehead(fireFighter, nearestBridgehead, targetPatient));   // transfer the patient to the Bridgehead
                //fireFighter.changeAction(new FireFighterTransferToHospital(fireFighter, nearestHospital, targetPatient));
            }
        }
    }
}
