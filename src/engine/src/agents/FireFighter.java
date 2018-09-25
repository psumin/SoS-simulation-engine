package agents;

import action.Action;
import action.MoveTo;
import action.Search;
import action.legacy.SearchLegacy;
import core.*;
import core.Map;

import java.util.*;

public class FireFighter extends CS {

    World world;
    Map localMap;
    Queue<Tile> unVisited;
    LinkedList<Patient> patientsMemory = new LinkedList<>();

    int sightRange = 3;

    Search search;

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
        search = new Search(this);
    }

    boolean isFirstUpdate = true;
    @Override
    public void onUpdate() {
        if(isFirstUpdate) {
            isFirstUpdate = false;
            search.start();
        }
    }

    @Override
    public void clear() {
        world = null;
    }

//    @Override
//    public void sendMessage(String msg, Object data) {
//        if(msg.startsWith("action complete")) {
//            search();
//        }
//    }
//
//    public void search() {
//        while(true) {
//            if(unVisited.isEmpty()) break;
//            Tile tile = unVisited.poll();
//            if(tile.isVisited()) continue;
//
//            Action moveTo = new MoveTo(this, tile.position);
//            moveTo.onComplete = () -> {
//                search();
//            };
//            moveTo.start();
//            break;
//        }
//    }

    @Override
    public void setPosition(int _x, int _y) {
        super.setPosition(_x, _y);
        //this.position.set(_x, _y);

        ArrayList<Patient> foundPatients = new ArrayList<>();
        for(int y = position.y - getSightRange() / 2; y <= position.y + getSightRange() / 2; ++y) {
            for(int x = position.x - getSightRange() / 2; x <= position.x + getSightRange() / 2; ++x) {

                Tile worldTile = getWorld().getMap().getTile(x, y);
                if(worldTile != null && worldTile.isVisited() == false) {
                    ArrayList<SoSObject> objects = worldTile.getObjects();
                    objects.forEach(obj -> {
                        if(obj instanceof Patient) {
                            foundPatients.add((Patient)obj);
                            //fireFighter.getPatientsMemory().add((Patient) obj);
                        }
                    });
                }
                getLocalMap().visited(x, y, true);
                getWorld().getMap().visited(x, y, true);
            }
        }
        getPatientsMemory().addAll(foundPatients);

        if(foundPatients.isEmpty() == false) {
            search.onFoundPatients(foundPatients);
            //onFoundPatients.accept(foundPatients);
        }
    }

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
}
