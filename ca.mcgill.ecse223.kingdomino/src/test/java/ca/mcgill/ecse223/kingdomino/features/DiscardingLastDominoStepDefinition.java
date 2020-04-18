package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;
import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.Gameplay;
import io.cucumber.java.en.*;

/**
 * @author Mathieu-Joseph Magri
 */
public class DiscardingLastDominoStepDefinition {

	@Given("the game is initialized for discarding last domino")
	public void the_game_is_initialized_for_discarding_last_domino() {
		HelperClass.testSetup();
		KingdominoController.shuffleDominos();
		Gameplay g = new Gameplay();
		KingdominoApplication.setGameplay(g);
		KingdominoApplication.getGameplay().setGamestatus("PlacingDomino");
	}

	@Then("the final results after discard shall be computed")
	public void the_final_results_after_discard_shall_be_computed() {
		assertEquals("ending", HelperClass.getState(KingdominoApplication.getGameplay().getGamestatusFullName()));
	}

}
