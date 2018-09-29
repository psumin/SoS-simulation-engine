package agents;

import core.ImageObject;
import core.SoSObject;
import core.World;
import misc.Position;
import misc.Time;

import java.util.ArrayList;

public class Ambulance extends CS{
    SafeZone targetSafeZone;
    Patient targetPatient;
    Patient onBoardPatient;
    public Ambulance(World world, String name) {
        super(world, name);

        addChild(new ImageObject("src/engine/resources/ambulance.png"));
    }

    Hospital targetHospital;
    @Override
    public void onUpdate() {
        if(onBoardPatient != null) {
            //ArrayList<SoSObject> hospitals = new ArrayList<>(world.hospitals);
            if(targetHospital == null) {
                ArrayList<SoSObject> hospitals = new ArrayList<>();
                for (Hospital hospital : world.hospitals) {
                    if (hospital.isFull() == false) {
                        hospitals.add(hospital);
                    }
                }
                SoSObject minDistantObject = SoSObject.minDistantObject(this, hospitals);
                if (minDistantObject != null) {
                    Hospital hospital = (Hospital) minDistantObject;
                    targetHospital = hospital;
                    hospital.reservation(onBoardPatient);
//                    hospital.patients.remove(onBoardPatient);
//                    hospital.patients.add(onBoardPatient);
                }
            } else {
                if (moveToUpdate(targetHospital.position)) {
                    // TODO: 환자 내려줌
                    //world.removeChild(onBoardPatient);
                    //targetHospital.patients.remove(onBoardPatient);
                    targetHospital.hospitalization(onBoardPatient);
                    targetHospital = null;
                    targetSafeZone = null;
                    onBoardPatient = null;
                    targetPatient = null;
//                world.savedPatient++;
                }
            }
        }
        else if(targetPatient != null) {
            if (moveToUpdate(targetSafeZone.position)) {
                // TODO: 환자 탑승
                onBoardPatient = targetPatient;
                onBoardPatient.visible(false);
                targetSafeZone = null;
                targetPatient = null;
            }
        } else {
            SafeZone maxSeriousSafeZone = maxZone(Patient.Status.Serious);
            if (statusCount(maxSeriousSafeZone, Patient.Status.Serious) == 0) {
                // TODO: 위험하지 않은 놈들 카운트
                SafeZone maxWoundedSafeZone = maxZone(Patient.Status.Wounded);
                if (targetSafeZone == null) {
                    if(maxWoundedSafeZone.patients.isEmpty()) return;

                    targetSafeZone = maxWoundedSafeZone;
                    for (Patient patient : maxWoundedSafeZone.patients) {
                        if (patient.getStatus() == Patient.Status.Wounded) {
                            targetPatient = patient;
                        }
                    }
                    maxWoundedSafeZone.patients.remove(targetPatient);
                }
            } else {
                if (targetSafeZone == null) {
                    targetSafeZone = maxSeriousSafeZone;
                    for (Patient patient : maxSeriousSafeZone.patients) {
                        if (patient.getStatus() == Patient.Status.Serious) {
                            targetPatient = patient;
                        }
                    }
                    maxSeriousSafeZone.patients.remove(targetPatient);
                }
            }
        }
    }

    SafeZone maxZone(Patient.Status status) {
        int maxCount = 0;
        SafeZone maxZone = null;
        for(SafeZone safeZone: world.safeZones) {
            int count = statusCount(safeZone, status);

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
    int statusCount(SafeZone safeZone, Patient.Status status) {
        int count = 0;
        for (Patient patient : safeZone.patients) {
            if (patient.getStatus() == status) {
                count++;
            }
        }
        return count;
    }

    boolean moveToUpdate(Position dest) {
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
}
