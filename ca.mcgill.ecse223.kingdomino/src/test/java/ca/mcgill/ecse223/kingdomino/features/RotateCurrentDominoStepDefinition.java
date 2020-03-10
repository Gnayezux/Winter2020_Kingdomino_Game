package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.Castle;
import ca.mcgill.ecse223.kingdomino.model.Domino;
import ca.mcgill.ecse223.kingdomino.model.Domino.DominoStatus;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Kingdom;
import ca.mcgill.ecse223.kingdomino.model.Kingdomino;
import ca.mcgill.ecse223.kingdomino.model.Player;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;
import ca.mcgill.ecse223.kingdomino.model.TerrainType;
import ca.mcgill.ecse223.kingdomino.model.User;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class RotateCurrentDominoStepDefinition {

	@Given("the game is initialized for rotate current domino")
	public void the_game_is_initialized_for_rotate_current_domino() {
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
		}
		KingdominoController.createAllDominos(kingdomino.getCurrentGame());
		KingdominoApplication.setKingdomino(kingdomino);
	}

	@When("{string} requests to rotate the domino with {string}")
	public void requests_to_rotate_the_domino_with(String string, String string2) {
		KingdominoController.rotateDomino(KingdominoApplication.getKingdomino(), string2);
	}

	@Then("the domino {int} is still tentatively placed at {int}:{int} but with new direction {string}")
	public void the_domino_is_still_tentatively_placed_at_but_with_new_direction(Integer int1, Integer int2, Integer int3, String string) {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		DominoInKingdom dom = (DominoInKingdom) player.getKingdom()
				.getTerritory(player.getKingdom().numberOfTerritories() - 1);
		assertEquals((int) int1, dom.getDomino().getId());
		assertEquals((int) int2, dom.getX());
		assertEquals((int) int3, dom.getY());
		assertEquals(getDirection(string), dom.getDirection());
	}

	@Then("the domino {int} should have status {string}")
	public void the_domino_should_have_status(Integer int1, String string) {
		Domino dom = getdominoByID(int1);
		assertEquals(getDominoStatus(string), dom.getStatus());
	}

	@Then("domino {int} is tentatively placed at the same position {int}:{int} with the same direction {string}")
	public void domino_is_tentatively_placed_at_the_same_position_with_the_same_direction(Integer int1, Integer int2, Integer int3, String string) {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		DominoInKingdom dom = (DominoInKingdom) player.getKingdom()
				.getTerritory(player.getKingdom().numberOfTerritories() - 1);
		assertEquals((int) int1, dom.getDomino().getId());
		assertEquals((int) int2, dom.getX());
		assertEquals((int) int3, dom.getY());
		assertEquals(getDirection(string), dom.getDirection());
	}

	@Then("domino {int} should still have status {string}")
	public void domino_should_still_have_status(Integer int1, String string) {
		Domino dom = getdominoByID(int1);
		assertEquals(getDominoStatus(string), dom.getStatus());
	}

	/**********************
	 * * Helper Methods * *
	 **********************/
	
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
	
	private Domino getdominoByID(int id) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		for (Domino domino : game.getAllDominos()) {
			if (domino.getId() == id) {
				return domino;
			}
		}
		throw new java.lang.IllegalArgumentException("Domino with ID " + id + " not found.");
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