package action.ambulanceaction;

import agents.Ambulance;
import agents.Hospital;
import agents.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class AmbulanceTransferToHospital extends AmbulanceAction {
    private static Logger LOGGER = LoggerFactory.getLogger(AmbulanceTransferToHospital.class);
    public Hospital hospital;
    public Patient patient;
    public AmbulanceTransferToHospital(Ambulance target, Hospital hospital, Patient targetPatient) {
        super(target);

        this.hospital = hospital;
        this.patient = targetPatient;
        name = "Transfer To Hospital";
        ambulance.transferImage.visible(true);
        ambulance.defaultImage.visible(false);
    }

    @Override
    // Transfer the patient to the hospital
    public void onUpdate() {
        ambulance.moveTo(hospital.position);
        if(ambulance.isArrivedAt(hospital.position)) {
            hospital.hospitalize(patient);
            LOGGER.info(ambulance.name + " carrying " + patient.name + " arrived in " + hospital.name);
            world.transferCounter++;
            ambulance.transferImage.visible(false);
            ambulance.defaultImage.visible(true);
            ambulance.changeAction(new AmbulanceFree(ambulance));       // After transfer, change the action to "Free"
        }
    }

}
