package stimulus.MessageStimulus;


import core.SoSObject;
import core.World;
import stimulus.Reflection;
import stimulus.Scenario;

import java.lang.reflect.Field;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class Message extends Scenario {
    protected Object targetObject;
    protected String fieldName;
    protected Object value;

    public Message(World world, int frame, Object targetObject, String fieldName, Object value) {
        super(world, frame);

        this.targetObject = targetObject;
        this.fieldName = fieldName;
        this.value = value;
    }

    @Override
    public void execute() {
        if (targetObject != null) {
            Reflection.setField(targetObject, fieldName, value);
            return;
        }
    }
}
