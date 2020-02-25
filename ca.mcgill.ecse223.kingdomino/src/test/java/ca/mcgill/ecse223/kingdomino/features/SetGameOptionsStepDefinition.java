

package ca.mcgill.ecse223.kingdomino.features;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import static org.junit.Assert.fail;

public class SetGameOptionsStepDefinition {
	@Given("the game is initialized for set game options")
	public void the_game_is_initialized_for_set_game_options() {
	    // Write code here that turns the phrase above into concrete actions
	
		boolean initiliazed = KingdominoController.initializeGame();
		if(!initiliazed == true) {
			fail();
		}
//	    throw new cucumber.api.PendingException();
	}

	@When("set game options is initiated")
	public void set_game_options_is_initiated() {
	    // Write code here that turns the phrase above into concrete actions
		boolean setGame = KingdominoController.SetGameOptions(4,false,false);
		if(!setGame == true) {
			fail();
		}
//	    throw new cucumber.api.PendingException();
	}

	@When("the number of players is set to {int}")
	public void the_number_of_players_is_set_to(Integer int1) {
	    // Write code here that turns the phrase above into concrete actions
		
//	    throw new cucumber.api.PendingException();
	}

	@When("Harmony {string} selected as bonus option")
	public void harmony_selected_as_bonus_option(String string) {
	    // Write code here that turns the phrase above into concrete actions
//	    throw new cucumber.api.PendingException();
	}

	@When("Middle Kingdom {string} selected as bonus option")
	public void middle_Kingdom_selected_as_bonus_option(String string) {
	    // Write code here that turns the phrase above into concrete actions
//	    throw new cucumber.api.PendingException();
	}

	@Then("the number of players shall be {int}")
	public void the_number_of_players_shall_be(Integer int1) {
	    // Write code here that turns the phrase above into concrete actions
//	    throw new cucumber.api.PendingException();
	}

	@Then("Harmony {string} an active bonus")
	public void harmony_an_active_bonus(String string) {
	    // Write code here that turns the phrase above into concrete actions
//	    throw new cucumber.api.PendingException();
	}

	@Then("Middle Kingdom {string} an active bonus")
	public void middle_Kingdom_an_active_bonus(String string) {
	    // Write code here that turns the phrase above into concrete actions
//	    throw new cucumber.api.PendingException();
	}
}

