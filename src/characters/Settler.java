package characters;

import items.CraftingTable;
import items.Inventory;
import main.Game;
import materials.Material;
import places.Asteroid;
import places.Place;
import places.TeleportGate;
import view.ViewController;

/**
 * A telepeseket reprezentáló osztály. A játékos velük interaktál közvetlenül
 */
public class Settler extends MiningCharacter {
    /**
     * A játékos inventoryja, amiben a nála található dolgokat tárolja.
     */
    private final Inventory inventory;
    /**
     * Settler konstruktora
     */
    public Settler(Asteroid ast) {
        inventory = new Inventory();
        this.asteroid = ast;
        this.view = ViewController.getInstance().AddSettlerView(this);
    }

    /**
     * Settler "destruktora"
     * Törli magát a game-ből, illetve ha van nála olyan teleportkapu, aminek
     * a párját már letette, azt is törli az aszteroidájáról
     */
    private void die() {
        for(TeleportGate tg : inventory.GetTeleportGates())
            tg.RemoveFromAsteroid();
        Game.getInstance().RemoveSteppable(this);
        Game.getInstance().RemoveSettler(this);
        this.DiedAlert();
        asteroid.TakeOff(this);
    }

    /**
     * Lereagálja, hogy az aszteroida, amin a telepes éppen
     * tartózkodik felrobbant. Törli magát mindenhonnan, ahol nyilván volt tartva.
     */
    public void HitByExplosion() {
        die();
    }

    /**
     * Lereagálja, hogy az aszteroida, amin a telepes éppen tartózkodik
     * napviharba került. Törli magát mindenhonnan, ahol nyilván volt tartva.
     */
    @Override
    public void HitByStorm() {
        die();
    }

    @Override
    public boolean Move(Place place) {
        Asteroid currentAsteroid = this.asteroid;
        if(place.Move(this)){
            currentAsteroid.TakeOff(this);

            this.view.CharacterMoved(currentAsteroid, this.asteroid);

            return true;
        }
        return false;
    }

    /**
     * A telepes megpróbál bányászni, ha sikerült, a saját inventory-jába teszi el
     * a bányászott nyersanyagot
     * @return true: a bányászás sikerült, false: nem sikerült
     */
    public boolean Mine() {
        return this.Mine(inventory);
    }

    /**
     * A telepes ezzel készít robotot. Ha sikerült a craftolás, a
     * visszatérési érték True, egyébként False.
     * @return true: sikerült a craftolás, false: nem sikerült
     */
    public boolean CraftRobot() {
        return CraftingTable.getInstance().Craft(Robot.class, this);
    }

    /**
     * A telepes ezzel készít teleportkapupárt. Ha sikerült a
     * craftolás, a visszatérési érték True, egyébként False.
     * @return true: sikerült a craftolás, false: nem sikerült
     */
    public boolean CraftTeleportGates() {
        int numberOfTeleportGates = inventory.GetNumberOfItems(TeleportGate.class);
        if (numberOfTeleportGates <= 1) {
            return CraftingTable.getInstance().Craft(TeleportGate.class, this);
        }

        return false;
    }

    /**
     * A telepes megpróbálja elhelyezni a nála található
     * teleportkapupár egyik tagját az aszteroidáján. Ha sikerrel járt, a visszatérési érték
     * True, egyébként False.
     * @return true: sikerült letenni, false: nem sikerült
     */
    public boolean PlaceTeleportGate() {
        TeleportGate teleportGate = GetTeleportGate();
        if (teleportGate!=null){
            asteroid.PlaceTeleport(teleportGate);
            inventory.RemoveItem(teleportGate);
            return true;
        }

        return false;
    }

    /**
     * Visszatér egy a telepesnél található teleportkapuval.
     * @return teleportgate: a telepes egy teleportkapuja, ha van nála, null: ha nincs nála
     */
    public TeleportGate GetTeleportGate() {
        return (TeleportGate) this.inventory.GetItem(TeleportGate.class);
    }

    /**
     * Inventory gettere
     * @return inventory: a telepes inventory-ja
     */
    public Inventory GetInventory(){
        return inventory;
    }

    /**
     * Elhelyezi az aszteroida magjába a
     * nyersanyagot tárolás céljából. Ha nem üres az aszteroida magja akkor false a
     * visszatérési érték, ha sikeresen lehelyeztük akkor true.
     * @param material: az elhelyezendő material
     * @return true: sikerült az elhelyezés, false: nem sikerült
     */
    public boolean PlaceMaterial(Material material) {
        Material pickedMaterial = inventory.RemoveMaterial(material);
        if (pickedMaterial!=null){
            if (!asteroid.PlaceMaterial(pickedMaterial)){
                inventory.AddMaterial(pickedMaterial);
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * Eggyel csökkenti az aszteroida köpenyének rétegét.True a
     * visszatérési érték ha sikeresen végrehajtódott a művelet,false ha nem lehet tovább
     * fúrni az aszteroidán mivel nincs már rajta köpeny réteg.
     * @return true: sikerült a fúrás, false: nem sikerült
     */
    public boolean Drill() {
        return asteroid.Drilled();
    }

    /**
     * A telepes lépésének végrehajtása. Itt választhat a játékos a végezhető
     * tevékenységek közül (pl: move, drill, placeteleport, stb.)
     */
    @Override
    public void Step() {
        ViewController.getInstance().CurrentSettlerWaitingForInput(this);
    }

    @Override
    public int GetSteppablePriority() {
        return 0;
    }
}
