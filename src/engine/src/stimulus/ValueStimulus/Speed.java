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

public class Speed extends Value {

    // 안쓰임
//    public Speed(World world, int frame, Object targetObject, Object value) {
//        super(world, frame, targetObject, "moveDelay", value);
//    }

    // Tile 범위 설정
    public Speed(World world, int frame, Range tileRange, Object value) {
        super(world, frame, tileRange, "moveDelayFactor", value);
    }

//    // Tile 범위 설정 + targetObject 설정 ==> 안됨
//    public Speed(World world, int frame, Object targetObject, Range tileRange, Object value) {
//        super(world, frame, targetObject, tileRange, "moveDelayFactor", value);
//    }

    // 하나의 CS, 전체 entity
    public Speed(World world, int frame, ArrayList<String> targetNames, Object value) {
        super(world, frame, targetNames, "moveDelay", value);
    }

    // 하나의 entity
    public Speed(World world, int frame, String targetName, Object value) {
        super(world, frame, targetName, "moveDelay", value);
    }
}
