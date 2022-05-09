package places;

import interfaces.Steppable;
import view.AsteroidBeltView;
import view.ViewController;

import java.util.*;

/**
 * Az aszteroidamezőt reprezentáló osztály, menedzseli az aszteroidamezőben található
 * aszteroidákat, állapotukat, illetve a közöttük lévő kapcsolatokat.
 */
public class AsteroidBelt implements Steppable {
    private static AsteroidBelt instance;

    /**
     * Az aszteroidamezőben található aszteroidák gyűjteménye.
     */
    private final List<Asteroid> asteroids;

    /**
     * View
     */
    private final AsteroidBeltView view;

    /**
     * Visszatér az AsteroidBelt osztály egyetlen objektumával
     * @return asteroidbelt: az egyetlen AsteroidBelt objektum
     */
    public static AsteroidBelt getInstance() {
        if(instance == null)
            instance = new AsteroidBelt();

        return instance;
    }

    /**
     * Konstruktor
     */
    private AsteroidBelt() {
        this.asteroids = new ArrayList<>();

        this.view = new AsteroidBeltView(this, 0);
        ViewController.getInstance().AddDrawable(this.view);
    }

    /**
     * Lépteti az aszteroidamezőt, aki választhat, hogy vagy nem fog történni
     * semmi, vagy napvihart kelt bizonyos aszteroidákon, vagy napközelbe kerül az
     * aszteroidamező összes aszteroidája.
     */
    @Override
    public void Step() {
        Random random = new Random();
        int rand = random.nextInt(5);

        if(rand == 0){
            this.NearSun();
        }
        else if(rand == 1){
            this.SolarFlare();
        }
    }

    @Override
    public int GetSteppablePriority() {
        return 4;
    }

    /**
     * Eltávolítja a paraméterül kapott
     * aszteroidát az aszteroidamezőből, vagyis törli a nyilvántartásból az aszteroidát, illetve
     * frissíti az érintett aszteroidák szomszédsági listáját.
     * @param asteroid: a felrobbant aszteroida
     */
    public void AsteroidExploded(Asteroid asteroid) {
        this.asteroids.remove(asteroid);
    }

    /**
     * Az aszteroidamező napközelbe került, vagyis meghívja az
     * aszteroidamezőben található összes aszteroidának a NearSun metódusát.
     */
    private void NearSun() {
        Random random = new Random();
        int numberOfAsteroidsInvolved = (int) (asteroids.size() * 0.2);
        int i = 0;

        while(i < numberOfAsteroidsInvolved && asteroids.size() != 0){
            int idx = random.nextInt(asteroids.size() -1);
            asteroids.get(idx).NearSun();
            ++i;
        }

        this.view.NearSun();
    }

    /**
     * Az aszteroidamező néhány aszteroidája napviharba került, vagyis
     * meghívja a napviharba kerület aszterodiáknak a SolarFlare metódusát.
     */
    private void SolarFlare() {
        Random random = new Random();
        int numberOfAsteroidsInvolved = (int) (asteroids.size() * 0.2);
        int i = 0;

        while(i < numberOfAsteroidsInvolved && asteroids.size() != 0){
            int idx = random.nextInt(asteroids.size() -1);
            asteroids.get(idx).SolarFlare();
            ++i;
        }

        this.view.SolarFlare();
    }

    /**
     * Hozzáad egy aszteroida az aszteroidamezőhöz
     * @param asteroid: az új aszteroida
     */
    public void AddAsteroid(Asteroid asteroid) {
        asteroids.add(asteroid);
    }

    /**
     * Összefüggővé alakítja az aszteroidamezőt
     */
    public void MakeItConnected(){
        List<List<Asteroid>> components = new ArrayList<>();

        List<Asteroid> visited;
        List<Asteroid> original;
        List<Asteroid> notVisited = this.asteroids;

        do{
            original = notVisited;
            notVisited = BFS(notVisited);

            visited = new ArrayList<>();
            for(Asteroid ast : original){
                if(!notVisited.contains(ast))
                    visited.add(ast);
            }

            components.add(visited);

        } while(notVisited.size() > 0);

        if(components.size() <= 1)
            return;

        Random random = new Random();

        for(int i = 0; i < components.size(); ++i){
            int componentIdx1, componentIdx2;
            int randomIdx1, randomIdx2;
            if(i == components.size() - 1){
                componentIdx1 = i;
                componentIdx2 = 0;
            }
            else{
                componentIdx1 = i;
                componentIdx2 = i+1;
            }
            randomIdx1 = random.nextInt(components.get(componentIdx1).size());
            randomIdx2 = random.nextInt(components.get(componentIdx2).size());

            Asteroid a1 = components.get(componentIdx1).get(randomIdx1);
            Asteroid a2 = components.get(componentIdx2).get(randomIdx2);

            a1.AddNeighbor(a2);
            a2.AddNeighbor(a1);
        }
    }

    /**
     * Az összefüggőség előállításához szükséges segédfüggvény (BFS algoritmus)
     * @param remainingAsteroids: még nem vizsgált komponense az aszteroidamezőnek
     * @return BFS után se vizsgált komponense az aszteroidamezőnek
     */
    private List<Asteroid> BFS(List<Asteroid> remainingAsteroids) {
        Map<Asteroid, Boolean> visitedAsteroidMap = new HashMap<>();
        for(Asteroid a: remainingAsteroids){
            visitedAsteroidMap.put(a, false);
        }

        Queue<Asteroid> queue = new LinkedList<>();

        Random random = new Random();
        Asteroid s = remainingAsteroids.get(random.nextInt(remainingAsteroids.size()));

        visitedAsteroidMap.put(s, true);

        queue.add(s);

        while(queue.size() != 0) {
            s = queue.poll();

            for (Asteroid n : s.GetNeighboringAsteroids()) {
                if (!visitedAsteroidMap.get(n)) {
                    visitedAsteroidMap.put(n, true);
                    queue.add(n);
                }
            }
        }

        List<Asteroid> notVisitedAsteroids = new ArrayList<>();

        for(Asteroid a : visitedAsteroidMap.keySet()) {
            if (!visitedAsteroidMap.get(a))
                notVisitedAsteroids.add(a);
        }

        return notVisitedAsteroids;
    }
}
