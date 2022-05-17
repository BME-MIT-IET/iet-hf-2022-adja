package places;

import io.cucumber.java.en.Then;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StepDefinitionsTeleport {


    @Then("the player should be2 {string}")
    public void iShouldBeTold(String arg0) {
        assertEquals(arg0, "oks");
    }
}
