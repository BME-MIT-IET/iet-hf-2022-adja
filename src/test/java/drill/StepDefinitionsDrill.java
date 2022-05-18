package drill;

import characters.Settler;
import io.cucumber.java.en.*;
import places.Asteroid;
import places.AsteroidBelt;
import view.AsteroidView;
import view.Position;
import view.ViewController;

import static org.junit.jupiter.api.Assertions.*;

class IsItDrilling{
    static String isItDrilling(boolean drill){
        if (drill)
            return "drilling";
        else
            return "not drilling";
    }
}

public class StepDefinitionsDrill {
    Asteroid asteroid;
    Settler player;
    String answer;

    @Given("an asteroid")
    public void an_asteroid() {
        asteroid = new Asteroid();
        asteroid.SetView(new AsteroidView(asteroid,new Position(0,0),1));
        ViewController.getInstance().AddAsteroidView(asteroid,new Position(0,0));
        AsteroidBelt.getInstance().AddAsteroid(asteroid);
    }
    @Given("a player on it")
    public void a_player_on_it() {
        player = new Settler(asteroid);
    }
    @When("I ask the player to drill")
    public void i_ask_the_player_to_drill() {
        boolean success = player.Drill();
        answer = IsItDrilling.isItDrilling(success);
    }
    @Then("the player should be {string}")
    public void the_player_should_be(String string) {
        assertEquals(string,answer);
    }

    @Given("an asteroid with zero thickness")
    public void an_asteroid_with_zero_thickness() {
        asteroid = new Asteroid();
        while(asteroid.GetThickness() != 0){asteroid.Drilled();}
        asteroid.SetView(new AsteroidView(asteroid,new Position(0,0),1));
        ViewController.getInstance().AddAsteroidView(asteroid,new Position(0,0));
        AsteroidBelt.getInstance().AddAsteroid(asteroid);
    }
}
