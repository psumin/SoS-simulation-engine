package agents;

import core.Map;
import core.MsgRouter;
import core.SoSObject;
import core.World;
import misc.Position;

public class CS extends SoSObject {

    public World world;
    public Map worldMap;
    public MsgRouter router;
    public CS(World world, String name) {
        this.world = world;
        worldMap = world.getMap();
        router = world.router;
        this.name = name;
    }

    public int getDistanceTo(CS other) {
        return Math.abs(other.position.x - position.x)
                + Math.abs(other.position.y - position.y);
    }

    public Position nextPosition(Position destination) {
        int differenceX = destination.x - position.x;
        int differenceY = destination.y - position.y;
        int distantX = Math.abs(differenceX);
        int distantY = Math.abs(differenceY);

        if(distantX + distantY == 0) {
            return null;
        }

        if(distantX > distantY) {
            return new Position(position.x + differenceX / distantX, position.y);
        } else {
            return new Position(position.x, position.y + differenceY / distantY);
        }
    }
}
