package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertFalse;
import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.Gameplay;
import io.cucumber.java.en.*;

/**
 * @author Mathieu-Joseph Magri
 */
public class DiscardingDominoStepDefinition {

	@Given("the game is initialized for discarding domino")
	public void the_game_is_initialized_for_discarding_domino() {
		HelperClass.testSetup();
		KingdominoController.shuffleDominos();
		Gameplay g = new Gameplay();
		KingdominoApplication.setGameplay(g);
		KingdominoApplication.getGameplay().setGamestatus("PlacingDomino");
	}

	@Given("it is impossible to place the current domino in his\\/her kingdom")
	public void it_is_impossible_to_place_the_current_domino_in_his_her_kingdom() {
	    assertFalse(KingdominoController.isPossibleToPlace());
	}

	@When("the current player discards his\\/her domino")
	public void the_current_player_discards_his_her_domino() {
		KingdominoController.discarding();
	}

}
