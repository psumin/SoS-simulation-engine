package action.firefighteraction;

import agents.FireFighter;
import agents.Hospital;
import agents.Patient;
import agents.SafeZone;
import core.*;
import misc.Position;
import misc.Time;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class FireFighterCollaborativeAction extends FireFighterAction {

    int communicationRange = 5;

    enum State {
        None, Search, Treatment, MoveToPatient, Transfer
    }

    State currentState = State.Search;

    World world;
    Map worldMap;
    Map individualMap;
    Queue<Tile> unvisitedTiles;
    Position destination;
    int sightRange;
    //public LinkedList<Patient> patientsMemory;
    public ArrayList<Patient> patientsMemory;

    MsgRouter router;


    // Search에서 목적지
    Position moveToDest = null;
    Position searchDestination = null;
    // MoveToPatient에서의 타겟
    Patient targetPatient = null;

    int treatmentTime = 10;

    ArrayList<SafeZone> safeZones;

    public FireFighterCollaborativeAction(FireFighter fireFighter) {
        super(fireFighter);

        this.fireFighter = fireFighter;
        world = fireFighter.world;
        safeZones = world.safeZones;
        worldMap = world.getMap();
        individualMap = fireFighter.individualMap;
        unvisitedTiles = fireFighter.unvisitedTiles;
        patientsMemory = fireFighter.patientsMemory;
        sightRange = fireFighter.getSightRange();

        router = world.router;
    }


    private void searchUpdate() {
        searchPatient();

        if(patientsMemory.isEmpty()) {
            // 기억하고 있는 환자가 없으면
            // 맵의 어두운 부분으로 가기
            if(searchDestination == null) {
                searchDestination = selectDestination();
            } else {
                if(delayedMoveToUpdate(searchDestination)) {
                    searchDestination = null;
                }
            }
        } else {
            // 기억하고 있는 환자가 있으면
            // 가장 가까운 환자 위치를 구한 후
            // 그 환자에게 가기
            LinkedList<Patient> serious = new LinkedList<>();
            LinkedList<Patient> wounded = new LinkedList<>();
            patientsMemory.forEach(patient -> {
                if(patient.getStatus() == Patient.Status.Serious) {
                    serious.add(patient);
                } else {
                    wounded.add(patient);
                }
            });

            Patient seriousMin = getMinDistantPatient(serious);
            Patient woundedMin = getMinDistantPatient(wounded);

            Patient minDistantPatient = null;
            if(seriousMin != null) {
                minDistantPatient = seriousMin;
            } else {
                minDistantPatient = woundedMin;
            }
            assert woundedMin != null : "null 일 때 처리 추가해야함";

            targetPatient = minDistantPatient;
            currentState = State.MoveToPatient;
            //fireFighter.changeAction(new MoveToPatient(fireFighter, minDistantPatient));
        }
    }

    int treatmentFrameCount = 0;
    private void treatmentInit() {
        treatmentFrameCount = treatmentTime;
    }
    private void treatmentUpdate() {
        Tile tile = fireFighter.world.getMap().getTile(targetPatient.position.x, targetPatient.position.y);
        if(tile.contain(targetPatient) == false) {
            treatmentPatient();
            return;
        }

        if(targetPatient.fireFighter != null && targetPatient.fireFighter != fireFighter) {
            fireFighter.patientsMemory.remove(targetPatient);
            currentState = State.Search;
            //fireFighter.changeAction(new Search(fireFighter));
        } else {
            targetPatient.fireFighter = fireFighter;
        }

        treatmentFrameCount--;
        if(treatmentFrameCount == 0) {
            assert targetPatient.fireFighter == null : "아니면 다시 생각해보자";
            targetPatient.fireFighter = fireFighter;
            treatmentPatient();

            fireFighter.transferImage.visible(true);
            fireFighter.defaultImage.visible(false);
            currentState = State.Transfer;
        }
    }
    private void moveToPatientUpdate() {
        if(targetPatient != null) {
            destination = targetPatient.position;
        }
        searchPatient();
        if(patientsMemory.isEmpty()) {
            // 기억하고 있는 환자가 없으면
            // Search로
            //fireFighter.changeAction(new Search(fireFighter));
            currentState = State.Search;
        } else {
            // 기억하고 있는 환자가 있으면
            if(targetPatient.getStatus() == Patient.Status.Wounded) {
                // 가장 가까운 환자 위치를 구한 후
                // 그 환자에게 가기
                LinkedList<Patient> serious = new LinkedList<>();
                //LinkedList<Patient> wounded = new LinkedList<>();
                patientsMemory.forEach(patient -> {
                    if(patient.getStatus() == Patient.Status.Serious) {
                        serious.add(patient);
                    }
                });

                // 더 위급한 환자가 있으면 그 환자한테 가기
                Patient seriousMin = getMinDistantPatient(serious);
                Patient minDistantPatient = null;
                if(seriousMin != null) {
                    minDistantPatient = seriousMin;
                    targetPatient = minDistantPatient;
                    //fireFighter.changeAction(new MoveToPatient(fireFighter, minDistantPatient));
                }
            }

            if(delayedMoveToUpdate(destination)) {
                // 목적지에 도착
                //fireFighter.changeAction(new Treatment(fireFighter, targetPatient));
                treatmentInit();
                currentState = State.Treatment;
            } else {
                int distanceX = Math.abs(targetPatient.position.x - fireFighter.position.x);
                int distanceY = Math.abs(targetPatient.position.y - fireFighter.position.y);

                //if(distanceX <= 1 && distanceY <= 1) {
                if(distanceX <= sightRange / 2 && distanceY <= sightRange / 2) {
                    // 시야 안
                    Tile tile = fireFighter.world.getMap().getTile(targetPatient.position.x, targetPatient.position.y);
                    if (tile.contain(targetPatient) == false) {
                        fireFighter.patientsMemory.remove(targetPatient);
                        //fireFighter.changeAction(new Search(fireFighter));
                        currentState = State.Search;
                        return;
                    }

                    if (targetPatient.fireFighter != null && targetPatient.fireFighter != fireFighter) {
                        fireFighter.patientsMemory.remove(targetPatient);
                        //fireFighter.changeAction(new Search(fireFighter));
                        currentState = State.Search;
                    }
//                    else {
//                        targetPatient.fireFighter = fireFighter;
//                    }
                }
            }
        }
    }

    SoSObject transferDestination = null;
    int transferTime = 5;
    void transferUpdate() {
        searchPatient();
        if(transferDestination == null) {
            ArrayList<SoSObject> safeZoneAndHospitals = new ArrayList<>();
            safeZoneAndHospitals.addAll(world.safeZones);
            //safeZoneAndHospitals.addAll(world.hospitals);
            for(Hospital hospital: world.hospitals) {
                if(hospital.isAvailable()) {
                    safeZoneAndHospitals.add(hospital);
                }
            }

            transferDestination = SoSObject.minDistantObject(fireFighter, safeZoneAndHospitals);
            if(transferDestination instanceof Hospital) {
                ((Hospital) transferDestination).reserve(targetPatient);
//                ((Hospital) transferDestination).patients.remove(targetPatient);
//                ((Hospital) transferDestination).patients.add(targetPatient);
            }
        } else {
            if(delayedMoveToUpdate(transferDestination.position)) {

                if(transferDestination instanceof SafeZone) {
                    SafeZone safeZone = (SafeZone)transferDestination;
                    //safeZone.patients.remove(targetPatient);
                    safeZone.patients.add(targetPatient);
                } else {
                    Hospital hospital = (Hospital)transferDestination;
                    //hospital.patients.remove(targetPatient);
                    hospital.hospitalize(targetPatient);
                }



                transferDestination = null;
                fireFighter.transferImage.visible(false);
                fireFighter.defaultImage.visible(true);
                world.addChild(targetPatient);
                targetPatient.setPosition(fireFighter.position);
                currentState = State.Search;

                //world.savedPatient++;
            }
        }
    }


    @Override
    public void onUpdate() {
        //fireFighter.addToTile();
        if(unvisitedTiles.isEmpty() && patientsMemory.isEmpty()) return;

        router.broadcast(fireFighter,
                new Msg()
                        .setFrom(fireFighter.name)
                        .setTitle("individual map")
                        .setTo("broadcast")
                        .setData(individualMap),
                fireFighter.position, communicationRange);
        router.broadcast(fireFighter,
                new Msg()
                        .setFrom(fireFighter.name)
                        .setTitle("patientsMemory")
                        .setTo("broadcast")
                        .setData(patientsMemory),
                fireFighter.position, communicationRange);
        switch (currentState) {
            case None:
                break;
            case Search:
                searchUpdate();
                break;
            case Treatment:
                treatmentUpdate();
                break;
            case MoveToPatient:
                moveToPatientUpdate();
                break;
            case Transfer:
                transferUpdate();
                break;
        }
        //fireFighter.removeFromTile();
    }

    // 시야 범위 내에 환자가 존재하는지 탐색
    void searchPatient() {
        ArrayList<Patient> foundPatients = new ArrayList<>();
        for(int y = fireFighter.position.y - sightRange / 2; y <= fireFighter.position.y + sightRange / 2; ++y) {
            for(int x = fireFighter.position.x - sightRange / 2; x <= fireFighter.position.x + sightRange / 2; ++x) {

                Tile worldTile = worldMap.getTile(x, y);
                Tile individualTile = individualMap.getTile(x, y);

                boolean isSafeZone = false;
                for(SafeZone zone: safeZones) {
                    if(zone.isSafeZone(x, y)) {
                        isSafeZone = true;
                        individualMap.visited(x, y, true);
                        worldMap.visited(x, y, true);
                        break;
                    }
                }

                if(isSafeZone == false) {
                    if (worldTile != null && individualTile.isVisited() == false) {
                        ArrayList<SoSObject> objects = worldTile.getObjects();
                        objects.forEach(obj -> {
                            if (obj instanceof Patient) {
                                foundPatients.add((Patient) obj);
                                //fireFighter.getPatientsMemory().add((Patient) obj);
                            }
                        });
                    }
                    individualMap.visited(x, y, true);
                    worldMap.visited(x, y, true);
                    //getIndividualMap().visited(x, y, true);
                    //getWorld().getMap().visited(x, y, true);
                }
            }
        }
        patientsMemory.removeAll(foundPatients);
        patientsMemory.addAll(foundPatients);

//        if(foundPatients.isEmpty() == false) {
//            search.onFoundPatients(foundPatients);
//            //onFoundPatients.accept(foundPatients);
//        }
    }


    // 인자로 넘겨준 환자들 중에서 가장 가까운 환자 리턴
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

    int moveDelay = 3;
    int counter = moveDelay;
    boolean delayedMoveToUpdate(Position dest) {
        counter--;
        if(counter <= 0) {
            counter = moveDelay;
            return moveToUpdate(dest);
        }
        return false;
    }

    // 인자로 넘겨준 목적지까지 이동
    // 한 프레임에 한 칸 이동
    // return true: 목적지에 도착했다
    // return false: 아직 도착하지 못했다
    boolean moveToUpdate(Position dest) {
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

    void treatmentPatient() {
        //targetPatient.remove();
        world.removeChild(targetPatient);
        fireFighter.patientsMemory.remove(targetPatient);
        targetPatient.isSaved = true;
        //targetPatient.setStatus(Patient.Status.Saved);
        currentState = State.Search;
        //fireFighter.changeAction(new Search(fireFighter));
    }

    @Override
    public void recvMsg(Msg msg) {
        if(msg.title == "individual map") {
            Map othersMap = (Map)msg.data;

            if(Time.getFrameCount() > 50) {
                int a = 10;
            }
            // 방문 정보 업데이트
            othersMap.getTiles().forEach(tile -> {
                if(tile.isVisited()) {
                    individualMap.getTile(tile.position.x, tile.position.y).visited(true);
                }
            });
        }
        else if(msg.title == "patientsMemory") {
            ArrayList<Patient> othersMemory = (ArrayList<Patient>)msg.data;
            patientsMemory.removeAll(othersMemory);
            //patientsMemory.addAll(othersMemory);
            othersMemory.forEach(patient -> {
                if(patient.isSaved == false) {
                    patientsMemory.add(patient);
                }
//                if(patient.getStatus() != Patient.Status.Saved) {
//                    patientsMemory.add(patient);
//                }
            });
        }
    }
}
