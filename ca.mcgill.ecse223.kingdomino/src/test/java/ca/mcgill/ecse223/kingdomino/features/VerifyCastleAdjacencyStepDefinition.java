package ca.mcgill.ecse223.kingdomino.features;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Kingdomino;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
import ca.mcgill.ecse223.kingdomino.model.*;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;

public class VerifyCastleAdjacencyStepDefinition {
	int x;
	int y;
	DirectionKind direction;
	boolean result;
	@Given("the game is initialized for castle adjacency")
	public void the_game_is_initialized_for_castle_adjacency() {
	    Kingdomino aKingdomino = new Kingdomino();
	    //throw new cucumber.api.PendingException();
	}

	@Given("the current player preplaced the domino with ID {int} at position {int}:{int} and direction {direction}")
	public void the_current_player_preplaced_the_domino_with_ID_at_position_and_direction(Integer int1, Integer int2, Integer int3, DirectionKind aDirection) {
	    this.x = int2;
	    this.y = int3;
	    this.direction = aDirection;
	    
	}

	@When("check castle adjacency is initiated")
	public void check_castle_adjacency_is_initiated() {
	    // Write code here that turns the phrase above into concrete actions
		this.result = KingdominoController.VerifyCastleAdjacency(x, y, direction);
	}

	@Then("the castle\\/domino adjacency is {string}")
	public void the_castle_domino_adjacency_is(String string) {
	    // Write code here that turns the phrase above into concrete actions
	    if (this.result) {
	    	assertEquals(this.result,true);
	    } else {
	    	fail();
	    }
	}
}