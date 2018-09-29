package agents;

import core.ImageObject;
import core.World;

import java.util.ArrayList;

public class Hospital extends CS {

    public int capacity = 5;
    public final ArrayList<Patient> patients = new ArrayList<>();

    public Hospital(World world, String name) {
        super(world, name);
        addChild(new ImageObject("src/engine/resources/hospital.png"));
    }

    public boolean isFull() {
        return patients.size() == capacity;
    }

    public void hospitalization(Patient patient) {
        patient.hospitalization = this;
        patient.treatmentStart();
        patients.add(patient);
    }
}
