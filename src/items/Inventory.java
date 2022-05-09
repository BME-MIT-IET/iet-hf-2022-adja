package items;

import interfaces.Item;
import materials.*;
import places.TeleportGate;

import java.util.ArrayList;
import java.util.List;

/**
 * A telepes itt tárolja a nála lévő nyersanyagokat, illetve megcraftolt eszközöket. Maximum 10
 * db nyersanyagot képes eltárolni
 */
public class Inventory extends MaterialStorage {

    /**
     * Az inventoryban tárolt craftolt eszközök
     */
    private final List<Item> items;

    /**
     * Konstruktor
     */
    public Inventory(){
        super();
        this.items = new ArrayList<>();
    }

    /**
     * Megpróbálja hozzáadni a paraméterként
     * kapott nyersanyagot az inventoryhoz. Ha nincs elég hely, akkor a visszatérési érték
     * False, ha sikerült a hozzáadás, akkor True
     * @param material: a hozzáadandó material
     * @return true: sikerült hozzáadni, false: nem sikerült
     */
    @Override
    public boolean AddMaterial(Material material) {

        if(materials.size() < 10) {
            super.AddMaterial(material);
            return true;
        }
        else
            return false;
    }

    /**
     * Visszatér a paraméternek megfelelő
     * nyersanyaggal, ha megtalálja azt az inventory-ban, egyébként null-al
     * @param material: az eltávolítandó material
     * @return material: ha sikerült az eltávolítás, null: ha nem sikerült
     */
    public Material RemoveMaterial(Material material) {
        for(Material m : materials){
            if(material.CompatibleWith(m)){
                materials.remove(m);
                return m;
            }
        }
        return null;
    }

    /**
     * Felveszi a paraméterként kapott craftolt tárgyat(ami nem
     * nyersanyag) az inventoryba
     * @param item: a hozzáadandó item
     */
    public void AddItem(Item item) {
        items.add(item);
    }

    /**
     * Eltávolítja a paraméterként kapott craftolt tárgyat(ami
     * nem nyersanyag) az inventoryból
     * @param item: az eltávolítandó item
     */
    public void RemoveItem(Item item) {
        items.remove(item);
    }

    /**
     * Megkeresi és visszatér a paraméterként kapott típusnak megfelelő Item-el az
     * inventory-ból. Ha nem talált ilyet, akkor null-al.
     * @param itemType: a megkeresendő item típusa
     * @return item: ha sikerült megtalálni, null: ha nem sikerült
     */
    public Item GetItem(Class<? extends Item> itemType){
        Item searchHelperItem;
        if(itemType == TeleportGate.class)
            searchHelperItem = new TeleportGate();
        else {
            return null;
        }

        for(Item item : items){
            if(item.CompatibleWith(searchHelperItem)) {
                return item;
            }
        }

        return null;
    }

    /**
     * Egy bizonyos típusú itemből hány található meg az inventoryban
     * @param itemType: a keresendő itemek típusa
     * @return int: a típusból talált itemek száma
     */
    public int GetNumberOfItems(Class<? extends Item> itemType) {

        Item searchHelperItem;
        if(itemType == TeleportGate.class)
            searchHelperItem = new TeleportGate();
        else
            return 0;

        int count = 0;
        for(Item item : items) {
            if(item.CompatibleWith(searchHelperItem)) {
               ++count;
            }
        }

        return count;
    }

    /**
     * Visszatér az inventoryban található teleportkapukkal
     * @return teleportgates: az inventoryban található teleportkapuk listája
     */
    public List<TeleportGate> GetTeleportGates() {
        List<TeleportGate> teleportGates = new ArrayList<>();
        TeleportGate tg = new TeleportGate();
        for(Item item : items) {
            if(item.CompatibleWith(tg))
                teleportGates.add((TeleportGate)item);
        }

        return teleportGates;
    }
}
