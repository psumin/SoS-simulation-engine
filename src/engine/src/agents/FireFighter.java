package agents;

import action.Action;
import action.MoveTo;
import core.ImageObject;
import core.SoSObject;
import core.Tile;
import core.World;
import misc.Position;
import misc.Time;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public class FireFighter extends SoSObject {

    World world;
    ArrayList<Tile> localMap;
    Queue<Tile> unVisited;

    public FireFighter(World world, String name) {
        super(name);
        this.world = world;
        addChild(new ImageObject("src/engine/resources/ff30x30.png"));

        localMap = world.createTiles();
        LinkedList<Tile> temp= new LinkedList<>(localMap);
        Collections.shuffle(temp);
        unVisited = temp;

        search();
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void clear() {
        world = null;
    }

    @Override
    public void sendMessage(String msg, Object data) {
        if(getName().startsWith("FireFighter10")) {
            System.out.println("completed: " + unVisited.size());
        }
        if(msg.startsWith("action complete")) {
            search();
        }
    }

    public void search() {
        //while(unVisited.isEmpty() == false) {
        if(getName().startsWith("FireFighter10")) {
            System.out.println("unvisited count: " + unVisited.size());
        }
        while(true) {
            if(unVisited.isEmpty()) break;
            Tile tile = unVisited.poll();
            if(tile.isVisited()) continue;

            new MoveTo(this, tile.position);
            break;
        }
    }
}
