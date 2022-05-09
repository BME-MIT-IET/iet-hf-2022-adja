package items;

import characters.Robot;
import characters.Settler;
import interfaces.Item;
import main.Game;
import materials.*;
import places.Asteroid;
import places.TeleportGate;
import view.ViewController;

import java.util.HashMap;
import java.util.Map;

/**
 * A telepesek a CraftingTable segítségével tudnak új craftolható eszközöket készíteni
 */
public class CraftingTable {
    /**
     * Az osztály egyetlen példánya (Singleton minta)
     */
    private static CraftingTable instance;

    /**
     * Az összes craftolható eszközhöz tartozó recept nyilvántartása
     */
    private final Map<Class<? extends Item>, Recipe> recipes;

    /**
     * Az osztály példányának lekérdezése
     * @return craftingtable: az egyetlen CraftingTable objektum
     */
    public static CraftingTable getInstance() {
        if(instance == null)
            instance = new CraftingTable();

        return instance;
    }

    /**
     * Konstruktor
     */
    private CraftingTable(){
        this.recipes = new HashMap<>();
    }

    /**
     * Igazzal tér vissza, ha sikerült
     * megcraftolni az elkészítendő itemet, hamissal, ha nem
     * @param itemType: a craftolandó eszköz típusa
     * @param settler: a settler, aki épít
     * @return true: sikerült a craftolás, false: nem sikerült
     */
    public boolean Craft(Class<? extends Item> itemType, Settler settler) {
        Inventory inventory = settler.GetInventory();
        Recipe recipe = this.recipes.get(itemType);
        boolean hasEnoughMaterial = this.HasEnoughMaterial(inventory, recipe);


        if(!hasEnoughMaterial) {
            return false;
        }
        else{
            if(itemType.equals(Robot.class)){
                Robot robot = new Robot(settler.GetAsteroid());

                Game.getInstance().AddSteppable(robot);
                Asteroid asteroid = settler.GetAsteroid();
                asteroid.Move(robot);

                return true;
            }
            else if(itemType.equals(TeleportGate.class)){
                TeleportGate teleportGate1 = new TeleportGate();
                TeleportGate teleportGate2 = new TeleportGate();

                inventory.AddItem(teleportGate1);
                inventory.AddItem(teleportGate2);

                teleportGate1.SetPair(teleportGate2);
                teleportGate2.SetPair(teleportGate1);

                Game game = Game.getInstance();
                game.AddSteppable(teleportGate1);
                game.AddSteppable(teleportGate2);

                ViewController.getInstance().AddTeleportGateView(teleportGate1, teleportGate2);

                return true;
            }
            else{
                return false;
            }
        }
    }

    /**
     * Megszámlálja az egyes tárolókban a különböző nyersanyagok számát, majd összehasonlítja őket,
     * hogy az inventoryban van-e elég nyersanyag a recipe szerinti item craftolásához, ha
     * igen, leveszi az inventory-ból a szükséges nyersanyagokat, majd visszatér igazzal. Ha
     * nincs elég nyersanyag, hamissal tér vissza
     * @param inventory: a telepes inventory-ja
     * @param recipe: az eszköz elkészítéséhez tartozó recept
     * @return true: van elég nyersanyaga a telepesnek, false: nincs elég
     */
    public boolean HasEnoughMaterial(Inventory inventory, Recipe recipe) {
        var inventoryAmounts = inventory.GetAmountOfMaterials();
        var recipeAmounts = recipe.GetAmountOfMaterials();

        for(var materialType : recipeAmounts.keySet()){
            if(!inventoryAmounts.containsKey(materialType)) {
                return false;
            }
            if(inventoryAmounts.get(materialType) < recipeAmounts.get(materialType)){
                return false;
            }
        }
        //has enough materials in the inventory if didnt't return

        var recipeMaterials = recipe.GetMaterials();

        for(Material material : recipeMaterials){
            inventory.RemoveMaterial(material);
        }

        return true;
    }

    /**
     * Hozzáad egy receptet az elkészítendő itemek receptjeihez
     * @param recipe: az új recept
     */
    public void AddRecipe(Recipe recipe) {
        this.recipes.put(recipe.GetItemType(), recipe);
    }
}
