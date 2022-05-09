package materials;

import places.Asteroid;

/**
 * A radioaktív nyersanyagok belőle származnak le.
 */
public abstract class RadioactiveMaterial extends Material {

    /**
     * Napközelbe érve egy teljesen megfúrt aszteroidán felrobban, vele együtt az aszteroida is.
     * @param asteroid: a nyersanyagot tartalmazó aszteroida
     */
    @Override
    public void OnNearSun(Asteroid asteroid) {
        asteroid.Explosion();
    }
}
