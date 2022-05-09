package materials;

import java.util.HashMap;
import java.util.Map;

/**
 * Nyersanyagok megszámlálására alkalmas segédosztály
 */
public class MaterialCounter {

    /**
     * A számláláskor az egyes nyersanyagok ezen
     * a listán növelik a hozzájuk tartozó előfordulási számot
     */
    private final Map<Class<? extends Material>, Integer> amountOfMaterials;

    public MaterialCounter(){
        this.amountOfMaterials = new HashMap<>();
    }

    /**
     * Megnöveli az amountOfMaterials attribútum
     * materialType-hoz tartozó értékét
     * @param materialType: a megnövelendő nyersanyag típusa
     */
    public void Count(Class<? extends Material> materialType){
        if(amountOfMaterials.containsKey(materialType)){
            Integer amount = amountOfMaterials.get(materialType);
            amountOfMaterials.put(materialType, amount + 1);
        }
        else{
            amountOfMaterials.put(materialType, 1);
        }
    }

    /**
     * Visszaad egy listát arról, hogy melyik
     * nyersanyagból mennyi van
     * @return megszámlált nyersanyagok típusai és mennyiségei map-ként
     */
    public Map<Class<? extends Material>, Integer> GetCountedMaterials(){
        return this.amountOfMaterials;
    }
}
