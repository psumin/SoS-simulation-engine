package stimulus.MessageStimulus;

import core.World;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class Loss extends Message {

    public Loss(int frame, int endFrame, String sender, String receiver) {
        this.frame = frame;
        this.endFrame = endFrame;
        this.sender = sender;
        this.receiver = receiver;
    }


//    public Delay(World world, int frame, Range tileRange, String fieldName, Object value) {
//        super(world, frame, tileRange, fieldName, value);
//    }
//
//    public Delay(World world, int frame, ArrayList<String> targetNames, String fieldName, Object value) {
//        super(world, frame, targetNames, fieldName, value);
//    }
//
//    public Delay(World world, int frame, String targetName, String fieldName, Object value) {
//        super(world, frame, targetName, fieldName, value);
//    }
}
