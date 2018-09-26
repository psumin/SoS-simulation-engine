package agents;

import action.Action;
import action.firefighteraction.Search;
import action.firefighteraction.FireFighterAction;
import core.*;
import core.Map;

import java.util.*;

public class FireFighter extends CS {

    public World world;
    public Map localMap;

    public Queue<Tile> unVisited;
    public LinkedList<Patient> patientsMemory = new LinkedList<>();

    public Action currentAction;

    int sightRange = 3;
    public FireFighter(World world, String name) {
        super(world, name);
        this.world = world;
        addChild(new ImageObject("src/engine/resources/ff30x30.png"));

        localMap = new Map();
        LinkedList<Tile> temp= new LinkedList<>(localMap.getTiles());
        //LinkedList<Tile> temp= new LinkedList<>(world.getMap().getTiles());
        Collections.shuffle(temp);
        unVisited = temp;

        //Search search = new Search(this);
        //search.start();
        //new SearchLegacy(this);
        //search();
        currentAction = new Search(this);
    }

    boolean isFirstUpdate = true;
    @Override
    public void onUpdate() {
        if(isFirstUpdate) {
            isFirstUpdate = false;
            currentAction.start();
        }
    }

    @Override
    public void clear() {
        world = null;
    }
//
//    @Override
//    public void setPosition(int _x, int _y) {
//        super.setPosition(_x, _y);
//        //this.position.set(_x, _y);
//
//        ArrayList<Patient> foundPatients = new ArrayList<>();
//        for(int y = position.y - getSightRange() / 2; y <= position.y + getSightRange() / 2; ++y) {
//            for(int x = position.x - getSightRange() / 2; x <= position.x + getSightRange() / 2; ++x) {
//
//                Tile worldTile = getWorld().getMap().getTile(x, y);
//                if(worldTile != null && worldTile.isVisited() == false) {
//                    ArrayList<SoSObject> objects = worldTile.getObjects();
//                    objects.forEach(obj -> {
//                        if(obj instanceof Patient) {
//                            foundPatients.add((Patient)obj);
//                            //fireFighter.getPatientsMemory().add((Patient) obj);
//                        }
//                    });
//                }
//                getLocalMap().visited(x, y, true);
//                getWorld().getMap().visited(x, y, true);
//            }
//        }
//        getPatientsMemory().addAll(foundPatients);
//
//        if(foundPatients.isEmpty() == false) {
//            search.onFoundPatients(foundPatients);
//            //onFoundPatients.accept(foundPatients);
//        }
//    }

    public Queue<Tile> getUnVisited() {
        return unVisited;
    }

    public LinkedList<Patient> getPatientsMemory() {
        return patientsMemory;
    }

    public int getSightRange() {
        return sightRange;
    }

    public World getWorld() {
        return world;
    }

    public Map getLocalMap() {
        return localMap;
    }


    public void changeAction(FireFighterAction action) {
        currentAction.stop();
        currentAction = action;
        currentAction.start();
    }
}
