package core;

import agents.Patient;
import misc.Position;
import misc.Size;

import java.util.ArrayList;

public class Map extends SoSObject {
    public static final Size mapSize = new Size(65, 65);
    public static final Size tileSize = new Size(15, 15);

    ArrayList<Tile> tiles;
    public Map() {
        tiles = createTiles();
        tiles.forEach(tile -> addChild(tile));
    }

    public static ArrayList<Tile> createTiles() {
        ArrayList<Tile> values = new ArrayList<>(mapSize.width * mapSize.height);
        for(int y = 0; y < mapSize.height; ++y) {
            for(int x = 0; x < mapSize.width; ++x) {
                Tile tile = new Tile(new Position(x, y));
                values.add(tile);
            }
        }
        return values;
    }

    public void visited(int x, int y, boolean _visited) {
        Tile tile = getTile(x, y);
        if(tile != null) {
            tile.visited(_visited);
        }
    }

    public Tile getTile(int x, int y) {
        if(x < 0 || x >= mapSize.width) return null;
        if(y < 0 || y >= mapSize.height) return null;
        return tiles.get(x + y * mapSize.width);
    }

    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    public void addObject(int x, int y, SoSObject object) {
        Tile tile = getTile(x, y);
        if(tile != null) {
            tile.add(object);
        }
    }
    public void removeObject(int x, int y, SoSObject object) {
        Tile tile = getTile(x, y);
        if(tile != null) {
            tile.remove(object);
        }
    }

    public int getUnvisitedTileCount() {
        int count = 0;
        for(Tile tile: tiles) {
            if(tile.isVisited() == false) {
                count++;
            }
        }
        return count;
    }

    public int getPatientCount() {
        int count = 0;
        for(Tile tile: tiles) {
            for(SoSObject object: tile.getObjects()) {
                if(object instanceof Patient) {
                    count++;
                }
            }
        }
        return count;
    }
}
