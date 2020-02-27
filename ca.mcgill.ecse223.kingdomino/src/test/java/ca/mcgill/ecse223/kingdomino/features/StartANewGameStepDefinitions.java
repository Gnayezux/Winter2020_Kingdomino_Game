package ca.mcgill.ecse223.kingdomino.features;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Kingdomino;
import ca.mcgill.ecse223.kingdomino.model.User;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;


public class StartANewGameStepDefinitions {
	
	Kingdomino kingdomino;
	@Given("the program is started and ready for starting a new game")
	public void the_program_is_started_and_ready_for_starting_a_new_game() {
	    // Write code here that turns the phrase above into concrete actions
		kingdomino = KingdominoApplication.getKingdomino();
		//User user = kingdomino.addUser("User1");
	    //throw new cucumber.api.PendingException();
	}

	@Given("there are four selected players")
	public void there_are_four_selected_players() {
	    
		// Write code here that turns the phrase above into concrete actions
//		Kingdomino kingdomino = KingdominoApplication.getKingdomino();
		Game newGame = new Game(48,kingdomino);
		newGame.setNumberOfPlayers(4);
		kingdomino.setCurrentGame(newGame);
		//kingdomino.setCurrentGame(newGame);
	    //throw new cucumber.api.PendingException();
	}

	@Given("bonus options Harmony and MiddleKingdom are selected")
	public void bonus_options_Harmony_and_MiddleKingdom_are_selected() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new cucumber.api.PendingException();
	}

	@When("starting a new game is initiated")
	public void starting_a_new_game_is_initiated() {    
		// Write code here that turns the phrase above into concrete actions
		Kingdomino kingdomino = KingdominoApplication.getKingdomino();
		KingdominoController.startNewGame(kingdomino);
	    throw new cucumber.api.PendingException();
	}

	@When("reveal first draft is initiated")
	public void reveal_first_draft_is_initiated() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new cucumber.api.PendingException();
	}

	@Then("all kingdoms shall be initialized with a single castle")
	public void all_kingdoms_shall_be_initialized_with_a_single_castle() {
	    // Write code here that turns the phrase above into concrete actions
		//assertEquals for all thens 
	    throw new cucumber.api.PendingException();
	}

	@Then("all castle are placed at {int}:{int} in their respective kingdoms")
	public void all_castle_are_placed_at_in_their_respective_kingdoms(Integer int1, Integer int2) {
	    // Write code here that turns the phrase above into concrete actions
	    throw new cucumber.api.PendingException();
	}

	@Then("the first draft of dominoes is revealed")
	public void the_first_draft_of_dominoes_is_revealed() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new cucumber.api.PendingException();
	}

	@Then("all the dominoes form the first draft are facing up")
	public void all_the_dominoes_form_the_first_draft_are_facing_up() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new cucumber.api.PendingException();
	}

	@Then("all the players have no properties")
	public void all_the_players_have_no_properties() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new cucumber.api.PendingException();
	}

	@Then("all player scores are initialized to zero")
	public void all_player_scores_are_initialized_to_zero() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new cucumber.api.PendingException();
	}

}
