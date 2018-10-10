package stimulus;

import core.World;
import misc.Range;

import java.util.ArrayList;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class CommunicationDelayScenario extends ChangeValueScenario {
    public CommunicationDelayScenario(World world, int frame, String fieldName, Object value) {
        super(world, frame, world.router, fieldName, value);
    }

//    public CommunicationDelayScenario(World world, int frame, Range tileRange, String fieldName, Object value) {
//        super(world, frame, tileRange, fieldName, value);
//    }
//
//    public CommunicationDelayScenario(World world, int frame, ArrayList<String> targetNames, String fieldName, Object value) {
//        super(world, frame, targetNames, fieldName, value);
//    }
//
//    public CommunicationDelayScenario(World world, int frame, String targetName, String fieldName, Object value) {
//        super(world, frame, targetName, fieldName, value);
//    }
}
