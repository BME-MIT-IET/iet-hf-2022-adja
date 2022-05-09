package places;

import characters.Character;
import interfaces.Item;
import interfaces.Steppable;
import main.Game;
import view.TeleportGateView;

/**
 * Egy teleportkaput reprezentáló osztály. A teleportkapuk segítségével el tudnak jutni a karakterek olyan
 * aszteroidákra is, amik alapból nem lennének szomszédosak. Ehhez szükséges a két aszteroidán elhelyezni a
 * teleportkapupár egy-egy elemét, a teleportkapupár ezután lesz működőképes állapotban.
 */
public class TeleportGate extends Place implements Item, Steppable {
    /**
     * A teleportkapu aszteroidája, amin letették
     */
    private Asteroid asteroid;

    /**
     * A teleportkapu párja
     */
    private TeleportGate pair;

    /**
     * Azt jelzi, hogy a teleportkapu megkergült-e már
     */
    private boolean crazy;

    /**
     * View
     */
    TeleportGateView view;

    /**
     * Konstruktor
     */
    public TeleportGate(){
        crazy = false;
    }

    /**
     *  A teleportkapu magától mozgatja magát, ha megkergül.
     */
    @Override
    public void Step() {
        if (crazy){
            var asteroids = this.asteroid.GetNeighboringAsteroids();
            this.asteroid.RemoveTeleportGate(this);
            //vmi random, most elso
            asteroids.get(0).PlaceTeleport(this);
        }
    }

    @Override
    public int GetSteppablePriority() {
        return 3;
    }

    /**
     * Beállítja a paraméterként kapott aszteroidát a sajét aszteroidájaként.
     * @param asteroid: a beállítandó aszteroida
     */
    public void SetAsteroid(Asteroid asteroid) {
        this.asteroid = asteroid;
    }

    /**
     * Beállítja a paraméterként kapott teleportkaput a párjaként.
     * @param teleportGate: a beállítandó teleportkapu
     */
    public void SetPair(TeleportGate teleportGate) {
        this.pair = teleportGate;
    }

    /**
     * Visszatér a teleportkapu párjával
     * @return teleportgate: a teleportkapu párja
     */
    public TeleportGate GetPair(){
        return this.pair;
    }

    /**
     * Visszatért a teleportkapu aszteroidájával.
     * @return asteroid: a teleportkapu aszteroidája, ha letették valahova, null: ha nem
     */
    public Asteroid GetAsteroid() {
        return asteroid;
    }

    /**
     * Eltávolítja a teleportkaput a jelenlegi aszteroidájáról, illetve a párját is az övéről.
     */
    public void RemoveFromAsteroid() {
        pair.GetAsteroid().RemoveTeleportGate(pair);
        pair.SetAsteroid(null);
        if(asteroid != null)
            asteroid.RemoveTeleportGate(this);

        Game.getInstance().RemoveSteppable(this);
        Game.getInstance().RemoveSteppable(pair);

        this.view.TeleportGateDestroyed();
    }

    /**
     * Igazzal tér vissza, ha a paraméterként kapott Item ugyanolyan típusú, mint ő, egyébként hamis.
     * @param item: az összehasonlítandó item
     * @return true: megegyezik a típusuk, false: nem egyezik meg
     */
    @Override
    public boolean CompatibleWith(Item item) {
        return this.getClass() == item.getClass();
    }

    /**
     * Megpróbálja átteleportálni a charactert a teleportkapu párjának az aszteroidájára.
     * @param character: a teleportálandó karakter
     * @return Ha a párját már lehelyezték valahova, létrejön a teleportálás és visszatér True-val, ha nem, False-al.
     */
    @Override
    public boolean Move(Character character) {
        if(pair == null)
            return false;
        Asteroid asteroid = pair.GetAsteroid();
        return asteroid != null && asteroid.Move(character);
    }

    /**
     * Napvihar éri a teleportkaput, megkergül
     */
    public void HitByStorm(){
        crazy = true;
    }
}