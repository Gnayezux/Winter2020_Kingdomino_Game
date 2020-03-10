


//package ca.mcgill.ecse223.kingdomino.features;
//
//import io.cucumber.java.en.Given;
//import io.cucumber.java.en.Then;
//import io.cucumber.java.en.When;
//import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
//import ca.mcgill.ecse223.kingdomino.model.Game;
//import ca.mcgill.ecse223.kingdomino.model.Kingdomino;
//import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
//import ca.mcgill.ecse223.kingdomino.model.*;
//import static org.junit.Assert.fail;
//import static org.junit.Assert.assertEquals;
//import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
//import static org.junit.Assert.fail;
//import static org.junit.Assert.assertEquals;
//
//
//public class VerifyCastleAdjacencyStepDefinition {
//	int x;
//	int y;
//	DirectionKind direction;
//	boolean result;
//	
//	@Given("the game is initialized for castle adjacency")
//	public void the_game_is_initialized_for_castle_adjacency() {
//	    Kingdomino aKingdomino = new Kingdomino();
//	    //throw new cucumber.api.PendingException();
//	}
//
//	@Given("the current player preplaced the domino with ID {int} at position {int}:{int} and string {String}")
//	public void the_current_player_preplaced_the_domino_with_ID_at_position_and_direction(Integer int1, Integer int2, Integer int3, String string) {
//	    this.x = int2;
//	    this.y = int3;
//	    this.direction = getDirection(string);
//	    
//	}
//
//	@When("check castle adjacency is initiated")
//	public void check_castle_adjacency_is_initiated() {
//	    // Write code here that turns the phrase above into concrete actions
//		this.result = KingdominoController.VerifyCastleAdjacency(x, y, direction);
//	}
//
//	@Then("the castle\\/domino adjacency is {string}")
//	public void the_castle_domino_adjacency_is(String string) {
//	    // Write code here that turns the phrase above into concrete actions
//	    if (this.result) {
//	    	assertEquals(this.result,getValidity(string));
//	    } else {
//	    	fail();
//	    }
//	}
//	
//	private DirectionKind getDirection(String dir) {
//		switch (dir) {
//		case "up":
//			return DirectionKind.Up;
//		case "down":
//			return DirectionKind.Down;
//		case "left":
//			return DirectionKind.Left;
//		case "right":
//			return DirectionKind.Right;
//		default:
//			throw new java.lang.IllegalArgumentException("Invalid direction: " + dir);
//		}
//	}
//	
//	private Boolean getValidity(String string) {
//		switch (string) {
//		case "valid":
//			return true;
//		case "invalid":
//			return false;
//		default:
//			throw new java.lang.IllegalArgumentException("Invalid validity: " + string);
//		}
//		
//	}
//}


package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.*;
import ca.mcgill.ecse223.kingdomino.model.Domino.DominoStatus;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;
import io.cucumber.java.en.*;

public class VerifyCastleAdjacencyStepDefinition {
	boolean isChecked;
	@Given("the game is initialized for castle adjacency")
	public void the_game_is_initialized_for_castle_adjacency() {
		Kingdomino kingdomino = new Kingdomino();
		Game game = new Game(48, kingdomino);
		game.setNumberOfPlayers(4);
		kingdomino.setCurrentGame(game);
		addDefaultUsersAndPlayers(game);
		KingdominoController.createAllDominos(game);
		game.setNextPlayer(game.getPlayer(0));
		KingdominoApplication.setKingdomino(kingdomino);
	}

	@Given("the current player preplaced the domino with ID {int} at position {int}:{int} and direction {string}")
	public void the_current_player_preplaced_the_domino_with_ID_at_position_and_direction(Integer int1, Integer int2, Integer int3, String string) {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		DominoInKingdom domIn = new DominoInKingdom(int2, int3, player.getKingdom(), KingdominoApplication.getKingdomino().getCurrentGame().getAllDomino(int1-1));
		domIn.setDirection(getDirection(string));
	}

	@When("check castle adjacency is initiated")
	public void check_castle_adjacency_is_initiated() {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		DominoInKingdom ter = (DominoInKingdom) player.getKingdom().getTerritory(player.getKingdom().numberOfTerritories()-1);
		isChecked = KingdominoController.verifyCastleAdjacency(ter.getX(), ter.getY(), ter.getDirection());
	}

	@Then("the castle\\/domino adjacency is {string}")
	public void the_castle_domino_adjacency_is(String string) {
		String a;
		if(isChecked) {
			a = "valid";
			assertEquals(string, a );
		}else {
			a = "invalid";
			assertEquals(string, a );
		}
	}
	
	
	/**********************
	 * * Helper Methods * *
	 **********************/
	
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

