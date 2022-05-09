package view;

import places.AsteroidBelt;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AsteroidBeltView extends Drawable {

    /**
     * Modell
     */
    private final AsteroidBelt asteroidBelt;

    /**
     * Az aszteroidamező csillagainak pozíciói
     */
    private final List<Position> stars;

    /**
     * Jelzi, hogy napvihar van-e
     */
    private boolean solarFlareHappening;

    /**
     * Jelzi, hogy napközel van-e
     */
    private boolean nearSunHappening;

    /**
     * Animáció milyen állapotban van
     */
    private int animationCounter;

    /**
     * Animáció ideje
     */
    private final int animationCounterMax;

    /**
     * Konstruktor
     * @param ab: modell
     * @param z: hányadik rétegre kell kirajzolni
     */
    public AsteroidBeltView(AsteroidBelt ab, int z){
        this.asteroidBelt = ab;
        this.zIndex = z;

        this.solarFlareHappening = false;
        this.nearSunHappening = false;
        this.animationCounter = 0;
        this.animationCounterMax = ViewController.getInstance().GetWindowSize().x;;

        this.stars = new ArrayList<>();

        Random random = new Random();
        for(int i = 0; i < 2000; ++i){
            stars.add(new Position(random.nextInt(2500*2)-2500, random.nextInt(2500*2)-2500));
        }
    }

    /**
     * Kirajzolás
     * @param graphics: graphics
     * @param cameraPos: kamera pozíciója
     */
    @Override
    public void Draw(Graphics2D graphics, Position cameraPos) {
        graphics.setColor(Color.WHITE);
        stars.forEach(star -> graphics.fillOval(star.x - cameraPos.x, star.y - cameraPos.y, 1, 1));

        int vx= ViewController.getInstance().GetWindowSize().x;
        int vy= ViewController.getInstance().GetWindowSize().y;

        if(solarFlareHappening){
            graphics.setColor(new Color(1.0f, 0.369f, 0.075f, (float)Math.max((float)animationCounter/(float)animationCounterMax,1.0)));
            Position windowSize = ViewController.getInstance().GetWindowSize();
            graphics.fillRect(0,0,windowSize.x,windowSize.y);

            graphics.setColor(new Color(255, 0, 0, 255));
            graphics.setFont(new Font("Monospaced", Font.BOLD, vx/10));
            FontMetrics metrics = graphics.getFontMetrics(new Font("Monospaced", Font.BOLD, vx/10));
            graphics.drawString("Solar Flare!",vx/2- metrics.stringWidth("Solar Flare!") / 2,vy/2);


            animationCounter+= (int) (float)vx/100.0f;
            if(animationCounter >= animationCounterMax){
                animationCounter = 0;
                solarFlareHappening = false;
            }
        }
        if(nearSunHappening){
            graphics.setColor(new Color(1.0f, 0.369f, 0.075f));
            int position= (int)((double)animationCounter*(vx/10)/(double)animationCounterMax)-vx;
            graphics.fillOval(position, ViewController.getInstance().GetWindowSize().y/2-vy,2*vy,2*vy);

            graphics.setColor(new Color(255, 0, 0, 255));
            graphics.setFont(new Font("Monospaced", Font.BOLD, vx/10));
            FontMetrics metrics = graphics.getFontMetrics(new Font("Monospaced", Font.BOLD, vx/10));
            graphics.drawString("Near Sun!",vx/2- metrics.stringWidth("Near Sun!") / 2,vy/2);

            animationCounter+= (int) (float)vx/10.0f;
            if(position >= ViewController.getInstance().GetWindowSize().x){
                animationCounter = 0;
                nearSunHappening = false;
            }

        }
    }

    /**
     * Jelzés, hogy napviharba került az aszteroidamező
     */
    public void SolarFlare(){
        this.solarFlareHappening = true;
    }

    /**
     * Jelzés, hogy napközelbe került az aszteroidamező
     */
    public void NearSun(){
        this.nearSunHappening = true;
    }
}
