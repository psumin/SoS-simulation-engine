package agents;

import core.SoSObject;
import core.Tile;
import core.World;

import java.util.ArrayList;

public class CS extends SoSObject {

    World world;
    public CS(World world, String name) {
        this.world = world;
        setName(name);
    }

    @Override
    public void setPosition(int _x, int _y) {
        world.getMap().removeObject(position.x, position.y, this);
        this.position.set(_x, _y);
        world.getMap().addObject(position.x, position.y, this);
    }


    public int getDistanceTo(CS other) {
        return Math.abs(other.position.x - position.x)
                + Math.abs(other.position.y - position.y);
    }

    public void addToTile() {
        world.getMap().getTile(position.x, position.y).add(this);
    }

    public void removeFromTile() {
        world.getMap().getTile(position.x, position.y).remove(this);
    }
}
