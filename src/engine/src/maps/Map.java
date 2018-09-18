package maps;

import misc.Position;
import misc.Size;
import misc.SoSObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Map extends SoSObject {

    Size mapSize;
    Size tileSize = new Size(10, 10);
    BufferedImage image;

    Tile[][] tiles;

    public Map(Size mapSize) {
        setMapSize(mapSize);
        setName("Map");
        try {
            image = ImageIO.read(new File("src/engine/resources/tile.png"));
        } catch(IOException e) {

        }
    }

    public void setMapSize(Size mapSize) {
        this.mapSize = mapSize;
        clear();
        init();
    }

    @Override
    public void init() {
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
            tiles = null;
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


    @Override
    public void draw(Graphics2D g) {
        g.translate(50, 50);
        for(int y = 0; y < mapSize.height; ++y) {
            for(int x = 0; x < mapSize.width; ++x) {
                g.drawImage(image, x * tileSize.width, y * tileSize.height, null);
            }
        }
    }

    @Override
    public void update() {
    }
}
