package action;

import agents.FireFighter;
import agents.Patient;
import core.Tile;

import java.util.LinkedList;

public class SearchLegacy extends ActionLegacy {

    FireFighter fireFighter;
    ActionLegacy currentActionLegacy;
    public SearchLegacy(FireFighter target) {
        super(target);
        fireFighter = target;

        search();
    }

    private void search() {
        if(selectPatient()) {
            return;
        }
        while(true) {
            if(fireFighter.getUnVisited().isEmpty()) break;
            Tile tile = fireFighter.getUnVisited().poll();
            if(tile.isVisited()) continue;

            currentActionLegacy = new MoveToLegacy(target, tile.position);
            currentActionLegacy.parentActionLegacy = this;
            break;
        }
    }

    @Override
    public void sendMessage(String msg, Object data) {
        if(msg.startsWith("action complete")) {
            search();
        } else if(msg.startsWith("move")) {
            selectPatient();
        } else if(msg == "rescue complete") {
            search();
        }
    }

    private boolean selectPatient() {
        LinkedList<Patient> patients =  fireFighter.getPatientsMemory();
        LinkedList<Patient> serious = new LinkedList<>();
        LinkedList<Patient> wounded = new LinkedList<>();

        patients.forEach(p -> {
            switch (p.getStatus()) {
                case Wounded:
                    wounded.add(p);
                    break;
                case Serious:
                    serious.add(p);
                    break;
                case Dead:
                    break;
            }
        });

        if(serious.isEmpty() == false) {
            int minDistance = 99999999;
            Patient minPatient = null;
            for(Patient patient : serious) {
                int dist = Math.abs(fireFighter.position.x - patient.position.x);
                dist += Math.abs(fireFighter.position.y - patient.position.y);
                if (dist < minDistance) {
                    minDistance = dist;
                    minPatient = patient;
                }
            }
            if(minPatient != null) {
                patients.remove(minPatient);
                //serious.remove(minPatient);
                //currentActionLegacy = new MoveToLegacy(target, minPatient.position);
                if(currentActionLegacy != null) {
                    currentActionLegacy.remove();
                    currentActionLegacy = null;
                }
                currentActionLegacy = new RescueLegacy(fireFighter, minPatient);
                currentActionLegacy.parentActionLegacy = this;
                return true;
            }
        }


        if(wounded.isEmpty() == false) {
            int minDistance = 99999999;
            Patient minPatient = null;
            for(Patient patient : wounded) {
                int dist = Math.abs(fireFighter.position.x - patient.position.x);
                dist += Math.abs(fireFighter.position.y - patient.position.y);
                if (dist < minDistance) {
                    minDistance = dist;
                    minPatient = patient;
                }
            }
            if(minPatient != null) {
                patients.remove(minPatient);
                //wounded.remove(minPatient);
                //currentActionLegacy = new MoveToLegacy(target, minPatient.position);
                if(currentActionLegacy != null) {
                    currentActionLegacy.remove();
                    currentActionLegacy = null;
                }
                currentActionLegacy = new RescueLegacy(fireFighter, minPatient);
                currentActionLegacy.parentActionLegacy = this;
                return true;
            }
        }
        return false;

//        if(patients.isEmpty() == false) {
//            int minDistance = 99999999;
//            Patient minPatient = null;
//            for(Patient patient : patients) {
//                int dist = Math.abs(fireFighter.position.x - patient.position.x);
//                dist += Math.abs(fireFighter.position.y - patient.position.y);
//                if (dist < minDistance) {
//                    minDistance = dist;
//                    minPatient = patient;
//                }
//            }
//            if(minPatient != null) {
//                patients.remove(minPatient);
//                //currentActionLegacy = new MoveToLegacy(target, minPatient.position);
//                if(currentActionLegacy != null) {
//                    currentActionLegacy.remove();
//                    currentActionLegacy = null;
//                }
//                currentActionLegacy = new RescueLegacy(fireFighter, minPatient);
//                currentActionLegacy.parentActionLegacy = this;
//                return true;
//            }
//        }
//        return false;
    }
}
