package agents;

import core.ImageObject;
import core.Msg;
import core.MsgRouter;
import core.World;

import java.util.ArrayList;


public class Hospital extends CS {

    private int capacity = 5;
    private final ArrayList<Patient> patients = new ArrayList<>();

    public Hospital(World world, String name) {
        super(world, name);
        router = world.router;
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
        patients.remove(patient);
        patient.position.set(position);
        //patient.setPosition(position);
        patient.treatmentStart(this);
        patients.add(patient);
    }

    // 환자 퇴원
    public void leavePatient(Patient patient) {
        patients.remove(patient);
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public void recvMsg(Msg msg) {
        if(msg.title == "is available") {
            String title = "";
            if(isAvailable()) {
                title = "available true";
            } else {
                title = "available false";
            }
            router.route(new Msg()
                    .setFrom(name)
                    .setTo(msg.from)
                    .setTitle(title)
                    .setData(this));
        } else if(msg.title == "reserve") {
            Patient patient = (Patient)msg.data;
            reserve(patient);
        }
    }
}
