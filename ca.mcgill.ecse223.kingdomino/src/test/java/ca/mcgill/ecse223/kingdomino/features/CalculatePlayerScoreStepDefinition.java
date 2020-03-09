package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;

//mport static org.junit.Assert.assertEquals;

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

public class CalculatePlayerScoreStepDefinition {

	@Given("the game is initialized for calculate player score")
	public void the_game_is_initialized_for_calculate_player_score() {
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

	@Given("the game has {string} bonus option")
	public void the_game_has_bonus_option(String string) {
		if (!string.equals("no")) {
			KingdominoController.setBonusOption(string, KingdominoApplication.getKingdomino(), true);
		}
		KingdominoController.identifyProperties(KingdominoApplication.getKingdomino());
		KingdominoController.calculatePropertyAttributes(KingdominoApplication.getKingdomino());
	}

	@When("calculate player score is initiated")
	public void calculate_player_score_is_initiated() {
		KingdominoController.calculatePlayerScore(KingdominoApplication.getKingdomino(), KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer());
	}

	@Then("the total score should be {int}")
	public void the_total_score_should_be(Integer int1) {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		assertEquals((int) int1, player.getTotalScore());
	}

	private TerrainType getTerrainType(String terrain) {
		switch (terrain) {
		case "W":
			return TerrainType.WheatField;
		case "F":
			return TerrainType.Forest;
		case "M":
			return TerrainType.Mountain;
		case "G":
			return TerrainType.Grass;
		case "S":
			return TerrainType.Swamp;
		case "L":
			return TerrainType.Lake;
		default:
			throw new java.lang.IllegalArgumentException("Invalid terrain type: " + terrain);
		}
	}
}