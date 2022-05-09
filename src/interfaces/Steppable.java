package interfaces;

/**
 * Ez egy interface. Az őt megvalósító osztályokat lépteti a Game.
 */
public interface Steppable {

    /**
     * Az adott körben végzendő tevékenységek kiválasztása és elvégzése.
     */
    void Step();

    /**
     * Visszatér, hogy a Steppable típus hanyadikként lép a játékban
     * @return prioritás
     */
    int GetSteppablePriority();
}
