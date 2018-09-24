package core;

import agents.FireFighter;
import agents.Patient;
import misc.Position;
import misc.Size;

import java.awt.*;
import java.util.ArrayList;

public class World extends SoSObject{

    public static final Size mapSize = new Size(20, 20);
    public static final Size tileSize = new Size(30, 30);
    public static final int maxPatient = 10;
    public static final int maxFireFighter = 10;

    ArrayList<Tile> tiles = new ArrayList<>(mapSize.width * mapSize.height);

    public World() {
        tiles = createTiles();
        tiles.forEach(tile -> addChild(tile));

        createPatients();
        createFireFighters();
    }

    public ArrayList<Tile> createTiles() {
        ArrayList<Tile> values = new ArrayList<>();
        for(int y = 0; y < mapSize.height; ++y) {
            for(int x = 0; x < mapSize.width; ++x) {
                Tile tile = new Tile(new Position(x, y));
                values.add(tile);
            }
        }
        return values;
    }

    private void createPatients() {
        for (int i = 0; i < maxPatient; i++) {
            Patient patient = new Patient();
            Position randomPosition = GlobalRandom.nextPosition(mapSize.width, mapSize.height);
            patient.position.set(randomPosition.x, randomPosition.y);
            addChild(patient);
        }
    }

    private void createFireFighters() {
        for (int i = 0; i < maxFireFighter; i++) {
            FireFighter ff = new FireFighter(this, "FireFighter" + (i + 1));
            addChild(ff);
        }
    }

    @Override
    public void onRender(Graphics2D g) {
        Rectangle rect = g.getDeviceConfiguration().getBounds();
        g.setColor(new Color(100, 100, 100));
        g.fillRect(rect.x, rect.y, rect.width, rect.height);
    }
}
