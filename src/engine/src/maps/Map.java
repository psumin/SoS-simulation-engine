import misc.Position;
import misc.Size;

public class Map {

    Size mapSize;
    Size tileSize = new Size(10, 10);

    Tile[][] tiles;

    public Map(Size mapSize) {
        init(mapSize);
    }

    public void init(Size mapSize) {
        clear();

        this.mapSize = mapSize;
        tiles = new Tile[mapSize.height][mapSize.width];
        for(int y = 0; y < mapSize.height; ++y) {
            for(int x = 0; x < mapSize.width; ++x) {
                tiles[y][x] = new Tile(this, x, y);
            }
        }
    }

    // 데이터 정리
    public void clear() {
        if(tiles != null) {
            for(int y = 0; y < mapSize.height; ++y) {
                for(int x = 0; x < mapSize.width; ++x) {
                    tiles[y][x].clear();
                }
            }
        }
    }

    public Tile getTile(int x, int y) {
        if(x < 0 || x >= mapSize.width) return null;
        if(y < 0 || y >= mapSize.height) return null;

        return tiles[y][x];
    }

    public Tile getTile(Position position) {
        return getTile(position.x, position.y);
    }


}
