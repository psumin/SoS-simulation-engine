package agents;

import core.Map;
import core.SoSObject;
import core.Tile;
import javafx.geometry.Pos;
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
import java.util.Collections;
import java.util.Random;
import java.util.function.Function;
import java.util.Queue;

public class FireFighter extends SoSObject<FireFighter> {

    Map localMap;

    ArrayList<Tile> unVisitedTiles;

    Size visualRange = new Size(1, 1);
    State currentState;

    public FireFighter setVisualRange(Size visualRange) {
        this.visualRange = visualRange;
        return this;
    }
    public Region getVisualRegion() {
        int halfWidth = visualRange.width / 2;
        int halfHeight = visualRange.height / 2;

        // from
        Position from = new Position(position.x - halfWidth, position.y - halfHeight);
        Position to = new Position(position.x + halfWidth, position.y + halfHeight);
        return new Region(from, to);
    }

    public FireFighter() {
    }

    @Override
    public FireFighter init() {
        loadImage("src/engine/resources/ff30x30.png");
        localMap = new Map().init(Map.global.getMapSize());
        //localMap = Map.global;

        unVisitedTiles = localMap.getTiles();
        Collections.shuffle(unVisitedTiles);

        return this;
    }

    private void setVisitedToTile() {
        ArrayList<Tile> globalTiles = Map.global.getTile(getVisualRegion());
        globalTiles.stream().forEach(tile -> tile.visited(true));
        ArrayList<Tile> tiles = localMap.getTile(getVisualRegion());
        tiles.stream().forEach(tile -> tile.visited(true));
    }

    int frameCount = 0;
    @Override
    public void onUpdate() {

        if(currentState != null) {
            if(currentState.isDone()) {
                currentState = null;
                return;
            }
            currentState.update();
        } else {
            while(unVisitedTiles.size() > 0) {
                Tile nextTile = unVisitedTiles.get(0);
                unVisitedTiles.remove(0);
                if(nextTile.isVisited()) {
                    continue;
                }
                currentState = new MoveTo(this, nextTile.getPosition());
                currentState.update();
                break;
            }
        }
    }

    @Override
    public FireFighter clear() {
        localMap = null;
        return this;
    }

    private void search() {

    }

    public class State {

        protected FireFighter fireFighter;
        protected boolean _isDone = false;

        public State(FireFighter fireFighter) {
            this.fireFighter = fireFighter;
        }

        public void update() { }

        public boolean isDone() {
            return _isDone;
        }
    }

    public class MoveTo extends State {
        Position dest;
        public MoveTo(FireFighter fireFighter, Position pos) {
            super(fireFighter);
            dest = pos;
        }

        public void update() {
            int distanceX = dest.x - fireFighter.position.x;
            int distanceY = dest.y - fireFighter.position.y;

            if(distanceX == 0 && distanceY == 0) {
                _isDone = true;
            }

            if(_isDone) {
                return;
            }

            if(Math.abs(distanceX) > Math.abs(distanceY)) {
                fireFighter.setPosition(fireFighter.position.x + distanceX / Math.abs(distanceX), fireFighter.position.y);

            } else {
                fireFighter.setPosition( fireFighter.position.x, fireFighter.position.y + distanceY / Math.abs(distanceY));
            }
        }
    }

    @Override
    public FireFighter setPosition(Position position) {
        return setPosition(position.x, position.y);
    }

    @Override
    public FireFighter setPosition(int x, int y) {
        Tile nextTile = Map.global.getTile(x, y);
        if(nextTile == null)
            return this;

        if(position != null) {
            Tile tile = Map.global.getTile(position);
            if(tile != null) {
                tile.remove(this);
            }
        }
        position = new Position(x, y);
        nextTile.add(this);
        setVisitedToTile();
        return this;
    }
}
