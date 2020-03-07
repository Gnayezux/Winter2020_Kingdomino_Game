package ca.mcgill.ecse223.kingdomino.features;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.Castle;
import ca.mcgill.ecse223.kingdomino.model.Domino;
import ca.mcgill.ecse223.kingdomino.model.DominoSelection;
import ca.mcgill.ecse223.kingdomino.model.Draft;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Kingdom;
import ca.mcgill.ecse223.kingdomino.model.Kingdomino;
import ca.mcgill.ecse223.kingdomino.model.Player;
import ca.mcgill.ecse223.kingdomino.model.TerrainType;
import ca.mcgill.ecse223.kingdomino.model.User;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.junit.Assert.assertEquals;

public class ChooseNextDominoStepDefinition {
Kingdomino kingdomino;
@Given("the game is initialized for choose next domino")
public void the_game_is_initialized_for_choose_next_domino() {
    // Write code here that turns the phrase above into concrete actions
//    throw new cucumber.api.PendingException();
	// Intialize empty game
	kingdomino = new Kingdomino();
	Game game = new Game(48, kingdomino);
	game.setNumberOfPlayers(4);
	kingdomino.setCurrentGame(game);
	// Populate game
	addDefaultUsersAndPlayers(game);
	createAllDominoes(game);
	game.setNextPlayer(game.getPlayer(0));
	KingdominoApplication.setKingdomino(kingdomino);
}

@Given("the next draft is sorted with dominoes {string}")
public void the_next_draft_is_sorted_with_dominoes(String string) {
    // Write code here that turns the phrase above into concrete actions
//	System.out.print(string);
//    throw new cucumber.api.PendingException();
	Game game = kingdomino.getCurrentGame();
	List<String> dominoString = new ArrayList<String>(Arrays.asList(string.split(",")));
//	List<Domino> allDominos=new ArrayList<Domino>();
	Draft draft = new Draft(Draft.DraftStatus.Sorted, game);
	for(String s : dominoString) {
//		allDominos.add(game.getAllDomino(Integer.parseInt(s)-1));
		draft.addIdSortedDomino(game.getAllDomino(Integer.parseInt(s)-1));
//		System.out.print(game.getPlayer(Integer.parseInt(s)-5));
	}
	game.setCurrentDraft(draft);
}

@Given("player's domino selection {string}")
public void player_s_domino_selection(String string) {
    // Write code here that turns the phrase above into concrete actions
//    throw new cucumber.api.PendingException();
//	System.out.print(string);
	Game game = kingdomino.getCurrentGame();
	Draft draft = game.getCurrentDraft();
	string=string.replaceAll("\\s+", "");
	List<String> selection = new ArrayList<String>(Arrays.asList(string.split(",")));
//	System.out.print(draft.getIdSortedDominos());
	int index = 0;
	for(Player player : game.getPlayers()) {
		
		for(String select : selection) {
			System.out.print(select+" //");
			if(select.equals(player.getColor().toString().toLowerCase())) {
				System.out.print(select+" ////");
				draft.addSelection(player, draft.getIdSortedDomino(index));
//				System.out.print(draft.getSelections().get(index).getPlayer().getColor());
			}
		}
		index++;
	}
	
}

@Given("the current player is {string}")
public void the_current_player_is(String string) {
    // Write code here that turns the phrase above into concrete actions
//    throw new cucumber.api.PendingException();
//	System.out.print(string);
	Game game = kingdomino.getCurrentGame();
	Player curPlayer = game.getPlayer(0);
	for(Player player : game.getPlayers()) {
		if(player.getColor().toString().toLowerCase().equals(string)) {
			curPlayer = player;
		}
	}
	game.setNextPlayer(curPlayer);
}

@When("current player chooses to place king on {string}")
public void current_player_chooses_to_place_king_on(String string) {
    // Write code here that turns the phrase above into concrete actions
//    throw new cucumber.api.PendingException();
//	System.out.print(string);
	KingdominoController.ChooseNextDomino(kingdomino.getCurrentGame().getNextPlayer(), kingdomino, string);
}

@Then("current player king now is on {string}")
public void current_player_king_now_is_on(String string) {
    // Write code here that turns the phrase above into concrete actions
//    throw new cucumber.api.PendingException();
	Game game = kingdomino.getCurrentGame();
	Draft draft = game.getCurrentDraft();
	int id=-1;
	for(DominoSelection selection : draft.getSelections()) {
		if(game.getNextPlayer().equals(selection.getPlayer())) {
			id = selection.getDomino().getId();
		}
		
	}
	assertEquals(Integer.parseInt(string),id);
}

@Then("the selection for next draft is now equal to {string}")
public void the_selection_for_next_draft_is_now_equal_to(String string) {
    // Write code here that turns the phrase above into concrete actions
//    throw new cucumber.api.PendingException();
	Game game = kingdomino.getCurrentGame();
	Draft draft = game.getCurrentDraft();
	String selection = "";
	for(Domino domino : draft.getIdSortedDominos()) {
		if(domino.getDominoSelection()!=null) {
//			System.out.print(domino.getDominoSelection().getPlayer().getColor());
			
		}
//		System.out.print(domino.getDominoSelection());
//		boolean isThere = false;
//		for(int i = 0;i<draft.getSelections().size();i++) {
//			if(draft.getSelection(i).getDomino().equals(domino)) {
//				isThere = true;
//			}
//		}
//		if(isThere) {
//			selection += domino.getDominoSelection().getPlayer().getColor().toString().toLowerCase() +",";
//		}else {
//			selection += "none,";
//		}
	}
//	System.out.print(draft.getSelections().get(0).getPlayer());
//	selection = selection.substring(0, selection.length() - 1);
	
//	System.out.print(selection);
	
}

@Then("the selection for the next draft selection is still {string}")
public void the_selection_for_the_next_draft_selection_is_still(String string) {
    // Write code here that turns the phrase above into concrete actions
    throw new cucumber.api.PendingException();
}

//HELPER METHODS
private static void addDefaultUsersAndPlayers(Game game) {
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

private static void createAllDominoes(Game game) {
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
}