package core;

import characters.Settler;
import interfaces.Steppable;
import io.cucumber.java.ParameterType;
import io.cucumber.java.bs.A;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import places.Asteroid;
import places.AsteroidBelt;
import view.AsteroidView;
import view.Position;
import view.ViewController;

import static org.junit.jupiter.api.Assertions.*;

class IsItMoveing{
    static String isItMoveing(boolean moved){
        if (moved)
            return "Moved Successfully";
        else
            return "Not Moved";
    }


}

public class StepDefinitions {


    Game game = new Game();
    Asteroid startAsteroid;
    Asteroid anotherAsteroid;
    Settler player;
    String answer_1, answer_2;

    @Given("an asteroid")
    public void anAsteroid() {
        startAsteroid = new Asteroid();
        startAsteroid.SetView(new AsteroidView(startAsteroid,new Position(0,0),1));
        ViewController.getInstance().AddAsteroidView(startAsteroid,new Position(0,0));
        AsteroidBelt.getInstance().AddAsteroid(startAsteroid);
        //game.AddAsteroid(startAsteroid);
    }

    @Given("a player")
    public void aPlayer() {
        player = new Settler(startAsteroid);
        game.AddSettler(player);
        game.AddSteppable(player );
    }

    @When("I ask the player to move")
    public void iAskToMoveIt() {
        game.NextStep();
        answer_1 = IsItMoveing.isItMoveing(player.Move(null));
        boolean t = ViewController.getInstance().GetCurrentSettlerWaitingForInput().GetAsteroid().GetNeighboringAsteroids().contains(new Asteroid());
        answer_2 = IsItMoveing.isItMoveing(player.Move(new Asteroid()) && t);
    }

    @Then("I should be told {string}")
    public void iShouldBeTold(String arg0) {
        assertEquals(arg0, answer_1);

        assertEquals(arg0, answer_2);
    }


}
