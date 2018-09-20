package agents;

import core.SoSObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FireFighter extends SoSObject {

    @Override
    public void init() {
        loadImage("src/engine/resources/ff30x30.png");
    }

    @Override
    public void update() {

    }

    @Override
    public void clear() {
    }
}
