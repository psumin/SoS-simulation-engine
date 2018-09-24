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
            targetPatient.remove();
            complete("rescue complete");
        }
    }
}
