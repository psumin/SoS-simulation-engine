package core;

import misc.Position;
import misc.Size;

import java.awt.*;
import java.util.ArrayList;

public class World extends SoSObject{

    public static final Size mapSize = new Size(20, 20);
    public static final Size tileSize = new Size(30, 30);
    ArrayList<Tile> tiles = new ArrayList<>(mapSize.width * mapSize.height);

    public World() {
        createTiles();
    }

    private void createTiles() {
        for(int y = 0; y < mapSize.height; ++y) {
            for(int x = 0; x < mapSize.width; ++x) {
                Tile tile = new Tile(new Position(x, y), tileSize);
                tiles.add(tile);
                addChild(tile);
            }
        }
    }

    @Override
    public void onRender(Graphics2D g) {
        Rectangle rect = g.getDeviceConfiguration().getBounds();
        g.setColor(new Color(100, 100, 100));
        g.fillRect(rect.x, rect.y, rect.width, rect.height);
    }

}
