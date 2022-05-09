package materials;

import places.Asteroid;

/**
 * Az Urán az egyik nyersanyag amit ki lehet bányászni a játékban. Ha napközelben van és teljesen meg van fúrva az
 * aszteroida akkor felrobban, rajta lévő telepesek mind meghalnak, robotok pedig egy másik aszteroidán landolnak.
 */
public class Uranium extends RadioactiveMaterial {

    /**
     * Még hátralévő napközelek száma a robbanásig
     */
    private int nearSuns;

    /**
     * Default konstruktor
     */
    public Uranium(){
        super();
        nearSuns = 2;
    }

    /**
     * Konstruktor, a hátralévő napközelek száma állítható
     * (csak teszteléshez)
     * @param expositions: hátralévő napközelek
     */
    public Uranium(int expositions){
        super();
        nearSuns = expositions;
    }

    /**
     * Növeli a paraméterként kapott számlálóban a típusához tartozó értéket.
     * @param counter: a számoláshoz használt segédosztály
     */
    @Override
    public void Count(MaterialCounter counter) {
        counter.Count(Uranium.class);
    }

    /**
     * Napközelbe érve egy teljesen megfúrt aszteroidán felrobban, vele együtt az aszteroida is.
     * @param asteroid: a nyersanyagot tartalmazó aszteroida
     */
    @Override
    public void OnNearSun(Asteroid asteroid) {
        nearSuns--;
        if (nearSuns < 0)
            asteroid.Explosion();

    }
}
