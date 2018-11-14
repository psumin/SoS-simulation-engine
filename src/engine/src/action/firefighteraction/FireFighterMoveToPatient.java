package action.firefighteraction;

import agents.FireFighter;
import agents.Patient;
import core.Tile;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class FireFighterMoveToPatient extends FireFighterAction {

    public Patient targetPatient;
    ArrayList<Patient> patientsMemory;

    public FireFighterMoveToPatient(FireFighter target, Patient targetPatient) {
        super(target);

        name = "MoveToPatient";

        this.targetPatient = targetPatient;
        world.map.remove(targetPatient);
        patientsMemory = fireFighter.patientsMemory;
        patientsMemory.remove(targetPatient);

        // Make different image for the "Move to Patient"
        fireFighter.defaultImage.visible(false);
        fireFighter.moveToPatient.visible(true);
    }

    // If the target has changed, move to the new target patient
    private void changeTargetPatient(Patient patient) {
        world.map.add(targetPatient);
        fireFighter.changeAction(new FireFighterMoveToPatient(fireFighter, patient));
    }

    @Override
    public void onUpdate() {

        if(targetPatient != null) {
            fireFighter.observe();
            //ArrayList<Patient> foundPatient = fireFighter.observe();
            //Patient newPatient = fireFighter.selectTargetPatient(foundPatient);

            Patient newPatient = fireFighter.selectTargetPatient(patientsMemory);       // Select the patient from the memory
            if(newPatient != null) {
                // New patient is Serious patient
                if (newPatient.isSerious()) {                                           // Change the target to the Serious patient
                    if(targetPatient.isWounded()) {
                        patientsMemory.add(targetPatient);
                        changeTargetPatient(newPatient);
                        return;
                    } else if (fireFighter.distantTo(targetPatient) > fireFighter.distantTo(newPatient)) {      // Change the target patient to the another Serious patient if it is more close
                        patientsMemory.add(targetPatient);
                        changeTargetPatient(newPatient);
                        return;
                    }
                } else {
                    // target patient and new patient are same Wounded patient
                    if (targetPatient.isWounded()) {                                    // Wounded patient and wounded patient
                        if (fireFighter.distantTo(targetPatient) > fireFighter.distantTo(newPatient)) {         // Change the target patient to the another Wounded patient if it is more close
                            patientsMemory.add(targetPatient);
                            changeTargetPatient(newPatient);
                            return;
                        }
                    }
                }
            }

            fireFighter.moveTo(targetPatient.position);
            fireFighter.markVisitedTiles();

            // TODO: 시야 내에 타겟 환자 존재 X
            // No target patient at the sight range of the firefighter
            if(targetPatient.position.x - fireFighter.sightRange / 2 <= fireFighter.position.x
                && fireFighter.position.x <= targetPatient.position.x + fireFighter.sightRange / 2) {
                if(targetPatient.position.y - fireFighter.sightRange / 2 <= fireFighter.position.y
                        && fireFighter.position.y <= targetPatient.position.y + fireFighter.sightRange / 2) {
                    if(!world.contains(targetPatient)) {                                    // No target patient at world
                        fireFighter.changeAction(new FireFighterSearch(fireFighter));       // change the action to "Search"
                        fireFighter.defaultImage.visible(true);
                        fireFighter.moveToPatient.visible(false);
                        return;
                    }
                    if(targetPatient.assignedFireFighter != null) {                         // There is a patient and other assigned firefighter
                        fireFighter.changeAction(new FireFighterSearch(fireFighter));       // change the action to "Search"
                        fireFighter.defaultImage.visible(true);
                        fireFighter.moveToPatient.visible(false);
                        return;
                    }
                }
            }

            if(fireFighter.isArrivedAt(targetPatient.position)) {                           // When the firefighter arrived at the target patient's position
                fireFighter.changeAction(new FireFighterFirstAid(fireFighter, targetPatient));      // Change the action to "First Aid"
                fireFighter.defaultImage.visible(true);
                fireFighter.moveToPatient.visible(false);
            }
        }
    }
}
