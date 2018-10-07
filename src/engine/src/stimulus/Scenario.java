package stimulus;

import core.SoSObject;
import core.Tile;
import core.World;
import misc.Range;

import java.lang.reflect.Field;
import java.util.ArrayList;

public abstract class Scenario {

    protected World world;
    public int frame;

    public Scenario(World world, int frame) {
        this.world = world;
        this.frame = frame;
    }

    public abstract void execute();
}
