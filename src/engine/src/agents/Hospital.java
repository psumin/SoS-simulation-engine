package agents;

import core.ImageObject;
import core.World;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;


public class Hospital extends CS {

    private int capacity = 5;
    private final ArrayList<Patient> patients = new ArrayList<>();

    public Hospital(World world, String name) {
        super(world, name);
        addChild(new ImageObject("src/engine/resources/hospital.png"));
    }

    // 환자 수용 공간이 남아있는지
    public boolean isAvailable() {
        return patients.size() < capacity;
    }

    // 예약
    public void reserve(Patient patient) {
        patients.remove(patient);
        patients.add(patient);
    }

    // 환자 입원
    public void hospitalize(Patient patient) {
        patient.currentHospital = this;
        patient.treatmentStart();
    }

    // 환자 퇴원
    public void leavePatient(Patient patient) {
        patients.remove(patient);
        patient.currentHospital = null;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
