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
import io.cucumber.java.en.*;

public class VerifyNoOverlappingStepDefinition {
	
	int x;
	int y;
	int id;
	DirectionKind direction;
	boolean validity;
	
	@Given("the game is initialized to check domino overlapping")
	public void the_game_is_initialized_to_check_domino_overlapping() {
		Kingdomino kingdomino = new Kingdomino();
		Game game = new Game(48, kingdomino);
		game.setNumberOfPlayers(4);
		kingdomino.setCurrentGame(game);
		createAllDominoes(game);
		User user = game.getKingdomino().addUser("User1");
		Player player = new Player(game);
		player.setUser(user);
		KingdominoApplication.setKingdomino(kingdomino);
	}

	@Given("the following dominoes are present in a player's kingdom:")
	public void the_following_dominoes_are_present_in_a_player_s_kingdom(io.cucumber.datatable.DataTable dataTable) {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getPlayer(0);
		Kingdom kingdom = new Kingdom(player);
		new Castle(0, 0, kingdom, player);
		List<Map<String, String>> valueMaps = dataTable.asMaps();
		for (Map<String, String> map : valueMaps) {
			int posx = Integer.decode(map.get("posx"));
			int posy = Integer.decode(map.get("posy"));
			DominoInKingdom dom = new DominoInKingdom(posx, posy,kingdom, getDominoByID(Integer.decode(map.get("id"))));
			dom.setDirection(getDirection(map.get("dominodir")));
		}
	}
	
	@Given("the current player preplaced the domino with ID {int} at position {int}:{int} and direction {string}")
	public void the_current_player_preplaced_the_domino_with_ID_at_position_and_direction(Integer int1, Integer int2,
			Integer int3, String string) {
		x = int2;
		y = int3;
		direction = getDirection(string);
		id = int1;
		System.out.println("*******************");
	}
	
	@When("check current preplaced domino adjacency is initiated")
	public void check_current_preplaced_domino_overlapping_is_initiated() {
		Kingdom kingdom = KingdominoApplication.getKingdomino().getCurrentGame().getPlayer(0).getKingdom();
		System.out.println(x);
		System.out.println(y);
		System.out.println(direction);
		validity = KingdominoController.verifyNoOverlapping(getDominoByID(1),kingdom, x, y, direction);
	}
	
	@Then("the current-domino\\/existing-domino overlapping is {string}")
	public void the_current_domino_existing_domino_overlapping_is(String string) {
		assertEquals(getValidity(string),this.validity);
	}
	
	// These are helper methods

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
		
		private Domino getDominoByID(int id) {
			Game game = KingdominoApplication.getKingdomino().getCurrentGame();
			for (Domino domino : game.getAllDominos()) {
				if (domino.getId() == id) {
					return domino;
				}
			}
			throw new java.lang.IllegalArgumentException("Domino with ID " + id + " not found.");
		}
		/**
		 * This is a helper method to convert the string valid/invalid to a boolean for comparison
		 * @param string this is the validity as a string
		 * @return boolean true or false if it's valid or not
		 */
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
		
		/**
		 * This is a helper method to go from a string of direction to the enum
		 * @param dir direction of the domino (as a string)
		 * @return the direction of the domino (as a DirectionKind)
		 */
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
	