package core;

import misc.Position;
import misc.Region;
import misc.Size;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Map {

    public static Map global = null;

    Size mapSize;
    Tile[][] tiles;

    static void setGlobal(Map map) {
        global = map;
    }

    public Map init(int width, int height) {
        mapSize = new Size(width, height);
        tiles = new Tile[height][width];
        for(int y = 0; y < height; ++y) {
            for(int x = 0; x < width; ++x) {
                Tile tile = new Tile();
                tile.init(x, y);
                tiles[y][x] = tile;
            }
        }
        return this;
    }
    public Map init(Size size) {
        return init(size.width, size.height);
    }

    public void render(Graphics2D g) {
        Arrays.stream(tiles).forEach(row ->
                Arrays.stream(row).forEach(tile ->
                        tile.render(g)));
    }

    public void clear() {
        if(tiles != null) {
            for(int y = 0; y < tiles.length; ++y) {
                for(int x = 0; x < tiles[y].length; ++x) {
                    tiles[y][x].clear();
                }
            }
            tiles = null;
        }
        mapSize = null;
    }

    public Tile getTile(int x, int y) {
        if(x < 0 || x >= mapSize.width)
            return null;
        if(y < 0 || y >= mapSize.height)
            return null;
        return tiles[y][x];
    }

    public Tile getTile(Position position) {
        return getTile(position.x, position.y);
    }

    // 사각 영역 [from, to]
    // from: 왼쪽 위
    // to: 오른쪽 아래
    public ArrayList<Tile> getTile(Position from, Position to) {
        ArrayList<Tile> values = new ArrayList<>();
        for(int y = from.y; y <= to.y; ++y) {
            for(int x = from.x; x <= to.x; ++x) {
                Tile tile = getTile(x, y);
                if(tile != null) {
                    values.add(tile);
                }
            }
        }
        return values;
    }
    public ArrayList<Tile> getTile(Region region) {
        return getTile(region.from, region.to);
    }

    public Size getMapSize() {
        return mapSize;
    }
}
