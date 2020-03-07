

package ca.mcgill.ecse223.kingdomino.features;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Kingdomino;
import ca.mcgill.ecse223.kingdomino.model.*;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;




public class SetGameOptionsStepDefinition {
	
	Kingdomino kingdomino;
	private Exception thrownException;
	
	@Given("the game is initialized for set game options")
	public void the_game_is_initialized_for_set_game_options() {
		// Intialize empty game
		kingdomino = new Kingdomino();
		Game game = new Game(48, kingdomino);
		game.setNumberOfPlayers(4);
		kingdomino.setCurrentGame(game);
	}

	@When("set game options is initiated")
	public void set_game_options_is_initiated() {
		assertEquals(null, thrownException);
	}

	@When("the number of players is set to {int}")
	public void the_number_of_players_is_set_to(Integer int1) {
		try {
			KingdominoController.SetGameOptions(int1, kingdomino);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@When("Harmony {string} selected as bonus option")
	public void harmony_selected_as_bonus_option(String string) {
		try {
			KingdominoController.SetGameOptions(string, kingdomino, "isUsingHarmony");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@When("Middle Kingdom {string} selected as bonus option")
	public void middle_Kingdom_selected_as_bonus_option(String string) {
		try {
			KingdominoController.SetGameOptions(string, kingdomino, "isUsingMiddleKingdom");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("the number of players shall be {int}")
	public void the_number_of_players_shall_be(Integer int1) {
		assertEquals(Integer.valueOf(int1), Integer.valueOf(kingdomino.getCurrentGame().getNumberOfPlayers()));
	}

	@Then("Harmony {string} an active bonus")
	public void harmony_an_active_bonus(String string) {
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
	}

	@Then("Middle Kingdom {string} an active bonus")
	public void middle_Kingdom_an_active_bonus(String string) {
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
	}
}

