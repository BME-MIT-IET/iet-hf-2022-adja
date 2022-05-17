package core;

import characters.Settler;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import places.Asteroid;
import places.AsteroidBelt;
import view.AsteroidView;
import view.Position;
import view.ViewController;

import static org.junit.jupiter.api.Assertions.*;

class IsItMoving{
    static String isItMoveingToNull(boolean moved){
        if (moved)
            return "moved somewhere";
        else
            return "not moved";
    }

    static String isItMoveingToNotNeighbour(boolean moved){
        if (moved)
            return "moved there";
        else
            return "stay still";
    }

}

public class StepDefinitionsMove {

    ViewController view;
    Asteroid startAsteroid, anotherAsteroid, neighbourAsteroid;
    Settler player;
    String answer;

    @Given("an asteroid")
    public void anAsteroid() {
        startAsteroid = new Asteroid();
        startAsteroid.SetView(new AsteroidView(startAsteroid,new Position(0,0),1));
        ViewController.getInstance().AddAsteroidView(startAsteroid,new Position(0,0));
        AsteroidBelt.getInstance().AddAsteroid(startAsteroid);
    }

    @Given("a player on it")
    public void aPlayer() {
        player = new Settler(startAsteroid);
    }

    @When("I ask the player to move into void")
    public void iAskToMoveIt() {
        startAsteroid.RemoveNeighbor(anotherAsteroid);
        answer = IsItMoving.isItMoveingToNull(player.Move(null));
      }

    @Then("the player should be {string}")
    public void iShouldBeTold(String arg0) {
        assertEquals(arg0, answer);
    }

    @Given("an another asteroid")
    public void anAnotherAsteroid() {
        anotherAsteroid = new Asteroid();
        anotherAsteroid.SetView(new AsteroidView(anotherAsteroid,new Position(2000,2000),2));
        ViewController.getInstance().AddAsteroidView(anotherAsteroid,new Position(2000,2000));
        AsteroidBelt.getInstance().AddAsteroid(anotherAsteroid);
    }

    @When("I ask the player to move there")
    public void iAskThePlayerToMoveThere() {
        ViewController.getInstance().CurrentSettlerWaitingForInput(player);
        startAsteroid.RemoveNeighbor(anotherAsteroid);
        boolean t = ViewController.getInstance().GetCurrentSettlerWaitingForInput().GetAsteroid().GetNeighboringAsteroids().contains(anotherAsteroid);
        answer = IsItMoving.isItMoveingToNotNeighbour(player.Move(anotherAsteroid) && t);
    }


    @Then("the player {string}")
    public void thePlayer(String arg0) {
        assertEquals(arg0, answer);
    }

    @Given("a neighbour asteroid")
    public void aNeighbourAsteroid() {
        neighbourAsteroid = new Asteroid();
        neighbourAsteroid.AddNeighbor(startAsteroid);
        startAsteroid.AddNeighbor(neighbourAsteroid);
        neighbourAsteroid.SetView(new AsteroidView(neighbourAsteroid,new Position(0,0),1));
        ViewController.getInstance().AddAsteroidView(neighbourAsteroid,new Position(0,0));
        AsteroidBelt.getInstance().AddAsteroid(neighbourAsteroid);
    }

    @When("I ask the player to move to the neighbour")
    public void iAskThePlayerToMoveToTheNeighbour() {
        ViewController.getInstance().CurrentSettlerWaitingForInput(player);
        startAsteroid.RemoveNeighbor(anotherAsteroid  );
        boolean t = ViewController.getInstance().GetCurrentSettlerWaitingForInput().GetAsteroid().GetNeighboringAsteroids().contains(neighbourAsteroid);
        answer = IsItMoving.isItMoveingToNotNeighbour(player.Move(neighbourAsteroid) && t  );
    }
}
