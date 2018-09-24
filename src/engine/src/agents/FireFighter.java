package agents;

import action.Action;
import action.MoveTo;
import core.*;
import misc.Position;
import misc.Time;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public class FireFighter extends SoSObject {

    World world;
    Map localMap;
    Queue<Tile> unVisited;

    public FireFighter(World world, String name) {
        super(name);
        this.world = world;
        addChild(new ImageObject("src/engine/resources/ff30x30.png"));

        localMap = new Map();
        //LinkedList<Tile> temp= new LinkedList<>(localMap.getTiles());
        LinkedList<Tile> temp= new LinkedList<>(world.getMap().getTiles());
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
        if(msg.startsWith("action complete")) {
            search();
        }
    }

    public void search() {
        while(true) {
            if(unVisited.isEmpty()) break;
            Tile tile = unVisited.poll();
            if(tile.isVisited()) continue;

            new MoveTo(this, tile.position);
            break;
        }
    }

    @Override
    public void setPosition(int _x, int _y) {
        this.position.set(_x, _y);
        for(int y = position.y - 1; y <= position.y + 1; ++y) {
            for(int x = position.x - 1; x <= position.x + 1; ++x) {
                localMap.visited(x, y, true);
                world.getMap().visited(x, y, true);
            }
        }
    }
}
