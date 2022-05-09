package view;

import characters.Robot;
import characters.Settler;
import characters.UFO;
import main.Game;
import places.Asteroid;
import places.TeleportGate;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

/**
 * Gui vezérléséért felelős osztály
 */
public class ViewController {

    /**
     * Singleton minta szerinti egyetlen objektum az osztályból
     */
    private static ViewController instance;

    /**
     * Amiket önmagukban ki lehet rajzolni
     */
    private final List<Drawable> drawables;

    /**
     * InterfacePanel
     */
    private final InterfacePanel interfacePanel;

    /**
     * asteroid-asteroidview párositások kereshetőseg miatt
     */
    private final Map<Asteroid, AsteroidView> asteroidViewMap;

    /**
     * setller-settlerview párositások kereshetőseg miatt
     */
    private final Map<Settler, SettlerView> settlerViewMap;

    /**
     * Kiválaszttott aszteroida
     */
    private Clickable currentClickedAsteroid;

    /**
     * Utoljára kattintott aszteroida
     */
    private AsteroidView lastClickedAsteroid;

    /**
     * Ablak mérete
     */
    private final Position windowSize;

    /**
     * Éppen inputra várakozó telepes
     */
    private Settler currentSettlerWaitingForInput;

    /**
     * Jelzi, hogy hívható-e következő Step a modellben
     */
    private boolean canCallNextStep = false;

    /**
     * Jelzik a játék kirajzolásbeli állapotát
     */
    private boolean running = true;
    private boolean end = false;

    /**
     * Mozgás animációhoz szükséges lista
     */
    private final List<DrawableCharacter> queuedCharactersForMoving = new ArrayList<>();
    private final List<DrawableCharacter> queuedFinishedCharacters = new ArrayList<>();

    /**
     * Események kiírásához EventFeed
     */
    private final EventFeed eventFeed;

    /**
     * Konstruktor
     */
    private ViewController(){
        this.drawables = new ArrayList<>();
        this.asteroidViewMap = new HashMap<>();
        this.settlerViewMap = new HashMap<>();
        this.currentSettlerWaitingForInput = null;
        this.lastClickedAsteroid = null;
        this.interfacePanel = new InterfacePanel();
        this.drawables.add(interfacePanel);

        this.AddDrawable(new CameraView());

        this.eventFeed = new EventFeed();

        this.AddDrawable(this.eventFeed);

        this.windowSize = new Position(1000,563); // lehetne paraméterként kapni
    }

    /**
     * Visszatér a ViewController osztály egyetlen objektumával
     * @return ViewController osztály egyetlen objektuma
     */
    public static ViewController getInstance() {
        if (instance == null) {
            synchronized (ViewController.class) {
                if (instance == null) {
                    instance = new ViewController();
                }
            }

        }

        return instance;
    }

    /**
     * Elindítja a modellbeli játékot
     */
    public static void StartGame(){
        Game.getInstance().Start();
    }

    /**
     * Jelzés, hogy átméreteződött az ablak
     * @param DownRight: új méret
     */
    public void FrameResized(Position DownRight){
        this.windowSize.x = DownRight.x;
        this.windowSize.y = DownRight.y;
    }

    /**
     * Visszatér az ablak méretével
     * @return ablak mérete
     */
    public Position GetWindowSize(){
        return this.windowSize;
    }

    /**
     * Jelzés, hogy esemény történt az aszteroidamezőben
     * @param string: esemény leírása
     */
    public void EventHappened(String string){
        this.eventFeed.EventHappened(string);
    }

    /**
     * Lekezeli az ablakon történt kattintást
     * @param clickPos: kattintés pozíciója
     * @param cameraPos: kamera pozíciója
     */
    public boolean ClickHandler(Position clickPos, Position cameraPos){

        if(!this.interfacePanel.HandleClick(clickPos)) {
            return true;
        }

        if (currentClickedAsteroid!=null && currentClickedAsteroid.ClickedCheck(clickPos,cameraPos)){
            currentClickedAsteroid.Clicked(clickPos, cameraPos);
            return true;
        }

        for (SettlerView setView: settlerViewMap.values()
        ) {
            setView.UnClicked();
        }

        for (SettlerView setView: settlerViewMap.values()
        ) {
            if (setView.ClickedCheck(clickPos,cameraPos))
                return true;
        }

        ArrayList<AsteroidView> allClickables = new ArrayList<>(asteroidViewMap.values());

        for(Clickable clickable : allClickables)
            clickable.UnClicked();

        currentClickedAsteroid=null;
        for(Clickable clickable : allClickables){
            if(clickable.ClickedCheck(clickPos, cameraPos)) {
                currentClickedAsteroid = clickable;
                lastClickedAsteroid = (AsteroidView) clickable;
            }
        }

        if(currentClickedAsteroid != null) {
            currentClickedAsteroid.Clicked(clickPos, cameraPos);
            return true;
        }
        else
            return false;

    }

    /**
     * Jelzi, hogy hívható a következő Step
     */
    public void TimerTicked(){
        if(canCallNextStep)
            Game.getInstance().NextStep();
    }

    /**
     * Jelzi, hogy befejeződött a modellben a Step hívás
     */
    public void StepEnded(){
        this.canCallNextStep = this.currentSettlerWaitingForInput == null;
    }

    /**
     * Jelzi, hogy a paraméter telepes inputra várakozik
     * @param settler: inputra váró telepes
     */
    public void CurrentSettlerWaitingForInput(Settler settler){
        this.currentSettlerWaitingForInput = settler;
        interfacePanel.SetCurrentWaitingSettler(currentSettlerWaitingForInput);
        try {
            java.awt.Robot robot = new java.awt.Robot();
            robot.keyPress(KeyEvent.VK_SPACE);
        } catch (Exception ignored) {
        }
    }

    /**
     * Jelzi, hogy a telepes lépett
     */
    public void SettlerStepped(){
        this.currentSettlerWaitingForInput = null;
        this.canCallNextStep = true;
    }

    /**
     * Visszatér az inputra várakozó telepessel
     * @return inputra várakozó telepes
     */
    public Settler GetCurrentSettlerWaitingForInput(){
        return this.currentSettlerWaitingForInput;
    }

    /**
     * Kirajzolás
     * @param g: graphics
     * @param cameraPos: kamera pozíciója
     * @param cursorPos: kurzor pozíciója
     */
    public void DrawAll(Graphics2D g, Position cameraPos, Position cursorPos){
        if(!end){
            synchronized (drawables) {
                for(var c : queuedCharactersForMoving) {
                    if(!c.PlayMoveAnimation(g, cameraPos, asteroidViewMap.get(c.GetLastAsteroid()), asteroidViewMap.get(c.GetAsteroid())))
                        queuedFinishedCharacters.add(c);
                }
                for(var c : queuedFinishedCharacters)
                     queuedCharactersForMoving.remove(c);
                queuedFinishedCharacters.clear();

                 if(currentSettlerWaitingForInput != null)
                     asteroidViewMap.get(currentSettlerWaitingForInput.GetAsteroid()).Draw_Neighbours_and_Teleports(g, cameraPos, cursorPos, false, true);

                     asteroidViewMap.forEach((asteroid, asteroidView) ->  asteroidView.Draw_Neighbours_and_Teleports(g, cameraPos, cursorPos, true, false) );
                 if(lastClickedAsteroid != null)
                     lastClickedAsteroid.Draw_Neighbours_and_Teleports(g, cameraPos, cursorPos, false, false);

                 for (Drawable drawable : drawables) {
                     drawable.Draw(g, cameraPos);
                 }
            }
        }
        if(!running && queuedFinishedCharacters.size()==0 && queuedCharactersForMoving.size() == 0) {
            end = true;
            drawables.forEach(drawable -> drawable.Draw(g, cameraPos));
            try {
                Thread.sleep(5000);
                System.exit(0);
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Hozzáad egy DrawableCharactert a kirajzoláshoz
     * @param dc: DrawableCharacter
     */
    private void AddDrawableCharacter(DrawableCharacter dc){
        AsteroidView av = asteroidViewMap.get(dc.GetAsteroid());
        av.AddDrawableCharacter(dc);
    }

    /**
     * Megfelelo asteroidView hoz hozzaadja a drawableCharactert
     * @param ufo: ufo
     */
    public UFOView AddUFOView(UFO ufo){
        UFOView uv = new UFOView(ufo);
        this.AddDrawableCharacter(uv);

        return uv;
    }

    /**
     * Megfelelo asteroidView hoz hozzaadja a drawableCharactert
     * @param robot: robot
     */
    public RobotView AddRobotView(Robot robot){
        RobotView rv = new RobotView(robot);
        this.AddDrawableCharacter(rv);

        return rv;
    }

    /**
     * Megfelelo asteroidView hoz hozzaadja a drawableCharactert
     * @param settler: telepes
     */
    public SettlerView AddSettlerView(Settler settler){
        SettlerView sv = new SettlerView(settler);
        this.AddDrawableCharacter(sv);
        this.settlerViewMap.put(settler, sv);

        return sv;
    }

    /**
     * View hoz hozzaad egy asteroidview t, itt tortenik init is a koordinatak miatt
     * @param asteroid: aszteroida
     * @param position: aszteroida pozíciója
     */
    public void AddAsteroidView(Asteroid asteroid, Position position){
        AsteroidView av = new AsteroidView(asteroid, position, 2);
        asteroid.SetView(av);

        this.AddDrawable(av);
        this.asteroidViewMap.put(av.GetAsteroid(), av);
    }

    /**
     * Hozzáad egy TeleportGateView-t a kirajzoláshoz
     * @param teleportGate1: egyik teleportkapu
     * @param teleportGate2: másik teleportkapu
     */
    public void AddTeleportGateView(TeleportGate teleportGate1, TeleportGate teleportGate2){
        TeleportGateView tv = new TeleportGateView(teleportGate1, teleportGate2, 1);
        this.AddDrawable(tv);
    }

    /**
     * Visszatér a modellbeli aszteroidához tartozó AsteroidView-al
     * @param asteroid: modellbeli aszteroida
     * @return AsteroidView
     */
    public AsteroidView GetAsteroidView(Asteroid asteroid){
        return this.asteroidViewMap.get(asteroid);
    }

    /**
     * Visszatér a modellbeli telepeshez tartozó SettlerView-al
     * @param settler: modellbeli telepes
     * @return SettlerView
     */
    public SettlerView GetSettlerView(Settler settler){
        return this.settlerViewMap.get(settler);
    }

    /**
     * Hozzáad a kirajzoláshoz egy Drawable-t
     * @param d: drawable
     */
    public void AddDrawable(Drawable d){
        synchronized (drawables) {
            this.drawables.add(d);
            this.drawables.sort(Comparator.comparingInt(Drawable::GetZIndex));
        }
    }


    /**
     * DrawableCharacter jelzett, hogy a modellben elmozgott a karakter, itt is változtatjuk
     * @param dc: DrawableCharacter
     * @param oldAsteroid: régi aszteroida
     * @param newAsteroid: új aszteroida
     */
    public void CharacterMoved(DrawableCharacter dc, Asteroid oldAsteroid, Asteroid newAsteroid){
        AsteroidView av1 = this.asteroidViewMap.get(oldAsteroid);
        AsteroidView av2 = this.asteroidViewMap.get(newAsteroid);

        synchronized (drawables) {
            queuedCharactersForMoving.add(dc);
            av1.RemoveDrawableCharacter(dc);
        }
    }

    /**
     * DrawableCharacter jelzett, hogy modellben meghalt a karakter
     * @param dc: DrawableCharacter
     */
    public void CharacterDied(DrawableCharacter dc){
        AsteroidView av = this.asteroidViewMap.get(dc.GetAsteroid());

        av.RemoveDrawableCharacter(dc);

        this.EventHappened(dc.CharacterToString() + " died!");
    }

    /**
     * SettlerView jelzett, hogy meghalt a modellben
     * @param sv: SettlerView
     */
    public void SettlerDied(SettlerView sv){
        this.CharacterDied(sv);

        this.settlerViewMap.values().remove(sv);
    }

    /**
     * TeleportGateView jelzett, h modellben felrobbant teleportkapu, toroljuk viewbol
     * @param tv: TeleportGateView
     */
    public void TeleportGateDestroyed(TeleportGateView tv){
        this.drawables.remove(tv);
    }

    /**
     * Asteroida felrobbant, nem kene tovabb kirajzolni (karakterek magukat elintezik)
     * @param av: AsteroidView
     */
    public void AsteroidExploded(AsteroidView av){
        this.drawables.remove(av);
        this.asteroidViewMap.values().remove(av);
    }

    /**
     * Jelzés, hogy véget ért a játék a modellben
     */
    public void GameEnded(){
        running = false;
        this.AddDrawable(new EndGameAnimation());
    }
}
