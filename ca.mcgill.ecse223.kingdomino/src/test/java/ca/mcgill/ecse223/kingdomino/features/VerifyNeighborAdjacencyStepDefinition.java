
package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.*;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;
import io.cucumber.java.en.*;

public class VerifyNeighborAdjacencyStepDefinition {
	
	boolean validity;
	
	/**
	 * @author Zeyang Xu
	 */
	@Given("the game is initialized for neighbor adjacency")
	public void the_game_is_initialized_for_neighbor_adjacency() {
		Kingdomino kingdomino = new Kingdomino();
		Game game = new Game(48, kingdomino);
		game.setNumberOfPlayers(4);
		kingdomino.setCurrentGame(game);
		addDefaultUsersAndPlayers(game);
		KingdominoController.createAllDominos(game);
		game.setNextPlayer(game.getPlayer(0));
		KingdominoApplication.setKingdomino(kingdomino);
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
			DominoInKingdom dom = new DominoInKingdom(posx, posy,kingdom, KingdominoApplication.getKingdomino().getCurrentGame().getAllDomino(Integer.decode(map.get("id"))-1));
			dom.setDirection(getDirection(map.get("dominodir")));
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
	
	private void addDefaultUsersAndPlayers(Game game) {
		String[] userNames = { "User1", "User2", "User3", "User4" };
		for (int i = 0; i < userNames.length; i++) {
			User user = game.getKingdomino().addUser(userNames[i]);
			Player player = new Player(game);
			player.setUser(user);
			player.setColor(PlayerColor.values()[i]);
			Kingdom kingdom = new Kingdom(player);
			new Castle(0, 0, kingdom, player);
			game.setNextPlayer(player);
		}
	}
}

