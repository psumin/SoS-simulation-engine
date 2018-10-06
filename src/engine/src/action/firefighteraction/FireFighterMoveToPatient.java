package action.firefighteraction;

import agents.FireFighter;
import agents.Patient;
import core.Tile;

import java.util.ArrayList;
import java.util.Collections;

public class FireFighterMoveToPatient extends FireFighterAction {

    Patient targetPatient;
    ArrayList<Patient> patientsMemory;

    public FireFighterMoveToPatient(FireFighter target, Patient targetPatient) {
        super(target);

        name = "MoveToPatient";

        this.targetPatient = targetPatient;
        world.map.remove(targetPatient);
        patientsMemory = fireFighter.patientsMemory;
        patientsMemory.remove(targetPatient);

        fireFighter.defaultImage.visible(false);
        fireFighter.moveToPatient.visible(true);
    }

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

            Patient newPatient = fireFighter.selectTargetPatient(patientsMemory);
            if(newPatient != null) {
                if (newPatient.isSerious()) {
                    if(targetPatient.isWounded()) {
                        patientsMemory.add(targetPatient);
                        changeTargetPatient(newPatient);
                        return;
                    } else if (fireFighter.distantTo(targetPatient) > fireFighter.distantTo(newPatient)) {
                        patientsMemory.add(targetPatient);
                        changeTargetPatient(newPatient);
                        return;
                    }
                } else {
                    // 여기 둘 다 wounded
                    if (targetPatient.isWounded()) {
                        if (fireFighter.distantTo(targetPatient) > fireFighter.distantTo(newPatient)) {
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
            if(targetPatient.position.x - fireFighter.sightRange / 2 <= fireFighter.position.x
                && fireFighter.position.x <= targetPatient.position.x + fireFighter.sightRange / 2) {
                if(targetPatient.position.y - fireFighter.sightRange / 2 <= fireFighter.position.y
                        && fireFighter.position.y <= targetPatient.position.y + fireFighter.sightRange / 2) {
                    if(!world.contains(targetPatient)) {
                        fireFighter.changeAction(new FireFighterSearch(fireFighter));
                        fireFighter.defaultImage.visible(true);
                        fireFighter.moveToPatient.visible(false);
                        return;
                    }
                    if(targetPatient.assignedFireFighter != null) {
                        fireFighter.changeAction(new FireFighterSearch(fireFighter));
                        fireFighter.defaultImage.visible(true);
                        fireFighter.moveToPatient.visible(false);
                        return;
                    }
                }
            }

            if(fireFighter.isArrivedAt(targetPatient.position)) {
                fireFighter.changeAction(new FireFighterFirstAid(fireFighter, targetPatient));
                fireFighter.defaultImage.visible(true);
                fireFighter.moveToPatient.visible(false);
            }
        }
    }
}
