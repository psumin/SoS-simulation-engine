package action;

import agents.CS;
import core.MsgRouter;
import core.SoSObject;
import core.World;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class Action extends SoSObject {

    protected World world;
    protected CS target;
    protected Action parentAction;
    protected MsgRouter router;
    public Func onComplete;

    public Action(CS target) {
        this.target = target;
        world = target.world;
        router = world.router;
    }

    public void setParentAction(Action parentAction) {
        this.parentAction = parentAction;
    }

    public void start() {
        target.addChild(this);
    }
    public void stop() {
        target.removeChild(this);
    }

    public void complete() {
        if(onComplete != null) {
            onComplete.invoke();
        }
        stop();
    }
}
