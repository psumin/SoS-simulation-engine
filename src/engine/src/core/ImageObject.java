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
        //int w = bufImage.getWidth();
        //int h = bufImage.getHeight();
        g.drawImage(bufImage, 0, 0, Map.tileSize.width, Map.tileSize.height, null);
    }

    @Override
    public void clear() {
        if(image != null) {
            image.clear();
            image = null;
        }
    }

}
