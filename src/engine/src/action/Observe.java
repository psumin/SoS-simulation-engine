package action;

import agents.FireFighter;
import agents.Patient;
import core.SoSObject;
import core.Tile;

import java.util.ArrayList;
import java.util.function.Consumer;

public class Observe extends Action {

    FireFighter fireFighter;
    public Consumer<ArrayList<Patient>> onFoundPatients;

    public Observe(FireFighter target) {
        super(target);
        fireFighter = target;
    }

    @Override
    public void start() {
        if(parentAction != null) {
            parentAction.addChild(this);
        } else {
            target.addChild(this);
        }
        //onUpdate();
    }

    @Override
    public void stop() {
        if(parentAction != null) {
            parentAction.removeChild(this);
        } else {
            target.removeChild(this);
        }
    }

    @Override
    public void onUpdate() {
        ArrayList<Patient> foundPatients = new ArrayList<>();
        for(int y = target.position.y - fireFighter.getSightRange() / 2; y <= target.position.y + fireFighter.getSightRange() / 2; ++y) {
            for(int x = target.position.x - fireFighter.getSightRange() / 2; x <= target.position.x + fireFighter.getSightRange() / 2; ++x) {

                Tile worldTile = fireFighter.getWorld().getMap().getTile(x, y);
                if(worldTile != null && worldTile.isVisited() == false) {
                    ArrayList<SoSObject> objects = worldTile.getObjects();
                    objects.forEach(obj -> {
                        if(obj instanceof Patient) {
                            foundPatients.add((Patient)obj);
                            //fireFighter.getPatientsMemory().add((Patient) obj);
                        }
                    });
                }
                fireFighter.getLocalMap().visited(x, y, true);
                fireFighter.getWorld().getMap().visited(x, y, true);
            }
        }
        fireFighter.getPatientsMemory().addAll(foundPatients);
        if(foundPatients.isEmpty() == false) {
            onFoundPatients.accept(foundPatients);
        }
    }
}
