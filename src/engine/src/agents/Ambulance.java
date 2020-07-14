package agents;

import action.ambulanceaction.AmbulanceFree;
import core.*;
import misc.Position;

import java.awt.*;

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

        transferImage = new ImageObject("C:/Users/User/IdeaProjects/SoS-simulation-engine/src/engine/resources/transfer2.png");
        defaultImage = new ImageObject("C:/Users/User/IdeaProjects/SoS-simulation-engine/src/engine/resources/ambulance1.png");
//        transferImage = new ImageObject("src/engine/resources/transfer2.png");
//        defaultImage = new ImageObject("src/engine/resources/ambulance1.png");

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

    @Override
    public Position nextPosition(Position destination) {
        int left = worldMap.mapSize.width;
        int right = 0;
        int top = worldMap.mapSize.height;
        int bottom = 0;

        for(Bridgehead bridgehead : world.bridgeheads) {
            if(bridgehead.position.x < left) {
                left = bridgehead.position.x;
            }
            if(bridgehead.position.x > right) {
                right = bridgehead.position.x;
            }
            if(bridgehead.position.y < top) {
                top = bridgehead.position.y;
            }
            if(bridgehead.position.y > bottom) {
                bottom = bridgehead.position.y;
            }
        }

        if(position.x < left || position.x > right
            || position.y < top || position.y > bottom) {
            return super.nextPosition(destination);
        }

        // 세이프 존 범위 안

        int differenceX = destination.x - position.x;
        int differenceY = destination.y - position.y;
        int distanceX = Math.abs(differenceX);
        int distanceY = Math.abs(differenceY);

        if(distanceX + distanceY == 0) {
            return null;
        }

        if(position.x == left || position.x == right) {
            if(distanceY > 0) {
                return new Position(position.x, position.y + differenceY / distanceY);
            } else if(distanceX > 0) {
                return new Position(position.x + differenceX / distanceX, position.y);
            }
        } else if(position.y == top || position.y == bottom) {
            if(distanceX > 0) {
                return new Position(position.x + differenceX / distanceX, position.y);
            } else if(distanceY > 0) {
                return new Position(position.x, position.y + differenceY / distanceY);
            }
        }

        return null;
    }
}
