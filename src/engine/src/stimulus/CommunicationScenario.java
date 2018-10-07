package stimulus;

import core.World;
import misc.Range;

import java.util.ArrayList;

public class CommunicationScenario extends ChangeValueScenario {
    public CommunicationScenario(World world, int frame, String fieldName, Object value) {
        super(world, frame, world.router, fieldName, value);
    }

//    public CommunicationScenario(World world, int frame, Range tileRange, String fieldName, Object value) {
//        super(world, frame, tileRange, fieldName, value);
//    }
//
//    public CommunicationScenario(World world, int frame, ArrayList<String> targetNames, String fieldName, Object value) {
//        super(world, frame, targetNames, fieldName, value);
//    }
//
//    public CommunicationScenario(World world, int frame, String targetName, String fieldName, Object value) {
//        super(world, frame, targetName, fieldName, value);
//    }
}
