package core;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageObject extends SoSObject {

    SoSImage image;
    float scale = 1;
    public ImageObject(String filePath) {
        setImage(filePath);
    }

    public ImageObject(String filePath, float scale) {
        setImage(filePath);
        this.scale = scale;
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
        g.translate((int)(-scale / 2) * Map.tileSize.width, (int)(-scale / 2) * Map.tileSize.height);
        g.scale(scale, scale);
        g.drawImage(bufImage, 0, 0, Map.tileSize.width, Map.tileSize.height, null);
    }

    @Override
    public void clear() {
        if(image != null) {
            image.clear();
            image = null;
        }
        scale = 1;
    }

}
