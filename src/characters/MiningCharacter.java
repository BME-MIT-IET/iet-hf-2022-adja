package characters;

import materials.Material;
import materials.MaterialStorage;

/**
 * Bányászni képes karaktert reprezentáló osztály
 */
public abstract class MiningCharacter extends Character {

    /**
     * A karakter megpróbálja kinyeri az aszteroidában található
     * nyersanyagot. Ha az aszteroida magja üres, akkor False a visszatérési érték, ha sikerült
     * a bányászás, akkor True
     * @param inventory: a tároló, amibe a bányászott nyersanyagot eltesszük
     * @return true: a bányászás sikerült, false: nem sikerült
     */
    public boolean Mine(MaterialStorage inventory) {
        Material material = asteroid.RemoveMaterial();
        if (material!=null){
            if (!inventory.AddMaterial(material)) {
                asteroid.PlaceMaterial(material);
            }
            else {
                return true;
            }
        }

        return false;
    }
}
