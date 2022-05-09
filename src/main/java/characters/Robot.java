package characters;

import interfaces.Item;
import places.Asteroid;
import places.Place;
import view.ViewController;

/**
 * A robotokat reprezentáló osztály. A robotokat a Controller irányítja. Csak fúrásra és
 * szomszédos aszteroidák közötti mozgásra képesek. A telepesek tudják őket craftolni megfelelő
 * mennyiségű nyersanyagból.
 */
public class Robot extends Character implements Item {

    public Robot(Asteroid ast){
        this.asteroid = ast;
        this.view = ViewController.getInstance().AddRobotView(this);
    }
    /**
     * A robot lépésének végrehajtása
     */
    @Override
    public void Step() {
        ControlRobot();
    }

    @Override
    public int GetSteppablePriority() {
        return 1;
    }

    /**
     *  Az aszteroida, amin a robot éppen tartózkodik felrobbant,
     * ezáltal a robot átrobban egy szomszédos aszteroidára
     */
    public void HitByExplosion() {
        for(Place place : asteroid.GetNeighbors()){
            if(Move(place))
                return;
        }
    }

    /**
     *  Irányítja a robotot. Mindaddig fúr amíg el nem éri a magot, ha
     *  elérte átmegy egy másik aszteroidára.
     */
    private void ControlRobot() {
        if (!this.asteroid.Drilled()){
            for(Place place : asteroid.GetNeighbors()){
                if(Move(place))
                    return;
            }
        }
    }

    /**
     * Igazzal tér vissza, ha a paraméterként kapott item ugyanolyan típusú, mint ő, egyébként hamis.
     * @param item: összehasonlítandó item
     * @return true: megegyezik, false: nem egyezik meg
     */
    @Override
    public boolean CompatibleWith(Item item) {
        return this.getClass() == item.getClass();
    }
}
