import misc.Position;
import publics.SoSObject;

import java.util.LinkedList;

public class Tile {

    Map map;
    Position position;
    LinkedList<SoSObject> objects = new LinkedList<>();

    public Tile(Map map) {
        this.map = map;
    }
    public Tile(Map map, int x, int y) {
        this.map = map;
        position.x = x;
        position.y = y;
    }
    public Tile(Position position) {
        this.position = position;
    }

//    public void init() {
//
//    }

    // 데이터 정리
    public void clear() {
        map = null;
        if(objects != null) {
            objects.clear();
        }
    }

    public void add(SoSObject object) {
        objects.add(object);
    }
    public void remove(SoSObject object) {
        objects.remove(object);
    }

    // 왼쪽 타일
    public Tile getLeft() {
        return map.getTile(position.x - 1, position.y);
    }
    // 오른쪽 타일
    public Tile getRight() {
        return map.getTile(position.x + 1, position.y);
    }

    // 위쪽 타일
    public Tile getUp() {
        return map.getTile(position.x, position.y - 1);
    }
    // 아래쪽 타일
    public Tile getDown() {
        return map.getTile(position.x, position.y + 1);
    }
}
