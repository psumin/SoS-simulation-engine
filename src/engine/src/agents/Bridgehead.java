package agents;

import core.*;
import misc.Position;

import java.awt.*;
import java.util.ArrayList;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class Bridgehead extends CS {

    private ArrayList<Patient> patients = new ArrayList<>();

    private TextObject textObject = new TextObject();
    private int scale = 3;
    public Bridgehead(World world, String name) {
        super(world, name);
        addChild(new ImageObject("C:/Users/User/IdeaProjects/SoS-simulation-engine/src/engine/resources/bridgehead.png", scale));
//        addChild(new ImageObject("src/engine/resources/bridgehead.png", scale));


        addChild(textObject);
        textObject.fontColor = Color.red;
    }

    public boolean isBridgehead(Position position) {
        return isBridgehead(position.x, position.y);
    }

    public boolean isBridgehead(int x, int y) {
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
            title = "serious patient arrived";                                  // Serious patient arrived at the Bridgehead
        } else {
            title = "wounded patient arrived";                                  // Wounded patient arrived at the Bridgehead
        }

        router.route(new Msg()                                                  // Send a message to the Organization
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

//    public int countPatient(Patient.Status status) {
//        int count = 0;
//        for (Patient patient : patients) {
//            if (patient.getStatus() == status) {
//                count++;
//            }
//        }
//        return count;
//    }
//
//    private int getWeight() {
//        int weight = 0;
//        for(Patient patient: patients) {
//            if(patient.getStatus() == Patient.Status.Serious) {
//                weight += 5;
//            } else {
//                weight += 1;
//            }
//        }
//        return weight;
//    }
//
//    private Patient getPriorityPatient() {
//        Patient patient = getPatient(Patient.Status.Serious);
//        if(patient == null) {
//            patient = getPatient(Patient.Status.Wounded);
//        }
//        return patient;
//    }
//
//    public void recvMsg(Msg msg) {
//        if(msg.title == "get weight") {
//            router.route(new Msg()
//                    .setFrom(name)
//                    .setTo(msg.from)
//                    .setTitle("weight")
//                    .setData(getWeight()));
//        } else if(msg.title == "get patient") {
//            router.route(new Msg()
//                    .setFrom(name)
//                    .setTo(msg.from)
//                    .setTitle("patient")
//                    .setData(getPriorityPatient()));
//        }
//    }
}
