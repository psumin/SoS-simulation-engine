package agents;

import action.Action;
import action.firefighteraction.FireFighterCollaborativeAction;
import action.firefighteraction.FireFighterAction;
import core.*;
import core.Map;

import java.awt.*;
import java.util.*;

public class FireFighter extends CS {

    public World world;
    public Map individualMap;

    public Queue<Tile> unvisitedTiles;
    public final ArrayList<Patient> patientsMemory = new ArrayList<>();

    public Action currentAction;

    public ImageObject transferImage;
    public ImageObject defaultImage;

    int sightRange = 20;
    public FireFighter(World world, String name) {
        super(world, name);
        this.world = world;

        transferImage = new ImageObject("src/engine/resources/transfer.png");
        defaultImage = new ImageObject("src/engine/resources/ff30x30.png");

        transferImage.visible(false);
        defaultImage.visible(true);

        addChild(transferImage);
        addChild(defaultImage);

        individualMap = new Map();
        LinkedList<Tile> temp = new LinkedList<>(individualMap.getTiles());
        //LinkedList<Tile> temp= new LinkedList<>(world.getMap().getTiles());
        Collections.shuffle(temp);

        unvisitedTiles = temp;
        currentAction = new FireFighterCollaborativeAction(this);
    }

    boolean isFirstUpdate = true;
    @Override
    public void onUpdate() {
        if(isFirstUpdate) {
            isFirstUpdate = false;
            currentAction.start();
        }
    }

    @Override
    public void clear() {
        world = null;
    }
    public int getSightRange() {
        return sightRange;
    }
    public World getWorld() {
        return world;
    }
    public void changeAction(FireFighterAction action) {
        currentAction.stop();
        currentAction = action;
        currentAction.start();
        currentAction.update();
    }

    @Override
    public void onRender(Graphics2D graphics2D) {
        graphics2D.setColor(Color.red);
        graphics2D.setFont(new Font("default", Font.BOLD, 16));
        graphics2D.drawChars(name.toCharArray(), 0, name.length(), 0, 0);
    }

    public void removeFromTile() {
        world.getMap().removeObject(position.x, position.y, this);
    }
    public void addToTile() {
        world.getMap().addObject(position.x, position.y, this);
    }

    @Override
    public void recvMsg(Msg msg) {
        currentAction.recvMsg(msg);
    }
}
