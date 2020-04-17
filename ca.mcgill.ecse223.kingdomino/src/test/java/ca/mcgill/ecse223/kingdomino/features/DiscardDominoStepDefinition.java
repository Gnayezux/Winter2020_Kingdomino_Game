package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;
import java.util.List;
import java.util.Map;
import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.Domino;
import ca.mcgill.ecse223.kingdomino.model.Domino.DominoStatus;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
import ca.mcgill.ecse223.kingdomino.model.DominoSelection;
import ca.mcgill.ecse223.kingdomino.model.Draft;
import ca.mcgill.ecse223.kingdomino.model.Draft.DraftStatus;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Kingdom;
import ca.mcgill.ecse223.kingdomino.model.Player;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class DiscardDominoStepDefinition {
	
	/**
	 * @author Mathieu-Joseph Magri
	 */
	@Given("the game is initialized for discard domino")
	public void the_game_is_initialized_for_discard_domino() {
		HelperClass.testSetup();
		KingdominoController.browseDominoPile();
		KingdominoController.generateInitialPlayerOrder();
	}

	/**
	 * @author Mathieu-Joseph Magri
	 */
	@Given("the player's kingdom has the following dominoes:")
	public void the_player_s_kingdom_has_the_following_dominoes(io.cucumber.datatable.DataTable dataTable) {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		List<Map<String, String>> valueMaps = dataTable.asMaps();
		for (Map<String, String> map : valueMaps) {
			// Get values from cucumber table
			Integer id = Integer.decode(map.get("id"));
			DirectionKind dir = HelperClass.getDirection(map.get("dominodir"));
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
	@Given("domino {int} is in the current draft")
	public void domino_is_in_the_current_draft(Integer domID) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Draft draft = new Draft(DraftStatus.FaceUp, game);
		draft.addSelection(new DominoSelection(game.getNextPlayer(), HelperClass.getDominoByID(domID), draft));
		game.setCurrentDraft(draft);
	}

	/**
	 * @author Mathieu-Joseph Magri
	 */
	@Given("the current player has selected domino {int}")
	public void the_current_player_has_selected_domino(Integer domID) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Player player = game.getNextPlayer();
		player.setDominoSelection(game.getCurrentDraft().getSelection(0));
	}
	
	/**
	 * @author Mathieu-Joseph Magri
	 */
	@Given("the player preplaces domino {int} at its initial position")
	public void the_player_preplaces_domino_at_its_initial_position(Integer domID) {
		KingdominoController.removeKing();
	}

	/**
	 * @author Mathieu-Joseph Magri
	 */
	@When("the player attempts to discard the selected domino")
	public void the_player_attempts_to_discard_the_selected_domino() {
		KingdominoController.discardDomino();
	}

	/**
	 * @author Mathieu-Joseph Magri
	 */
	@Then("domino {int} shall have status {string}")
	public void domino_shall_have_status(Integer domID, String domStatus) {
		DominoStatus actualStatus = HelperClass.getDominoByID(domID).getStatus();
		DominoStatus expectedStatus = HelperClass.getDominoStatus(domStatus);
		assertEquals(expectedStatus, actualStatus);
	}
}
