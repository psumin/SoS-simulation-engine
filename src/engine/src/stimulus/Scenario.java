package stimulus;

import core.SoSObject;
import core.Tile;
import core.World;
import misc.Range;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Scenario {

    private World world;
    public int frame;

    private Object targetObject;
    private Range tileRange;
    private ArrayList<String> targetNames;
    private String targetName;
    private String fieldName;
    private Object value;

    //
    public Scenario(World world, int frame, Object targetObject, String fieldName, Object value) {
        this.world = world;
        this.frame = frame;

        this.targetObject = targetObject;
        this.fieldName = fieldName;
        this.value = value;
    }

    // Tile 범위 설정
    public Scenario(World world, int frame, Range tileRange, String fieldName, Object value) {
        this.world = world;
        this.frame = frame;

        this.tileRange = tileRange;
        this.fieldName = fieldName;
        this.value = value;
    }

    // CS 전체
    public Scenario(World world, int frame, ArrayList<String> targetNames, String fieldName, Object value) {
        this.world = world;
        this.frame = frame;

        this.targetNames = targetNames;
        this.fieldName = fieldName;
        this.value = value;
    }

    public Scenario(World world, int frame, String targetName, String fieldName, Object value) {
        this.world = world;
        this.frame = frame;

        this.targetName = targetName;
        this.fieldName = fieldName;
        this.value = value;
    }

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
