package privates;

import misc.SoSObject;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

public class SoSObjectManager {
    private static SoSObjectManager ourInstance = new SoSObjectManager();
    public static SoSObjectManager getInstance() {
        return ourInstance;
    }

    private SoSObjectManager() {
    }

    Queue<SoSObject> objects = new LinkedList<>();

    public void add(SoSObject object) {
        objects.add(object);
    }
    public void remove(SoSObject object) {
        objects.add(object);
    }

    public void draw(Graphics2D g) {
        objects.add(null);
        while(true) {
            SoSObject object = objects.poll();
            if(object == null) break;
            object.draw(g);
        }
    }
    public void update() {
        objects.add(null);
        while(true) {
            SoSObject object = objects.poll();
            if(object == null) break;
            object.update();
        }
    }
}
