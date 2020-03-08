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

//CHANGES MADE TO THE SCENARIO
//All DominoStatus' had a capital letter first, changed to lowercase

public class MoveCurrentDominoStepDefinition {
	Player player;
	DominoInKingdom provisionaryDomino;

	@Given("the game is initialized for move current domino")
	public void the_game_is_initialized_for_move_current_domino() {
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

	@Given("it is {string}'s turn")
	public void it_is_s_turn(String string) {
	    // Write code here that turns the phrase above into concrete actions
	    //throw new cucumber.api.PendingException();
		Game currentGame = KingdominoApplication.getKingdomino().getCurrentGame();
		currentGame.setNextPlayer(getPlayerWithColor(string));
	
	}

	@Given("{string} has selected domino {int}")
	public void has_selected_domino(String string, Integer int1) {
	    // Write code here that turns the phrase above into concrete actions
	    //throw new cucumber.api.PendingException();
		Game currentGame = KingdominoApplication.getKingdomino().getCurrentGame();
		Domino currentDomino = getdominoByID(int1);
		Player currentPlayer = getPlayerWithColor(string);
		provisionaryDomino = new DominoInKingdom(0,0,currentPlayer.getKingdom(), currentDomino);
		
	}

	@When("{string} removes his king from the domino {int}")
	public void removes_his_king_from_the_domino(String string, Integer int1) {
	    // Write code here that turns the phrase above into concrete actions
	    //throw new cucumber.api.PendingException();
		Player currentPlayer = getPlayerWithColor(string);
		Domino currentDomino = getdominoByID(int1);
		provisionaryDomino = new DominoInKingdom(0,0,currentPlayer.getKingdom(), currentDomino);
		KingdominoController.moveCurrentDomino(provisionaryDomino, null, currentPlayer);
	}

	@Then("domino {int} should be tentative placed at position {int}:{int} of {string}'s kingdom with ErroneouslyPreplaced status")
	public void domino_should_be_tentative_placed_at_position_of_s_kingdom_with_ErroneouslyPreplaced_status(Integer int1, Integer int2, Integer int3, String string) {
	    // Write code here that turns the phrase above into concrete actions
	    //throw new cucumber.api.PendingException(); 
		Player currentPlayer = getPlayerWithColor(string);
		Domino currentDomino = getdominoByID(int1);
		provisionaryDomino = new DominoInKingdom(int2,int3,currentPlayer.getKingdom(), currentDomino);
		assertEquals(int1, provisionaryDomino.getDomino().getId());
		assertEquals(int2, provisionaryDomino.getX());
		assertEquals(int3, provisionaryDomino.getY());
		assertEquals(DominoStatus.ErroneouslyPreplaced, provisionaryDomino.getDomino().getStatus());
	}

	@Given("{string}'s kingdom has following dominoes:")
	public void s_kingdom_has_following_dominoes(String string, io.cucumber.datatable.DataTable dataTable) {
	    // Write code here that turns the phrase above into concrete actions
	    // For automatic transformation, change DataTable to one of
	    // E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
	    // Map<K, List<V>>. E,K,V must be a String, Integer, Float,
	    // Double, Byte, Short, Long, BigInteger or BigDecimal.
	    //
	    // For other transformations you can register a DataTableType.
	    //throw new cucumber.api.PendingException();
		Player currentPlayer = getPlayerWithColor(string);
		
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		List<Map<String, String>> valueMaps = dataTable.asMaps();
		for (Map<String, String> map : valueMaps) {
			
			// Get values from cucumber table
			Integer id = Integer.decode(map.get("id"));
			DirectionKind dir = getDirection(map.get("dir"));
			Integer posx = Integer.decode(map.get("posx"));
			Integer posy = Integer.decode(map.get("posy"));

			// Add the domino to a player's kingdom
			Domino dominoToPlace = getdominoByID(id);
			Kingdom kingdom = game.getPlayer(0).getKingdom();
			DominoInKingdom domInKingdom = new DominoInKingdom(posx, posy, kingdom, dominoToPlace);
			domInKingdom.setDirection(dir);
			dominoToPlace.setStatus(DominoStatus.PlacedInKingdom);
		}
	}
	
	
	@Given("domino {int} is tentatively placed at position {int}:{int} with direction {string}")
	public void domino_is_tentatively_placed_at_position_with_direction(Integer int1, Integer int2, Integer int3, String string) {
	    //Write code here that turns the phrase above into concrete actions
	    //throw new cucumber.api.PendingException();
		
		Domino currentDomino = getdominoByID(int1);
		if(currentDomino.getId() == int1) {
			provisionaryDomino.setX(int2);
			provisionaryDomino.setY(int3);
			provisionaryDomino.setDirection(getDirection(string));
			
		}else {
			throw new IllegalArgumentException("Wrong Domino ID!"); //Change
		}
	
		
	}

	//This is wrong!
	@When("{string} requests to move the domino {string}")
	public void requests_to_move_the_domino(String string, String string2) {
	    // Write code here that turns the phrase above into concrete actions
	    //throw new cucumber.api.PendingException();
		
		Player currentPlayer = getPlayerWithColor(string);
		//Domino currentDomino = currentPlayer.getDominoSelection().getDomino();
		//provisionaryDomino = new DominoInKingdom()
		
		/*
		
		Kingdom currentKD = currentPlayer.getKingdom();
		provisionaryDomino = (DominoInKingdom) currentKD.getTerritory(currentKD.numberOfTerritories()-1);
		
		
		*/
		KingdominoController.moveCurrentDomino(provisionaryDomino, getDirection(string2), currentPlayer);
		
	}

	@Then("the domino {int} should be tentatively placed at position {int}:{int} with direction {string}")
	public void the_domino_should_be_tentatively_placed_at_position_with_direction(Integer int1, Integer int2, Integer int3, String string) {
	    // Write code here that turns the phrase above into concrete actions
	    //throw new cucumber.api.PendingException();
		Domino currentDomino = getdominoByID(int1);
		//if(currentDomino.getId()==int1) {
			assertEquals(int2, provisionaryDomino.getX());
			assertEquals(int3, provisionaryDomino.getY());
			assertEquals(getDirection(string), provisionaryDomino.getDirection());
		//}else {
		//	throw new RuntimeException("Wrong Domino ID!"); //Change
		//}
	}

	@Then("the new status of the domino is {string}")
	public void the_new_status_of_the_domino_is(String string) {
	    // Write code here that turns the phrase above into concrete actions
	    //throw new cucumber.api.PendingException();
		assertEquals(getDominoStatus(string), provisionaryDomino.getDomino().getStatus());
	}

	@Given("domino {int} has status {string}")
	public void domino_has_status(Integer int1, String string) {
	    // Write code here that turns the phrase above into concrete actions
	    //throw new cucumber.api.PendingException();
		Domino currentDomino = getdominoByID(int1);
		currentDomino.setStatus(getDominoStatus(string));
	}

	@Then("the domino {int} is still tentatively placed at position {int}:{int}")
	public void the_domino_is_still_tentatively_placed_at_position(Integer int1, Integer int2, Integer int3) {
	    // Write code here that turns the phrase above into concrete actions
	    //throw new cucumber.api.PendingException();
		Domino currentDomino = getdominoByID(int1);
		assertEquals(int2, provisionaryDomino.getX());
		assertEquals(int3, provisionaryDomino.getY());
		
	}

	@Then("the domino should still have status {string}")
	public void the_domino_should_still_have_status(String string) {
	    // Write code here that turns the phrase above into concrete actions
	    //throw new cucumber.api.PendingException();
		assertEquals(getDominoStatus(string), provisionaryDomino.getDomino().getStatus());
	}


	
	///////////////////////////////////////
	/// -----Private Helper Methods---- ///
	///////////////////////////////////////

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
