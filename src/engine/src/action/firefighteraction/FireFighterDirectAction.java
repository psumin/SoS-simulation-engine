
package action.firefighteraction;

import agents.FireFighter;
import agents.Patient;
import core.*;
import misc.Position;
import misc.Range;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public class FireFighterDirectAction extends FireFighterAction {

    public FireFighterDirectAction(FireFighter fireFighter) {
        super(fireFighter);
    }

    @Override
    public void onUpdate() {
    }

    // 인자로 넘겨준 목적지까지 이동
    // 한 프레임에 한 칸 이동
    // return true: 목적지에 도착했다
    // return false: 아직 도착하지 못했다
    boolean moveToUpdate(Position dest) {
        int distanceX = dest.x - fireFighter.position.x;
        int distanceY = dest.y - fireFighter.position.y;

        if(distanceX == 0 && distanceY == 0) {
            return true;
        }

        if(Math.abs(distanceX) > Math.abs(distanceY)) {
            fireFighter.setPosition(fireFighter.position.x + distanceX / Math.abs(distanceX), fireFighter.position.y);
        } else {
            fireFighter.setPosition( fireFighter.position.x, fireFighter.position.y + distanceY / Math.abs(distanceY));
        }
        return false;
    }
}
