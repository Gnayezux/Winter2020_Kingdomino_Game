package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;
import java.util.*;
import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.*;
import ca.mcgill.ecse223.kingdomino.model.Domino.DominoStatus;
import ca.mcgill.ecse223.kingdomino.model.Draft.DraftStatus;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
import io.cucumber.java.en.*;

public class MoveCurrentDominoStepDefinition {
	
	/**
	 * @author Mathieu-Joseph Magri
	 */
	@Given("the game is initialized for move current domino")
	public void the_game_is_initialized_for_move_current_domino() {
		HelperClass.testSetup();
		KingdominoController.shuffleDominos();
	}

	/**
	 * @author Mathieu-Joseph Magri
	 */
	@Given("it is {string}'s turn")
	public void it_is_s_turn(String string) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		List<Player> players = KingdominoApplication.getKingdomino().getCurrentGame().getPlayers();
		for (Player p : players) {
			if (p.getColor() == HelperClass.getColor(string)) {
				game.setNextPlayer(p);
				break;
			}
		}
	}

	/**
	 * @author Mathieu-Joseph Magri
	 */
	@Given("{string} has selected domino {int}")
	public void has_selected_domino(String string, Integer int1) {
		Draft draft = new Draft(DraftStatus.FaceUp, KingdominoApplication.getKingdomino().getCurrentGame());
		List<Player> players = KingdominoApplication.getKingdomino().getCurrentGame().getPlayers();
		for (Player p : players) {
			if (p.getColor() == HelperClass.getColor(string)) {
				new DominoSelection(p, HelperClass.getDominoByID(int1), draft);
				break;
			}
		}

	}

	/**
	 * @author Mathieu-Joseph Magri
	 */
	@When("{string} removes his king from the domino {int}")
	public void removes_his_king_from_the_domino(String string, Integer int1) {
		KingdominoController.removeKing();
	}

	/**
	 * @author Mathieu-Joseph Magri
	 */
	@Then("domino {int} should be tentative placed at position {int}:{int} of {string}'s kingdom with ErroneouslyPreplaced status")
	public void domino_should_be_tentative_placed_at_position_of_s_kingdom_with_ErroneouslyPreplaced_status(
			Integer int1, Integer int2, Integer int3, String string) {

		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		DominoInKingdom dom = (DominoInKingdom) player.getKingdom()
				.getTerritory(player.getKingdom().numberOfTerritories() - 1);
		assertEquals((int) int1, dom.getDomino().getId());
		assertEquals((int) int2, dom.getX());
		assertEquals((int) int3, dom.getY());
		assertEquals(HelperClass.getColor(string), player.getColor());
		assertEquals(DominoStatus.ErroneouslyPreplaced, dom.getDomino().getStatus());

	}

	/**
	 * @author Mathieu-Joseph Magri
	 */
	@Given("{string}'s kingdom has following dominoes:")
	public void s_kingdom_has_following_dominoes(String string, io.cucumber.datatable.DataTable dataTable) {

		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		List<Map<String, String>> valueMaps = dataTable.asMaps();
		for (Map<String, String> map : valueMaps) {
			// Get values from cucumber table
			Integer id = Integer.decode(map.get("id"));
			DirectionKind dir = HelperClass.getDirection(map.get("dir"));
			Integer posx = Integer.decode(map.get("posx"));
			Integer posy = Integer.decode(map.get("posy"));

			// Add the domino to a player's kingdom
			Domino dominoToPlace = HelperClass.getDominoByID(id);
			Kingdom kingdom = player.getKingdom();

			DominoInKingdom domInKingdom = new DominoInKingdom(posx, posy, kingdom, dominoToPlace);
			domInKingdom.setDirection(dir);
			dominoToPlace.setStatus(DominoStatus.PlacedInKingdom);
		}
	}

	/**
	 * @author Mathieu-Joseph Magri
	 */
	@Given("domino {int} is tentatively placed at position {int}:{int} with direction {string}")
	public void domino_is_tentatively_placed_at_position_with_direction(Integer int1, Integer int2, Integer int3,
			String string) {

		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		Domino dominoToPlace = HelperClass.getDominoByID(int1);
		dominoToPlace.setStatus(DominoStatus.ErroneouslyPreplaced);
		Kingdom kingdom = player.getKingdom();
		DominoInKingdom domInKingdom = new DominoInKingdom(int2, int3, kingdom, dominoToPlace);
		domInKingdom.setDirection(HelperClass.getDirection(string));
		

	}

	/**
	 * @author Mathieu-Joseph Magri
	 */
	@When("{string} requests to move the domino {string}")
	public void requests_to_move_the_domino(String string, String string2) {
		KingdominoController.moveDomino(string2);
	}

	/**
	 * @author Mathieu-Joseph Magri
	 */
	@Then("the domino {int} should be tentatively placed at position {int}:{int} with direction {string}")
	public void the_domino_should_be_tentatively_placed_at_position_with_direction(Integer int1, Integer int2,
			Integer int3, String string) {

		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		DominoInKingdom dom = (DominoInKingdom) player.getKingdom()
				.getTerritory(player.getKingdom().numberOfTerritories() - 1);
		assertEquals((int) int1, dom.getDomino().getId());
		assertEquals((int) int2, dom.getX());
		assertEquals((int) int3, dom.getY());
		assertEquals(HelperClass.getDirection(string), dom.getDirection());

	}

	/**
	 * @author Mathieu-Joseph Magri
	 */
	@Then("the new status of the domino is {string}")
	public void the_new_status_of_the_domino_is(String string) {

		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		DominoInKingdom dom = (DominoInKingdom) player.getKingdom()
				.getTerritory(player.getKingdom().numberOfTerritories() - 1);
		assertEquals(HelperClass.getDominoStatus(string), dom.getDomino().getStatus());

	}

	/**
	 * @author Mathieu-Joseph Magri
	 */
	@Given("domino {int} has status {string}")
	public void domino_has_status(Integer int1, String string) {
		Domino dom = HelperClass.getDominoByID(int1);
		dom.setStatus(HelperClass.getDominoStatus(string));
	}

	/**
	 * @author Mathieu-Joseph Magri
	 */
	@Then("the domino {int} is still tentatively placed at position {int}:{int}")
	public void the_domino_is_still_tentatively_placed_at_position(Integer int1, Integer int2, Integer int3) {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		DominoInKingdom dom = (DominoInKingdom) player.getKingdom()
				.getTerritory(player.getKingdom().numberOfTerritories() - 1);
		assertEquals((int) int1, dom.getDomino().getId());
		assertEquals((int) int2, dom.getX());
		assertEquals((int) int3, dom.getY());
	}

	/**
	 * @author Mathieu-Joseph Magri
	 */
	@Then("the domino should still have status {string}")
	public void the_domino_should_still_have_status(String string) {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		DominoInKingdom dom = (DominoInKingdom) player.getKingdom()
				.getTerritory(player.getKingdom().numberOfTerritories() - 1);
		assertEquals(HelperClass.getDominoStatus(string), dom.getDomino().getStatus());
	}
}
