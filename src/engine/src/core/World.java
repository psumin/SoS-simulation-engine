package core;

import agents.FireFighter;
import agents.Patient;

import java.awt.*;

public class World {

    Map map;

    public void init() {
        map = new Map();
        map.init(20, 20);

        SoSObject.initAll();


        FireFighter ff = new FireFighter();
        ff.init();
        ff.setPosition(1, 1);

        Patient p = new Patient();
        p.init();
        p.setPosition(0, 0);
    }

    public void update() {
        SoSObject.updateAll();
    }

    public void render(Graphics2D g) {
        map.render(g);
        SoSObject.renderAll(g);
    }
    public void clear() {
        SoSObject.clearAll();
        map.clear();
    }
}
