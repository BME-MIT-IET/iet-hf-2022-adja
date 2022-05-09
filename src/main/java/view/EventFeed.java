package view;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.*;

/**
 * Eseményeket jelző felület
 */
public class EventFeed extends Drawable {

    /**
     * Események várakozási sora
     */
    private final Queue<EventBox> eventQueue;

    /**
     * Animáció ideje
     */
    private static final int animationTimeMax = 200;

    /**
     * Konstruktor
     */
    public EventFeed(){
        this.eventQueue = new PriorityQueue<>(25, Comparator.comparingInt(EventBox::GetTimeLeft));
        this.zIndex = 1000;
    }

    /**
     * Jelzi, hogy kiírandó esemény történt
     * @param eventDescription: esemény leírása
     */
    public void EventHappened(String eventDescription){
        this.eventQueue.add(new EventBox(eventDescription));
    }

    /**
     * Kirajzolás
     * @param graphics: graphics
     * @param cameraPos: kameria pozíciója
     */
    @Override
    public void Draw(Graphics2D graphics, Position cameraPos) {
        if(eventQueue.size() > 0){
            boolean finished = false;
            while(!finished && eventQueue.size() > 0){
                if(eventQueue.peek().GetTimeLeft() <= 0) {
                    eventQueue.poll();
                }
                else
                    finished = true;
            }

            int ct = 0;
            for(EventBox eb : eventQueue){
                Position winSize = ViewController.getInstance().GetWindowSize();
                eb.SetPosition(new Position(winSize.x - EventBox.eventBoxDimensions.width - 20, 10 + ct * (EventBox.eventBoxDimensions.height + 10)));
                ++ct;
                eb.Draw(graphics, cameraPos);
                eb.DecreaseTimeLeft();

                if(ct == 4)
                    break;
            }
        }
    }

    /**
     * Kirajzolandó doboz
     */
    private static class EventBox extends Drawable{

        /**
         * Szöveg
         */
        private final String text;

        /**
         * Szöveg betűtípusa
         */
        private Font font;

        /**
         * Doboz pozíciója
         */
        private Position pos;

        /**
         * Dobozok mérete
         */
        private static final Dimension eventBoxDimensions = new Dimension(200, 25);

        /**
         * Az animációból hátralévő idő
         */
        private int timeLeft;

        /**
         * Konstruktor
         * @param string: esemény leírása
         */
        public EventBox(String string){
            this.text = string;

            this.font = new Font("Dialog", Font.PLAIN, 20);

            this.timeLeft = EventFeed.animationTimeMax;
        }

        /**
         * Pozíció beállítása
         * @param pos: új pozíció
         */
        public void SetPosition(Position pos){
            this.pos = pos;
        }

        /**
         * Csökkenti a hátralévő időt az animációból
         */
        public void DecreaseTimeLeft(){
            --timeLeft;
        }

        /**
         * Visszatér a hátralévő idővel az animációból
         * @return hátralévő idő
         */
        public int GetTimeLeft(){
            return this.timeLeft;
        }

        /**
         * Kirajzolás
         * @param graphics: graphics
         * @param cameraPos: kameria pozíciója
         */
        @Override
        public void Draw(Graphics2D graphics, Position cameraPos) {
            graphics.setColor(Color.GRAY);
            graphics.fillRect(pos.x, pos.y, eventBoxDimensions.width, eventBoxDimensions.height);
            graphics.setColor(Color.WHITE);
            graphics.setFont(font);
            Font font = Font.decode("Dialog");
            Rectangle2D r2d = graphics.getFontMetrics(font).getStringBounds(this.text, graphics);
            this.font = font.deriveFont((float) (font.getSize2D() * (eventBoxDimensions.width - 30) / r2d.getWidth()));
            DrawCenteredText(graphics);
        }

        /**
         * A doboz közepére kirajzolja a a szöveget
         * @param g: graphics
         */
        private void DrawCenteredText(Graphics2D g){
            FontMetrics metrics = g.getFontMetrics(this.font);
            int x = this.pos.x + (eventBoxDimensions.width - metrics.stringWidth(this.text)) / 2;
            int y = this.pos.y + eventBoxDimensions.height - (eventBoxDimensions.height - (-(int) metrics.getLineMetrics(this.text, g).getBaselineOffsets()[2])) / 2 - 3;
            g.drawString(this.text, x, y);
        }
    }
}