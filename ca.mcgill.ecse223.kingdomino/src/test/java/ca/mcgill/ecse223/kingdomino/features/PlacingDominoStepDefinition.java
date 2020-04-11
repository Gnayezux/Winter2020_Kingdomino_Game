package ca.mcgill.ecse223.kingdomino.features;
import ca.mcgill.ecse223.kingdomino.model.*;
import ca.mcgill.ecse223.kingdomino.model.Domino.DominoStatus;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;
import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.*;
import io.cucumber.java.en.*;
import static org.junit.Assert.*;
import java.util.*;

public class PlacingDominoStepDefinition {
	Domino dom;
	/**
	 * @author Zeyang Xu
	 */
	@Given("the game has been initialized for placing domino")
	public void the_game_has_been_initialized_for_placing_domino() {
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
	
	/**
	 * @author Zeyang Xu
	 */
	
	@Given("it is not the last turn of the game")
	public void it_is_not_the_last_turn_of_the_game() {
		assertEquals(KingdominoApplication.getKingdomino().getAllGame(0).hasNextDraft(),true);
	}
	
	/**
	 * @author Zeyang Xu
	 */
	
	@Given("the current player is not the last player in the turn")
	public void the_current_player_is_not_the_last_player_in_the_turn() {
		assertEquals(KingdominoApplication.getKingdomino().getAllGame(0).hasNextPlayer(),true);
	}
	
	/**
	 * @author Zeyang Xu
	 */
	
	@Given("the current player is preplacing his/her domino with ID 6 at location 2:2 with direction \"down\"")
	public void the_current_player_is_preplacing_their_domino_with_ID6_at_location_22_with_direction_down() {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		dom = KingdominoApplication.getKingdomino().getCurrentGame().getAllDomino(6 - 1);
		DominoInKingdom domIn = new DominoInKingdom(2, 2, player.getKingdom(), dom);
		domIn.setDirection(getDirection("down"));
		player.getKingdom().addTerritory(domIn);
	}
	
	/**
	 * @author Zeyang Xu
	 */
	
	@And("the preplaced domino has the status \"CorrectlyPreplaced\"")
	public void the_preplaced_domino_has_the_status_CorrectlyPreplaced() {
		assertEquals(dom.getStatus(),DominoStatus.CorrectlyPreplaced);
	}
	
	/**
	 * @author Zeyang Xu
	 */
	
	@When("the current player places his/her domino")
	public void the_current_player_places_their_domino() {
		assertEquals(KingdominoController.placeDomino(KingdominoApplication.getKingdomino()),true);
	}
	@Then("this player now shall be making his/her domino selection")
	public void this_player_now_shall_be_making_their_domino_selection() {
		assertEquals(KingdominoController.ChooseNextDomino(KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer(), KingdominoApplication.getKingdomino(), 6),true);
	}
	
	@Given("the current player is the last player in the turn")
	public void the_current_player_is_the_last_player_in_the_turn() {
		assertEquals(KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer(), null);
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
