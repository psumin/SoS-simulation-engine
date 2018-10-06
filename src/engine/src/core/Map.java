package core;

import agents.FireFighter;
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
            boolean prev =  tile._visited;
            tile.visited(_visited);
            if(prev != _visited) {
                if(_visited) unvisitedTileCount--;
                else unvisitedTileCount++;
            }
        }
    }

    public Tile getTile(int x, int y) {
        if(x < 0 || x >= mapSize.width) return null;
        if(y < 0 || y >= mapSize.height) return null;
        return tiles.get(x + y * mapSize.width);
    }

    public Tile getTile(Position position) {
        return getTile(position.x, position.y);
    }

    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    public void add(FireFighter fireFighter) {
        Tile tile = getTile(fireFighter.position.x, fireFighter.position.y);
        if(tile != null) {
            tile.remove(fireFighter);
            tile.add(fireFighter);
        }
    }
    public void add(Patient patient) {
        Tile tile = getTile(patient.position.x, patient.position.y);
        if(tile != null) {
            tile.remove(patient);
            tile.add(patient);
        }
    }

    public void remove(FireFighter fireFighter) {
        Tile tile = getTile(fireFighter.position.x, fireFighter.position.y);
        if(tile != null) {
            tile.remove(fireFighter);
        }
    }
    public void remove(Patient patient) {
        if(patient != null) {
            Tile tile = getTile(patient.position.x, patient.position.y);
            if(tile != null) {
                tile.remove(patient);
            }
        }
    }


    int unvisitedTileCount = mapSize.width * mapSize.height;
    public int getUnvisitedTileCount() {
        return unvisitedTileCount;
    }
}
