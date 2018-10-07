package agents;

import action.ambulanceaction.AmbulanceFree;
import core.*;
import misc.Position;

import java.util.ArrayList;

public class Ambulance extends CS{
    public Ambulance(World world, String name) {
        super(world, name);
        router = world.router;
        addChild(new ImageObject("src/engine/resources/ambulance.png"));

        currentAction = new AmbulanceFree(this);

        moveDelay = 0;
        frameCounter = moveDelay;
    }
}
