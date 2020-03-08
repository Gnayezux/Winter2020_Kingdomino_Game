package ca.mcgill.ecse223.kingdomino.features;

import io.cucumber.java.en.*;
import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.*;
import static org.junit.Assert.*;

public class SetGameOptionsStepDefinition {
	@Given("the game is initialized for set game options")
	public void the_game_is_initialized_for_set_game_options() {
		Kingdomino kingdomino = new Kingdomino();
		KingdominoApplication.setKingdomino(kingdomino);
	}

	@When("set game options is initiated")
	public void set_game_options_is_initiated() {
		KingdominoController.setGameOptions(KingdominoApplication.getKingdomino());
	}

	@When("the number of players is set to {int}")
	public void the_number_of_players_is_set_to(Integer int1) {
		KingdominoController.setNumberOfPlayers(int1, KingdominoApplication.getKingdomino());
	}

	@When("Harmony {string} selected as bonus option")
	public void harmony_selected_as_bonus_option(String string) {
		boolean bonusActive;
		if(string.equals("is")) {
			bonusActive = true;
		} else {
			bonusActive = false;
		}
		KingdominoController.setBonusOption("Harmony", KingdominoApplication.getKingdomino(), bonusActive);
	}

	@When("Middle Kingdom {string} selected as bonus option")
	public void middle_Kingdom_selected_as_bonus_option(String string) {
		boolean bonusActive;
		if(string.equals("is")) {
			bonusActive = true;
		} else {
			bonusActive = false;
		}
		KingdominoController.setBonusOption("MiddleKingdom", KingdominoApplication.getKingdomino(), bonusActive);
	}

	@Then("the number of players shall be {int}")
	public void the_number_of_players_shall_be(Integer int1) {
		assertEquals(Integer.valueOf(int1), Integer.valueOf(KingdominoApplication.getKingdomino().getCurrentGame().getNumberOfPlayers()));
	}

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

