package stimulus.MessageStimulus;


import core.World;
import stimulus.Reflection;
import stimulus.Stimulus;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class Message extends Stimulus {
    protected Object targetObject;
    protected String fieldName;
    protected Object value;

    public int frame;
    public int endFrame;
    public String sender;
    public String receiver;

    public Message() {

    }

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
