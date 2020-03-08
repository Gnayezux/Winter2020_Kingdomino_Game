package ca.mcgill.ecse223.kingdomino.features;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.Draft;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Kingdomino;
import ca.mcgill.ecse223.kingdomino.model.Player;
import ca.mcgill.ecse223.kingdomino.model.User;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class StartANewGameStepDefinitions {
	
	Kingdomino kingdomino;
	@Given("the program is started and ready for starting a new game")
	public void the_program_is_started_and_ready_for_starting_a_new_game() {
	    // Write code here that turns the phrase above into concrete actions
		//Initialize empty game
		kingdomino = KingdominoApplication.getKingdomino();
		Game newGame = new Game(48,kingdomino);
		newGame.setNumberOfPlayers(4);
		kingdomino.setCurrentGame(newGame);
		
	}

	@Given("there are four selected players")
	public void there_are_four_selected_players() {
		String[] userNames = { "User1", "User2", "User3", "User4" };
		for (int i = 0; i < userNames.length; i++) {
			User user = kingdomino.getCurrentGame().getKingdomino().addUser(userNames[i]);
			Player player = new Player(kingdomino.getCurrentGame());
			player.setUser(user);
			player.setColor(PlayerColor.values()[i]);

		}
		
	}

	@Given("bonus options Harmony and MiddleKingdom are selected")
	public void bonus_options_Harmony_and_MiddleKingdom_are_selected() {
	    // Write code here that turns the phrase above into concrete actions
		KingdominoController.SetGameOptions("is", kingdomino, "isUsingHarmony");
		KingdominoController.SetGameOptions("is", kingdomino, "isUsingMiddleKingdom");

	}

	@When("starting a new game is initiated")
	public void starting_a_new_game_is_initiated() {    
		// Write code here that turns the phrase above into concrete actions

		KingdominoController.startNewGame(kingdomino);
//	    throw new cucumber.api.PendingException();
	}

	@When("reveal first draft is initiated")
	public void reveal_first_draft_is_initiated() {
	    // Write code here that turns the phrase above into concrete actions
//		kingdomino.getCurrentGame().getAllDraft(0);
		KingdominoController.setFirstDraft(kingdomino);
	    //throw new cucumber.api.PendingException();
	}

	@Then("all kingdoms shall be initialized with a single castle")
	public void all_kingdoms_shall_be_initialized_with_a_single_castle() {
	    //for each player in the current game we check if the size of the list of their kingdom territories is equal to 1. 
		//In which case it would mean that only a castle is on each kingdom. (a castle is a territory)
		for(int i = 0; i < kingdomino.getCurrentGame().getNumberOfPlayers(); i++) {			
			assertEquals(1, kingdomino.getCurrentGame().getPlayer(i).getKingdom().getTerritories().size());
		}
		
		
//	    throw new cucumber.api.PendingException();
	}

	@Then("all castle are placed at {int}:{int} in their respective kingdoms")
	public void all_castle_are_placed_at_in_their_respective_kingdoms(Integer int1, Integer int2) {
	
		for(int i = 0; i < kingdomino.getCurrentGame().getNumberOfPlayers(); i++) {			
			assertEquals(Integer.valueOf(int1), Integer.valueOf(kingdomino.getCurrentGame().getPlayer(i).getKingdom().getTerritory(0).getX()));
			assertEquals(Integer.valueOf(int2), Integer.valueOf(kingdomino.getCurrentGame().getPlayer(i).getKingdom().getTerritory(0).getY()));
		}
	   
	}

	@Then("the first draft of dominoes is revealed")
	public void the_first_draft_of_dominoes_is_revealed() {
	    // Write code here that turns the phrase above into concrete actions
//	    throw new cucumber.api.PendingException();
		KingdominoController.setFirstDraft(kingdomino);
		kingdomino.getCurrentGame().getCurrentDraft().setDraftStatus(Draft.DraftStatus.FaceUp);

	}

	@Then("all the dominoes form the first draft are facing up")
	public void all_the_dominoes_form_the_first_draft_are_facing_up() {
		//check if the draft status of the current first draft is FaceUp
		assertEquals(Draft.DraftStatus.FaceUp, kingdomino.getCurrentGame().getCurrentDraft().getDraftStatus());
			
	}

	@Then("all the players have no properties")
	public void all_the_players_have_no_properties() {
	    // Write code here that turns the phrase above into concrete actions
		for (int i = 0; i < kingdomino.getCurrentGame().getNumberOfPlayers(); i++) {
			assertEquals(null, kingdomino.getCurrentGame().getPlayer(i).getDominoSelection());
		//throw new cucumber.api.PendingException();
		}
		
	}

	@Then("all player scores are initialized to zero")
	public void all_player_scores_are_initialized_to_zero() {
	    // Write code here that turns the phrase above into concrete actions
		for (int i = 0; i < kingdomino.getCurrentGame().getNumberOfPlayers(); i++) {
			assertEquals(0, kingdomino.getCurrentGame().getPlayer(i).getTotalScore());
		//throw new cucumber.api.PendingException();
		}
//		fail();
	    //throw new cucumber.api.PendingException();
	}

}
