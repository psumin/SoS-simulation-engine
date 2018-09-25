package action;

import agents.CS;
import agents.FireFighter;
import agents.Patient;
import core.SoSObject;
import core.Tile;
import misc.Position;

import java.util.ArrayList;
import java.util.function.Consumer;

public class MoveTo extends Action {

    Position dest;
    FireFighter fireFighter;

    public MoveTo(CS target, Position dest) {
        super(target);
        if(target instanceof FireFighter) {
            fireFighter = (FireFighter) target;
        }
        this.dest = dest;
    }

    public void setDest(Position dest) {
        this.dest = dest;
    }

    @Override
    public void start() {
        if(parentAction != null) {
            parentAction.addChild(this);
        } else {
            target.addChild(this);
        }
    }

    @Override
    public void stop() {
        if(parentAction != null) {
            parentAction.removeChild(this);
        } else {
            target.removeChild(this);
        }
    }

    @Override
    public void onUpdate() {
        int distanceX = dest.x - target.position.x;
        int distanceY = dest.y - target.position.y;

        if(distanceX == 0 && distanceY == 0) {
            complete();
            return;
        }

        if(Math.abs(distanceX) > Math.abs(distanceY)) {
            target.setPosition(target.position.x + distanceX / Math.abs(distanceX), target.position.y);
        } else {
            target.setPosition( target.position.x, target.position.y + distanceY / Math.abs(distanceY));
        }
    }
}
