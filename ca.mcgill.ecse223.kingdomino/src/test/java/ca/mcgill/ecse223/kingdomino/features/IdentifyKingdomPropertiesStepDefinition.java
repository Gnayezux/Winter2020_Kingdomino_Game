package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;

//mport static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
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
import ca.mcgill.ecse223.kingdomino.model.KingdomTerritory;
import ca.mcgill.ecse223.kingdomino.model.Kingdomino;
import ca.mcgill.ecse223.kingdomino.model.Player;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;
import ca.mcgill.ecse223.kingdomino.model.Property;
import ca.mcgill.ecse223.kingdomino.model.TerrainType;
import ca.mcgill.ecse223.kingdomino.model.User;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class IdentifyKingdomPropertiesStepDefinition {

	@Given("the game is initialized for identify properties")
	public void the_game_is_initialized_for_identify_properties() {
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
			game.setNextPlayer(players.get(0));
		}
		KingdominoController.createAllDominos(kingdomino.getCurrentGame());
		KingdominoApplication.setKingdomino(kingdomino);
	}

	@When("the properties of the player are identified")
	public void the_properties_of_the_player_are_identified() {
		KingdominoController.identifyProperties(KingdominoApplication.getKingdomino());
	}

	@Then("the player shall have the following properties:")
	public void the_player_shall_have_the_following_properties(io.cucumber.datatable.DataTable dataTable) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		List<Property> properties = game.getNextPlayer().getKingdom().getProperties();
		List<Map<String, String>> valueMaps = dataTable.asMaps();
		for (Property prop : properties) {
			System.out.println(prop.getLeftTile() + ": " + getDominos(prop));
		}

		for (Map<String, String> map : valueMaps) {
			List<String> idList = new ArrayList<String>(Arrays.asList(map.get("dominoes").split(",")));
			HashSet<Integer> set = new HashSet<>();
			for (int i = 0; i < idList.size(); i++) {
				set.add(Integer.parseInt(idList.get(i)));
			}
			// findProperty(map.get("type"), map.get("dominoes"), properties);
			assertEquals(true, hasProperty(map.get("type"), set, properties));

		}
	}

	private boolean hasProperty(String type, HashSet<Integer> dominosExpected, List<Property> properties) {
//		System.out.println(dominosExpected);
		boolean found = false;
		for (Property prop : properties) {
			if (prop.getLeftTile() == getTerrainType(type) && checkSet(dominosExpected, prop)) {
				found = true;
			}
		}
		return found;
	}

	private boolean checkSet(HashSet<Integer> set, Property prop) {
		boolean full = false;
		for (int i = 0; i < prop.getIncludedDominos().size(); i++) {
			if (set.contains(prop.getIncludedDomino(i).getId())) {
				full = true;
			}
		}
		return full;
	}

	private String getDominos(Property property) {
		List<Domino> dominos = property.getIncludedDominos();
		String doms = "";
		for (Domino dominoInProp : dominos) {
			if (!doms.equals("")) {
				doms += ',';
			}
			doms += dominoInProp.getId();
		}
		return doms;
	}

	private TerrainType getTerrainType(String terrain) {
		switch (terrain) {
		case "wheat":
			return TerrainType.WheatField;
		case "forest":
			return TerrainType.Forest;
		case "mountain":
			return TerrainType.Mountain;
		case "grass":
			return TerrainType.Grass;
		case "swamp":
			return TerrainType.Swamp;
		case "lake":
			return TerrainType.Lake;
		default:
			throw new java.lang.IllegalArgumentException("Invalid terrain type: " + terrain);
		}
	}
}
