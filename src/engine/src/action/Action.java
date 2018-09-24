package action;

import core.SoSObject;

public class Action extends SoSObject {

    protected Action parentAction;
    protected SoSObject target;
    protected boolean _complete = false;

    public Action(SoSObject target) {
        this.target = target;
        target.addChild(this);
    }

    public void complete() {
        complete("action complete");
    }

    public void complete(String msg) {

        if(parentAction != null) {
            parentAction.sendMessage(msg, null);
        } else {
            target.sendMessage(msg, null);
        }
        parentAction = null;
        clear();
    }
}
