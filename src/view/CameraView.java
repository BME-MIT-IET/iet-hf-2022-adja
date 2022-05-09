package view;

import java.awt.*;

/**
 * Kamera pozíciójának megjelenítése
 */
public class CameraView extends Drawable{

    /**
     * Szöveg betűtípusa
     */
    private static final Font font;

    static {
        font = new Font("Dialog",Font.PLAIN,14);
    }

    /**
     * Konstruktor
     */
    public CameraView(){
        this.zIndex = 100;
    }

    /**
     * Kirajzolás
     * @param graphics: graphics
     * @param cameraPos: kameria pozíciója
     */
    @Override
    public void Draw(Graphics2D graphics, Position cameraPos) {
        graphics.setColor(Color.GRAY); // Tesztelésre csak talán
        graphics.setFont(font);
        graphics.drawString("X: "+cameraPos.x,0,12);
        graphics.drawString("Y: "+cameraPos.y,0,28);
    }
}
