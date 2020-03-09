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
import ca.mcgill.ecse223.kingdomino.model.Domino.DominoStatus;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;
import io.cucumber.java.en.*;

public class VerifyNoOverlappingStepDefinition {
	
	boolean valid;

@Given("the game is initialized to check domino overlapping")
public void the_game_is_initialized_to_check_domino_overlapping() {
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

@Given("the following dominoes are present in a player's kingdom:")
public void the_following_dominoes_are_present_in_a_player_s_kingdom(io.cucumber.datatable.DataTable dataTable) {
	Game game = KingdominoApplication.getKingdomino().getCurrentGame();
	List<Map<String, String>> valueMaps = dataTable.asMaps();
	for (Map<String, String> map : valueMaps) {

		Integer id = Integer.decode(map.get("id"));
		DirectionKind dir = getDirection(map.get("dominodir"));
		Integer posx = Integer.decode(map.get("posx"));
		Integer posy = Integer.decode(map.get("posy"));


		Domino dominoToPlace = getdominoByID(id);
		Kingdom kingdom = game.getPlayer(0).getKingdom();
		DominoInKingdom domInKingdom = new DominoInKingdom(posx, posy, kingdom, dominoToPlace);
		domInKingdom.setDirection(dir);
		dominoToPlace.setStatus(DominoStatus.PlacedInKingdom);
	}
}

@Given("the current player preplaced the domino with ID {int} at position {int}:{int} and direction {string}")
public void the_current_player_preplaced_the_domino_with_ID_at_position_and_direction(Integer int1, Integer int2, Integer int3, String string) {
 
	Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();

	
	Domino dom = KingdominoApplication.getKingdomino().getCurrentGame().getAllDomino(int1-1);

	DominoInKingdom domIn = new DominoInKingdom(int2, int3, player.getKingdom(), dom);
	domIn.setDirection(getDirection(string));
	player.getKingdom().addTerritory(domIn);
}

@When("check current preplaced domino overlapping is initiated")
public void check_current_preplaced_domino_overlapping_is_initiated() {
	int i = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer().getKingdom().getTerritories().size()-1;
	DominoInKingdom inDomino = (DominoInKingdom) KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer().getKingdom().getTerritory(i);

	valid = KingdominoController.verifyNoOverlapping(inDomino.getDomino(), KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer().getKingdom(), inDomino.getX(), inDomino.getY(), inDomino.getDirection());
}

@Then("the current-domino\\/existing-domino overlapping is {string}")
public void the_current_domino_existing_domino_overlapping_is(String string) {String isValid;
	if(valid) {
		isValid = "valid";
	}else {
		isValid="invalid";
	}
	assertEquals(string,isValid);
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
	
	
	

private Domino getdominoByID(int id) {
	Game game = KingdominoApplication.getKingdomino().getCurrentGame();
	for (Domino domino : game.getAllDominos()) {
		if (domino.getId() == id) {
			return domino;
		}
	}
	throw new java.lang.IllegalArgumentException("Domino with ID " + id + " not found.");
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
	
	
	}
	