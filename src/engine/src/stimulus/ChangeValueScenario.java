package stimulus;

import core.SoSObject;
import core.Tile;
import core.World;
import misc.Range;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class ChangeValueScenario extends Scenario {
    protected Object targetObject;
    protected Range tileRange;
    protected ArrayList<String> targetNames;
    protected String targetName;
    protected String fieldName;
    protected Object value;

    //
    public ChangeValueScenario(World world, int frame, Object targetObject, String fieldName, Object value) {
        super(world, frame);

        this.targetObject = targetObject;
        this.fieldName = fieldName;
        this.value = value;
    }

    // Tile 범위 설정
    public ChangeValueScenario(World world, int frame, Range tileRange, String fieldName, Object value) {
        super(world, frame);

        this.tileRange = tileRange;
        this.fieldName = fieldName;
        this.value = value;
    }

//    // Tile 범위 설정 + targetObject 설정
//    public ChangeValueScenario(World world, int frame, Object targetObject, Range tileRange, String fieldName, Object value) {
//        super(world, frame);
//
//        this.tileRange = tileRange;
//        this.fieldName = fieldName;
//        this.value = value;
//        this.targetObject = targetObject;
//    }

    // CS 전체
    public ChangeValueScenario(World world, int frame, ArrayList<String> targetNames, String fieldName, Object value) {
        super(world, frame);

        this.targetNames = targetNames;
        this.fieldName = fieldName;
        this.value = value;
    }

    public ChangeValueScenario(World world, int frame, String targetName, String fieldName, Object value) {
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
                        setField(tile, fieldName, value);
                    }
                }
            }
            return;
        }

        if(targetObject != null) {
            setField(targetObject, fieldName, value);
            return;
        }
    }
    private void setField(String targetName) {
        SoSObject singleTarget = world.findObject(targetName);
        if (singleTarget != null) {
            setField(singleTarget, fieldName, value);
        }
    }


    public static boolean setField(Object targetObject, String fieldName, Object fieldValue) {
        Field field;
        try {
            field = targetObject.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            field = null;
        }
        Class superClass = targetObject.getClass().getSuperclass();
        while (field == null && superClass != null) {
            try {
                field = superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                superClass = superClass.getSuperclass();
            }
        }
        if (field == null) {
            return false;
        }
        field.setAccessible(true);
        try {
            field.set(targetObject, fieldValue);
            return true;
        } catch (IllegalAccessException e) {
            return false;
        }
    }
}
