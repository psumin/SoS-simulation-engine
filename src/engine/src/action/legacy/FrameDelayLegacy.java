package action;

import core.SoSObject;

public class FrameDelayLegacy extends ActionLegacy {
    int frame;
    public FrameDelayLegacy(SoSObject target, int frame) {
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
