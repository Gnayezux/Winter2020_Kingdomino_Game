package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;

import java.util.List;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.Castle;
import ca.mcgill.ecse223.kingdomino.model.Domino;
import ca.mcgill.ecse223.kingdomino.model.Domino.DominoStatus;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Kingdom;
import ca.mcgill.ecse223.kingdomino.model.Kingdomino;
import ca.mcgill.ecse223.kingdomino.model.Player;
import ca.mcgill.ecse223.kingdomino.model.User;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CalculatingPlayerScoreStepDefinition {
	
	@Given("the game is initialized for calculating player score")
	public void the_game_is_initialized_for_calculating_player_score() {
	    // Write code here that turns the phrase above into concrete actions
		Kingdomino kingdomino = new Kingdomino();
		Game game = new Game(48, kingdomino);
		kingdomino.setCurrentGame(game);
		KingdominoController.setNumberOfPlayers(4, kingdomino);
		for (int i = 0; i < 4; i++) {
			KingdominoController.selectColor(PlayerColor.values()[i], i, kingdomino);
		}
		List<Player> players = kingdomino.getCurrentGame().getPlayers();
		for (int i = 0; i < players.size(); i++) {
			Player player = players.get(i);
			Kingdom kingdom = new Kingdom(player);
			new Castle(0, 0, kingdom, player);
			player.setBonusScore(0);
			player.setPropertyScore(0);
			player.setDominoSelection(null);
			kingdom.setPlayer(player);
			player.setKingdom(kingdom);
			game.setNextPlayer(game.getPlayer(0));
		}
		KingdominoController.createAllDominos(kingdomino.getCurrentGame());
		KingdominoApplication.setKingdomino(kingdomino);
		
	
	}

	@Given("the current player has no dominoes in his\\/her kingdom yet")
	public void the_current_player_has_no_dominoes_in_his_her_kingdom_yet() {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Domino adomino = game.getAllDomino(0);
		game.removeAllDomino(adomino);
	    // Write code here that turns the phrase above into concrete actions

	}

	@Given("the score of the current player is {int}")
	public void the_score_of_the_current_player_is(Integer int1) {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		assertEquals((int) int1, player.getTotalScore());
	    // Write code here that turns the phrase above into concrete actions

	}

	@Given("the current player is preplacing his\\/her domino with ID {int} at location {int}:{int} with direction {string}")
	public void the_current_player_is_preplacing_his_her_domino_with_ID_at_location_with_direction(Integer int1, Integer int2, Integer int3, String string) {
	    // Write code here that turns the phrase above into concrete actions
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		Domino dom = KingdominoApplication.getKingdomino().getCurrentGame().getAllDomino(int1 - 1);
		DominoInKingdom domIn = new DominoInKingdom(int2, int3, player.getKingdom(), dom);
		domIn.setDirection(getDirection(string));
		player.getKingdom().addTerritory(domIn);

	}

	@Given("the preplaced domino has the status {string}")
	public void the_preplaced_domino_has_the_status(String string) {
		Domino dom = KingdominoApplication.getKingdomino().getCurrentGame().getTopDominoInPile();
		dom.setStatus(getDominoStatus(string));
		
	    // Write code here that turns the phrase above into concrete actions
	}

	@When("the current player places his\\/her domino")
	public void the_current_player_places_his_her_domino() {
	    // Write code here that turns the phrase above into concrete actions
		KingdominoController.placeDomino(KingdominoApplication.getKingdomino());
	}

	@Then("the score of the current player shall be {int}")
	public void the_score_of_the_current_player_shall_be(Integer int1) {
	    // Write code here that turns the phrase above into concrete actions
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		assertEquals((int) int1, player.getTotalScore());

	}
	
	@Given("the game has no bonus options selected")
	public void the_game_has_no_bonus_options_selected() {
	    // Write code here that turns the phrase above into concrete actions
		KingdominoController.setBonusOption("no", KingdominoApplication.getKingdomino(), true);

	}

	@Given("the current player is placing his\\/her domino with ID {int}")
	public void the_current_player_is_placing_his_her_domino_with_ID(Integer int1) {
	    // Write code here that turns the phrase above into concrete actions
		KingdominoController.placeDomino(KingdominoApplication.getKingdomino());
	}

	@Given("it is impossible to place the current domino in his\\/her kingdom")
	public void it_is_impossible_to_place_the_current_domino_in_his_her_kingdom() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}

	@When("the current player discards his\\/her domino")
	public void the_current_player_discards_his_her_domino() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}
	
	
	/**********************
	 * * Helper Methods * *
	 **********************/
	
	private void addDefaultUsersAndPlayers(Game game) {
		String[] userNames = { "User1", "User2", "User3", "User4" };
		for (int i = 0; i < userNames.length; i++) {
			User user = game.getKingdomino().addUser(userNames[i]);
			Player player = new Player(game);
			player.setUser(user);
			player.setColor(PlayerColor.values()[i]);
			Kingdom kingdom = new Kingdom(player);
			new Castle(0, 0, kingdom, player);
		}
	}

	private DirectionKind getDirection(String dir) {
		switch (dir) {
		case "up":
			return DirectionKind.Up;
		case "down":
			return DirectionKind.Down;
		case "left":
			return DirectionKind.Left;
		case "right":
			return DirectionKind.Right;
		default:
			throw new java.lang.IllegalArgumentException("Invalid direction: " + dir);
		}
	}
	
	private DominoStatus getDominoStatus(String status) {
		switch (status) {
		case "inPile":
			return DominoStatus.InPile;
		case "excluded":
			return DominoStatus.Excluded;
		case "inCurrentDraft":
			return DominoStatus.InCurrentDraft;
		case "inNextDraft":
			return DominoStatus.InNextDraft;
		case "ErroneouslyPreplaced":
			return DominoStatus.ErroneouslyPreplaced;
		case "CorrectlyPreplaced":
			return DominoStatus.CorrectlyPreplaced;
		case "placedInKingdom":
			return DominoStatus.PlacedInKingdom;
		case "discarded":
			return DominoStatus.Discarded;
		default:
			throw new java.lang.IllegalArgumentException("Invalid domino status: " + status);
		}
	}

	

}
