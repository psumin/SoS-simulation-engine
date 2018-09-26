package action.firefighteraction;


import agents.FireFighter;
import agents.Patient;
import core.Map;
import core.SoSObject;
import core.Tile;
import core.World;
import misc.Position;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

// 환자 구하러 이동
// 이동 중 위급도 높은 환자 발견시 타겟 변경
public class MoveToPatient extends FireFighterAction {

    Patient targetPatient;
    World world;
    Map worldMap;
    Map individualMap;
    Queue<Tile> unvisitedTiles;
    Position destination;
    int sightRange;
    public LinkedList<Patient> patientsMemory;

    public MoveToPatient(FireFighter target, Patient patient) {
        super(target);

        this.fireFighter = fireFighter;
        targetPatient = patient;

        world = fireFighter.world;
        worldMap = world.getMap();
        individualMap = fireFighter.localMap;
        unvisitedTiles = fireFighter.unVisited;
        patientsMemory = fireFighter.patientsMemory;
        sightRange = fireFighter.getSightRange();

        destination = patient.position;
    }

    @Override
    public void onUpdate() {
        searchPatient();
        if(patientsMemory.isEmpty()) {
            // 기억하고 있는 환자가 없으면
            // Search로
            fireFighter.changeAction(new Search(fireFighter));
        } else {
            // 기억하고 있는 환자가 있으면
            if(targetPatient.getStatus() == Patient.Status.Wounded) {
                // 가장 가까운 환자 위치를 구한 후
                // 그 환자에게 가기
                LinkedList<Patient> serious = new LinkedList<>();
                LinkedList<Patient> wounded = new LinkedList<>();
                patientsMemory.forEach(patient -> {
                    if(patient.getStatus() == Patient.Status.Serious) {
                        serious.add(patient);
                    }
                });

                Patient seriousMin = getMinDistantPatient(serious);
                Patient minDistantPatient = null;
                if(seriousMin != null) {
                    minDistantPatient = seriousMin;
                    fireFighter.changeAction(new MoveToPatient(fireFighter, minDistantPatient));
                }
            }

            if(moveTo(destination)) {
                // 목적지에 도착
                fireFighter.changeAction(new Treatment(fireFighter, targetPatient));
            } else {
                // 목적지로 가는 도중
            }
        }

//        if(moveTo(destination)) {
//            // 목적지 도착 완료
//            destination = null;
//        } else {
//            // 목적지 도착 전
//        }
    }

    void searchPatient() {
        ArrayList<Patient> foundPatients = new ArrayList<>();
        for(int y = fireFighter.position.y - sightRange / 2; y <= fireFighter.position.y + sightRange / 2; ++y) {
            for(int x = fireFighter.position.x - sightRange / 2; x <= fireFighter.position.x + sightRange / 2; ++x) {

                Tile worldTile = worldMap.getTile(x, y);
                Tile individualTile = individualMap.getTile(x, y);
                if(worldTile != null && individualTile.isVisited() == false) {
                    ArrayList<SoSObject> objects = worldTile.getObjects();
                    objects.forEach(obj -> {
                        if(obj instanceof Patient) {
                            foundPatients.add((Patient)obj);
                            //fireFighter.getPatientsMemory().add((Patient) obj);
                        }
                    });
                }
                individualMap.visited(x, y, true);
                worldMap.visited(x, y, true);
                //getLocalMap().visited(x, y, true);
                //getWorld().getMap().visited(x, y, true);
            }
        }
        patientsMemory.addAll(foundPatients);

//        if(foundPatients.isEmpty() == false) {
//            search.onFoundPatients(foundPatients);
//            //onFoundPatients.accept(foundPatients);
//        }
    }

    Patient getMinDistantPatient(LinkedList<Patient> patients) {
        int minDistance = 0;
        Patient minDistantPatient = null;
        for(Patient patient: patients) {
            int currentDistance = fireFighter.getDistanceTo(patient);
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
        return minDistantPatient;
    }

    boolean moveTo(Position dest) {
        int distanceX = dest.x - fireFighter.position.x;
        int distanceY = dest.y - fireFighter.position.y;

        if(distanceX == 0 && distanceY == 0) {
            return true;
        }

        if(Math.abs(distanceX) > Math.abs(distanceY)) {
            fireFighter.setPosition(fireFighter.position.x + distanceX / Math.abs(distanceX), fireFighter.position.y);
        } else {
            fireFighter.setPosition( fireFighter.position.x, fireFighter.position.y + distanceY / Math.abs(distanceY));
        }
        return false;
    }


    Position selectDestination() {
        while(true) {
            if(unvisitedTiles.isEmpty()) break;
            Tile tile = unvisitedTiles.poll();
            if(tile.isVisited()) continue;

            //destination = tile.position;
            return tile.position;
        }
        return null;
    }

}
