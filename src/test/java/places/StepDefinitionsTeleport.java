package places;

import characters.Settler;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import view.AsteroidView;
import view.Position;
import view.ViewController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StepDefinitionsTeleport {

    Settler player;
    boolean moved = false;
    Map<String, Asteroid> tableAsteroids = new HashMap<String, Asteroid>();
    Map<String, TeleportGate> tableTeleports = new HashMap<String, TeleportGate>();
    @Given("asteroids:")
    public void followingAsteroids(List<String> asteroids) {
        tableAsteroids.put(asteroids.get(0),new Asteroid());
        tableAsteroids.put(asteroids.get(1),new Asteroid());
        tableAsteroids.put(asteroids.get(2),new Asteroid());
    }

    @Given("teleports:")
    public void followingTeleports(List<String> teleports) {
        tableTeleports.put(teleports.get(0),new TeleportGate());
        tableTeleports.put(teleports.get(1),new TeleportGate());
    }

    @Given("config Asteroid {string}")
    public void anAsteroid2(String arg0){
        Asteroid temp = tableAsteroids.get(arg0);
        temp.SetView(new AsteroidView(temp,new Position(0,0),1));
        ViewController.getInstance().AddAsteroidView(temp,new Position(0,0));
        AsteroidBelt.getInstance().AddAsteroid(temp);
        tableAsteroids.put(arg0,temp);
    }

    @Then("the player should be2 {string}")
    public void iShouldBeTold(String arg0) {
        assertEquals(arg0, "oks");
    }

    @Given("a player on asteroid {string}")
    public void aPlayerOnAsteroid(String arg0) {
        player = new Settler(tableAsteroids.get(arg0));
    }

    @Given("neighbour between {string} and {string}")
    public void neighbourBetweenAnd(String arg0, String arg1) {
        tableAsteroids.get(arg0).AddNeighbor(tableAsteroids.get(arg1));
        tableAsteroids.get(arg1).AddNeighbor(tableAsteroids.get(arg0));
    }

    @Given("pair {string} and {string} teleportgates")
    public void pairAndTeleportgates(String arg0, String arg1) {
        tableTeleports.get(arg0).SetPair(tableTeleports.get(arg1));
        tableTeleports.get(arg1).SetPair(tableTeleports.get(arg0));
    }

    @Given("add teleportgate {string} to player")
    public void addTeleportgateToPlayer(String arg0) {
        player.GetInventory().AddItem(tableTeleports.get(arg0));
    }


    @Then("the player should hold {int} teleport gates")
    public void thePlayerShouldHoldTeleportGates(int arg0) {
        assertEquals(arg0,player.GetInventory().GetTeleportGates().size());
    }

    @When("the player put down a teleportgate")
    public void thePlayerPutDownATeleportgate() {
        player.PlaceTeleportGate();
    }

    @When("the player moves to asteroid {string}")
    public void thePlayerMovesToAsteroid(String arg0) {
        if (player.GetAsteroid().GetNeighboringAsteroids().contains(tableAsteroids.get(arg0)))
            moved = player.Move(tableAsteroids.get(arg0));
        else
            moved = false;
    }

    @Then("the player {string} asteroid {string}")
    public void thePlayerAsteroid(String arg0, String arg1) {
        if (!moved){
            assertEquals(arg0,"stays on");
            assertEquals(player.GetAsteroid(),tableAsteroids.get(arg1));
        } else {
            assertEquals(arg0, "moved to");
            assertEquals(player.GetAsteroid(),tableAsteroids.get(arg1));
        }
        moved = false;
    }

    @When("the player moves to teleport {string}")
    public void thePlayerMovesToTeleport(String arg0) {
        if (player.GetAsteroid().GetNeighbors().contains(tableTeleports.get(arg0)))
            moved = player.Move(tableTeleports.get(arg0));
        else
            moved = false;
    }
}
