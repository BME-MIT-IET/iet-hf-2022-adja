package places;

import characters.Character;

/**
 * A Place osztály egy olyan absztrakt osztály amely a játékban olyan dolgokat reprezentál amelyre rá lehet repülni.
 */
public abstract class Place {
    /**
     * @param character: a karakter, aki ide szeretne mozogni
     * @return true: sikerült a mozgás, false: nem sikerült
     */
    public abstract boolean Move(Character character);
}
