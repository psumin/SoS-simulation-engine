package core;

import misc.Position;
import misc.Size;

import java.awt.*;
import java.util.Arrays;

public class Map {

    static Map global = null;

    Size mapSize;
    Tile[][] tiles;

    static void setGlobal(Map map) {
        global = map;
    }

    public void init(int width, int height) {
        mapSize = new Size(width, height);
        tiles = new Tile[height][width];
        for(int y = 0; y < height; ++y) {
            for(int x = 0; x < width; ++x) {
                Tile tile = new Tile();
                tile.init(x, y);
                tiles[y][x] = tile;
            }
        }
    }
    public void init(Size size) {
        init(size.width, size.height);
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

    Tile getTile(int x, int y) {
        if(x < 0 || x >= mapSize.width)
            return null;
        if(y < 0 || y >= mapSize.height)
            return null;
        return tiles[y][x];
    }

    Tile getTile(Position position) {
        return getTile(position.x, position.y);
    }
}
