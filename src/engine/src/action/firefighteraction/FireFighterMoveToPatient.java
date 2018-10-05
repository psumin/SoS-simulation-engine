package action.firefighteraction;

import agents.FireFighter;
import agents.Patient;

import java.util.ArrayList;

public class FireFighterMoveToPatient extends FireFighterAction {

    Patient targetPatient;
    ArrayList<Patient> patientsMemory;

    public FireFighterMoveToPatient(FireFighter target, Patient targetPatient) {
        super(target);

        name = "MoveToPatient";

        this.targetPatient = targetPatient;
        world.map.remove(targetPatient);
        patientsMemory = fireFighter.patientsMemory;
    }

    @Override
    public void onUpdate() {

        if(targetPatient != null) {
            ArrayList<Patient> foundPatient = fireFighter.observe();

            Patient patient = fireFighter.selectTargetPatient(foundPatient);
            if(patient != null) {
                if (patient.isSerious()) {
                    if (distantTo(targetPatient) > distantTo(patient)) {
                        world.map.add(targetPatient);
                        fireFighter.changeAction(new FireFighterMoveToPatient(fireFighter, patient));
                    }
                } else {
                    if (targetPatient.isWounded()) {
                        if (distantTo(targetPatient) > distantTo(patient)) {
                            world.map.add(targetPatient);
                            fireFighter.changeAction(new FireFighterMoveToPatient(fireFighter, patient));
                        }
                    }
                }
            }

            fireFighter.moveTo(targetPatient.position);
            fireFighter.markVisitedTiles();

            // TODO: 시야 내에 타겟 환자 존재 X

            // TODO: 관찰, 더 위급한 놈한테 가기


            if(fireFighter.isArrivedAt(targetPatient.position)) {
                fireFighter.changeAction(new FireFighterFirstAid(fireFighter, targetPatient));
            }
        }

//        //------------------------------------- observe environment
//        ArrayList<Patient> foundPatient = fireFighter.observe();
//        patientsMemory.addAll(foundPatient);
//
//        //------------------------------------- 타겟 환자 선택
//        Patient targetPatient = fireFighter.selectTargetPatient(patientsMemory);
//        patientsMemory.remove(targetPatient);
//
//        if(targetPatient != null) {
//            fireFighter.changeAction(new FireFighterMoveToPatient(fireFighter, targetPatient));
//        }
//
//
//        else {
//            //------------------------------------- 타겟 환자가 존재하지 않을 때, 방문하지 않은 타일로 이동
//            // select unvisited tile
//            if(unvisitedTile == null) {
//                unvisitedTile = selectUnvisitedTile();
//            }
//            // move to unvisited tile (1 tile per 3 frame)
//            if(unvisitedTile != null) {
//                fireFighter.moveTo(unvisitedTile);
//                fireFighter.markVisitedTiles();
//            }
//
//            if(isArrivedAt(unvisitedTile)) {
//                unvisitedTile = null;
//            }
//        }
    }
}
