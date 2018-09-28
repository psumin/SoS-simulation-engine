package agents;

import core.*;

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
//            return values[GlobalRandom.nextInt(values.length - 1)];
       }
    }

    Status status = Status.Wounded;
    public FireFighter fireFighter = null;
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
}
