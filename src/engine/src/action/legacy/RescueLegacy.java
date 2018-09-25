package action.legacy;

import agents.Patient;
import core.SoSObject;

public class RescueLegacy extends ActionLegacy {

    Patient targetPatient;
    public RescueLegacy(SoSObject target, Patient patient) {
        super(target);
        targetPatient = patient;

        new MoveToLegacy(target, patient.position).parentActionLegacy = this;
    }


    @Override
    public void sendMessage(String msg, Object data) {
        if(msg == "action complete") {
            ActionLegacy actionLegacy = new FrameDelayLegacy(this, 3);
        } else if(msg == "delay complete") {
            targetPatient.remove();
            complete("rescue complete");
        }
    }
}
