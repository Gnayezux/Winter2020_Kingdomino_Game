package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.*;


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
import static org.junit.jupiter.api.Assertions.assertEquals;


public class RotateDominoStepDefinitions {
	DominoInKingdom rotatedDomino;
	
	@Given("the game is initialized for rotate current domino")
	public void the_game_is_initialized_for_rotate_current_domino() {
		// Write code here that turns the phrase above into concrete actions
	    //throw new cucumber.api.PendingException();
		
		// Intialize empty game	
		Kingdomino kingdomino = new Kingdomino();
		Game game = new Game(48, kingdomino);
		game.setNumberOfPlayers(4);
		kingdomino.setCurrentGame(game);
		// Populate game
		addDefaultUsersAndPlayers(game);
		createAllDominoes(game);
		game.setNextPlayer(game.getPlayer(0));
		KingdominoApplication.setKingdomino(kingdomino);
	}

	@When("{string} requests to rotate the domino with {string}")
	public void requests_to_rotate_the_domino_with(String string, String string2) {
	    //Write code here that turns the phrase above into concrete actions
	    //throw new cucumber.api.PendingException();
		//Game currentGame = KingdominoApplication.getKingdomino().getCurrentGame();
		//Player player = getPlayerWithColor(string);
		//Domino currentDomino = player.getGame().getNextPlayer().getDominoSelection().getDomino();
		//Domino currentDomino = player.getDominoSelection().getDomino();
		if(rotatedDomino.getKingdom().getPlayer()==getPlayerWithColor(string)) {
			if(string2.equalsIgnoreCase("clockwise")) {
				KingdominoController.rotateCurrentDomino(true, rotatedDomino);
			}else {
				KingdominoController.rotateCurrentDomino(false, rotatedDomino);
			}
		}else {
			throw new RuntimeException("Domino was not identified with proper player!");
		}
		
		
	}

	@Then("the domino {int} is still tentatively placed at {int}:{int} but with new direction {string}")
	public void the_domino_is_still_tentatively_placed_at_but_with_new_direction(Integer int1, Integer int2, Integer int3, String string) {
	    // Write code here that turns the phrase above into concrete actions
	    //throw new cucumber.api.PendingException();
		//Player currentPlayer = getPlayerWithColor(string);
		Game currentGame = KingdominoApplication.getKingdomino().getCurrentGame();
		Player player = currentGame.getNextPlayer();
		Domino currentDomino = getdominoByID(int1);
		rotatedDomino = new DominoInKingdom(int2,int3,player.getKingdom(), currentDomino);
		assertEquals(int1, rotatedDomino.getDomino().getId());
		assertEquals(int2, rotatedDomino.getX());
		assertEquals(int3, rotatedDomino.getY());
		assertEquals(getDirection(string), rotatedDomino.getDirection());
	}

	@Then("the domino {int} should have status {string}")
	public void the_domino_should_have_status(Integer int1, String string) {
	    // Write code here that turns the phrase above into concrete actions
	    //throw new cucumber.api.PendingException();
		Domino currentDomino = getdominoByID(int1);
		assertEquals(getDominoStatus(string),currentDomino.getStatus());
		
	}

	@Then("domino {int} is tentatively placed at the same position {int}:{int} with the same direction {string}")
	public void domino_is_tentatively_placed_at_the_same_position_with_the_same_direction(Integer int1, Integer int2, Integer int3, String string) {
	    // Write code here that turns the phrase above into concrete actions
	    //throw new cucumber.api.PendingException();
		Game currentGame = KingdominoApplication.getKingdomino().getCurrentGame();
		Player player = currentGame.getNextPlayer();
		Domino currentDomino = getdominoByID(int1);
		rotatedDomino = new DominoInKingdom(int2,int3,player.getKingdom(), currentDomino);
		assertEquals(int1, rotatedDomino.getDomino().getId());
		assertEquals(int2, rotatedDomino.getX());
		assertEquals(int3, rotatedDomino.getY());
		assertEquals(getDirection(string), rotatedDomino.getDirection());
	}

	@Then("domino {int} should still have status {string}")
	public void domino_should_still_have_status(Integer int1, String string) {
	    // Write code here that turns the phrase above into concrete actions
	   // throw new cucumber.api.PendingException();
		Domino currentDomino = getdominoByID(int1);
		assertEquals(getDominoStatus(string),currentDomino.getStatus());
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
	
	private Player getPlayerWithColor(String color) {
		Game currentGame = KingdominoApplication.getKingdomino().getCurrentGame();
		PlayerColor colorOfPlayer;
		
		if(color.equalsIgnoreCase("Blue")) {
			colorOfPlayer = PlayerColor.Blue;
		}else if(color.equalsIgnoreCase("Green")) {
			colorOfPlayer = PlayerColor.Green;
		}else if(color.equalsIgnoreCase("Yellow")) {
			colorOfPlayer = PlayerColor.Yellow;
		}else if(color.equalsIgnoreCase("Pink")) {
			colorOfPlayer = PlayerColor.Pink;
		}else {
			throw new IllegalArgumentException("Can't pick that color!");
		}
		
		for(Player currentPlayer: currentGame.getPlayers()) {
			if(currentPlayer.getColor()==colorOfPlayer) {
				return currentPlayer;
			}
		}
		
		throw new IllegalArgumentException("No player with that color exists!");

		
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

	private void createAllDominoes(Game game) {
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

	private DominoStatus getDominoStatus(String status) {
		switch (status) {
		case "inPile":
			return DominoStatus.InPile;
		case "excluded":
			return DominoStatus.Excluded;
		case "inCurrentDraft":
			return DominoStatus.InCurrentDraft;
		case "inNextDraft":
			return DominoStatus.InNextDraft;
		case "erroneouslyPreplaced":
			return DominoStatus.ErroneouslyPreplaced;
		case "correctlyPreplaced":
			return DominoStatus.CorrectlyPreplaced;
		case "placedInKingdom":
			return DominoStatus.PlacedInKingdom;
		case "discarded":
			return DominoStatus.Discarded;
		default:
			throw new java.lang.IllegalArgumentException("Invalid domino status: " + status);
		}
		
		
	}


}
