package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;

//mport static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

public class IdentifyKingdomPropertiesStepDefinition {

	

@Given("the game is initialized for identify properties")
public void the_game_is_initialized_for_identify_properties() {
    // Write code here that turns the phrase above into concrete actions
//    throw new cucumber.api.PendingException();
	// Intialize empty game
			Kingdomino kingdomino = new Kingdomino();
			Game game = new Game(48, kingdomino);
			game.setNumberOfPlayers(4);
			kingdomino.setCurrentGame(game);
			// Populate game
			addDefaultUsersAndPlayers(game);
			KingdominoController.createAllDominos(game);
			game.setNextPlayer(game.getPlayer(0));
			KingdominoApplication.setKingdomino(kingdomino);
}

@When("the properties of the player are identified")
public void the_properties_of_the_player_are_identified() {
    // Write code here that turns the phrase above into concrete actions
//    throw new cucumber.api.PendingException();
//	System.out.print(KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer().getKingdom().getTerritories());
	KingdominoController.identifyProperties(KingdominoApplication.getKingdomino());
	
}

@Then("the player shall have the following properties:")
public void the_player_shall_have_the_following_properties(io.cucumber.datatable.DataTable dataTable) {
    // Write code here that turns the phrase above into concrete actions
    // For automatic transformation, change DataTable to one of
    // E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
    // Map<K, List<V>>. E,K,V must be a String, Integer, Float,
    // Double, Byte, Short, Long, BigInteger or BigDecimal.
    //
    // For other transformations you can register a DataTableType.
//    throw new cucumber.api.PendingException();
	
	List<Map<String, String>> valueMaps = dataTable.asMaps();
	for (Map<String, String> map : valueMaps) {
		
		String type = map.get("type");
		String dominos = map.get("dominoes");
		
		boolean assertion = false;
		List<String> idList = new ArrayList<String>(Arrays.asList(dominos.split(",")));
		TerrainType terrain = getTerrainType(type);
		Kingdom kingdom = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer().getKingdom();
		for(int i =0;i<kingdom.getProperties().size();i++) {
			
			if(kingdom.getProperty(i).getLeftTile().equals(terrain)) {
				
				for(int j =0;j<kingdom.getProperty(i).getIncludedDominos().size();j++) {
					
					if(j<idList.size()&&kingdom.getProperty(i).getIncludedDomino(j).getId() == Integer.parseInt(idList.get(j))) {
						assertion = true;
					}
					
				}
			}
		}
		assertEquals(true,assertion);
	}
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
	}
}


}
