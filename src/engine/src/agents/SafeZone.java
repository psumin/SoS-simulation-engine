package agents;

import core.ImageObject;
import core.TextObject;
import core.World;
import misc.Position;

import java.awt.*;
import java.util.ArrayList;

public class SafeZone extends CS {

    private ArrayList<Patient> patients = new ArrayList<>();


    private TextObject textObject = new TextObject();
    private int scale = 5;
    public SafeZone(World world, String name) {
        super(world, name);
        addChild(new ImageObject("src/engine/resources/safezone.png", scale));
        addChild(textObject);
        textObject.fontColor = Color.red;
    }

    public boolean isSafeZone(Position position) {
        return isSafeZone(position.x, position.y);
    }

    public boolean isSafeZone(int x, int y) {
        int distanceX = Math.abs(position.x - x);
        int distanceY = Math.abs(position.y - y);

        return distanceX <= (scale / 2) && distanceY <= (scale / 2);
    }

    @Override
    public void onUpdate() {
        textObject.text = "" + patients.size();
    }

    public boolean isEmpty() {
        return patients.isEmpty();
    }

    public void arrivedPatient(Patient patient) {
        //patients.remove(patient);
        patients.add(patient);
    }
    public void leavePatient(Patient patient) {
        patients.remove(patient);
    }

    public Patient getPatient(Patient.Status status) {
        for (Patient patient : patients) {
            if (patient.getStatus() == status) {
                //targetPatient = patient;
                return patient;
            }
        }
        return null;
    }

    public int countPatient(Patient.Status status) {
        int count = 0;
        for (Patient patient : patients) {
            if (patient.getStatus() == status) {
                count++;
            }
        }
        return count;
    }
}
