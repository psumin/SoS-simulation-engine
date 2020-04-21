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
    int mobilize = 8;
    int frameCounter = mobilize;
    int mobilize_delay_repeat = 0;             // counter, number of mobilize
    int counter = mobilize_delay_repeat;

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
            frameCounter = mobilize;
            if(counter <= 0) {
                counter = mobilize_delay_repeat;
                ambulance.changeAction(new AmbulanceSearch(ambulance));     // Search at Bridgehead
            }
            counter--;
        }
        frameCounter--;
    }

    @Override
    // When Ambulance receive the message
    public void recvMsg(Msg msg) {
        if(msg.title == "move to bridgehead") {
            Bridgehead bridgehead = (Bridgehead)msg.data;
            ambulance.changeAction(new AmbulanceMoveToBridgehead(ambulance, bridgehead));
        }
    }
}
