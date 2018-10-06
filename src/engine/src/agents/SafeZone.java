package agents;

import core.*;
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

        for(Patient p: patients) {
            if(p == patient) return;
        }

        world.addChild(patient);
        patient.position.set(position);
        patients.add(patient);

        String title = "";
        if(patient.isSerious()) {
            title = "serious patient arrived";
        } else {
            title = "wounded patient arrived";
        }

        router.route(new Msg()
                .setFrom(name)
                .setTo("Organization")
                .setTitle(title)
                .setData(this));
    }
    public void leavePatient(Patient patient) {
        world.removeChild(patient);
        patients.remove(patient);
    }

    public Patient getPatient(Patient.Status status) {
        for (Patient patient : patients) {
            if (patient.getStatus() == status) {
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

    private int getWeight() {
        int weight = 0;
        for(Patient patient: patients) {
            if(patient.getStatus() == Patient.Status.Serious) {
                weight += 2;
            } else {
                weight += 1;
            }
        }
        return weight;
    }

    private Patient getPriorityPatient() {
        Patient patient = getPatient(Patient.Status.Serious);
        if(patient == null) {
            patient = getPatient(Patient.Status.Wounded);
        }
        return patient;
    }

    public void recvMsg(Msg msg) {
        if(msg.title == "get weight") {
            router.route(new Msg()
                    .setFrom(name)
                    .setTo(msg.from)
                    .setTitle("weight")
                    .setData(getWeight()));
        } else if(msg.title == "get patient") {
            router.route(new Msg()
                    .setFrom(name)
                    .setTo(msg.from)
                    .setTitle("patient")
                    .setData(getPriorityPatient()));
        }
    }
}
