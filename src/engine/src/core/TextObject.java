package core;

import java.awt.*;

public class TextObject extends SoSObject {

    Color defaultFontColor = Color.black;
    public Color fontColor = null;
    int defaultFontSize = 16;

    public String text = "";

    @Override
    public void onRender(Graphics2D graphics2D) {
        if(fontColor == null) {
            graphics2D.setColor(defaultFontColor);
        } else {
            graphics2D.setColor(fontColor);
        }

        graphics2D.setFont(new Font("default", Font.BOLD, defaultFontSize));
        graphics2D.drawChars(text.toCharArray(), 0, text.length(), 0, 0);
    }
}
