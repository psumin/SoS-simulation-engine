package agents;

import core.Map;
import core.SoSObject;
import core.Tile;
import misc.Position;
import misc.Region;
import misc.Size;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class FireFighter extends SoSObject<FireFighter> {

    enum Direction {
        None, Left, Up, Right, Down;

        public static Direction fromInteger(int x) {
            return Direction.values()[x];
        }
        public static Direction random() {
            Direction[] values = Direction.values();
            return values[new Random().nextInt(values.length)];
        }
    };

    Map localMap;
    Size visualRange = new Size(1, 1);
    Direction direction = Direction.None;

    public FireFighter setVisualRange(Size visualRange) {
        this.visualRange = visualRange;
        return this;
    }
    public Region getVisualRegion() {
        Region region = new Region(
                new Position(0, 0),
                new Position(visualRange.width - 1, visualRange.height - 1));
        region.move(position);
        return region;
    }

    public FireFighter() {
        direction = Direction.random();
    }

    @Override
    public FireFighter init() {
        loadImage("src/engine/resources/ff30x30.png");
        localMap = new Map().init(Map.global.getMapSize());
        return this;
    }

    private void setVisitedToTile() {
        ArrayList<Tile> tiles = localMap.getTile(getVisualRegion());
        tiles.stream().forEach(tile -> tile.visited(true));
    }

    @Override
    public void onUpdate() {

        if(moveForward() == false) {
            direction = Direction.random();
        } else {
            setVisitedToTile();
        }
    }

    @Override
    public FireFighter clear() {
        localMap = null;
        return this;
    }

    // direction 변수가 SoSObject로 올라가면
    // 이 함수도 올리는게 좋을듯
    public boolean moveForward() {
        Position newPosition = null;
        switch (direction) {
            case None:
                break;
            case Left:
                newPosition = position.left();
                break;
            case Up:
                newPosition = position.up();
                break;
            case Right:
                newPosition = position.right();
                break;
            case Down:
                newPosition = position.down();
                break;
        }
        if(newPosition != null) {
            if(Map.global.getTile(newPosition) != null) {
                setPosition(newPosition);
                return true;
            }
            return false;
        }
        return false;
    }
}
