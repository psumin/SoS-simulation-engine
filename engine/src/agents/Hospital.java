package agents;

import core.ImageObject;
import core.Msg;
import core.MsgRouter;
import core.World;
import misc.Time;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class Hospital extends CS {

    private int capacity = 10;

    private final ArrayList<Patient> treatmentPatients = new ArrayList<>();

    // 대기열
    private final ArrayList<Patient> patients = new ArrayList<>();
    private Sheet sheet;
    private Row row;

    public Hospital(World world, String name, Sheet sheet) {
        super(world, name);
        router = world.router;
//        addChild(new ImageObject("src/engine/resources/hospital.png"));

        addChild(new ImageObject("engine/resources/hospital.png"));

        this.sheet = sheet;
    }

    // 환자 수용 공간이 남아있는지
    public boolean isAvailable() {
        return treatmentPatients.size() < capacity;
    }

    // 예약
    public void reserve(Patient patient) {
        patients.remove(patient);
        patients.add(patient);
    }

    @Override
    public void onUpdate() {
        row = sheet.createRow(sheet.getPhysicalNumberOfRows());
        row.createCell(0).setCellValue(Time.getFrameCount());
        row.createCell(1).setCellValue(name);
        row.createCell(2).setCellValue(treatmentPatients.size());
        row.createCell(3).setCellValue(patients.size());

//        if(isAvailable() && !patients.isEmpty()) {
//            Patient treatmentTarget = null;
//            for(Patient p: patients) {
//                if(p.isSerious()) {
//                    treatmentTarget = p;
//                    break;
//                }
//            }
//
//            if(treatmentTarget != null) {
//                patients.remove(treatmentTarget);
//            } else {
//                treatmentTarget = patients.get(0);
//                patients.remove(0);
//            }
//            treatmentPatients.add(treatmentTarget);
//            treatmentTarget.treatmentStart(this);
//        }
    }

    // 환자 입원
    public void hospitalize(Patient patient) {
        patients.remove(patient);
        patient.position.set(position);
        //patient.setPosition(position);

        if(isAvailable()) {
            treatmentPatients.add(patient);
            patient.treatmentStart(this);
            world.addChild(patient);
        } else {
            patients.add(patient);
        }

//        row.createCell(4).setCellValue("hospitalize");
    }

    // 환자 퇴원
    public void leavePatient(Patient patient) {
        treatmentPatients.remove(patient);
        if(isAvailable() && !patients.isEmpty()) {
            Patient treatmentTarget = null;
            for(Patient p: patients) {
                if(p.isSerious()) {
                    treatmentTarget = p;
                    break;
                }
            }

            if(treatmentTarget != null) {
                patients.remove(treatmentTarget);
            } else {
                treatmentTarget = patients.get(0);
                patients.remove(0);
            }
            treatmentPatients.add(treatmentTarget);
            treatmentTarget.treatmentStart(this);
            world.addChild(treatmentTarget);
        }
        //patients.remove(patient);
//        row.createCell(5).setCellValue("leave");
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
