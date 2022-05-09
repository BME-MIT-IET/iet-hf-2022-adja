package materials;

import places.Asteroid;

/**
 * A vízjég az egyik nyersanyag amit ki lehet bányászni a játékban. Napközelben és teljesen megfúrt
 * aszteroidán elpárolog.
 */
public class WaterIce extends Material {
    /**
     * Lereagálja, hogy a nyersanyagot tartalmazó aszteroida napközelbe került.
     * @param asteroid: aszteroida, ami napközelbe került
     */
    @Override
    public void OnNearSun(Asteroid asteroid) {
        asteroid.RemoveMaterial();
    }

    /**
     * Növeli a paraméterként kapott számlálóban a típusához tartozó értéket.
     * @param counter: a számláláshoz használt segédosztály
     */
    @Override
    public void Count(MaterialCounter counter) {
        counter.Count(WaterIce.class);
    }
}
