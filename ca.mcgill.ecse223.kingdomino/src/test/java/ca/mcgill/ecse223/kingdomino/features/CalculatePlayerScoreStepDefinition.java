package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;
import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.Player;
import io.cucumber.java.en.*;

public class CalculatePlayerScoreStepDefinition {

	/**
	 * @author Kaichengwu
	 */
	@Given("the game is initialized for calculate player score")
	public void the_game_is_initialized_for_calculate_player_score() {
		HelperClass.testSetup();
		KingdominoController.browseDominoPile();
		KingdominoController.generateInitialPlayerOrder();
	}

	/**
	 * @author Kaichengwu
	 */
	@Given("the game has {string} bonus option")
	public void the_game_has_bonus_option(String string) {
		if (!string.equals("no")) {
			if(string.equals("Middle Kingdom")) {
				KingdominoController.setBonusOption("MiddleKingdom", true);
			}else {
				KingdominoController.setBonusOption(string, true);
			}
		}
		KingdominoController.identifyProperties();
		KingdominoController.calculatePropertyAttributes();
		KingdominoController.calculateBonusScore();
	}

	/**
	 * @author Kaichengwu
	 */
	@When("calculate player score is initiated")
	public void calculate_player_score_is_initiated() {
		KingdominoController.calculatePlayerScore();
	}

	/**
	 * @author Kaichengwu
	 */
	@Then("the total score should be {int}")
	public void the_total_score_should_be(Integer int1) {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		assertEquals((int) int1, player.getTotalScore());
	}
}