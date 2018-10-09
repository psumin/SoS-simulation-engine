package agents;

import action.Action;
import action.firefighteraction.FireFighterAction;
import core.*;
import misc.Position;

public class CS extends SoSObject {

    public World world;
    public Map worldMap;
    public MsgRouter router;

    public Action currentAction;

    public int totalDistance = 0;

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

//    public void moveTo(Position destination) {
//        Position nextPosition = nextPosition(destination);
//        if(nextPosition != null) {
//            setPosition(nextPosition);
//        }
//    }

    public int moveDelay = 0;
    int frameCounter;

    public void moveTo(Position destination) {
        if(frameCounter <= 0) {
            frameCounter = moveDelay;
            Position nextPosition = nextPosition(destination);
            if(nextPosition != null) {
                if(!isArrivedAt(nextPosition)) {
                    totalDistance++;
                }
                setPosition(nextPosition);

                frameCounter = (int)(moveDelay * worldMap.getTile(position).moveDelayFactor);
            }
        }
        frameCounter--;
    }

    public boolean isArrivedAt(Position position) {
        return this.position.x == position.x && this.position.y == position.y;
    }

    @Override
    public void recvMsg(Msg msg) {
        currentAction.recvMsg(msg);
    }

    public void changeAction(Action action) {
        if(currentAction != null) {
            currentAction.stop();
        }
        currentAction = action;
        currentAction.start();
        //currentAction.update();
    }

    boolean isFirstUpdate = true;
    @Override
    public void onUpdate() {
        if(isFirstUpdate) {
            isFirstUpdate = false;
            if(currentAction != null) {
                currentAction.start();
            }
        }
    }
}
