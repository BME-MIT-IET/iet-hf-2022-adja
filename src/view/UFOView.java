package view;

import characters.Character;
import characters.UFO;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * UFO-t megjelenítő View
 */
public class UFOView extends DrawableCharacter {

    /**
     * Modell
     */
    private final UFO ufo;

    /**
     * UFO sugara
     */
    private static final int ufoRadius = 10;

    /**
     * Konstruktor
     * @param u: modellbeli ufo
     */
    public UFOView(UFO u){
        super();

        this.ufo = u;

        try{
            img = ImageIO.read(new File("Textures/ufo.png"));
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }

    /**
     * Kirajzolás
     * @param graphics: graphics
     */
    @Override
    public void Draw(Graphics2D graphics) {
        this.Draw(graphics, this.pos);
    }

    /**
     * Kirajzolás pozíció szerint
     * @param graphics: graphics
     * @param position: pozíció
     */
    @Override
    public void Draw(Graphics2D graphics, Position position) {
        graphics.drawImage(Rotate(angle),position.x-ufoRadius,position.y-ufoRadius,2*ufoRadius,2*ufoRadius,null);
    }

    /**
     * Visszatér a modellbeli karakterrel
     * @return modell
     */
    @Override
    public Character GetCharacter() {
        return this.ufo;
    }

    /**
     * EventFeed-es kiíratáshoz
     * @return szöveges azonosító
     */
    @Override
    public String CharacterToString() {
        return "ufo";
    }

}
