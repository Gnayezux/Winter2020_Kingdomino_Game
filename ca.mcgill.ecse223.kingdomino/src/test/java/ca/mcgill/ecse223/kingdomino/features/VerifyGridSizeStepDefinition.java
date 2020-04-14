package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.*;
import io.cucumber.java.en.*;

public class VerifyGridSizeStepDefinition {
	boolean valid = false;

	/**
	 * @author Zeyang Xu
	 */
	@Given("the game is initialized for verify grid size")
	public void the_game_is_initialized_for_verify_grid_size() {
		HelperClass.testSetup();
		KingdominoController.generateInitialPlayerOrder();
	}

	/**
	 * @author Zeyang Xu
	 */
	@When("validation of the grid size is initiated")
	public void validation_of_the_grid_size_is_initiated() {
		valid = KingdominoController
				.verifyGridSize(KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer().getKingdom());
	}

	/**
	 * @author Zeyang Xu
	 */
	@Then("the grid size of the player's kingdom shall be {string}")
	public void the_grid_size_of_the_player_s_kingdom_shall_be(String string) {
		String isValid;
		if (valid) {
			isValid = "valid";
		} else {
			isValid = "invalid";
		}
		assertEquals(string, isValid);

	}

	/**
	 * @author Zeyang Xu
	 */
	@Given("the  player preplaces domino {int} to their kingdom at position {int}:{int} with direction {string}")
	public void the_player_preplaces_domino_to_their_kingdom_at_position_with_direction(Integer int1, Integer int2,
			Integer int3, String string) {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		DominoInKingdom domIn = new DominoInKingdom(int2, int3, player.getKingdom(), HelperClass.getDominoByID(int1));
		domIn.setDirection(HelperClass.getDirection(string));
	}

}
		

