package places;

import characters.Character;
import materials.*;
import view.AsteroidView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Az Asteroid osztály egy aszteroidát reprezentál, ami karakterek gyűjtőhelye. Az aszteroidán lévő karakterek
 * különböző műveletek végzésére képesek.
 */
public class Asteroid extends Place {
    /**
     * Az aszteroida köpenyének vastagsága
     */
    private int thickness;

    /**
     * Az aszteroida magjában található nyersanyag
     */
    private Material material;

    /**
     * Az aszteroidával szomszédos aszteroidák
     */
    private final List<Asteroid> neighbors;

    /**
     * Az aszteroidán található teleportkapuk
     */
    private final List<TeleportGate> teleportGates;

    /**
     * Az aszteroidán található karakterek
     */
    private final List<Character> characters;

    private AsteroidView view;

    /**
     * Konstruktor
     */
    public Asteroid() {
        neighbors = new ArrayList<>();
        teleportGates = new ArrayList<>();
        characters = new ArrayList<>();

        Random random = new Random();
        switch (random.nextInt(5)) {
            case 0: material = new Coal(); break;
            case 1: material = new Iron(); break;
            case 2: material = new Uranium(); break;
            case 3: material = new WaterIce(); break;
            case 4: material = null; break;
        }
        thickness = random.ints(2,6).findFirst().getAsInt();
    }

    /**
     * Csökkenti az aszteroida vastagságát.
     * @return True-val tér vissza, ha a fúrás sikerrel járt(nem volt még teljesen lefúrva az aszteroida),
     * False-al, ha nem(az aszteroida köpenye már teljesen át volt fúrva).
     */
    public boolean Drilled() {
        if((thickness - 1) >= 0) {
            thickness--;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Kiveszi az aszteroidában található nyersanyagot.
     * @return Ha az aszteroida magjában van nyersanyag, visszatér a nyersanyaggal, ha nem volt, null-al.
     */
    public Material RemoveMaterial() {
        if(thickness == 0 && material != null) {
            Material ret = material;
            material = null;
            return ret;
        } else {
            return null;
        }
    }

    /**
     * Napvihart kelt az aszteroidán. Meghívja az aszteroidán található karaktereken a saját HitByStorm metódusukat.
     */
    public void SolarFlare() {
        if(thickness != 0 || material != null) {
            int n = characters.size();
            List<Character> copy = List.copyOf(characters);
            for(int i = 0; i < n; i++)
                copy.get(i).HitByStorm();
        }

        for(TeleportGate teleportGate : teleportGates){
            teleportGate.HitByStorm();
        }
    }

    /**
     * Elment egy karakter az aszteroidáról
     * @param character: a karakter, aki elment az aszteroidáról
     */
    public void TakeOff(Character character) {
        this.characters.remove(character);
    }

    /**
     * Elhelyez egy teleportkaput az aszteroidán. Beállítja a teleportgate asteroid attribútumának saját magát.
     * @param teleportGate: az elhelyezendő teleportkapu
     */
    public void PlaceTeleport(TeleportGate teleportGate) {
        this.teleportGates.add(teleportGate);
        teleportGate.SetAsteroid(this);
    }

    /**
     * Hozzáad egy aszteroidát az aszteroida szomszédsági listájához.
     * @param asteroid: az új szomszédos aszteroida
     */
    public void AddNeighbor(Asteroid asteroid) {
        if(!neighbors.contains(asteroid))
            this.neighbors.add(asteroid);
    }

    /**
     * Törli a szomszédsági listából a paraméterként kapott aszteroidát.
     * @param asteroid: az eltávolítandó szomszédos aszteroida
     */
    public void RemoveNeighbor(Asteroid asteroid) {
        this.neighbors.remove(asteroid);
    }

    /**
     *  Felrobbantja az aszteroidát. Meghívja a rajta található karaktereken a HitByExplosion metódusukat, jelez az
     *  asteriodbeltnek, illetve megszünteti a teleportkapu összeköttetést, ha volt rajta éles teleportkapu.
     */
    public void Explosion() {
        List<Character> copy = List.copyOf(characters);
        for(int i = 0; i < copy.size(); i++)
            copy.get(i).HitByExplosion();

        List<TeleportGate> copyGates = List.copyOf(teleportGates);
        for(int i = 0; i < copyGates.size(); i++)
            copyGates.get(i).RemoveFromAsteroid();


        List<Asteroid> copyNeighbor = List.copyOf(neighbors);
        for(int i = 0; i < copyNeighbor.size(); ++i)
            copyNeighbor.get(i).RemoveNeighbor(this);

        AsteroidBelt.getInstance().AsteroidExploded(this);

        view.AsteroidExploded();
    }

    /**
     * Ha az aszteroida teljesen le van fúrva, valamint a magja rendelkezik nyersanyaggal, meghívja a magjában
     * található nyersanyagon az OnNearSun metódust.
     */
    public void NearSun() {
        if(thickness == 0 && material != null)
            material.OnNearSun(this);
    }

    /**
     * Eltávolítja az aszteroidáról a paraméterként kapott teleportkaput.
     * @param teleportGate: az eltávolítandó teleportkapu
     */
    public void RemoveTeleportGate(TeleportGate teleportGate) {
        this.teleportGates.remove(teleportGate);
    }

    /**
     * Visszatér az aszteroidáról elérhető helyekkel.
     * @return az aszteroidáról elérhető helyek
     */
    public List<Place> GetNeighbors() {
        List<Place> ret = new ArrayList<>(neighbors);
        ret.addAll(teleportGates);

        return ret;
    }

    /**
     * Visszatér a szomszédos aszteroidákkal
     * @return asteroids: szomszédos aszteroidák
     */
    public List<Asteroid> GetNeighboringAsteroids(){
        return this.neighbors;
    }

    /**
     * Egy karakter rámegy az aszteroidára
     * @param character: a karakter, aki idejött
     * @return true: sikerült a mozgás, false: nem sikerült
     */
    @Override
    public boolean Move(Character character) {
        this.characters.add(character);
        character.SetAsteroid(this);
        return true;
    }

    /**
     * Visszahelyezi a paraméterül kapott nyersanyagot az aszteroida magjába.
     * @param material: a visszahelyezendő nyersanyag
     * @return True-val tér vissza, ha sikerült visszahelyezni, egyébként False.
     */
    public boolean PlaceMaterial(Material material) {
        if(thickness == 0 && this.material == null){
            this.material = material;
            return true;
        }
        else
            return false;
    }

    /**
     * Visszaadja az aszteroida köpenyének vastagságát
     * @return thickness: az aszteroida köpenyének vastagsága
     */
    public int GetThickness() {
        return thickness;
    }

    /**
     * Visszatér az aszteroida magjában található nyersanyaggal
     * @return material: ha van benne nyersanyag, null: ha nincs benne
     */
    public Material GetMaterial() {
        return material;
    }

    /**
     * Visszatér az aszteroidán található teleportkapukkal
     * @return teleportgates: az aszteroidán található teleportkapuk
     */
    public List<TeleportGate> GetTeleportGates() {
        return teleportGates;
    }

    /**
     * Beállítja az aszteroida View-ját, hogy tudjon jelezni neki az állapotáról a modellben
     * @param view: view
     */
    public void SetView(AsteroidView view) {
        this.view = view;
    }
}
