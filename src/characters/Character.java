package characters;

import main.Game;
import places.Asteroid;
import interfaces.Steppable;
import places.Place;
import view.DrawableCharacter;

import java.util.List;
import java.lang.Math;

/**
 * A játékban a mozgásra képes karakterek (Robot, MiningCharacter) abszrakt ősosztálya.
 */
public abstract class Character implements Steppable {
    /**
     *Az aktuális aszteroida ahol a karakter éppen tartózkodik
     */
    protected Asteroid asteroid;

    /**
     * View
     */
    protected DrawableCharacter view;

    /**
     * Lereagálja, hogy felrobbant az aszteroida, amin éppen
     * tartózkodik.
     */
    abstract public void HitByExplosion();

    /**
     * Lereagálja, hogy napviharba került az aszteroida, amin éppen tartózkodik.
     */
    public void HitByStorm() {
        Game.getInstance().RemoveSteppable(this);
        asteroid.TakeOff(this);

        this.DiedAlert();
    }

    /**
     * Atviszi egy elérhető aszteroidára a karaktert.True ha sikerült false ha
     * nem.
     * @param place: a hely, ahova menni akar a karakter
     * @return true: sikerült a mozgás, false: nem sikerült
     */
    public boolean Move(Place place) {
        List<Place> destinations = this.asteroid.GetNeighbors();

        double random = Math.random()*(destinations.size()-1);
        Place choosenDestination = destinations.get((int)random);

        Asteroid currentAsteroid = this.asteroid;

        if(choosenDestination.Move(this)){
            currentAsteroid.TakeOff(this);

            this.view.CharacterMoved(currentAsteroid, this.asteroid);

            return true;
        }

        return false;
    }

    /**
     * Beállítja az aktuális aszteroida helyét.
     * @param asteroid: a karakter új aszteroidája
     */
    public void SetAsteroid(Asteroid asteroid){
        this.asteroid = asteroid;
    }

    /**
     * Visszaadja a karakter aszteroidáját
     * @return asteroid: a karakter aszteroidája
     */
    public Asteroid GetAsteroid(){
        return this.asteroid;
    }

    /**
     * Jelzi, hogy meghalt
     */
    protected void DiedAlert(){
        this.view.CharacterDied();
    }
}
