package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;

import java.util.List;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.Domino;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom;
import ca.mcgill.ecse223.kingdomino.model.DominoSelection;
import ca.mcgill.ecse223.kingdomino.model.Draft;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Gameplay;
import ca.mcgill.ecse223.kingdomino.model.Player;
import ca.mcgill.ecse223.kingdomino.model.Domino.DominoStatus;
import ca.mcgill.ecse223.kingdomino.model.Draft.DraftStatus;
import io.cucumber.java.en.*;

public class CalculatingPlayerScoreStepDefinition {

	@Given("the game is initialized for calculating player score")
	public void the_game_is_initialized_for_calculating_player_score() {
		HelperClass.testSetup();
		KingdominoController.generateInitialPlayerOrder();
		Gameplay g = new Gameplay();
		KingdominoApplication.setGameplay(g);
		KingdominoApplication.getGameplay().setGamestatus("PlacingDomino");
	}

	@Given("the current player has no dominoes in his\\/her kingdom yet")
	public void the_current_player_has_no_dominoes_in_his_her_kingdom_yet() {
		// Done in the initializing
	}

	@Given("the score of the current player is {int}")
	public void the_score_of_the_current_player_is(Integer int1) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Player player = game.getNextPlayer();
		player.setPropertyScore(int1);
	}

	@Then("the score of the current player shall be {int}")
	public void the_score_of_the_current_player_shall_be(Integer int1) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		List<Player> players = game.getPlayers();
		assertEquals((int) int1, (int) players.get(0).getTotalScore());
	}

	@Given("the game has no bonus options selected")
	public void the_game_has_no_bonus_options_selected() {
		KingdominoController.setBonusOption("MiddleKingdom", false);
		KingdominoController.setBonusOption("Harmony", false);
	}

	@Given("the current player is placing his\\/her domino with ID {int}")
	public void the_current_player_is_placing_his_her_domino_with_ID(Integer int1) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Player nextPlayer = game.getNextPlayer();
		Draft draft = new Draft(DraftStatus.FaceUp, KingdominoApplication.getKingdomino().getCurrentGame());
		List<Player> players = KingdominoApplication.getKingdomino().getCurrentGame().getPlayers();
		for (Player p : players) {
			if (p.equals(nextPlayer)) {
				new DominoSelection(p, HelperClass.getDominoByID(int1), draft);
				break;
			}
		}
		Domino dom = nextPlayer.getDominoSelection().getDomino();
		dom.setStatus(DominoStatus.ErroneouslyPreplaced);
		new DominoInKingdom(0, 0, nextPlayer.getKingdom(), dom);
	}

}