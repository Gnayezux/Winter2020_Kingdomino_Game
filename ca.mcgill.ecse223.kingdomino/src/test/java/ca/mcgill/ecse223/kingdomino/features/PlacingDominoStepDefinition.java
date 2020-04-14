package ca.mcgill.ecse223.kingdomino.features;

import ca.mcgill.ecse223.kingdomino.model.*;
import ca.mcgill.ecse223.kingdomino.model.Domino.DominoStatus;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
import ca.mcgill.ecse223.kingdomino.model.Draft.DraftStatus;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;
import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.*;
import io.cucumber.java.en.*;
import static org.junit.Assert.*;
import java.util.*;

public class PlacingDominoStepDefinition {

	@Given("the game has been initialized for placing domino")
	public void the_game_has_been_initialized_for_placing_domino() {
		HelperClass.testSetup();
		KingdominoController.shuffleDominos();
		Gameplay g = new Gameplay();
		KingdominoApplication.setGameplay(g);
		KingdominoApplication.getGameplay().setGamestatus("PlacingDomino");
	}

	@Given("it is not the last turn of the game")
	public void it_is_not_the_last_turn_of_the_game() {
		KingdominoController.shuffleDominos();
	}

	@Given("the current player is not the last player in the turn")
	public void the_current_player_is_not_the_last_player_in_the_turn() {
		KingdominoController.generateInitialPlayerOrder();
		// Now the next player is the first in the list
	}

	@Given("the current player is preplacing his\\/her domino with ID {int} at location {int}:{int} with direction {string}")
	public void the_current_player_is_preplacing_his_her_domino_with_ID_at_location_with_direction(Integer int1, Integer int2, Integer int3, String string) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Player nextPlayer = game.getNextPlayer();
		Draft draft = new Draft(DraftStatus.FaceUp, KingdominoApplication.getKingdomino().getCurrentGame());
		List<Player> players = KingdominoApplication.getKingdomino().getCurrentGame().getPlayers();
		for (Player p : players) {
			if (p.equals(nextPlayer)) {
				System.out.println("here");
				new DominoSelection(p, HelperClass.getDominoByID(int1), draft);
				break;
			}
		}
		Domino dom = nextPlayer.getDominoSelection().getDomino();
		dom.setStatus(DominoStatus.CorrectlyPreplaced);
		DominoInKingdom domIn = new DominoInKingdom(int2, int3, nextPlayer.getKingdom(), dom);
		domIn.setDirection(HelperClass.getDirection(string));
		//KingdominoController.resetDominoStatus(domIn);
	}
	
	@Given("the preplaced domino has the status {string}")
	public void the_preplaced_domino_has_the_status(String string) {
		assertTrue(KingdominoApplication.getGameplay().isDominoCorrectlyPreplaced());
	}

	@When("the current player places his\\/her domino")
	public void the_current_player_places_his_her_domino() {
		KingdominoController.readyToPlace();
	}

	@Then("this player now shall be making his\\/her domino selection")
	public void this_player_now_shall_be_making_his_her_domino_selection() {
		assertEquals("Playing.SelectingDomino", KingdominoApplication.getGameplay().getGamestatusFullName());
	}

	@Given("the current player is the last player in the turn")
	public void the_current_player_is_the_last_player_in_the_turn() {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		game.setNextPlayer(game.getPlayer(game.getNumberOfPlayers() - 1));
	}


}
