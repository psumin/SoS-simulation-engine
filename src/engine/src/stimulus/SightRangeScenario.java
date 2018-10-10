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

public class SightRangeScenario extends ChangeValueScenario {
    public SightRangeScenario(World world, int frame, Object targetObject, Object value) {
        super(world, frame, targetObject, "defaultSightRange", value);
    }

    public SightRangeScenario(World world, int frame, Range tileRange, Object value) {
        super(world, frame, tileRange, "sightRangeFactor", value);
    }

    public SightRangeScenario(World world, int frame, ArrayList<String> targetNames, Object value) {
        super(world, frame, targetNames, "defaultSightRange", value);
    }

    public SightRangeScenario(World world, int frame, String targetName, Object value) {
        super(world, frame, targetName, "defaultSightRange", value);
    }
}
