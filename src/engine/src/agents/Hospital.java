package agents;

import core.ImageObject;
import core.World;

public class Hospital extends CS {

    public Hospital(World world, String name) {
        super(world, name);
        addChild(new ImageObject("src/engine/resources/hospital.png"));
    }
}
