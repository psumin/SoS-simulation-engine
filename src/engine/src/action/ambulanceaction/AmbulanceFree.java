package action.ambulanceaction;

import agents.Ambulance;
import agents.SafeZone;
import core.Msg;

public class AmbulanceFree extends AmbulanceAction {

    boolean isWaiting = false;
    public AmbulanceFree(Ambulance target) {
        super(target);
    }

    boolean isFirstUpdate = true;

    @Override
    public void onUpdate() {
        if(isFirstUpdate) {
            isFirstUpdate = false;
            router.route(new Msg()
                    .setFrom(ambulance.name)
                    .setTo("Organization")
                    .setTitle("free state start")
                    .setData(ambulance));
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
