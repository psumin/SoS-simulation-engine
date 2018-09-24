package action;

import core.SoSObject;
import misc.Position;

public class MoveTo extends Action {

    Position dest;

    public MoveTo(SoSObject target, Position dest) {
        super(target);
        this.dest = dest;
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

        if(parentAction != null) {
            parentAction.sendMessage("move", null);
        }
    }
}