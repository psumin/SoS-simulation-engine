package action;

import core.SoSObject;

public class FrameDelay extends Action {
    int frame;
    public FrameDelay(SoSObject target, int frame) {
        super(target);
        this.frame = frame + 1;
    }

    @Override
    public void onUpdate() {
        frame--;
        if(frame == 0) {
            complete("delay complete");
        }
    }
}
