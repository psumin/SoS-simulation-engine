package action.ambulanceaction;

import agents.Ambulance;
import agents.Bridgehead;
import core.Msg;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class AmbulanceFree extends AmbulanceAction {

    public AmbulanceFree(Ambulance target) {
        super(target);
        name = "Free";
    }

    boolean isFirstUpdate = true;

    int timeout = 30;               // timeout duration, 1frame을 2초로 생각했을 때 1분간!
    int frameCounter = timeout;

    int maxTimeout = 3;             // timeoutCounter, number of timeout
    int timeoutCounter = maxTimeout;


    @Override
    public void onUpdate() {
        if(isFirstUpdate) {
            isFirstUpdate = false;
            router.route(new Msg()
                    .setFrom(ambulance.name)
                    .setTo("Organization")
                    .setTitle("free state start")                           // announce to the organization "Free"
                    .setData(ambulance));
        }
        if(frameCounter <= 0) {
            frameCounter = timeout;
            if(timeoutCounter <= 0) {
                timeoutCounter = maxTimeout;
                ambulance.changeAction(new AmbulanceSearch(ambulance));     // Search at Bridgehead
            }
            timeoutCounter--;
        }
        frameCounter--;
    }

    @Override
    // When Ambulance receive the message
    public void recvMsg(Msg msg) {
        if(msg.title == "move to bridgehead") {
            Bridgehead bridgehead = (Bridgehead)msg.data;
            ambulance.changeAction(new AmbulanceMoveTobridgehead(ambulance, bridgehead));
        }
    }
}
