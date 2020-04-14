package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;
import java.util.List;
import java.util.Map;
import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.model.*;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;
import io.cucumber.java.en.*;

/**
 * @author Victoria Iannotti
 */
public class ResolveTiebreakStepDefinition {
	@Given("the game is initialized for resolve tiebreak")
	public void the_game_is_initialized_for_resolve_tiebreak() {
		Kingdomino kingdomino = new Kingdomino();
		Game game = new Game(48, kingdomino);
		game.setNumberOfPlayers(4);
		kingdomino.setCurrentGame(game);
		KingdominoApplication.setKingdomino(kingdomino);
	}

	@Then("player standings should be the followings:")
	public void player_standings_should_be_the_followings(io.cucumber.datatable.DataTable dataTable) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		List<Player> players = game.getPlayers();
		List<Map<String, String>> valueMaps = dataTable.asMaps();
		for (Map<String, String> map : valueMaps) {
			int actualRanking = getPlayer(players, getColor(map.get("player"))).getCurrentRanking();
			int expectedRanking = Integer.decode(map.get("standing"));
			assertEquals(expectedRanking, actualRanking);
		}
	}

	private PlayerColor getColor(String color) {
		switch (color) {
		case "blue":
			return PlayerColor.Blue;
		case "green":
			return PlayerColor.Green;
		case "yellow":
			return PlayerColor.Yellow;
		case "pink":
			return PlayerColor.Pink;
		default:
			throw new java.lang.IllegalArgumentException("Invalid color: " + color);
		}
	}

	private Player getPlayer(List<Player> players, PlayerColor col) {
		for (Player p : players) {
			if (p.getColor() == col) {
				return p;
			}
		}
		throw new java.lang.IllegalArgumentException("Inexistant player of color: " + col);
	}

}
