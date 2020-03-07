package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.Domino;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Kingdomino;
import ca.mcgill.ecse223.kingdomino.model.TerrainType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class BrowseDominoPileStepDefinition {
Kingdomino kingdomino;
@Given("the program is started and ready for browsing dominoes")
public void the_program_is_started_and_ready_for_browsing_dominoes() {
    // Write code here that turns the phrase above into concrete actions
	// Intialize empty game
			kingdomino = new Kingdomino();
			Game game = new Game(48, kingdomino);
			game.setNumberOfPlayers(4);
			kingdomino.setCurrentGame(game);
			
			try {
				BufferedReader br = new BufferedReader(new FileReader("src/main/resources/alldominoes.dat"));
				String line = "";
				String delimiters = "[:\\+()]";
				while ((line = br.readLine()) != null) {
					String[] dominoString = line.split(delimiters); // {id, leftTerrain, rightTerrain, crowns}
					int dominoId = Integer.decode(dominoString[0]);
					TerrainType leftTerrain = getTerrainType(dominoString[1]);
					TerrainType rightTerrain = getTerrainType(dominoString[2]);
					int numCrown = 0;
					if (dominoString.length > 3) {
						numCrown = Integer.decode(dominoString[3]);
					}
					new Domino(dominoId, leftTerrain, rightTerrain, numCrown, game);
				}
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new java.lang.IllegalArgumentException(
						"Error occured while trying to read alldominoes.dat: " + e.getMessage());
			}
			
//    throw new cucumber.api.PendingException();
}
ArrayList<Domino> dominoPile;
@When("I initiate the browsing of all dominoes")
public void i_initiate_the_browsing_of_all_dominoes() {
    // Write code here that turns the phrase above into concrete actions
//    throw new cucumber.api.PendingException();
	dominoPile = KingdominoController.BrowseDominoPile(kingdomino);
}

@Then("all the dominoes are listed in increasing order of identifiers")
public void all_the_dominoes_are_listed_in_increasing_order_of_identifiers() {
    // Write code here that turns the phrase above into concrete actions
//    throw new cucumber.api.PendingException();
	boolean ordered = true;
	for(int i =0;i<dominoPile.size()-1;i++) {
		if(dominoPile.get(i).getId()>dominoPile.get(i+1).getId()) {
			ordered = false;
		}
	}
	
	assertEquals(true,ordered);
}

Domino domino;
@When("I provide a domino ID {int}")
public void i_provide_a_domino_ID(Integer int1) {
    // Write code here that turns the phrase above into concrete actions
//    throw new cucumber.api.PendingException();
	domino = KingdominoController.BrowseDomino(int1, kingdomino);
}

@Then("the listed domino has {string} left terrain")
public void the_listed_domino_has_left_terrain(String string) {
    // Write code here that turns the phrase above into concrete actions
//    throw new cucumber.api.PendingException();
	assertEquals(getTerrainTypeFilter(string),domino.getLeftTile());
//	System.out.print(string);
}

@Then("the listed domino has {string} right terrain")
public void the_listed_domino_has_right_terrain(String string) {
    // Write code here that turns the phrase above into concrete actions
//    throw new cucumber.api.PendingException();
	assertEquals(getTerrainTypeFilter(string),domino.getRightTile());
}

@Then("the listed domino has {int} crowns")
public void the_listed_domino_has_crowns(Integer int1) {
    // Write code here that turns the phrase above into concrete actions
//    throw new cucumber.api.PendingException();
	assertEquals(int1, Integer.valueOf(domino.getLeftCrown()+domino.getRightCrown()));
}

List<Domino> filteredList;
@When("I initiate the browsing of all dominoes of {string} terrain type")
public void i_initiate_the_browsing_of_all_dominoes_of_terrain_type(String string) {
    // Write code here that turns the phrase above into concrete actions
//    throw new cucumber.api.PendingException();
	filteredList = KingdominoController.BrowseFilteredDominos(string, kingdomino);
	
}

@Then("list of dominoes with IDs {string} should be shown")
public void list_of_dominoes_with_IDs_should_be_shown(String string) {
    // Write code here that turns the phrase above into concrete actions
//    throw new cucumber.api.PendingException();
	List<String> idList = new ArrayList<String>(Arrays.asList(string.split(",")));
	boolean filtered = true;
	for(int i = 0; i<idList.size();i++) {
		if(filteredList.get(i).getId()!=Integer.parseInt(idList.get(i))) {
			filtered = false;
		}
	}
	assertEquals(true,filtered);
}


//HELPER METHODS
private static TerrainType getTerrainType(String terrain) {
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


private static TerrainType getTerrainTypeFilter(String terrain) {
	terrain = terrain.toLowerCase();
	if(terrain.equals("wheatfield")) {
		terrain = "wheat";
	}
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
