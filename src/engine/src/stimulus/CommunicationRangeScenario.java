package stimulus;

import agents.FireFighter;
import core.World;
import misc.Position;
import misc.Range;

import java.util.ArrayList;

public class CommunicationRangeScenario extends ChangeValueScenario {
    public CommunicationRangeScenario(World world, int frame, Object targetObject, Object value) {
        super(world, frame, targetObject, "communicationRange", value);
    }

    public CommunicationRangeScenario(World world, int frame, Range tileRange, Object value) {
        super(world, frame, tileRange, "communicationRangeFactor", value);
    }

    public CommunicationRangeScenario(World world, int frame, ArrayList<String> targetNames, Object value) {
        super(world, frame, targetNames, "communicationRange", value);
    }

    public CommunicationRangeScenario(World world, int frame, String targetName, Object value) {
        super(world, frame, targetName, "communicationRange", value);
    }
}