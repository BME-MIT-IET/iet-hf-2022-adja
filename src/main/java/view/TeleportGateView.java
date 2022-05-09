package view;

import places.TeleportGate;

import java.awt.*;

/**
 * Teleportkapupár kirajzolására szolgáló View
 */
public class TeleportGateView extends Drawable {

    /**
     * Egyik teleportkapu
     */
    private final TeleportGate teleportGate1;

    /**
     * Másik teleportkapu
     */
    private final TeleportGate teleportGate2;

    /**
     * Konstruktor
     * @param tg1: Egyik teleportkapu
     * @param tg2: Másik teleportkapu
     * @param z: melyik rétegre kell kirajzolni
     */
    public TeleportGateView(TeleportGate tg1, TeleportGate tg2, int z){
        this.teleportGate1 = tg1;
        this.teleportGate2 = tg2;
        this.zIndex = z;
    }

    /**
     * Kirajzolás
     * @param graphics: graphics
     * @param cameraPos: kameria pozíciója
     */
    @Override
    public void Draw(Graphics2D graphics, Position cameraPos) {
        if (teleportGate1.GetAsteroid() != null && teleportGate2.GetAsteroid()!=null){
            graphics.setColor(Color.BLUE);
            AsteroidView ast1 = ViewController.getInstance().GetAsteroidView(teleportGate1.GetAsteroid());
            AsteroidView ast2 = ViewController.getInstance().GetAsteroidView(teleportGate2.GetAsteroid());
            graphics.drawLine(ast1.GetPos().x - cameraPos.x + 50,ast1.GetPos().y - cameraPos.y + 50,ast2.GetPos().x - cameraPos.x + 50,ast2.GetPos().y - cameraPos.y + 50);
        }
    }

    /**
     * Jelzés, hogy a pár egyik eleme felrobbant a modellben
     */
    public void TeleportGateDestroyed(){
        ViewController.getInstance().TeleportGateDestroyed(this);
    }
}
