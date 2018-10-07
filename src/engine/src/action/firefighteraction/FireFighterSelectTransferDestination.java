package action.firefighteraction;

import agents.FireFighter;
import agents.Hospital;
import agents.Patient;
import agents.SafeZone;
import core.Msg;
import core.SoSObject;

import java.util.ArrayList;

public class FireFighterSelectTransferDestination extends FireFighterAction {

    private Patient targetPatient;
    private int timeout = 10;
    private int frameCounter = timeout;

    public FireFighterSelectTransferDestination(FireFighter target, Patient targetPatient) {
        super(target);

        name = "SelectTransferDestination";
        this.targetPatient = targetPatient;
    }

    @Override
    public void onUpdate() {

        if(frameCounter == timeout) {
            router.route(new Msg()
                    .setFrom(fireFighter.name)
                    .setTo("Organization")
                    .setTitle("nearest hospital")
                    .setData(fireFighter));
        } else if(frameCounter <= 0) {
            SafeZone nearestSafeZone = (SafeZone)fireFighter.nearestObject(new ArrayList<>(world.safeZones));
            fireFighter.changeAction(new FireFighterTransferToSafeZone(fireFighter, nearestSafeZone, targetPatient));
        }
        frameCounter--;
    }

    @Override
    public void recvMsg(Msg msg) {
        if(msg.from == "Organization" && msg.title == "nearest hospital") {
            Hospital nearestHospital = (Hospital)msg.data;
            SafeZone nearestSafeZone = (SafeZone)fireFighter.nearestObject(new ArrayList<>(world.safeZones));

            if(fireFighter.distantTo(nearestHospital) < fireFighter.distantTo(nearestSafeZone)) {
                fireFighter.changeAction(new FireFighterTransferToHospital(fireFighter, nearestHospital, targetPatient));
            } else {
                fireFighter.changeAction(new FireFighterTransferToSafeZone(fireFighter, nearestSafeZone, targetPatient));
                //fireFighter.changeAction(new FireFighterTransferToHospital(fireFighter, nearestHospital, targetPatient));
            }
        }
    }
}
