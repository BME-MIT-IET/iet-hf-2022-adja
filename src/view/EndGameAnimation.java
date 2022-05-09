package view;

import main.Game;
import main.GameState;

import java.awt.*;

/**
 * A játék végén lejátszott animáció
 */
public class EndGameAnimation extends Drawable {

    /**
     * Kirajzolt szöveg színe
     */
    private Color fontColor;

    /**
     * Kirajzolt szöveg
     */
    private String string;

    /**
     * Szöveg betűtípusa
     */
    private Font font;

    /**
     * Konstruktor
     */
    public EndGameAnimation(){
        this.zIndex = 101;
        GameState endGameState = Game.getInstance().GetGameState();
        if(endGameState == GameState.SETTLERSWON) {
            this.fontColor = Color.GREEN;
            this.string = "SETTLERS WON THE GAME!";
            this.font = new Font("Dialog", Font.ITALIC, 80);
        }
        else if(endGameState == GameState.SETTLERSLOST){
            this.fontColor = Color.RED;
            this.string = "SETTLERS LOST THE GAME :(";
            this.font = new Font("Dialog", Font.ITALIC, 80);
        }
    }

    /**
     * A képernyő középére kirajzolja a szöveget
     * @param g: graphics
     * @param string: kirajzolandó szöveg
     */
    private void DrawCenteredText(Graphics2D g, String string){
        FontMetrics metrics = g.getFontMetrics(this.font);
        Position winSize = ViewController.getInstance().GetWindowSize();
        int x = (winSize.x - metrics.stringWidth(string)) / 2;
        int y = winSize.y - (winSize.y - (-(int) metrics.getLineMetrics(string, g).getBaselineOffsets()[2])) / 2;
        g.drawString(string, x, y);
    }

    /**
     * Kirajzolás
     * @param g: graphics
     * @param cameraPos: kameria pozíciója
     */
    @Override
    public void Draw(Graphics2D g, Position cameraPos) {
        g.setFont(this.font);
        g.setColor(this.fontColor);
        this.DrawCenteredText(g, this.string);
    }
}
