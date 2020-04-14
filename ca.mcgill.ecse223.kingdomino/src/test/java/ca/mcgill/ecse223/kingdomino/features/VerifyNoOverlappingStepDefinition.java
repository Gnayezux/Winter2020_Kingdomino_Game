package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;
import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.*;
import io.cucumber.java.en.*;

public class VerifyNoOverlappingStepDefinition {

	boolean valid;

	/**
	 * @author Zeyang Xu
	 */
	@Given("the game is initialized to check domino overlapping")
	public void the_game_is_initialized_to_check_domino_overlapping() {
		HelperClass.testSetup();
		KingdominoController.generateInitialPlayerOrder();
	}

	/**
	 * @author Zeyang Xu
	 */
	@When("check current preplaced domino overlapping is initiated")
	public void check_current_preplaced_domino_overlapping_is_initiated() {
		int i = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer().getKingdom().getTerritories()
				.size() - 1;
		DominoInKingdom inDomino = (DominoInKingdom) KingdominoApplication.getKingdomino().getCurrentGame()
				.getNextPlayer().getKingdom().getTerritory(i);

		valid = KingdominoController.verifyNoOverlapping(inDomino.getDomino(),
				KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer().getKingdom(), inDomino.getX(),
				inDomino.getY(), inDomino.getDirection());
	}

	/**
	 * @author Zeyang Xu
	 */
	@Then("the current-domino\\/existing-domino overlapping is {string}")
	public void the_current_domino_existing_domino_overlapping_is(String string) {
		String isValid;
		if (valid) {
			isValid = "valid";
		} else {
			isValid = "invalid";
		}
		assertEquals(string, isValid);
	}

}


