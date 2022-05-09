package materials;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A nyersanyagok tárolására szolgáló absztrakt osztály
 */
public class MaterialStorage {

    /**
     * A tárolóban lévő nyersanyagok
     */
    protected List<Material> materials;

    /**
     * Konstruktor
     */
    public MaterialStorage(){
        this.materials = new ArrayList<>();
    }

    /**
     * Hozzáadja a tárolóhoz a paraméterként
     * kapott nyersanyagot
     * @param material: a hozzáadandó material
     * @return true: sikerült hozzáadni, false: nem sikerült
     */
    public boolean AddMaterial(Material material) {
        materials.add(material);
        return true;
    }

    /**
     * Visszaad egy listát arról, hogy
     * melyik nyersanyagból mennyi található a tárolóban
     * @return nyersanyagok típusai és mennyiségei map-ként
     */
    public Map<Class<? extends Material>, Integer> GetAmountOfMaterials(){
        MaterialCounter materialCounter = new MaterialCounter();

        for(Material material : materials){
            material.Count(materialCounter);
        }

        return materialCounter.GetCountedMaterials();
    }

    /**
     *  Visszatér a tárolt nyersanyagok listájával
     * @return materials: a tárolt nyersanyagok listája
     */
    public List<Material> GetMaterials(){
        return this.materials;
    }
}
