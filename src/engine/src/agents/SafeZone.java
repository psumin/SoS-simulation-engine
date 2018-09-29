package agents;

import core.ImageObject;
import core.TextObject;
import core.World;
import misc.Position;

import java.awt.*;
import java.util.ArrayList;

public class SafeZone extends CS {

    public ArrayList<Patient> patients = new ArrayList<>();

    TextObject textObject = new TextObject();

    int scale = 5;

    public SafeZone(World world, String name) {
        super(world, name);
        addChild(new ImageObject("src/engine/resources/safezone.png", scale));
        addChild(textObject);
        textObject.fontColor = Color.red;

//        Position[] positions = new Position[]{
//                new Position(-1, -1),
//                new Position(0, -1),
//                new Position(1, -1),
//
//                new Position(-1, 0),
//                new Position(0, 0),
//                new Position(1, 0),
//
//                new Position(-1, 1),
//                new Position(0, 1),
//                new Position(1, 1),
//        };
//        for(int i = 0; i < 9; ++i) {
//            SoSObject object = new ImageObject("src/engine/resources/safezone.png");
//            object.setPosition(positions[i]);
//            addChild(object);
//        }
    }

    public boolean isSafeZone(Position position) {
        return isSafeZone(position.x, position.y);
    }

    public boolean isSafeZone(int x, int y) {
        int distanceX = Math.abs(position.x - x);
        int distanceY = Math.abs(position.y - y);

        return distanceX <= (scale / 2) && distanceY <= (scale / 2);
    }

    @Override
    public void onUpdate() {
        textObject.text = "" + patients.size();
    }
}
