package action;

import core.SoSObject;

public class Action extends SoSObject {

    protected SoSObject target;
    protected boolean _complete = false;

    public Action(SoSObject target) {
        this.target = target;
        target.addChild(this);
    }

    public void complete() {
        clear();
        target.sendMessage("action complete", null);
    }
}
