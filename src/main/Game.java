package main;

import characters.Robot;
import characters.Settler;
import characters.UFO;
import interfaces.Steppable;
import items.CraftingTable;
import items.Recipe;
import materials.Coal;
import materials.Iron;
import materials.Material;
import materials.Uranium;
import materials.WaterIce;
import places.Asteroid;
import places.AsteroidBelt;
import places.TeleportGate;
import view.ViewController;
import view.Position;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *A Game osztály felelős a játék működéséért, ő tárolja kollektíven a játékban résztvevő
 * entitásokat, valamint ő kérdezgeti a lépésre képes résztvevőit a játéknak, hogy mit csinálnak a
 * jelenlegi körben.
 */
public class Game {
    /**
     * A Game osztály egyetlen példánya
     */
    private static Game instance;

    /**
     * A játékban található összes telepes (settler).
     */
    private final List<Settler> settlers;

    /**
     * Az adott körben még lépésre váró steppable-ök
     */
    private final Queue<Steppable> steppablesLeftinRound;

    /**
     * A játékban található lépésre képes (steppable) entitások.
     */
    private final List<Steppable> allSteppables;

    /**
     * A játék állapota
     */
    private GameState gameState;

    /**
     * Visszatér Game osztály egyetlen objetumával
     * @return testgame: az egyetlen TestGame objektum
     */
    public static Game getInstance() {
        if(instance == null)
            instance = new Game();

        return instance;
    }

    /**
     * Konstruktor
     */
    public Game(){
        this.settlers = new ArrayList<>();
        this.allSteppables = new ArrayList<>();
        this.steppablesLeftinRound = new PriorityQueue<>(30, Comparator.comparingInt(Steppable::GetSteppablePriority));

        this.gameState = GameState.NOTENDED;
    }

    /**
     * Elvégzi a játék inicializálását, majd elindítja a játékot.
     */
    public void Start() {
        this.Init();

        this.NextStep();
    }

    /**
     * Elvégzi a játék inicializálását
     */
    private void Init(){
        CraftingTable craftingTable = CraftingTable.getInstance();
        craftingTable.AddRecipe(new Recipe(Robot.class,
                Stream.of(new Coal(), new Iron(), new Uranium()).collect(Collectors.toList())));
        craftingTable.AddRecipe(new Recipe(TeleportGate.class,
                Stream.of(new Iron(), new Iron(), new WaterIce(), new Uranium()).collect(Collectors.toList())));

        Random random = new Random();
        List<Position> positions = new ArrayList<>();
        List<Asteroid> asteroids = new ArrayList<>();

        int asteroidNumber = 50;

        int[] p = new int[2];
        for(int i = 0; i < asteroidNumber; i++) {
            do {
                p[0] = random.nextInt(2000 + 2000) - 2000;
                p[1] = random.nextInt(2000 + 2000) - 2000;

                // Magic constant a sugarhoz, azert nem 42 + 42 hogy kicsit tavolabb legyenek egymastol
            } while (positions.stream().anyMatch(position -> Math.pow(p[0] - position.x, 2) + Math.pow(p[1] - position.y, 2) <= Math.pow(42 + 50, 2)));

            Position pos = new Position(p[0], p[1]);
            Asteroid ast = new Asteroid();

            positions.add(pos);
            asteroids.add(ast);
            AsteroidBelt.getInstance().AddAsteroid(ast);

            ViewController.getInstance().AddAsteroidView(ast, pos);
        }

        for(Asteroid ast : asteroids) {
            List<Double> distances = asteroids.stream()
                    .map(asteroid ->
                            Math.sqrt(Math.pow(positions.get(asteroids.indexOf(ast)).x - positions.get(asteroids.indexOf(asteroid)).x, 2) +
                                      Math.pow(positions.get(asteroids.indexOf(ast)).y - positions.get(asteroids.indexOf(asteroid)).y, 2)))
                    .collect(Collectors.toList());

            final List<Double> distancesCopy = new ArrayList<>(distances);
            distances.remove(distances.stream().mapToDouble(d -> d).min().getAsDouble());

            while(ast.GetNeighboringAsteroids().size() < 5 && distances.size() > 0) {
                double min = distances.stream().mapToDouble(d -> d).min().getAsDouble();
                Asteroid neighbor = asteroids.get(distancesCopy.indexOf(min));

                if(!ast.GetNeighboringAsteroids().contains(neighbor) && neighbor.GetNeighboringAsteroids().size() < 5) {
                    ast.AddNeighbor(neighbor);
                    neighbor.AddNeighbor(ast);
                }
                distances.remove(min);
            }
        }

        AsteroidBelt.getInstance().MakeItConnected();

        int numOfUFOs = random.nextInt(20 - 5) + 5;
        for(int i = 0; i < numOfUFOs; i++) {
            UFO ufo = new UFO(asteroids.get(random.nextInt(asteroids.size())));
            this.AddSteppable(ufo);
        }

        for(int i = 0; i < 5; i++) {
            Settler settler = new Settler(asteroids.get(random.nextInt(asteroids.size())));
            this.AddSteppable(settler);
            this.AddSettler(settler);
        }

        this.AddSteppable(AsteroidBelt.getInstance());
    }

    /**
     * Végrehajtja a következő lépést
     */
    public void NextStep(){
        if(steppablesLeftinRound.size() == 0){
            steppablesLeftinRound.addAll(allSteppables);
        }

        Steppable currentSteppable = this.steppablesLeftinRound.poll();
        if(currentSteppable == null) return;

        currentSteppable.Step();

        //check
        GameState currentGameState = this.CheckGameStatus();
        if(currentGameState != GameState.NOTENDED){
            this.gameState = currentGameState;
            ViewController.getInstance().GameEnded();
        }

        AsteroidBelt.getInstance().MakeItConnected();

        ViewController.getInstance().StepEnded();
    }

    /**
     * Visszatér a játék állapotával
     * @return a játék állapota
     */
    public GameState GetGameState(){
        return this.gameState;
    }

    /**
     * Ellenőrzi, hogy véget ért-e a játék. Ha a telepesek
     * nyertek (felépült a bázis), vesztetettek (minden
     * telepes meghalt, vagy megnyerhetetlenné vált a játék),
     * vagy még nem ért véget
     * @return SETTLERSLOST: a telepesek vesztettek, SETTLERSWON: a telepesek nyertek, NOTENDED: még nem ért véget
     */
    public GameState CheckGameStatus() {
        if(this.settlers.size() == 0)
            return GameState.SETTLERSLOST;
        Map<Asteroid, Map<Class<? extends Material>, Integer>> asteroidMaterialAmounts = new HashMap<>();

        //counting the current materialAmounts of all asteroids in asteroidbelt
        for(Settler settler : settlers){
            var asteroid = settler.GetAsteroid();
            var settlerAmounts = settler.GetInventory().GetAmountOfMaterials();

            if(!asteroidMaterialAmounts.containsKey(asteroid)){
                asteroidMaterialAmounts.put(asteroid, settlerAmounts);
            }
            else{
                var alreadyCountedMaterials = asteroidMaterialAmounts.get(asteroid);

                //merge alreadyCountedMaterials and settlerAmounts
                for(var materialType : settlerAmounts.keySet()){
                    Integer settlerAmount = settlerAmounts.get(materialType);

                    if(!alreadyCountedMaterials.containsKey(materialType)){
                        alreadyCountedMaterials.put(materialType, settlerAmount);
                    }
                    else{
                        Integer alreadyCountedAmount = alreadyCountedMaterials.get(materialType);
                        alreadyCountedMaterials.put(materialType, settlerAmount + alreadyCountedAmount);
                    }
                }

                asteroidMaterialAmounts.put(asteroid, alreadyCountedMaterials);
            }
        }

        //iterate through asteroids, decide if one has enough materials

        Map<Class<? extends Material>, Integer> goalAmount = new HashMap<>();
        goalAmount.put(Coal.class, 3);
        goalAmount.put(Iron.class, 3);
        goalAmount.put(Uranium.class, 3);
        goalAmount.put(WaterIce.class, 3);

        for(Asteroid asteroid : asteroidMaterialAmounts.keySet()) {
            var currentAmounts = asteroidMaterialAmounts.get(asteroid);

            boolean hasEnough = true;

            for (var materialType : goalAmount.keySet()) {
                if (!currentAmounts.containsKey(materialType)) {
                    hasEnough = false;
                    break;
                }
                if (currentAmounts.get(materialType) < goalAmount.get(materialType)) {
                    hasEnough = false;
                    break;
                }
            }

            if(hasEnough)
                return GameState.SETTLERSWON;
        }

        return GameState.NOTENDED;
    }

    /**
     * Felveszi a paraméterként kapott
     * steppablet a nyilvántartásba.
     * @param steppable: a hozzáadandó steppable
     */
    public void AddSteppable(Steppable steppable) {
        this.allSteppables.add(steppable);
        this.steppablesLeftinRound.add(steppable);
    }

    /**
     *  Eltávolítja a paraméterként kapott
     * steppable-t a nyilvántartásból.
     * @param steppable: az eltávolítandó steppable
     */
    public void RemoveSteppable(Steppable steppable) {
        this.allSteppables.remove(steppable);
        this.steppablesLeftinRound.remove(steppable);
    }

    /**
     * Hozzáad egy settlert a nyilvántartásba.
     * @param settler: a hozzáadandó settler
     */
    public void AddSettler(Settler settler) {
        this.settlers.add(settler);
    }

    /**
     *  Eltávolítja a paraméterként kapott settler-t a
     * settlerek nyilvántartásából.
     * @param settler: az eltávolítandó settler
     */
    public void RemoveSettler(Settler settler) {
        this.settlers.remove(settler);
    }
}
