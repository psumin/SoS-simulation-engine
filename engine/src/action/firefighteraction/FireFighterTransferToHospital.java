package action.firefighteraction;

import agents.FireFighter;
import agents.Hospital;
import agents.Patient;
import misc.ElasticHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class FireFighterTransferToHospital extends FireFighterAction {

    Hospital hospital;
    public Patient targetPatient;

    int prevMoveDelay;
    private static Logger LOGGER = LoggerFactory.getLogger(FireFighterTransferToHospital.class);
    private ElasticHelper elasticHelperInstance = ElasticHelper.getElasticHelperInstance();

    public FireFighterTransferToHospital(FireFighter target, Hospital hospital, Patient targetPatient) {
        super(target);

        name = "TransferToHospital";
        this.hospital = hospital;
        this.targetPatient = targetPatient;

        fireFighter.transferImage.visible(true);
        fireFighter.firstAid.visible(false);
        fireFighter.defaultImage.visible(false);


        prevMoveDelay = fireFighter.moveDelay;
        fireFighter.moveDelay = prevMoveDelay * 3;          // Reduce the speed while transferring the patient
    }

    @Override
    public void onUpdate() {
        fireFighter.observe();
        fireFighter.moveTo(hospital.position);              // Transfer the patient to the hospital
        fireFighter.markVisitedTiles();
        if(fireFighter.isArrivedAt(hospital.position)) {    // When the Firefighter arrived at the hospital
            hospital.hospitalize(targetPatient);            // Patient is hospitalized at the hospital
            LinkedHashMap<String, String> logArgs = new LinkedHashMap<>();
            logArgs.put("action", "firefighter.transfertohospital");
            logArgs.put("firefighter", fireFighter.name);
            logArgs.put("patient", targetPatient.name);
            logArgs.put("hospital", hospital.name);
            elasticHelperInstance.indexLogs(this.getClass(), logArgs);
            LOGGER.info(fireFighter.name + " successfully transferred " + targetPatient.name + " to " + hospital.name);
            //world.addChild(targetPatient);

            fireFighter.moveDelay = prevMoveDelay;
            fireFighter.changeAction(new FireFighterSearch(fireFighter));       // Change the Firefighter's action to "Search"
            fireFighter.transferImage.visible(false);
            fireFighter.defaultImage.visible(true);
//            world.rescuedPatientCount++;
            world.transferCounter++;
        }
    }
}
