package core;

import misc.Position;
import misc.Size;

import java.util.ArrayList;

public class Tile extends SoSObject {

    SoSObject light;
    SoSObject dark;

    ArrayList<SoSObject> objects = new ArrayList<>();

    boolean _visited = false;
    public void visited(boolean _visited) {
        this._visited = _visited;

        light.visible(_visited);
        dark.visible(!_visited);
    }
    public boolean isVisited() {
        return _visited;
    }

    public Tile(Position position) {

        setPosition(position);

        light = new ImageObject("src/engine/resources/tile30x30.png");
        dark = new ImageObject("src/engine/resources/tile_dark30x30.png");

        addChild(light);
        addChild(dark);
    }


    public void add(SoSObject object) {
        objects.add(object);
    }
    public void remove(SoSObject object) {
        objects.remove(object);
    }

    public boolean contain(SoSObject object) {
        for(SoSObject obj: objects) {
            if(obj == object) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<SoSObject> getObjects() {
        return objects;
    }
}
