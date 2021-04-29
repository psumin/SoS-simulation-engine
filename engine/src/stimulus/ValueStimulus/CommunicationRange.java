package stimulus.ValueStimulus;

import core.World;
import misc.Range;

import java.util.ArrayList;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class CommunicationRange extends Value {

    // 안쓰는 듯
//    public CommunicationRange(World world, int frame, Object targetObject, Object value) {
//        super(world, frame, targetObject, "communicationRange", value);
//    }

    // Tile 범위 설정
    public CommunicationRange(World world, int frame, Range tileRange, Object value) {
        super(world, frame, tileRange, "communicationRangeFactor", value);
    }

    // 하나의 CS, 전체 entity
    public CommunicationRange(World world, int frame, ArrayList<String> targetNames, Object value) {
        super(world, frame, targetNames, "communicationRange", value);
    }

    // 하나의 entity
    public CommunicationRange(World world, int frame, String targetName, Object value) {
        super(world, frame, targetName, "communicationRange", value);
    }
}