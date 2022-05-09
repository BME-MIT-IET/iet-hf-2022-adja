package items;

import interfaces.Item;
import materials.Material;
import materials.MaterialStorage;

import java.util.List;

/**
 * Egy készíthető Itemhez szükséges alapanyagok mennyiségének feljegyzése
 */
public class Recipe extends MaterialStorage {
    /**
     * A recepthez tartozó Item típusa
     */
    private final Class<? extends Item> itemType;

    /**
     * Konstruktor
     * @param itemType: a recepttel elkészíthető item típusa
     * @param materialList: a recept (nyersanyagok)
     */
    public Recipe(Class<? extends Item> itemType, List<Material> materialList){
        super();
        this.itemType = itemType;
        materials = materialList;
    }

    /**
     * Visszatér a recepthez tartozó Item típusával
     * @return itemType: a recepttel elkészíthető item típusa
     */
    public Class<? extends Item> GetItemType(){
        return this.itemType;
    }
}
