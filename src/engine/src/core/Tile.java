package core;

import misc.Position;
import misc.Size;

public class Tile extends SoSObject {

    SoSObject light;
    SoSObject dark;

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



}
