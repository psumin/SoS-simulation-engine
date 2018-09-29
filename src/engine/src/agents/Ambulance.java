package agents;

import core.ImageObject;
import core.SoSObject;
import core.World;
import misc.Position;

import java.util.ArrayList;

public class Ambulance extends CS{
    private SafeZone targetSafeZone;
    private Patient targetPatient;
    private Patient onBoardPatient;
    public Ambulance(World world, String name) {
        super(world, name);

        addChild(new ImageObject("src/engine/resources/ambulance.png"));
    }

    private Hospital targetHospital;
    @Override
    public void onUpdate() {
        if(onBoardPatient != null) {
            //ArrayList<SoSObject> hospitals = new ArrayList<>(world.hospitals);
            if(targetHospital == null) {
                ArrayList<SoSObject> hospitals = new ArrayList<>();
                for (Hospital hospital : world.hospitals) {
                    if (hospital.isAvailable()) {
                        hospitals.add(hospital);
                    }
                }
                SoSObject minDistantObject = SoSObject.minDistantObject(this, hospitals);
                if (minDistantObject != null) {
                    Hospital hospital = (Hospital) minDistantObject;
                    targetHospital = hospital;
                    hospital.reserve(onBoardPatient);
//                    hospital.patients.remove(onBoardPatient);
//                    hospital.patients.add(onBoardPatient);
                }
            } else {
                if (moveToUpdate(targetHospital.position)) {
                    // TODO: 환자 내려줌
                    //world.removeChild(onBoardPatient);
                    //targetHospital.patients.remove(onBoardPatient);
                    targetHospital.hospitalize(onBoardPatient);
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
            if (maxSeriousSafeZone.countPatient(Patient.Status.Serious) == 0) {
                // TODO: 위험하지 않은 놈들 카운트
                SafeZone maxWoundedSafeZone = maxZone(Patient.Status.Wounded);
                if (targetSafeZone == null) {
                    //if(maxWoundedSafeZone.patients.isEmpty()) return;
                    if(maxWoundedSafeZone.isEmpty()) return;

                    targetSafeZone = maxWoundedSafeZone;
                    targetPatient = targetSafeZone.getPatient(Patient.Status.Wounded);
                    maxWoundedSafeZone.leavePatient(targetPatient);
                }
            } else {
                if (targetSafeZone == null) {
                    targetSafeZone = maxSeriousSafeZone;
                    targetPatient = targetSafeZone.getPatient(Patient.Status.Serious);
                    maxSeriousSafeZone.leavePatient(targetPatient);
                }
            }
        }
    }

    private SafeZone maxZone(Patient.Status status) {
        int maxCount = 0;
        SafeZone maxZone = null;
        for(SafeZone safeZone: world.safeZones) {
            //int count = statusCount(safeZone, status);
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
//    int statusCount(SafeZone safeZone, Patient.Status status) {
//        int count = 0;
//        for (Patient patient : safeZone.patients) {
//            if (patient.getStatus() == status) {
//                count++;
//            }
//        }
//        return count;
//    }

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
}
