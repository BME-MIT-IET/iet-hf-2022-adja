package view;

import characters.Character;
import characters.Settler;
import materials.Coal;
import materials.Iron;
import materials.WaterIce;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Telepes kirajzolására szolgáló osztály
 */
public class SettlerView extends DrawableCharacter implements Clickable {

    /**
     * Modellbeli telepes
     */
    private final Settler settler;

    /**
     * Telepes sugara
     */
    private static final int settlerRadius = 10;

    /**
     * Megkülönböztető szín
     */
    private final Color color;

    /**
     * Nyersanyagok képei
     */
    private Image CoalImg, IronImg, WaterIceImg, UraniumImg;

    /**
     * Lehetséges megkülönböztető színek
     */
    private static final List<Color> settlerColors;

    /**
     * Létrehozott telepesek számlálója
     */
    private static int settlerViewCreationCounter;

    /**
     * Szöveg betűtípusa
     */
    private static final Font font;

    static{
        settlerViewCreationCounter = 0;
        settlerColors = new ArrayList<>(Arrays.asList(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.WHITE));
        font = new Font("Dialog",Font.PLAIN,12);
    }

    /**
     * Konstruktor
     * @param s: modellbeli telepes
     */
    public SettlerView(Settler s){
        super();

        this.settler = s;

        try{
            //Beolvasas utan automatikusan bezarodnak a fajlok az ImageIO-nal
            img= ImageIO.read(new File("Textures/settler.png"));
            CoalImg = ImageIO.read(new File("Textures/szen.png"));
            IronImg = ImageIO.read(new File("Textures/vas.png"));
            WaterIceImg = ImageIO.read(new File("Textures/vizjeg.png"));
            UraniumImg = ImageIO.read(new File("Textures/uran.png"));
        }
        catch (IOException ex){
            ex.printStackTrace();
        }

        this.color = settlerColors.get(settlerViewCreationCounter);
        ++settlerViewCreationCounter;
    }

    /**
     * Kirajzolás
     * @param graphics: graphics
     */
    @Override
    public void Draw(Graphics2D graphics) {
        this.Draw(graphics, this.pos);

        if (clicked) {
            graphics.setColor(new Color(255,255,255,100));
            graphics.fillRect(2,46,68,145);
            graphics.setColor(new Color(255,255,255,220));
            graphics.fillRect(2,48,65,140);
            graphics.setColor(color);
            graphics.setStroke(new BasicStroke(3));
            graphics.drawRect(2,48,65,140);
            graphics.setColor(Color.BLACK);

            int coal = 0;
            int iron = 0;
            int waterice = 0;
            int uranium = 0;
            var materials = this.settler.GetInventory().GetMaterials();

            for ( var material: materials
            ) {
                if (material.CompatibleWith(new Coal()))
                    coal++;
                else if (material.CompatibleWith(new Iron()))
                    iron++;
                else if (material.CompatibleWith(new WaterIce()))
                    waterice++;
                else
                    uranium++;
            }
            graphics.drawImage(CoalImg,10,55,30,30,null,null);
            graphics.drawImage(IronImg,10,86,30,30,null,null);
            graphics.drawImage(WaterIceImg,10,117,30,30,null,null);
            graphics.drawImage(UraniumImg,10,148,30,30,null,null);
            graphics.setFont(font);
            graphics.drawString(coal+"" ,45,55 + 20);
            graphics.drawString(iron+"" ,45,86+ 20);
            graphics.drawString(waterice+"" ,45,117+ 20);
            graphics.drawString(uranium+"" ,45,148+ 20);
        }
    }

    /**
     * Kirajzolás pozíció szerint
     * @param graphics: graphics
     * @param position: pozíció
     */
    @Override
    public void Draw(Graphics2D graphics, Position position) {
        graphics.setColor(color);
        graphics.setStroke(new BasicStroke(2));
        graphics.rotate(angle,position.x,position.y);
        graphics.drawRect((position.x-settlerRadius-1),(position.y-settlerRadius-2),2*(settlerRadius)+2,2*(settlerRadius)+4);
        graphics.rotate(-angle,position.x,position.y);
        graphics.drawImage(Rotate(angle),position.x-settlerRadius,position.y-settlerRadius,2*settlerRadius,2*settlerRadius,null);
    }

    /**
     * Visszatér a telepes megkülönböztető színével
     * @return szín
     */
    public Color GetColor(){ return this.color;}

    /**
     * Visszatér a modellbeli karakterrel
     * @return modell
     */
    @Override
    public Character GetCharacter() {
        return this.settler;
    }

    /**
     * Jelzés, hogy meghalt a modellben a telepes
     */
    @Override
    public void CharacterDied(){
        ViewController.getInstance().SettlerDied(this);
    }

    /**
     * EventFeed-es kiíratáshoz
     * @return szöveges azonosító
     */
    @Override
    public String CharacterToString() {
        String colorString = "";
        if (Color.RED.equals(color)) colorString = "red";
        else if(Color.BLUE.equals(color)) colorString = "blue";
        else if(Color.WHITE.equals(color)) colorString = "white";
        else if(Color.YELLOW.equals(color)) colorString = "yellow";
        else if(Color.GREEN.equals(color)) colorString = "green";

        return colorString + " " + "settler";
    }

    /**
     * Jelzés, hogy rákattintottak
     * @param pos: kattintás pozíciója
     * @param cameraPos: kamera pozíciója
     */
    @Override
    public void Clicked(Position pos, Position cameraPos) {
        clicked=true;
    }

    /**
     * Jelzés, hogy nincs rákattintva
     */
    @Override
    public void UnClicked() {
        clicked=false;
    }

    /**
     * Ellenőrzi, hogy rákattintottak-e
     * @param clickPos: kattintás pozíciója
     * @param cameraPos: kamera pozíciója
     * @return bool: rákattintottak-e
     */
    @Override
    public boolean ClickedCheck(Position clickPos, Position cameraPos) {
        if (pos.x==0)   //gyenge szűrés
            return false;

        if (!(pos.x - 12 < clickPos.x && pos.x - 12 + 20 > clickPos.x &&
                pos.y - 12 < clickPos.y && pos.y - 12 + 20 > clickPos.y))
            return false;
        Clicked(clickPos,cameraPos);
        return true;
    }
}
