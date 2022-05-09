package view;

import java.awt.*;

/**
 * Interface Gomb
 */
public class Button implements Clickable {
    /**
     * Szöveg
     */
    private String string;

    /**
     * Háttérszín
     */
    private Color backGroundColor;

    /**
     * Határoló négyszög
     */
    private final Rectangle border;

    /**
     * Belső négyszög
     */
    private final Rectangle inner;

    /**
     * Szöveg betűtípusa
     */
    private final Font font;

    /**
     * Gomb állapota
     */
    private boolean state;

    /**
     * Kattintáskor meghívandó függvény
     */
    private Runnable callback;

    /**
     * Konstruktor
     * @param border: határoló négyszög
     * @param inner: belső négyszög
     * @param string: szöveg
     * @param fontSize: betűméret
     */
    public Button(Rectangle border, Rectangle inner, String string, int fontSize){
        this.border = border;
        this.inner = inner;
        this.string = string;
        this.backGroundColor = Color.GRAY;
        this.state = false;
        this.font = new Font("Dialog",Font.PLAIN,fontSize);
    }

    /**
     * Jelzés, hogy rákattintottak
     * @param pos: kattintás pozíciója
     * @param cameraPos: kamera pozíciója
     */
    @Override
    public void Clicked(Position pos, Position cameraPos) {
        this.state = true;
        if(callback != null)
            callback.run();
    }

    /**
     * Jelzés, hogy nincs rákattintva
     */
    @Override
    public void UnClicked() {
        this.state = false;
    }

    /**
     * Ellenőrzi, hogy rákattintottak-e
     * @param clickPos: kattintás pozíciója
     * @param cameraPos: kamera pozíciója
     * @return bool: rákattintottak-e
     */
    @Override
    public boolean ClickedCheck(Position clickPos, Position cameraPos) {
        Position windowSize = ViewController.getInstance().GetWindowSize();
        return border.contains(clickPos.x*1000/windowSize.x, clickPos.y*563/windowSize.y);
    }

    /**
     * Beállítja a gomb szövegét
     * @param string: beállítandó szöveg
     */
    public void SetString(String string){
        this.string = string;
    }

    /**
     * Beállítja a gomb színét
     * @param color: beállítandó szín
     */
    public void SetBackGroundColor(Color color){
        this.backGroundColor = color;
    }

    /**
     * Visszatér a gomb állapotával
     * @return állapot
     */
    public boolean GetState() {
        return state;
    }

    /**
     * Beállítja a kattintás esetén meghívandó függvényt
     * @param runnable: meghívandó függvény
     */
    public void SetCallback(Runnable runnable) {
        this.callback = runnable;
    }

    /**
     * A gomb közepére kiírja a szövegét
     * @param g: graphics
     */
    private void DrawCenteredText(Graphics2D g) {
        FontMetrics metrics = g.getFontMetrics(font);
        Position winSize = ViewController.getInstance().GetWindowSize();
        int x = border.x*winSize.x/1000 + (border.width*winSize.x/1000 - metrics.stringWidth(string)) / 2;
        int y = border.y*winSize.y/563 + ((border.height*winSize.y/563 - metrics.getHeight()) / 2) + metrics.getAscent();
        g.drawString(string, x, y);
    }

    /**
     * Kirajzolás
     * @param graphics: graphics
     */
    public void Draw(Graphics2D graphics){
        Position winSize = ViewController.getInstance().GetWindowSize();
        graphics.setColor(this.backGroundColor);
        graphics.fillRect(border.x*winSize.x/1000, border.y*winSize.y/563, border.width*winSize.x/1000, border.height*winSize.y/563);
        graphics.setColor(Color.LIGHT_GRAY);
        graphics.fillRect(inner.x*winSize.x/1000, inner.y*winSize.y/563, inner.width*winSize.x/1000, inner.height*winSize.y/563);
        graphics.setColor(Color.BLACK);
        graphics.setFont(font);
        DrawCenteredText(graphics);
    }

}
