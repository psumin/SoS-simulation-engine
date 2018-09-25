package action;

import action.legacy.MoveToLegacy;
import agents.CS;
import agents.FireFighter;
import agents.Patient;
import core.Tile;
import misc.Position;

import java.util.ArrayList;
import java.util.Comparator;

public class Search extends Action {

    FireFighter fireFighter;
    Observe observe;

    public Search(FireFighter target) {
        super(target);

        fireFighter = target;
    }

    public void onFoundPatients(ArrayList<Patient> patients) {
        int minDistance = 0;
        Patient minDistantPatient = null;
        for(Patient patient: patients) {
            int currentDistance =
                    Math.abs(patient.position.x - target.position.x)
                            + Math.abs(patient.position.y - target.position.y);
            if(minDistantPatient == null) {
                minDistantPatient = patient;
                minDistance = currentDistance;
            } else {
                if(minDistance > currentDistance) {
                    minDistantPatient = patient;
                    minDistance = currentDistance;
                }
            }
        }

//        if(minDistantPatient != null) {
//            this.stop();
//            Rescue(target, patient);
//        }

    }

    @Override
    public void start() {
        super.start();
        search();
    }

    @Override
    public void onUpdate() {
    }

    private void search() {
        while(true) {
            if(fireFighter.getUnVisited().isEmpty()) break;
            Tile tile = fireFighter.getUnVisited().poll();
            if(tile.isVisited()) continue;

            Action moveTo = new MoveTo(target, tile.position);
            moveTo.setParentAction(this);
            moveTo.onComplete = () -> {
                search();
            };
            moveTo.start();
            //observe.start();
            break;
        }
    }
}
