package action;

import core.SoSObject;

public class ActionLegacy extends SoSObject {

    protected ActionLegacy parentActionLegacy;
    protected SoSObject target;
    protected boolean _complete = false;

    public ActionLegacy(SoSObject target) {
        this.target = target;
        target.addChild(this);
    }

    public void complete() {
        complete("action complete");
    }

    public void complete(String msg) {

        if(parentActionLegacy != null) {
            parentActionLegacy.sendMessage(msg, null);
        } else {
            target.sendMessage(msg, null);
        }
        parentActionLegacy = null;
        clear();
    }
}
