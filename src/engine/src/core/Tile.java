package core;

import misc.Position;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Tile {

    Position position;
    ArrayList<SoSObject> objects;

    BufferedImage lightImage;
    BufferedImage darkImage;

    boolean _visited = false;
    public Tile visited(boolean _visited) {
        this._visited = _visited;
        return this;
    }

    public boolean isVisited() {
        return  _visited;
    }

    public Tile init(int x, int y) {
        position = new Position(x, y);
        objects = new ArrayList<>();

        try {
            lightImage = ImageIO.read(new File("src/engine/resources/tile30x30.png"));
            darkImage = ImageIO.read(new File("src/engine/resources/tile_dark30x30.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public void clear() {
        objects = null;
    }

    public void add(SoSObject object) {
        objects.add(object);
    }
    public void remove(SoSObject object) {
        objects.remove(object);
    }

    public void render(Graphics2D g) {
        int width = lightImage.getWidth();
        int height = lightImage.getHeight();
        if(_visited) {
            g.drawImage(lightImage, position.x * width, position.y * height, null);
        } else {
            //g.drawImage(lightImage, position.x * width, position.y * height, null);
            g.drawImage(darkImage, position.x * width, position.y * height, null);
        }
    }


    public Tile left() {
        return Map.global.getTile(position.left());
    }
    public Tile right() {
        return Map.global.getTile(position.right());
    }
    public Tile up() {
        return Map.global.getTile(position.up());
    }
    public Tile down() {
        return Map.global.getTile(position.down());
    }

    public Position getPosition() {
        return position;
    }
}
