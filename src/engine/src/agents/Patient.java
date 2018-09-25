package agents;

import core.*;

public class Patient extends CS {

   public enum Status {
        Wounded, Serious, Dead;

       public static Status random() {
            Status[] values = Status.values();
            return values[GlobalRandom.nextInt(values.length - 1)];
       }
    }

    Status status = Status.Wounded;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;

        wounded.visible(false);
        serious.visible(false);
        switch (status) {
            case Wounded:
                wounded.visible(true);
                break;
            case Serious:
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
}
