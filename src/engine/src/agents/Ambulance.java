package agents;

import action.ambulanceaction.AmbulanceFree;
import core.*;
import misc.Position;

import java.awt.*;
import java.util.ArrayList;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class Ambulance extends CS{
    public ImageObject transferImage;
    public ImageObject defaultImage;
    public Ambulance(World world, String name) {
        super(world, name);
        router = world.router;
        transferImage = new ImageObject("src/engine/resources/transfer2.png");
        defaultImage = new ImageObject("src/engine/resources/ambulance1.png");

//        addChild(new ImageObject("src/engine/resources/ambulance.png"));

        transferImage.visible(false);
        defaultImage.visible(true);

        addChild(transferImage);
        addChild(defaultImage);

        currentAction = new AmbulanceFree(this);        // Start action

        moveDelay = 2;
        frameCounter = moveDelay;
    }

    @Override
    public void onRender(Graphics2D graphics2D) {
        graphics2D.setColor(Color.red);
        graphics2D.setFont(new Font("default", Font.BOLD, 16));
        graphics2D.drawChars(name.toCharArray(), 0, name.length(), 0, 0);
    }
}
