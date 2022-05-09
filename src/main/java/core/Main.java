package core;

import view.ViewController;

/**
 * core.Main osztály
 */
public class Main {

    /**
     * core.Main függvény
     * @param args: args
     */
    public static void main(String[] args){
        Frame frame = new Frame();
        ViewController.StartGame();
        frame.StartGame();
    }
}
