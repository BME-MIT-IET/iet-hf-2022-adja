package view;

import places.Asteroid;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Aszteroida View-ja
 */
public class AsteroidView extends Drawable implements Clickable {

    /**
     * A View modellje
     */
    private final Asteroid asteroid;

    /**
     * A karakterek, akiket neki kell kirajzolnia (rajta vannak)
     */
    private final List<DrawableCharacter> drawableCharacterList;

    /**
     * Az aszteroida pozíciója
     */
    private final Position pos;

    /**
     * Az aszteroida sugara
     */
    public static final int asteroidRadius = 42;

    /**
     * Rá van-e kattintva
     */
    private boolean clicked;

    /**
     * Forgási szög
     */
    private final double angle;

    /**
     * Az aszteroida képe
     */
    private static BufferedImage img;

    /**
     * Szöveg betűtípusa
     */
    private static final Font moveFont;
    private static final Font infoFont;

    static{
        moveFont = new Font("Dialog", Font.PLAIN, 22);
        infoFont = new Font("Dialog", Font.PLAIN, 12);
        try{
            img= ImageIO.read(new File("Textures/aszteroida.png"));
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }

    /**
     * Konstruktor
     * @param a: a modell
     * @param pos: az aszteroida pozíciója
     * @param z: hányadik rétegre kell kirajzolni
     */
    public AsteroidView(Asteroid a, Position pos, int z){
        this.asteroid = a;
        this.pos = pos;
        this.zIndex = z;
        this.clicked = false;
        this.drawableCharacterList = new ArrayList<>();
        this.angle=new Random().nextDouble()*2*Math.PI;
    }

    /**
     * Visszatér a pozícióval
     * @return pozíció
     */
    public Position GetPos(){
        return pos;
    }

    /**
     * Kirajzolja a kapcsolatok
     * @param graphics: graphics
     * @param cameraPos: kamera pozíciója
     * @param cursorPos: egér pozíciója
     * @param check: check
     * @param waitingSettlersAsteroid: inputra várakozó settler aszteroidája-e
     */
    public void Draw_Neighbours_and_Teleports(Graphics2D graphics, Position cameraPos, Position cursorPos, boolean check, boolean waitingSettlersAsteroid){
        if(check){
            if(!(Math.sqrt((pos.x + 30 - cameraPos.x - cursorPos.x) * (pos.x + 30 - cameraPos.x - cursorPos.x) +
                    (pos.y + 30 - cameraPos.y - cursorPos.y) * (pos.y + 30 - cameraPos.y  - cursorPos.y)) < AsteroidView.asteroidRadius))
                return;
        }
        Color color = waitingSettlersAsteroid ? Color.WHITE : Color.CYAN;
        var asteroidviews = new ArrayList<AsteroidView>();
        var asteroids = this.GetAsteroid().GetNeighboringAsteroids();
        for (Asteroid ast: asteroids) {
            asteroidviews.add(ViewController.getInstance().GetAsteroidView(ast));
        }

        graphics.setColor(color);
        graphics.setStroke(new BasicStroke(3));

        for (var asteroidv: asteroidviews) {
            graphics.drawLine(pos.x- cameraPos.x + 50, pos.y -cameraPos.y + 50, asteroidv.pos.x - cameraPos.x + 50,  asteroidv.pos.y - cameraPos.y + 50);
        }
    }

    /**
     * Kirajzolás
     * @param graphics: graphics
     * @param cameraPos: kamera pozíciója
     */
    @Override
    public void Draw(Graphics2D graphics, Position cameraPos) {
        Position windowSize = ViewController.getInstance().GetWindowSize();
        if (!(pos.x >cameraPos.x-100 && pos.x < cameraPos.x+1000*windowSize.x/1000))
            return;
        if (!(pos.y >cameraPos.y-100 && pos.y < cameraPos.y+600*windowSize.y/563))
            return;

        graphics.drawImage(Rotate(angle),pos.x - cameraPos.x , pos.y  - cameraPos.y ,asteroidRadius*2,asteroidRadius*2,null);

        for(int i = 0; i < drawableCharacterList.size(); i++){

            double phi = i * 2 * Math.PI/drawableCharacterList.size();
            phi -= Math.PI/2;
            Position p = new Position(this.pos.x  - cameraPos.x  + asteroidRadius + (int) ((asteroidRadius +12) * Math.cos(phi))  ,this.pos.y  - cameraPos.y  + asteroidRadius +(int) ((asteroidRadius +12) * Math.sin(phi)));

            drawableCharacterList.get(i).SetPosition(p,phi + Math.PI/2);
            drawableCharacterList.get(i).Draw(graphics);
        }

        if (clicked){
            Color faded = new Color(255,255,255,220);
            graphics.setColor(faded);
            graphics.fillRect(pos.x-cameraPos.x-70, pos.y-cameraPos.y, 70,65);
            graphics.setColor(Color.WHITE);
            graphics.drawRect(pos.x-cameraPos.x-70, pos.y-cameraPos.y, 70,65);
            graphics.setColor(Color.BLACK);
            graphics.setFont(infoFont);
            graphics.drawString("Layers: "+asteroid.GetThickness(),pos.x-cameraPos.x-64,pos.y-cameraPos.y+18);

            graphics.drawString("Material:",pos.x-cameraPos.x-64,pos.y-cameraPos.y+38);
            if (asteroid.GetThickness()==0) {
                if (asteroid.GetMaterial() != null)
                    graphics.drawString(asteroid.GetMaterial().getClass().getSimpleName(), pos.x - cameraPos.x - 64, pos.y - cameraPos.y + 58);
                else
                    graphics.drawString("hollow", pos.x - cameraPos.x - 64, pos.y - cameraPos.y + 58);
            } else {
                graphics.drawString("???", pos.x - cameraPos.x - 64, pos.y - cameraPos.y + 58);
            }
            boolean teleportabel = false;
            if (ViewController.getInstance().GetCurrentSettlerWaitingForInput()!=null) {
                var teleportGates = ViewController.getInstance().GetCurrentSettlerWaitingForInput().GetAsteroid().GetTeleportGates();

                for (var teleport : teleportGates
                ) {
                    if (teleport.GetPair().GetAsteroid() == this.asteroid)
                        teleportabel = true;
                }
            }
            if (ViewController.getInstance().GetCurrentSettlerWaitingForInput()!=null &&
                    (ViewController.getInstance().GetCurrentSettlerWaitingForInput().GetAsteroid().GetNeighboringAsteroids().contains(this.asteroid)
                    || teleportabel)) {
                graphics.setColor(faded);
                graphics.fillRect(pos.x - cameraPos.x - 70, pos.y - cameraPos.y + 65, 70, 30);
                graphics.setColor(Color.GRAY);
                graphics.drawRect(pos.x - cameraPos.x - 70, pos.y - cameraPos.y + 65, 70, 30);
                graphics.setColor(Color.BLACK);
                graphics.setFont(moveFont);
                graphics.drawString("Move", pos.x - cameraPos.x - 63, pos.y - cameraPos.y + 90);
            }
        }
    }

    /**
     * Visszatér az asteroidview modelljével
     * @return asteroid
     */
    public Asteroid GetAsteroid(){
        return this.asteroid;
    }

    /**
     * Eltávolítja a paramétert a drawableCharacterjei közül
     * @param dc: eltávolítandó
     */
    public void RemoveDrawableCharacter(DrawableCharacter dc){
        this.drawableCharacterList.remove(dc);
    }

    /**
     * Hozzáadja a paramétert a drawableCharacterjeihez
     * @param dc: hozzáadandó
     */
    public void AddDrawableCharacter(DrawableCharacter dc){
        this.drawableCharacterList.add(dc);
    }

    /**
     * Jelzés, hogy felrobbant a modellben
     */
    public void AsteroidExploded(){
        ViewController.getInstance().AsteroidExploded(this);
    }

    /**
     * Jelzés, hogy rákattintottak
     * @param clickPos: kattintás pozíciója
     * @param cameraPos: kamera pozíciója
     */
    @Override
    public void Clicked(Position clickPos, Position cameraPos) {
        if (clicked) {
            if ((clickPos.x > pos.x - cameraPos.x - 70) && (clickPos.x < pos.x - cameraPos.x - 70 + 70) &&
                    (clickPos.y > pos.y - cameraPos.y + 65) && (clickPos.y < pos.y - cameraPos.y + 65 + 30)) {

                ViewController.getInstance().GetCurrentSettlerWaitingForInput().Move(this.asteroid);
                ViewController.getInstance().EventHappened(ViewController.getInstance().GetSettlerView(ViewController.getInstance().GetCurrentSettlerWaitingForInput()).CharacterToString() + " moved!");
                ViewController.getInstance().SettlerStepped();
            }
        }
        this.clicked = true;
    }

    /**
     * Jelzés, hogy nincs rákattintva
     */
    @Override
    public void UnClicked() {
        this.clicked = false;
    }

    /**
     * Ellenőrzi, hogy rákattintottak-e
     * @param clickPos: kattintás pozíciója
     * @param cameraPos: kamera pozíciója
     * @return bool: rákattintottak-e
     */
    @Override
    public boolean ClickedCheck(Position clickPos, Position cameraPos) {
        double tes = Math.sqrt(Math.pow((clickPos.x - (pos.x + 45 - cameraPos.x)), 2) + Math.pow(clickPos.y - (pos.y + 40 - cameraPos.y), 2));
        if (tes <= 35) {
            return true;
        }
        if (clicked){
            if (clickPos.x > pos.x-cameraPos.x-70 && clickPos.x < pos.x-cameraPos.x-70 + 70 &&
                    clickPos.y > pos.y-cameraPos.y && clickPos.y < pos.y-cameraPos.y + 65 + 30){
                var teleportGates = ViewController.getInstance().GetCurrentSettlerWaitingForInput().GetAsteroid().GetTeleportGates();
                boolean teleportabel = false;
                for (var teleport: teleportGates
                     ) {
                    if (teleport.GetPair().GetAsteroid()==this.asteroid)
                        teleportabel=true;
                }

                if (ViewController.getInstance().GetCurrentSettlerWaitingForInput()!=null &&
                        clickPos.y > pos.y-cameraPos.y + 65 &&
                        (!ViewController.getInstance().GetCurrentSettlerWaitingForInput().GetAsteroid().GetNeighboringAsteroids().contains(this.asteroid)
                                && !teleportabel)) {
                    return false;
                }
                return true;
            }
        }

        return false;
    }

    /**
     * Elforgatja az aszteroida képét
     * @param angle: forgatási szög
     * @return elforgatott kép
     */
    public BufferedImage Rotate(double angle) {
        int w = img.getWidth();
        int h = img.getHeight();

        BufferedImage rotated = new BufferedImage(w, h, img.getType());
        Graphics2D graphic = rotated.createGraphics();
        graphic.rotate(angle, w/2.0f, h/2.0f);
        graphic.drawImage(img, null, 0, 0);
        graphic.dispose();
        return rotated;
    }

}
