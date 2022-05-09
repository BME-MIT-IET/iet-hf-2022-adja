package main;

import view.ViewController;

/**
 * Main osztály
 */
public class Main {

    /**
     * Main függvény
     * @param args: args
     */
    public static void main(String[] args){
        Frame frame = new Frame();
        ViewController.StartGame();
        frame.StartGame();
    }
}
