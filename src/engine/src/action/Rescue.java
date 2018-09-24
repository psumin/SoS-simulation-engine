package action;

import agents.Patient;
import core.SoSObject;

public class Rescue extends Action {

    Patient targetPatient;
    public Rescue(SoSObject target, Patient patient) {
        super(target);
        targetPatient = patient;

        new MoveTo(target, patient.position).parentAction = this;
    }


    @Override
    public void sendMessage(String msg, Object data) {
        if(msg == "action complete") {
            Action action = new FrameDelay(this, 1);
        } else if(msg == "delay complete") {
            targetPatient.remove();
            complete("rescue complete");
        }
    }
}
