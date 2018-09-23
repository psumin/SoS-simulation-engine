package core;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageObject extends SoSObject {

    SoSImage image;
    public ImageObject(String filePath) {
        setImage(filePath);
    }
    public void setImage(String filePath) {
        clear();
        image = SoSImage.create(filePath);
    }

    @Override
    protected void onRender(Graphics2D g) {
        BufferedImage bufImage = image.getImage();
        g.drawImage(bufImage, 0, 0, null);
    }

    @Override
    public void clear() {
        if(image != null) {
            image.clear();
            image = null;
        }
    }

}
