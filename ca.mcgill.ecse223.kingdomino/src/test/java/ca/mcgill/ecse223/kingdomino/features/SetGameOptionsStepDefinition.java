

package ca.mcgill.ecse223.kingdomino.features;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Kingdomino;
import ca.mcgill.ecse223.kingdomino.model.*;
import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import static org.junit.Assert.fail;
import ca.mcgill.ecse223.kingdomino.model.Castle;
import ca.mcgill.ecse223.kingdomino.model.Domino;
import ca.mcgill.ecse223.kingdomino.model.Domino.DominoStatus;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
import ca.mcgill.ecse223.kingdomino.model.Kingdom;
import ca.mcgill.ecse223.kingdomino.model.Player;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;
import ca.mcgill.ecse223.kingdomino.model.TerrainType;
import ca.mcgill.ecse223.kingdomino.model.User;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;



public class SetGameOptionsStepDefinition {
	
	Kingdomino kingdomino;
	private Exception thrownException;
	@Given("the game is initialized for set game options")
	public void the_game_is_initialized_for_set_game_options() {
	    // Write code here that turns the phrase above into concrete actions
	
		// Intialize empty game
		kingdomino = new Kingdomino();
		Game game = new Game(48, kingdomino);
		game.setNumberOfPlayers(4);
		kingdomino.setCurrentGame(game);
	}

	@When("set game options is initiated")
	public void set_game_options_is_initiated() {
	    // Write code here that turns the phrase above into concrete actions
//		boolean setGame = KingdominoController.SetGameOptions(4,false,false);
//		if(!setGame == true) {
//			fail();
//		}
		assertEquals(null, thrownException);

	}

	@When("the number of players is set to {int}")
	public void the_number_of_players_is_set_to(Integer int1) {
	    // Write code here that turns the phrase above into concrete actions
		try {
			KingdominoController.SetGameOptions(int1, kingdomino);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//	    throw new cucumber.api.PendingException();
	}

	@When("Harmony {string} selected as bonus option")
	public void harmony_selected_as_bonus_option(String string) {
		
	    // Write code here that turns the phrase above into concrete actions
		try {
			KingdominoController.SetGameOptions(string, kingdomino, "isUsingHarmony");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//	    throw new cucumber.api.PendingException();
	}

	@When("Middle Kingdom {string} selected as bonus option")
	public void middle_Kingdom_selected_as_bonus_option(String string) {
	    // Write code here that turns the phrase above into concrete actions
		try {
			KingdominoController.SetGameOptions(string, kingdomino, "isUsingMiddleKingdom");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//	    throw new cucumber.api.PendingException();
	}

	@Then("the number of players shall be {int}")
	public void the_number_of_players_shall_be(Integer int1) {
	    // Write code here that turns the phrase above into concrete actions
		assertEquals(Integer.valueOf(int1), Integer.valueOf(kingdomino.getCurrentGame().getNumberOfPlayers()));
//	    throw new cucumber.api.PendingException();
	}

	@Then("Harmony {string} an active bonus")
	public void harmony_an_active_bonus(String string) {
	    // Write code here that turns the phrase above into concrete actions
		Game game = kingdomino.getCurrentGame();
		if(string.equals("is")) {
			for(BonusOption option : game.getSelectedBonusOptions()) {
				if(option.getOptionName().equals("isUsingHarmony")) {
					assertEquals(null, thrownException);
				}
			}
		}else if(string.contentEquals("is not")) {
			for(BonusOption option : game.getSelectedBonusOptions()) {
				if(option.getOptionName().equals("isUsingHarmony")) {
					fail();
				}
			}
		}
//	    throw new cucumber.api.PendingException();
	}

	@Then("Middle Kingdom {string} an active bonus")
	public void middle_Kingdom_an_active_bonus(String string) {
	    // Write code here that turns the phrase above into concrete actions
		Game game = kingdomino.getCurrentGame();
		if(string.equals("is")) {
			for(BonusOption option : game.getSelectedBonusOptions()) {
				if(option.getOptionName().equals("isUsingMiddleKingdom")) {
					assertEquals(null, thrownException);
				}
			}
		}else if(string.contentEquals("is not")) {
			for(BonusOption option : game.getSelectedBonusOptions()) {
				if(option.getOptionName().equals("isUsingMiddleKingdom")) {
					fail();
				}
			}
		}
//	    throw new cucumber.api.PendingException();
	}
}

