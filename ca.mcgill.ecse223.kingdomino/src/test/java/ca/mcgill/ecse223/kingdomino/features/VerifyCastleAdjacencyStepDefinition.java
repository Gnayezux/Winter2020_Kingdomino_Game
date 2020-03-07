package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;
import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.*;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
import io.cucumber.java.en.*;

public class VerifyCastleAdjacencyStepDefinition {
	int x;
	int y;
	int id;
	DirectionKind direction;
	Boolean validity;

	@Given("the game is initialized for castle adjacency")
	public void the_game_is_initialized_for_castle_adjacency() {
		 Kingdomino kingdomino = new Kingdomino(); 
		 Game game = new Game(48,kingdomino); 
		 game.setNumberOfPlayers(4); 
		 kingdomino.setCurrentGame(game);
		 KingdominoApplication.setKingdomino(kingdomino);
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

	@When("check castle adjacency is initiated")
	public void check_castle_adjacency_is_initiated() {
		validity = KingdominoController.verifyCastleAdjacency(x, y, direction);
	}

	@Then("the castle\\/domino adjacency is {string}")
	public void the_castle_domino_adjacency_is(String string) {
		assertEquals(getValidity(string), this.validity);
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