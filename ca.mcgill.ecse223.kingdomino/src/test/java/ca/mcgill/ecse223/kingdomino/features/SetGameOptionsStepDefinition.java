package ca.mcgill.ecse223.kingdomino.features;

import io.cucumber.java.en.*;
import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.*;
import static org.junit.Assert.*;

public class SetGameOptionsStepDefinition {
	/**
	 * @author Abdallah Shapsough
	 */
	@Given("the game is initialized for set game options")
	public void the_game_is_initialized_for_set_game_options() {
		// Begininning of the game... Nothing needs to be done
	}

	/**
	 * @author Abdallah Shapsough
	 */
	@When("set game options is initiated")
	public void set_game_options_is_initiated() {
		KingdominoController.startGameSetup();
	}

	/**
	 * @author Abdallah Shapsough
	 */
	@When("the number of players is set to {int}")
	public void the_number_of_players_is_set_to(Integer int1) {
		KingdominoController.setNumberOfPlayers(int1);
	}

	/**
	 * @author Abdallah Shapsough
	 */
	@When("Harmony {string} selected as bonus option")
	public void harmony_selected_as_bonus_option(String string) {
		boolean bonusActive;
		if(string.equals("is")) {
			bonusActive = true;
		} else {
			bonusActive = false;
		}
		KingdominoController.setBonusOption("Harmony", bonusActive);
	}

	/**
	 * @author Abdallah Shapsough
	 */
	@When("Middle Kingdom {string} selected as bonus option")
	public void middle_Kingdom_selected_as_bonus_option(String string) {
		boolean bonusActive;
		if(string.equals("is")) {
			bonusActive = true;
		} else {
			bonusActive = false;
		}
		KingdominoController.setBonusOption("MiddleKingdom", bonusActive);
	}

	/**
	 * @author Abdallah Shapsough
	 */
	@Then("the number of players shall be {int}")
	public void the_number_of_players_shall_be(Integer int1) {
		assertEquals(Integer.valueOf(int1), Integer.valueOf(KingdominoApplication.getKingdomino().getCurrentGame().getNumberOfPlayers()));
	}

	/**
	 * @author Abdallah Shapsough
	 */
	@Then("Harmony {string} an active bonus")
	public void harmony_an_active_bonus(String string) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		boolean bonusActive = false;
		for(BonusOption option : game.getSelectedBonusOptions()) {
			if(option.getOptionName().equals("Harmony")) {
				bonusActive = true;
			}
		}
		if(string.equals("is")) {
			assertEquals(bonusActive, true);
		}else if(string.contentEquals("is not")) {
			assertEquals(bonusActive, false);
		}
	}

	/**
	 * @author Abdallah Shapsough
	 */
	@Then("Middle Kingdom {string} an active bonus")
	public void middle_Kingdom_an_active_bonus(String string) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		boolean bonusActive = false;
		for(BonusOption option : game.getSelectedBonusOptions()) {
			if(option.getOptionName().equals("MiddleKingdom")) {
				bonusActive = true;
			}
		}
		
		if(string.equals("is")) {
			assertEquals(bonusActive, true);
		}else if(string.contentEquals("is not")) {
			assertEquals(bonusActive, false);
		}
	}
}

