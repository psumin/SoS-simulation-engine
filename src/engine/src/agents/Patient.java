package agents;

import core.*;
import misc.Time;

public class Patient extends CS {

   public enum Status {
        Wounded, Serious, Dead;

       public static Status random() {
           Status[] values = Status.values();
           int index = GlobalRandom.nextInt(1000);
           if (index >= 100)
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
            case Wounded:
                serious.visible(false);
                wounded.visible(true);
                break;
            case Serious:
                wounded.visible(false);
                serious.visible(true);
                break;
            case Dead:
                break;
        }
    }

    SoSObject serious;
    SoSObject wounded;

    public Patient(World world, String name) {
        super(world, name);
        serious = new ImageObject("src/engine/resources/patient_serious.png");
        wounded = new ImageObject("src/engine/resources/patient_wounded.png");
        addChild(serious);
        addChild(wounded);
    }

    @Override
    public void remove() {
        removeFromTile();
        super.remove();
    }

    private Hospital currentHospital = null;
    private int seriousTreatmentTime = 50;
    private int woundedTreatmentTime = 50;
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
                currentHospital.leavePatient(this);
                currentHospital = null;
                world.removeChild(this);
                world.savedPatientCount++;
            }
        }
    }
}
