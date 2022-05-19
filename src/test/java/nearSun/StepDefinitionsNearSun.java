package nearSun;

import characters.Settler;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import materials.Material;
import materials.Uranium;
import materials.WaterIce;
import places.Asteroid;
import places.AsteroidBelt;
import view.AsteroidView;
import view.Position;
import view.ViewController;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IsCoreExpected {
    static String isCoreExpected(Material materialCore, Material materialReq){
        if (materialCore == null)
            return "sublimated";
        if (materialCore.CompatibleWith(materialReq) && materialCore.CompatibleWith(new WaterIce()))
            return "water ice";
        if (materialCore.CompatibleWith(materialReq) && materialCore.CompatibleWith(new Uranium()))
            return "uran";
        else
            return "other material";
    }
}

public class StepDefinitionsNearSun {
    Asteroid asteroid;
    Settler player;
    WaterIce waterice = new WaterIce();
    Uranium uran = new Uranium();
    String answer;

    @Given("an asteroid with water ice core")
    public void anAsteroid() {
        asteroid = new Asteroid();
        boolean run = true;
        while (run)
            if(asteroid.GetMaterial() == null) {
                asteroid = new Asteroid();
            }
            else if(!asteroid.GetMaterial().CompatibleWith(waterice)) {
                asteroid = new Asteroid();
            }
            else
                run = false;
        asteroid.SetView(new AsteroidView(asteroid,new Position(0,0),1));
        ViewController.getInstance().AddAsteroidView(asteroid,new Position(0,0));
        AsteroidBelt.getInstance().AddAsteroid(asteroid);
        answer = IsCoreExpected.isCoreExpected(asteroid.GetMaterial(), waterice);
    }

    @Given("a settler")
    public void a_player_on_it() {
        player = new Settler(asteroid);
    }

    @When("settler drill asteroid crust")
    public void drillAsteroidCrustFull() {
        while(player.Drill())
            player.Drill();
        answer = IsCoreExpected.isCoreExpected(asteroid.GetMaterial(), waterice);
    }

    @Then("the asteroid's core should be {string}")
    public void getCoreMaterial(String string) {
        assertEquals(string,answer);
    }

    @When("I set asteroid near Sun")
    public void setAsteroidNearSun() {
        asteroid.NearSun();
        if(asteroid == null)
            answer = "exploded";
        else
            answer = IsCoreExpected.isCoreExpected(asteroid.GetMaterial(), waterice);
    }

    @When("I set asteroid with uran near Sun")
    public void setAsteroidNearSunUran() {
        asteroid.NearSun();
        if(IsCoreExpected.isCoreExpected(asteroid.GetMaterial(), uran).equals("other material"))
            answer = "exploded";
        else
            answer = IsCoreExpected.isCoreExpected(asteroid.GetMaterial(), uran);
    }

    @Given("an asteroid with uran core")
    public void anAsteroidUran() {
        asteroid = new Asteroid();
        boolean run = true;
        while (run)
            if(asteroid.GetMaterial() == null) {
                asteroid = new Asteroid();
            }
            else if(!asteroid.GetMaterial().CompatibleWith(uran)) {
                asteroid = new Asteroid();
            }
            else
                run = false;
        asteroid.SetView(new AsteroidView(asteroid,new Position(0,0),1));
        ViewController.getInstance().AddAsteroidView(asteroid,new Position(0,0));
        AsteroidBelt.getInstance().AddAsteroid(asteroid);
        answer = IsCoreExpected.isCoreExpected(asteroid.GetMaterial(), uran);
    }
}
