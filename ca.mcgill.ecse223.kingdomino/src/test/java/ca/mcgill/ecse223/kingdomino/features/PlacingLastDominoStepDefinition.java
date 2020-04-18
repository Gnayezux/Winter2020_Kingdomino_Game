package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.Gameplay;
import io.cucumber.java.en.*;

/**
 * @author Abdallah Shapsough
 */
public class PlacingLastDominoStepDefinition {
	
	@Given("the game has been initialized for placing last domino")
	public void the_game_has_been_initialized_for_placing_last_domino() {
		HelperClass.testSetup();
		Gameplay g = new Gameplay();
		KingdominoApplication.setGameplay(g);
		KingdominoApplication.getGameplay().setGamestatus("PlacingDomino");
	}

	@Given("it is the last turn of the game")
	public void it_is_the_last_turn_of_the_game() {
		KingdominoController.browseDominoPile();
	    for(int i = 0; i < 12; i++) {
	    	KingdominoController.setNextDraft();
	    }
	}

	@Then("the next player shall be placing his\\/her domino")
	public void the_next_player_shall_be_placing_his_her_domino() {
		assertEquals("placing", HelperClass.getState(KingdominoApplication.getGameplay().getGamestatusFullName()));
	}

	@Then("the game shall be finished")
	public void the_game_shall_be_finished() {
		System.out.println(KingdominoApplication.getGameplay().getGamestatusFullName());
		assertEquals("ending", HelperClass.getState(KingdominoApplication.getGameplay().getGamestatusFullName()));
	}

	@Then("the final results after successful placement shall be computed")
	public void the_final_results_after_successful_placement_shall_be_computed() {
		assertEquals("ending", HelperClass.getState(KingdominoApplication.getGameplay().getGamestatusFullName()));
	}
}
