package agents;

import core.*;
import misc.Position;

import java.util.ArrayList;

public class Ambulance extends CS{
    private SafeZone targetSafeZone;
    private Patient targetPatient;
    private Patient onBoardPatient;
    private MsgRouter router;
    public Ambulance(World world, String name) {
        super(world, name);
        router = world.router;
        addChild(new ImageObject("src/engine/resources/ambulance.png"));
    }

    private Hospital targetHospital;
    @Override
    public void onUpdate() {
        if(onBoardPatient != null) {
            if(targetHospital == null) {
                // TODO: MoveToHospital
                //ArrayList<SoSObject> hospitals = new ArrayList<>();
                for (Hospital hospital : world.hospitals) {
                    router.route(new Msg()
                            .setFrom(name)
                            .setTo(hospital.name)
                            .setTitle("is available"));
//                    if (hospital.isAvailable()) {
//                        hospitals.add(hospital);
//                    }
                }
//                SoSObject minDistantObject = SoSObject.minDistantObject(this, hospitals);
//                if (minDistantObject != null) {
//                    Hospital hospital = (Hospital) minDistantObject;
//                    targetHospital = hospital;
//                    hospital.reserve(onBoardPatient);
//                }
            } else {
                if (moveToUpdate(targetHospital.position)) {
                    // TODO: 환자 내려줌
                    targetHospital.hospitalize(onBoardPatient);
                    targetHospital = null;
                    targetSafeZone = null;
                    onBoardPatient = null;
                    targetPatient = null;
                }
            }
        }
        else if(targetPatient != null) {
            // TODO: MoveToSafeZone
            if (moveToUpdate(targetSafeZone.position)) {
                // TODO: 환자 탑승
                onBoardPatient = targetPatient;
                onBoardPatient.visible(false);
                targetSafeZone = null;
                targetPatient = null;
            }
        } else {
            for(SafeZone zone: world.safeZones) {
                router.route(new Msg()
                        .setFrom(name)
                        .setTo(zone.name)
                        .setTitle("get weight"));
            }
//            SafeZone maxSeriousSafeZone = maxZone(Patient.Status.Serious);
//            if (maxSeriousSafeZone.countPatient(Patient.Status.Serious) == 0) {
//                // TODO: 위험하지 않은 놈들 카운트
//                SafeZone maxWoundedSafeZone = maxZone(Patient.Status.Wounded);
//                if (targetSafeZone == null) {
//                    if(maxWoundedSafeZone.isEmpty()) return;
//
//                    targetSafeZone = maxWoundedSafeZone;
//                    targetPatient = targetSafeZone.getPatient(Patient.Status.Wounded);
//                    maxWoundedSafeZone.leavePatient(targetPatient);
//                }
//            } else {
//                if (targetSafeZone == null) {
//                    targetSafeZone = maxSeriousSafeZone;
//                    targetPatient = targetSafeZone.getPatient(Patient.Status.Serious);
//                    maxSeriousSafeZone.leavePatient(targetPatient);
//                }
//            }
        }
    }

    private SafeZone maxZone(Patient.Status status) {
        int maxCount = 0;
        SafeZone maxZone = null;
        for(SafeZone safeZone: world.safeZones) {
            int count = safeZone.countPatient(status);

            if(maxZone == null) {
                maxCount = count;
                maxZone = safeZone;
            } else {
                if(maxCount < count) {
                    maxCount = count;
                    maxZone = safeZone;
                }
            }
        }
        return maxZone;
    }

    private boolean moveToUpdate(Position dest) {
        int distanceX = dest.x - position.x;
        int distanceY = dest.y - position.y;

        if(distanceX == 0 && distanceY == 0) {
            return true;
        }

        if(Math.abs(distanceX) > Math.abs(distanceY)) {
            setPosition(position.x + distanceX / Math.abs(distanceX), position.y);
        } else {
            setPosition( position.x, position.y + distanceY / Math.abs(distanceY));
        }
        return false;
    }

    private final ArrayList<Msg> receivedMsgFromHospital = new ArrayList<>();
    private final ArrayList<Msg> receivedMsgFromSafeZone = new ArrayList<>();
    @Override
    public void recvMsg(Msg msg) {
        if(msg.from.startsWith("Hospital")) {
            receivedMsgFromHospital.add(msg);
            if(receivedMsgFromHospital.size() == World.maxHospital) {
                ArrayList<SoSObject> hospitals = new ArrayList();
                //safeZoneAndHospitals.addAll(world.hospitals);
                for(Msg respond: receivedMsgFromHospital) {
                    if(respond.title == "available true") {
                        hospitals.add((SoSObject)respond.data);
                    }
                }
                targetHospital = (Hospital)minDistantObject(this, hospitals);
                router.route(new Msg()
                        .setFrom(name)
                        .setTo(targetHospital.name)
                        .setTitle("reserve")
                        .setData(targetPatient));
                receivedMsgFromHospital.clear();
            }
        } else if(msg.from.startsWith("SafeZone")) {
            if(msg.title == "weight") {
                receivedMsgFromSafeZone.add(msg);
                if(receivedMsgFromSafeZone.size() == World.maxSafeZone) {
                    Msg maxMsg = null;
                    for(Msg respond: receivedMsgFromSafeZone) {
                        if(maxMsg == null) {
                            maxMsg = respond;
                        } else if((int)maxMsg.data < (int)respond.data) {
                            maxMsg = respond;
                        }
                    }

                    targetSafeZone = (SafeZone)world.findObject(maxMsg.from);

                    router.route(new Msg()
                            .setFrom(name)
                            .setTo(targetSafeZone.name)
                            .setTitle("get patient"));

                    receivedMsgFromSafeZone.clear();
                }
            } else if(msg.title == "patient") {
                targetPatient = (Patient)msg.data;
                targetSafeZone.leavePatient(targetPatient);
            }
        }
    }
}
