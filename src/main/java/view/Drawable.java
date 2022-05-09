package view;

import java.awt.*;

/**
 * Önállóan kirajzolható entitások
 */
public abstract class Drawable {
    /**
     * Melyik rétegre rajzoljuk ki
     */
    protected int zIndex;

    /**
     * Kirajzolás
     * @param graphics: graphics
     * @param cameraPos: kameria pozíciója
     */
    public abstract void Draw(Graphics2D graphics, Position cameraPos);

    /**
     * Visszatér azzal, hogy melyik rétegre kell kirajzolni
     * @return zIndex
     */
    public int GetZIndex(){
        return this.zIndex;
    }
}
