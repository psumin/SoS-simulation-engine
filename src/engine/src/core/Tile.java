package core;

import agents.FireFighter;
import agents.Patient;
import misc.Position;
import misc.Size;

import java.awt.*;
import java.util.ArrayList;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class Tile extends SoSObject {

    ImageObject light;        // For visited tiles
    ImageObject dark;         // For unvisited tiles

    // Initial values
    public float moveDelayFactor = 1;
    public float sightRangeFactor = 1;
    public float communicationRangeFactor = 1;

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

        light = new ImageObject("C:/Users/User/IdeaProjects/SoS-simulation-engine/src/engine/resources/tile30x30.png");              // For visited tiles
        dark = new ImageObject("C:/Users/User/IdeaProjects/SoS-simulation-engine/src/engine/resources/tile_dark30x30.png");          // For unvisited tiles
//        light = new ImageObject("src/engine/resources/tile30x30.png");              // For visited tiles
//        dark = new ImageObject("src/engine/resources/tile_dark30x30.png");          // For unvisited tiles

        addChild(light);
        addChild(dark);
    }


    public void add(Patient patient) {                      // Add patient at tile

        patients.remove(patient);
        patients.add(patient);
        //objects.remove(object);
        //objects.add(object);
    }
    public void add(FireFighter fireFighter) {             // Add Fire fighter at tile

        fireFighters.remove(fireFighter);
        fireFighters.add(fireFighter);
    }

    public void remove(Patient patient)  {                  // Remove patient at tile

        patients.remove(patient);
    }
    public void remove(FireFighter fireFighter) {            // Remove Fire fighter at tile
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

    // 속도를 느리게 만들면서 타일의 색깔을 변경 시켜준다.
    // float 값은 9까지 가능.
    @Override
    public void onUpdate() {
        light.setColor(new Color(255, 255 - (int)(255 * (moveDelayFactor - 1) / 10), 255 - (int)(255 * (moveDelayFactor - 1)/ 10)));        // 색깔 변경을 위한 method
    }
}
