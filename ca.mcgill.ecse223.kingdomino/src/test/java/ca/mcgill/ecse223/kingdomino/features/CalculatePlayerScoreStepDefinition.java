package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;

import java.util.List;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.Domino;
import ca.mcgill.ecse223.kingdomino.model.Player;
import ca.mcgill.ecse223.kingdomino.model.Property;
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
		for (Property prop : KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer().getKingdom().getProperties()) {
			System.out.println(prop.getLeftTile() + ": " + getDominos(prop));
		}
		KingdominoController.calculatePropertyAttributes();
		for (Property prop : KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer().getKingdom().getProperties()) {
			System.out.println(prop.getLeftTile() + ": " + getDominos(prop));
			System.out.println(prop.getScore());
		}
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
	
	private String getDominos(Property property) {
		List<Domino> dominos = property.getIncludedDominos();
		String doms = "";
		for (Domino dominoInProp : dominos) {
			if (!doms.equals("")) {
				doms += ',';
			}
			doms += dominoInProp.getId();
		}
		return doms;
	}
}