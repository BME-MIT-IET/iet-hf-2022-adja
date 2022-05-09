package view;

import characters.Character;
import places.Asteroid;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Kirajzolható karakterek
 */
public abstract class DrawableCharacter {

    /**
     * Karakter képe
     */
    protected BufferedImage img;

    /**
     * Rá van-e kattintva
     */
    protected boolean clicked;

    /**
     * Karakter pozíciója
     */
    protected Position pos;

    /**
     * Forgatási szög
     */
    protected double angle;

    /**
     * Előző aszteroida
     */
    private Asteroid from;

    /**
     * Animáció ideje
     */
    private int animationTime;

    /**
     * Konstruktor
     */
    public DrawableCharacter(){
        this.pos = new Position(0,0);
        animationTime = 1;
    }

    /**
     * Kirajzolás
     * @param graphics: graphics
     */
    public abstract void Draw(Graphics2D graphics);

    /**
     * Kirajzolás pozíció szerint
     * @param graphics: graphics
     * @param pos: pozíció
     */
    public abstract void Draw(Graphics2D graphics, Position pos);

    /**
     * Visszatér a karakter aszteroidájával a modellben
     * @return asteroid
     */
    public Asteroid GetAsteroid(){
        return this.GetCharacter().GetAsteroid();
    }

    /**
     * Visszatér az előző aszteroidájával a karakternek
     * @return from
     */
    public Asteroid GetLastAsteroid() {
        return from;
    }

    /**
     * Visszatér a karakter modelljével
     * @return modellbeli karakter
     */
    public abstract Character GetCharacter();

    /**
     * Jelzés, hogy meghalt a karakter a modellben
     */
    public void CharacterDied(){
        ViewController.getInstance().CharacterDied(this);
    }

    /**
     * Jelzés, hogy elmozdult a karakter a modellben
     * @param oldAsteroid: előző aszteroida
     * @param newAsteroid: új aszteroida
     */
    public void CharacterMoved(Asteroid oldAsteroid, Asteroid newAsteroid){
        ViewController.getInstance().CharacterMoved(this, oldAsteroid, newAsteroid);
        from = oldAsteroid;
    }

    /**
     * Mozgási animáció
     * @param g: graphics
     * @param cameraPos: kamera pozíciója
     * @param lastPos: előző pozíció az animációban
     * @param newPos: új pozíció az animációban
     * @return vége lett-e az animációnak
     */
    public boolean PlayMoveAnimation(Graphics2D g, Position cameraPos, AsteroidView lastPos, AsteroidView newPos) {
        if(animationTime < 120) {
            Position last = new Position(lastPos.GetPos().x, lastPos.GetPos().y);
            Position next = new Position(newPos.GetPos().x, newPos.GetPos().y);

            Position temp = new Position(0, 0);
            temp.x = next.x - last.x;
            temp.y = next.y - last.y;

            float tempX = temp.x;
            float tempY = temp.y;

            temp.x = Math.round(tempX * ((float)animationTime / (float)120));
            temp.y = Math.round(tempY * ((float)animationTime / (float)120));

            temp.x = temp.x + last.x - cameraPos.x;
            temp.y = temp.y + last.y - cameraPos.y;

            animationTime++;
            Draw(g, temp);

            return true;
        }
        animationTime = 1;
        newPos.AddDrawableCharacter(this);
        return false;
    }

    /**
     * Beállítja a karakter pozícióját
     * @param pos: pozíció
     * @param ang: forgási szög
     */
    public void SetPosition(Position pos, double ang){
        this.pos.x = pos.x;
        this.pos.y = pos.y;
        this.angle = ang;
    }

    /**
     * Elforgatja a karaktert
     * @param angle: forgatási szög
     * @return elforgatott kép
     */
    public BufferedImage Rotate(double angle) {

        int w = this.img.getWidth();
        int h = this.img.getHeight();

        BufferedImage rotated = new BufferedImage(w, h, this.img.getType());
        Graphics2D graphic = rotated.createGraphics();
        graphic.rotate(angle, (double)w/(double)2, (double)h/(double)2);
        graphic.drawImage(this.img, null, 0, 0);
        graphic.dispose();
        return rotated;
    }

    /**
     * EventFeed-es kiíratáshoz
     * @return szöveges azonosító
     */
    public abstract String CharacterToString();
}
