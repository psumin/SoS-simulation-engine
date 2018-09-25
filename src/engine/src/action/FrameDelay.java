package action;

import action.legacy.ActionLegacy;
import agents.CS;
import core.SoSObject;

public class FrameDelay extends Action {
    int frame;
    public FrameDelay(CS target, int frame) {
        super(target);
        this.frame = frame + 1;
    }

    @Override
    public void onUpdate() {
        frame--;
        if(frame == 0) {
            complete();
        }
    }
}
