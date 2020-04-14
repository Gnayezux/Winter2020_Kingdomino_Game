package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;
import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.*;
import io.cucumber.java.en.*;


public class VerifyCastleAdjacencyStepDefinition {
	boolean isChecked;
	/**
	 * @author Zeyang Xu
	 */
	@Given("the game is initialized for castle adjacency")
	public void the_game_is_initialized_for_castle_adjacency() {
		HelperClass.testSetup();
		KingdominoController.generateInitialPlayerOrder();
	}
	/**
	 * @author Zeyang Xu
	 */
	@Given("the current player preplaced the domino with ID {int} at position {int}:{int} and direction {string}")
	public void the_current_player_preplaced_the_domino_with_ID_at_position_and_direction(Integer int1, Integer int2, Integer int3, String string) {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		DominoInKingdom domIn = new DominoInKingdom(int2, int3, player.getKingdom(), HelperClass.getDominoByID(int1));
		domIn.setDirection(HelperClass.getDirection(string));
	}

	/**
	 * @author Zeyang Xu
	 */
	@When("check castle adjacency is initiated")
	public void check_castle_adjacency_is_initiated() {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		DominoInKingdom ter = (DominoInKingdom) player.getKingdom().getTerritory(player.getKingdom().numberOfTerritories()-1);
		isChecked = KingdominoController.verifyCastleAdjacency(ter.getX(), ter.getY(), ter.getDirection());
	}

	/**
	 * @author Zeyang Xu
	 */
	@Then("the castle\\/domino adjacency is {string}")
	public void the_castle_domino_adjacency_is(String string) {
		String a;
		if(isChecked) {
			a = "valid";
			assertEquals(string, a );
		}else {
			a = "invalid";
			assertEquals(string, a );
		}
	}
}

