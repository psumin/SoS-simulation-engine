package agents;

import core.*;
import misc.Position;
import misc.Time;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class Patient extends CS {

   public enum Status {
        Wounded, Serious, Dead;

       public static Status random() {
           Status[] values = Status.values();
           int index = GlobalRandom.nextInt(1000);
           if (index >= 0)                                    // Ratio for Serious patient
               index = 0;
           else
               index = 1;
           return values[index];
       }
    }

    private Status status = Status.Wounded;
    public FireFighter assignedFireFighter = null;
    public boolean isSaved = false;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;

        switch (status) {
            case Wounded:                                       // Blue dot
                serious.visible(false);
                wounded.visible(true);
                break;
            case Serious:                                       // Red dot
                wounded.visible(false);
                serious.visible(true);
                break;
            case Dead:                                          // TODO: To be implemented
                break;
        }
    }
    public boolean isSerious() {
        return status == Status.Serious;
    }
    public boolean isWounded() {
        return status == Status.Wounded;
    }

    SoSObject serious;
    SoSObject wounded;

    public Patient(World world, String name) {
        super(world, name);
        serious = new ImageObject("C:/Users/User/IdeaProjects/SoS-simulation-engine/src/engine/resources/patient_serious1.png");              // Red dot
        wounded = new ImageObject("C:/Users/User/IdeaProjects/SoS-simulation-engine/src/engine/resources/patient_wounded1.png");              // Blue dot
//        serious = new ImageObject("src/engine/resources/patient_serious1.png");              // Red dot
//        wounded = new ImageObject("src/engine/resources/patient_wounded1.png");              // Blue dot
        addChild(serious);
        addChild(wounded);
    }

    @Override
    public void remove() {
        super.remove();
    }

    private Hospital currentHospital = null;
    private int seriousTreatmentTime = 40;                              // Hospital's treatment time for Serious patient
    private int woundedTreatmentTime = 40;                               // Hospital's treatment time for Wounded patient
    private boolean isTreatmenting = false;
    private int counter = 0;
    public void treatmentStart(Hospital hospital) {
        currentHospital = hospital;
        isTreatmenting = true;
        if(status == Status.Serious) {
            counter = seriousTreatmentTime;
        } else if(status == Status.Wounded) {
            counter = woundedTreatmentTime;
        }
    }

    @Override
    public void onUpdate() {
        if(isTreatmenting) {
            counter--;
            if(counter <= 0) {
                // TODO: 치료 완료
                assert currentHospital != null: "이러면 안된다";
                currentHospital.leavePatient(this);                         // Leave patient after treatment
                currentHospital = null;
                world.removeChild(this);
                world.savedPatientCount++;                                  // Number of Saved patients
            }
        }
    }

    @Override
    public void setPosition(int x, int y) {
        worldMap.remove(this);
        super.setPosition(x, y);
        worldMap.add(this);
    }

    @Override
    public void setPosition(Position position) {
        worldMap.remove(this);
        super.setPosition(position);
        worldMap.add(this);
    }
}
