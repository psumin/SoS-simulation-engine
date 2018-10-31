package action.firefighteraction;

import agents.FireFighter;
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

public class FireFighterSelectTransferDestination extends FireFighterAction {

    private Patient targetPatient;
    private int timeout = 10;           // Timeout duration
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
            SafeZone nearestSafeZone = (SafeZone)fireFighter.nearestObject(new ArrayList<>(world.safeZones));               // Select the nearest Safe Zone
            fireFighter.changeAction(new FireFighterTransferToSafeZone(fireFighter, nearestSafeZone, targetPatient));       // transfer the patient to the nearest Safe Zone
        }
        frameCounter--;
    }

    @Override
    // If the Firefighter get a nearest hospital's location from the Organization
    public void recvMsg(Msg msg) {
        if(msg.from == "Organization" && msg.title == "nearest hospital") {
            Hospital nearestHospital = (Hospital)msg.data;
            SafeZone nearestSafeZone = (SafeZone)fireFighter.nearestObject(new ArrayList<>(world.safeZones));

            if(fireFighter.distantTo(nearestHospital) < fireFighter.distantTo(nearestSafeZone)) {                           // Calculate the distance between nearest Safe Zone and nearest Safe Zone
                fireFighter.changeAction(new FireFighterTransferToSafeZone(fireFighter, nearestSafeZone, targetPatient));   // transfer the patient to the hospital
            } else {
                fireFighter.changeAction(new FireFighterTransferToSafeZone(fireFighter, nearestSafeZone, targetPatient));   // transfer the patient to the Safe Zone
                //fireFighter.changeAction(new FireFighterTransferToHospital(fireFighter, nearestHospital, targetPatient));
            }
        }
    }
}
