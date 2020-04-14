package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.*;
import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.*;
import ca.mcgill.ecse223.kingdomino.model.Draft.DraftStatus;
import io.cucumber.java.en.*;

public class SelectingFirstDominoStepDefinitions {

	@Given("the game has been initialized for selecting first domino")
	public void the_game_has_been_initialized_for_selecting_first_domino() {
		HelperClass.testSetup();
		KingdominoController.browseDominoPile();
		Gameplay g = new Gameplay();
		KingdominoApplication.setGameplay(g);
		KingdominoApplication.getGameplay().setGamestatus("SelectingFirstDomino");
	}

	@Given("the initial order of players is {string}")
	public void the_initial_order_of_players_is(String string) {
		String[] order = string.split(",");
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		List<Player> players = game.getPlayers();
		for (int i = 0; i < players.size(); i++) {
			players.get(i).setColor(HelperClass.getColor(order[i]));
		}
	}

	@Given("the current draft has the dominoes with ID {string}")
	public void the_current_draft_has_the_dominoes_with_ID(String string) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		List<String> dominoString = new ArrayList<String>(Arrays.asList(string.split(",")));
		Draft draft = new Draft(Draft.DraftStatus.Sorted, game);
		for (String s : dominoString) {
			draft.addIdSortedDomino(HelperClass.getDominoByID(Integer.parseInt(s)));
		}
		game.setNextDraft(draft); // Hardcode what the draft is depending on the test
		game.setTopDominoInPile(HelperClass.getDominoByID(5));
	}

	@Given("player's first domino selection of the game is {string}")
	public void player_s_first_domino_selection_of_the_game_is(String string) {
		List<String> choices = new ArrayList<String>(Arrays.asList(string.split(",")));
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		List<Player> players = game.getPlayers();
		Draft draft = game.getNextDraft();
		for(String choice: choices) {
			if(!choice.equalsIgnoreCase("none")) {
				int j = 0;
				for(Player p: players) {
					if(p.getColor() == HelperClass.getColor(choice)) {
						new DominoSelection(p, draft.getIdSortedDomino(j), draft);
						break;
					}
					j++;
				}
			} 
		}
	}

	@Given("the {string} player is selecting his\\/her domino with ID {int}")
	public void the_player_is_selecting_his_her_domino_with_ID(String string, Integer int1) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		List<Player> players = game.getPlayers();
		for(Player p: players) {
			if(p.getColor() == HelperClass.getColor(string)) {
				game.setNextPlayer(p);
				KingdominoController.selectDomino(int1);
				break;
			}
		}
	}

	@Given("the {string} player is selecting his\\/her first domino with ID {int}")
	public void the_player_is_selecting_his_her_first_domino_with_ID(String string, Integer int1) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		List<Player> players = game.getPlayers();
		for(Player p: players) {
			if(p.getColor() == HelperClass.getColor(string)) {
				game.setNextPlayer(p);
				KingdominoController.selectDomino(int1);
				break;
			}
		}
	}
	
	// We use the annotation @And to signal precondition check instead of
	// initialization (which is done in @Given methods)
	@And("the validation of domino selection returns {string}")
	public void the_validation_of_domino_selection_returns(String expectedValidationResultString) {
		boolean expectedValidationResult = true;
		if ("success".equalsIgnoreCase(expectedValidationResultString.trim())) {
			expectedValidationResult = true;
		} else if ("error".equalsIgnoreCase(expectedValidationResultString.trim())) {
			expectedValidationResult = false;
		} else {
			throw new IllegalArgumentException(
					"Unknown validation result string \"" + expectedValidationResultString + "\"");
		}
		boolean actualValidationResult = KingdominoApplication.getGameplay().isSelectionValid();
		assertEquals(expectedValidationResult, actualValidationResult);
	}
	
	@When("the {string} player completes his\\/her domino selection")
	public void the_player_completes_his_her_domino_selection(String string) {
		KingdominoController.firstSelectionComplete();
	}

	@Then("the {string} player shall be {string} his\\/her domino")
	public void the_player_shall_be_his_her_domino(String string, String string2) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		List<Player> players = game.getPlayers();
		for(Player p: players) {
			if(p.getColor() == HelperClass.getColor(string)) {
				assertEquals(game.getNextPlayer(), p);
				break;
			}
		}
		assertEquals("Initializing.SelectingFirstDomino", KingdominoApplication.getGameplay().getGamestatusFullName());
	}
	
	@When("the {string} player completes his\\/her domino selection of the game")
	public void the_player_completes_his_her_domino_selection_of_the_game(String string) {
		KingdominoController.firstSelectionComplete();
	}

	@Then("a new draft shall be available, face down")
	public void a_new_draft_shall_be_available_face_down() {
		Draft draft = KingdominoApplication.getKingdomino().getCurrentGame().getNextDraft();
		assertNotEquals(null, draft);
		assertEquals(DraftStatus.FaceDown, draft.getDraftStatus());
	}

}
