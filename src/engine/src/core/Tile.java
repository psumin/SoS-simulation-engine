package core;

import agents.FireFighter;
import agents.Patient;
import misc.Position;
import misc.Size;

import java.util.ArrayList;

public class Tile extends SoSObject {

    SoSObject light;
    SoSObject dark;

    public final ArrayList<Patient> patients = new ArrayList<>();
    public final ArrayList<FireFighter> fireFighters = new ArrayList<>();
    //ArrayList<SoSObject> objects = new ArrayList<>();

    boolean _visited = false;
    public void visited(boolean _visited) {
        this._visited = _visited;

        light.visible(_visited);
        dark.visible(!_visited);
    }
    public boolean isVisited() {
        return _visited;
    }

    public Tile(Position position) {

        setPosition(position);

        light = new ImageObject("src/engine/resources/tile30x30.png");
        dark = new ImageObject("src/engine/resources/tile_dark30x30.png");

        addChild(light);
        addChild(dark);
    }


    public void add(Patient patient) {

        patients.remove(patient);
        patients.add(patient);
        //objects.remove(object);
        //objects.add(object);
    }
    public void add(FireFighter fireFighter) {

        fireFighters.remove(fireFighter);
        fireFighters.add(fireFighter);
    }

    public void remove(Patient patient)  {

        patients.remove(patient);
    }
    public void remove(FireFighter fireFighter) {
        fireFighters.remove(fireFighter);
    }

    public boolean contain(Patient patient) {
        return patients.contains(patient);
//        for(Patient element: patients) {
//            if(element == patient) {
//                return true;
//            }
//        }
//        return false;
    }
}
