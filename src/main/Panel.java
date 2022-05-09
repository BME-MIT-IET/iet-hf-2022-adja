package main;

import view.ViewController;
import view.Position;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * JPanel, amit kirajzolunk a Frame-re
 */
public class Panel extends JPanel {

    /**
     * Kamera pozíciója
     */
    Position cameraPos;

    /**
     * Kamera pozíciójának mentése
     */
    Position cameraPosSaved;

    /**
     * Utolsó kattintás pozíciója
     */
    Position lastClickPos;

    /**
     * Egér pozíciója
     */
    Position cursorPos;

    boolean nem_interface=true;

    /**
     * Panel konstruktora
     */
    public Panel(){
        cameraPos = new Position(0,0);
        cameraPosSaved = new Position(0,0);
        lastClickPos = new Position(0,0);
        this.addMouseListener(new MouseClickedListener());
        this.addMouseMotionListener(new MouseMovedListener());
        this.addKeyListener(new KeyListenerHM());
        setFocusable(true);
        cursorPos = new Position(0, 0);
    }

    /**
     * Kirajzolás
     * @param g: graphics
     */
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        this.setBackground(Color.BLACK);
        Graphics2D graphics = (Graphics2D) g;

        ViewController.getInstance().DrawAll(graphics, cameraPos, cursorPos);
    }

    /**
     * KeyListener a space-re
     */
    private class KeyListenerHM implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode()==KeyEvent.VK_SPACE) {

                if (ViewController.getInstance().GetCurrentSettlerWaitingForInput()!=null) {
                    Position pos = ViewController.getInstance().GetAsteroidView(ViewController.getInstance().GetCurrentSettlerWaitingForInput().GetAsteroid()).GetPos();

                    cameraPos.x = pos.x - 500;
                    cameraPos.y = pos.y - 281;
                    cameraPosSaved.x = pos.x - 500;
                    cameraPosSaved.y = pos.y - 281;
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }

    /**
     * Egér kattintásra figyelő Listener
     */
    private class MouseClickedListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON1) {
                lastClickPos.x = e.getX();
                lastClickPos.y = e.getY();

                nem_interface = ViewController.getInstance().ClickHandler(lastClickPos, cameraPos);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON1) {
                cameraPosSaved.x = cameraPos.x;
                cameraPosSaved.y = cameraPos.y;
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    /**
     * Jelzés, hogy átméreteződött az ablak
     * @param winSize: új ablakméret
     */
    public void WindowResized(Position winSize){
        if (cameraPos.x>1500+1000-winSize.x) cameraPos.x=1500+1000-winSize.x;
        if (cameraPos.y>2060+563-winSize.y) cameraPos.y=2060+563-winSize.y;
    }

    /**
     * Egér mozgását figyelő Listener
     */
    private class MouseMovedListener implements MouseMotionListener{

        @Override
        public void mouseDragged(MouseEvent e) {
            if(SwingUtilities.isLeftMouseButton(e)) {
                if (!nem_interface) {
                    Position winSize = ViewController.getInstance().GetWindowSize();
                    cameraPos.x = cameraPosSaved.x - e.getX() + lastClickPos.x;
                    cameraPos.y = cameraPosSaved.y - e.getY() + lastClickPos.y; // ez valószínűleg negálni kell
                    if (cameraPos.x < -2500) cameraPos.x = -2500;
                    if (cameraPos.x > 1500 + 1000 - winSize.x) cameraPos.x = 1500 + 1000 - winSize.x;
                    if (cameraPos.y < -2500) cameraPos.y = -2500;
                    if (cameraPos.y > 2060 + 563 - winSize.y) cameraPos.y = 2060 + 563 - winSize.y;

                    cursorPos.x = e.getX();
                    cursorPos.y = e.getY();
                }
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            cursorPos.x = e.getX();
            cursorPos.y = e.getY();
        }
    }
}