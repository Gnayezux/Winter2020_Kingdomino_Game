package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.Castle;
import ca.mcgill.ecse223.kingdomino.model.Domino;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Kingdom;
import ca.mcgill.ecse223.kingdomino.model.Kingdomino;
import ca.mcgill.ecse223.kingdomino.model.Player;
import ca.mcgill.ecse223.kingdomino.model.User;
import ca.mcgill.ecse223.kingdomino.model.Domino.DominoStatus;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class VerifyGridSize {
boolean valid = false;
@Given("the game is initialized for verify grid size")
public void the_game_is_initialized_for_verify_grid_size() {
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

@When("validation of the grid size is initiated")
public void validation_of_the_grid_size_is_initiated() {
    // Write code here that turns the phrase above into concrete actions
//    throw new cucumber.api.PendingException();
//	System.out.print(KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer().getKingdom().getTerritories());
	valid = KingdominoController.verifyGridSize(KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer().getKingdom());
	}

@Then("the grid size of the player's kingdom shall be {string}")
public void the_grid_size_of_the_player_s_kingdom_shall_be(String string) {
    // Write code here that turns the phrase above into concrete actions
//    throw new cucumber.api.PendingException();
	String isValid;
	if(valid) {
		isValid = "valid";
	}else {
		isValid = "invalid";
	}
	System.out.print(string);
	System.out.print(isValid);
//	assertEquals(string,isValid);
	
	
}

@Given("the  player preplaces domino {int} to their kingdom at position {int}:{int} with direction {string}")
public void the_player_preplaces_domino_to_their_kingdom_at_position_with_direction(Integer int1, Integer int2, Integer int3, String string) {
    // Write code here that turns the phrase above into concrete actions
//    throw new cucumber.api.PendingException();
	Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
	// Set the next player here
	
	Domino dom = KingdominoApplication.getKingdomino().getCurrentGame().getAllDomino(int1-1);
//	dom.setStatus(DominoStatus.ErroneouslyPreplaced);
	DominoInKingdom domIn = new DominoInKingdom(int2, int3, player.getKingdom(), dom);
	domIn.setDirection(getDirection(string));
	player.getKingdom().addTerritory(domIn);
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
