package agents;

import core.ImageObject;
import core.SoSImage;
import core.SoSObject;

public class Patient extends SoSObject {

    public Patient() {
        addChild(new ImageObject("src/engine/resources/patient30x30.png"));
    }
}
