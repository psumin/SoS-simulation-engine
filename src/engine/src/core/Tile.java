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

    BufferedImage image;

    public void init(int x, int y) {
        position = new Position(x, y);
        objects = new ArrayList<>();

        try {
            image = ImageIO.read(new File("src/engine/resources/tile30x30.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        int width = image.getWidth();
        int height = image.getHeight();
        g.drawImage(image, position.x * width, position.y * height, null);
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
}
