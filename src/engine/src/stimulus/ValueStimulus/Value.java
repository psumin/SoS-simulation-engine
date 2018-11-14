package stimulus.ValueStimulus;

import core.SoSObject;
import core.Tile;
import core.World;
import misc.Range;
import stimulus.Reflection;
import stimulus.Stimulus;

import java.util.ArrayList;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class Value extends Stimulus {
    protected Object targetObject;
    protected Range tileRange;
    protected ArrayList<String> targetNames;
    protected String targetName;
    protected String fieldName;
    protected Object value;

    // sight range, communication range
//    public ValueStimulus(World world, int frame, Object targetObject, String fieldName, Object value) {
//        super(world, frame);
//
//        this.targetObject = targetObject;
//        this.fieldName = fieldName;
//        this.value = value;
//    }

    // Tile 범위 설정
    public Value(World world, int frame, Range tileRange, String fieldName, Object value) {
        super(world, frame);

        this.tileRange = tileRange;
        this.fieldName = fieldName;
        this.value = value;
    }

//    // Tile 범위 설정 + targetObject 설정
//    public ValueStimulus(World world, int frame, Object targetObject, Range tileRange, String fieldName, Object value) {
//        super(world, frame);
//
//        this.tileRange = tileRange;
//        this.fieldName = fieldName;
//        this.value = value;
//        this.targetObject = targetObject;
//    }

    // CS 전체
    public Value(World world, int frame, ArrayList<String> targetNames, String fieldName, Object value) {
        super(world, frame);

        this.targetNames = targetNames;
        this.fieldName = fieldName;
        this.value = value;
    }

    // 얘 안쓰이는 것 같은데...???
    public Value(World world, int frame, String targetName, String fieldName, Object value) {
        super(world, frame);

        this.targetName = targetName;
        this.fieldName = fieldName;
        this.value = value;
    }

    @Override
    public void execute() {

        if(targetName != null) {
            setField(targetName);
            return;
        }

        if(targetNames != null) {
            for(String name: targetNames) {
                setField(name);
            }
            return;
        }

        if(tileRange != null) {
            for(int y = tileRange.top; y <= tileRange.bottom; ++y) {
                for(int x = tileRange.left; x <= tileRange.right; ++x) {
                    Tile tile = world.map.getTile(x, y);
                    if(tile != null) {
                        Reflection.setField(tile, fieldName, value);
                    }
                }
            }
            return;
        }

        if(targetObject != null) {
            Reflection.setField(targetObject, fieldName, value);
            return;
        }
    }
    private void setField(String targetName) {
        SoSObject singleTarget = world.findObject(targetName);
        if (singleTarget != null) {
            Reflection.setField(singleTarget, fieldName, value);
        }
    }
}
