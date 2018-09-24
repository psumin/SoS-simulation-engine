package action;

import agents.FireFighter;
import agents.Patient;
import core.Tile;

import java.util.LinkedList;

public class Search extends Action {

    FireFighter fireFighter;
    Action currentAction;
    public Search(FireFighter target) {
        super(target);
        fireFighter = target;

        search();
    }

    private void search() {
        if(rescue()) {
            return;
        }
        while(true) {
            if(fireFighter.getUnVisited().isEmpty()) break;
            Tile tile = fireFighter.getUnVisited().poll();
            if(tile.isVisited()) continue;

            currentAction = new MoveTo(target, tile.position);
            currentAction.parentAction = this;
            break;
        }
    }

    @Override
    public void sendMessage(String msg, Object data) {
        if(msg.startsWith("action complete")) {
            search();
        } else if(msg.startsWith("move")) {
            rescue();
        } else if(msg == "rescue complete") {
            search();
        }
    }

    private boolean rescue() {
        LinkedList<Patient> patients =  fireFighter.getPatientsMemory();
        if(patients.isEmpty() == false) {
            int minDistance = 99999999;
            Patient minPatient = null;
            for(Patient patient : patients) {
                int dist = Math.abs(fireFighter.position.x - patient.position.x);
                dist += Math.abs(fireFighter.position.y - patient.position.y);
                if (dist < minDistance) {
                    minDistance = dist;
                    minPatient = patient;
                }
            }
            if(minPatient != null) {
                patients.remove(minPatient);
                //currentAction = new MoveTo(target, minPatient.position);
                if(currentAction != null) {
                    currentAction.remove();
                    currentAction = null;
                }
                currentAction = new Rescue(fireFighter, minPatient);
                currentAction.parentAction = this;
                return true;
            }
        }
        return false;
    }
}
