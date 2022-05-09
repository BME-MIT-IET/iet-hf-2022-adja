package view;

import characters.Character;
import characters.Robot;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Robot kirajzolásáért felelős View
 */
public class RobotView extends DrawableCharacter {

    /**
     * Modellbeli robot
     */
    private final Robot robot;

    /**
     * Robot sugara
     */
    private static final int robotRadius = 10;

    /**
     * Konstruktor
     * @param r: modellbeli robot
     */
    public RobotView(Robot r){
        super();

        this.robot = r;

        try{
            img= ImageIO.read(new File("Textures/robot.png"));
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
        graphics.drawImage(Rotate(this.angle),position.x-robotRadius,position.y-robotRadius,2*robotRadius,2*robotRadius,null);
    }

    /**
     * Visszatér a modellbeli karakterrel
     * @return modell
     */
    @Override
    public Character GetCharacter() {
        return this.robot;
    }

    /**
     * EventFeed-es kiíratáshoz
     * @return szöveges azonosító
     */
    @Override
    public String CharacterToString() {
        return "robot";
    }


}
