package agents;

import action.Action;
import action.firefighteraction.FireFighterCollaborativeAction;
import action.firefighteraction.FireFighterVirtualAction;
import action.firefighteraction.FireFighterAction;
import core.*;
import core.Map;

import java.awt.*;
import java.util.*;

public class FireFighter extends CS {

    public World world;
    public Map individualMap;

    public Queue<Tile> unvisitedTiles;
    public LinkedList<Patient> patientsMemory = new LinkedList<>();

    public Action currentAction;

    int sightRange = 3;
    public FireFighter(World world, String name) {
        super(world, name);
        this.world = world;
        addChild(new ImageObject("src/engine/resources/ff30x30.png"));

        individualMap = new Map();
        LinkedList<Tile> temp = new LinkedList<>(individualMap.getTiles());
        //LinkedList<Tile> temp= new LinkedList<>(world.getMap().getTiles());

//        for(int i = 0; i < temp.size() * 3; ++i) {
//            int leftIndex = GlobalRandom.nextInt(temp.size());
//            int rightIndex = GlobalRandom.nextInt(temp.size());
//            Tile swapTemp = temp.get(leftIndex);
//            temp.set(leftIndex, temp.get(rightIndex));
//            temp.set(rightIndex, swapTemp);
//        }

        Collections.shuffle(temp);

        unvisitedTiles = temp;

        //Search search = new Search(this);
        //search.start();
        //new SearchLegacy(this);
        //search();
        //currentAction = new Search(this);
        //currentAction = new FireFighterVirtualAction(this);
        currentAction = new FireFighterCollaborativeAction(this);
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
//                getIndividualMap().visited(x, y, true);
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

    public Queue<Tile> getUnvisitedTiles() {
        return unvisitedTiles;
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

    public Map getIndividualMap() {
        return individualMap;
    }


    public void changeAction(FireFighterAction action) {
        currentAction.stop();
        currentAction = action;
        currentAction.start();
        currentAction.update();
    }

    @Override
    public void onRender(Graphics2D graphics2D) {

        graphics2D.setColor(Color.red);
        graphics2D.setFont(new Font("default", Font.BOLD, 16));
        graphics2D.drawChars(name.toCharArray(), 0, name.length(), 0, 0);

    }

    public void removeFromTile() {
        world.getMap().removeObject(position.x, position.y, this);
    }
    public void addToTile() {
        world.getMap().addObject(position.x, position.y, this);
    }

    @Override
    public void recvMsg(Msg msg) {
        currentAction.recvMsg(msg);
    }
}
