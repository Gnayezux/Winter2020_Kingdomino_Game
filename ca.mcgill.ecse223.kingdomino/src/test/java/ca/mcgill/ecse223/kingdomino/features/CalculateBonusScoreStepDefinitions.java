package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;
import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.Domino;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom;
import ca.mcgill.ecse223.kingdomino.model.Kingdom;
import ca.mcgill.ecse223.kingdomino.model.Player;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CalculateBonusScoreStepDefinitions {

	/**
	 * @author Kaichengwu
	 */
	@Given("the game is initialized for calculate bonus scores")
	public void the_game_is_initialized_for_calculate_bonus_scores() {
		HelperClass.testSetup();
		KingdominoController.browseDominoPile();
		KingdominoController.generateInitialPlayerOrder();
	}

	/**
	 * @author Kaichengwu
	 */
	@Given("Middle Kingdom is selected as bonus option")
	public void middle_Kingdom_is_selected_as_bonus_option() {
		KingdominoController.setBonusOption("MiddleKingdom", true);

	}

	/**
	 * @author Kaichengwu
	 */
	@Given("the player's kingdom also includes the domino {int} at position {int}:{int} with the direction {string}")
	public void the_player_s_kingdom_also_includes_the_domino_at_position_with_the_direction(Integer int1, Integer int2,
			Integer int3, String string) {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		Kingdom kingdom = player.getKingdom();
		Domino domino = HelperClass.getDominoByID(int1);
		DominoInKingdom dominoinkingdom = new DominoInKingdom(int2, int3, kingdom, domino);
		dominoinkingdom.setDirection(HelperClass.getDirection(string));
		kingdom.addTerritory(dominoinkingdom);
	}

	/**
	 * @author Kaichengwu
	 */
	@When("calculate bonus score is initiated")
	public void calculate_bonus_score_is_initiated() {
		KingdominoController.calculateBonusScore();
	}

	/**
	 * @author Kaichengwu
	 */
	@Then("the bonus score should be {int}")
	public void the_bonus_score_should_be(Integer int1) {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		assertEquals((int) int1, player.getBonusScore());
	}

	/**
	 * @author Kaichengwu
	 */
	@Given("Harmony is selected as bonus option")
	public void harmony_is_selected_as_bonus_option() {
		KingdominoController.setBonusOption("Harmony", true);
	}


}
