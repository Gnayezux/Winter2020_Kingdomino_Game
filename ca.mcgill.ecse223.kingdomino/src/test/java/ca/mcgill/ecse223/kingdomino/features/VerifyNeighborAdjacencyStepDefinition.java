package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;
import java.util.List;
import java.util.Map;
import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.*;
import io.cucumber.java.en.*;

public class VerifyNeighborAdjacencyStepDefinition {
	
	boolean validity;
	
	/**
	 * @author Zeyang Xu
	 */
	@Given("the game is initialized for neighbor adjacency")
	public void the_game_is_initialized_for_neighbor_adjacency() {
		HelperClass.testSetup();
		KingdominoController.generateInitialPlayerOrder();
	}

	/**
	 * @author Zeyang Xu
	 */
	@Given("the following dominoes are present in a player's kingdom:")
	public void the_following_dominoes_are_present_in_a_player_s_kingdom(io.cucumber.datatable.DataTable dataTable) {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		Kingdom kingdom = player.getKingdom();
		new Castle(0, 0, kingdom, player);
		List<Map<String, String>> valueMaps = dataTable.asMaps();
		for (Map<String, String> map : valueMaps) {
			int posx = Integer.decode(map.get("posx"));
			int posy = Integer.decode(map.get("posy"));
			DominoInKingdom dom = new DominoInKingdom(posx, posy,kingdom,HelperClass.getDominoByID(Integer.parseInt(map.get("id"))));
			dom.setDirection(HelperClass.getDirection(map.get("dominodir")));
		}
	}

	/**
	 * @author Zeyang Xu
	 */
	@When("check current preplaced domino adjacency is initiated")
	public void check_current_preplaced_domino_adjacency_is_initiated() {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		DominoInKingdom ter = (DominoInKingdom) player.getKingdom().getTerritory(player.getKingdom().numberOfTerritories()-1);
		validity = KingdominoController.verifyNeighborAdjacency(player.getKingdom(),  ter.getDomino(), ter.getX(), ter.getY(), ter.getDirection());
	}

	/**
	 * @author Zeyang Xu
	 */
	@Then("the current-domino\\/existing-domino adjacency is {string}")
	public void the_current_domino_existing_domino_adjacency_is(String string) {
		assertEquals(getValidity(string), this.validity);
	}

	/**********************
	 * * Helper Methods * *
	 **********************/
	
	private Boolean getValidity(String string) {
		switch (string) {
		case "valid":
			return true;
		case "invalid":
			return false;
		default:
			throw new java.lang.IllegalArgumentException("Invalid validity: " + string);
		}
	}
	
}

