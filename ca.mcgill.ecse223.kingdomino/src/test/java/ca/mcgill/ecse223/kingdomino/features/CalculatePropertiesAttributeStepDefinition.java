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
import ca.mcgill.ecse223.kingdomino.model.Property;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;
import ca.mcgill.ecse223.kingdomino.model.TerrainType;
import ca.mcgill.ecse223.kingdomino.model.User;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CalculatePropertiesAttributeStepDefinition {

	/**
	 * @author Kaichengwu
	 */
	@Given("the game is initialized for calculate property attributes")
	public void the_game_is_initialized_for_calculate_property_attributes() {
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

	/**
	 * @author Kaichengwu
	 */
	@When("calculate property attributes is initiated")
	public void calculate_property_attributes_is_initiated() {
		KingdominoController.identifyProperties(KingdominoApplication.getKingdomino());
		for (Property prop : KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer().getKingdom().getProperties()) {
			System.out.println(prop.getLeftTile() + ": " + getDominos(prop));
		}
		KingdominoController.calculatePropertyAttributes(KingdominoApplication.getKingdomino());
		
	}

	/**
	 * @author Kaichengwu
	 */
	@Then("the player shall have a total of {int} properties")
	public void the_player_shall_have_a_total_of_properties(Integer int1) {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		assertEquals((int) int1, player.getKingdom().getProperties().size());
	}

	/**
	 * @author Kaichengwu
	 */
	@Then("the player shall have properties with the following attributes:")
	public void the_player_shall_have_properties_with_the_following_attributes(
			io.cucumber.datatable.DataTable dataTable) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		List<Property> properties = game.getNextPlayer().getKingdom().getProperties();
		List<Map<String, String>> valueMaps = dataTable.asMaps();
		for (Map<String, String> map : valueMaps) {
			assertEquals(true, hasProperty(map.get("type"), Integer.decode(map.get("size")),
					Integer.decode(map.get("crowns")), properties));

		}
	}

	//HELPER METHODS
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
	
	private boolean hasProperty(String type, int expectedSize, int expectedCrowns, List<Property> properties) {
		boolean found = false;
		for (Property prop : properties) {
//			System.out.println(prop.getLeftTile());
//			System.out.println(prop.getSize());
//			System.out.println(prop.getCrowns());
			
			if (prop.getLeftTile() == getTerrainType(type) && expectedSize==prop.getSize() && expectedCrowns==prop.getCrowns()) {
				found = true;
			}
		}
		return found;
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
