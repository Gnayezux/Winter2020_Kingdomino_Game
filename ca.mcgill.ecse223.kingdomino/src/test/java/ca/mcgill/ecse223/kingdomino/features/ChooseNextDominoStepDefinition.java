package ca.mcgill.ecse223.kingdomino.features;

import java.util.*;
import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.*;
import io.cucumber.java.en.*;
import static org.junit.Assert.assertEquals;

public class ChooseNextDominoStepDefinition {
	/**
	 * @author Abdallah Shapsough
	 */
	@Given("the game is initialized for choose next domino")
	public void the_game_is_initialized_for_choose_next_domino() {
		HelperClass.testSetup();
		KingdominoController.shuffleDominos();
	}

	/**
	 * @author Abdallah Shapsough
	 */
	@Given("the next draft is sorted with dominoes {string}")
	public void the_next_draft_is_sorted_with_dominoes(String string) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		List<String> dominoString = new ArrayList<String>(Arrays.asList(string.split(",")));
		Draft draft = new Draft(Draft.DraftStatus.Sorted, game);
		for (String s : dominoString) {
			draft.addIdSortedDomino(HelperClass.getDominoByID(Integer.parseInt(s)));
		}
		game.setNextDraft(draft); // Hardcode what the draft is depending on the test
	}

	/**
	 * @author Abdallah Shapsough
	 */
	@Given("player's domino selection {string}")
	public void player_s_domino_selection(String string) {
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

	/**
	 * @author Abdallah Shapsough
	 */
	@Given("the current player is {string}")
	public void the_current_player_is(String string) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Player curPlayer = game.getPlayer(0);
		for (Player player : game.getPlayers()) {
			if (player.getColor().toString().toLowerCase().equals(string)) {
				curPlayer = player;
				break;
			}
		}
		game.setNextPlayer(curPlayer);
	}

	/**
	 * @author Abdallah Shapsough
	 */
	@When("current player chooses to place king on {int}")
	public void current_player_chooses_to_place_king_on(Integer int1) {
		KingdominoController.selectDomino((int)int1);
	}

	/**
	 * @author Abdallah Shapsough
	 */
	@Then("current player king now is on {string}")
	public void current_player_king_now_is_on(String string) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Player curPlayer = game.getNextPlayer();
		assertEquals(Integer.parseInt(string), curPlayer.getDominoSelection().getDomino().getId());
	}
	
	/**
	 * @author Abdallah Shapsough
	 */
	@Then("the selection for next draft is now equal to {string}")
	public void the_selection_for_next_draft_is_now_equal_to(String string) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Draft draft = game.getNextDraft();
		string = string.replaceAll("\\s+", "");
		List<String> selection = new ArrayList<String>(Arrays.asList(string.split(",")));
		boolean isThere = false;
		int j = 0;
		for (int i = 0; i < selection.size(); i++) {
			if (!selection.get(i).equals("none")) {

				if (draft.getSelection(i - j).getPlayer().getColor().toString().toLowerCase()
						.equals(selection.get(i))) {
					isThere = true;
				} else {
					isThere = false;
				}

			} else {
				j++;
			}

		}
		assertEquals(true, isThere);

	}

	/**
	 * @author Abdallah Shapsough
	 */
	@Then("the selection for the next draft selection is still {string}")
	public void the_selection_for_the_next_draft_selection_is_still(String string) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Draft draft = game.getNextDraft();
		string = string.replaceAll("\\s+", "");
		List<String> selection = new ArrayList<String>(Arrays.asList(string.split(",")));
		boolean isThere = false;
		int j = 0;
		for (int i = 0; i < selection.size(); i++) {
			if (!selection.get(i).equals("none")) {

				if (draft.getSelection(i - j).getPlayer().getColor().toString().toLowerCase()
						.equals(selection.get(i))) {
					isThere = true;
				} else {
					isThere = false;
				}

			} else {
				j++;
			}
		}
		assertEquals(true, isThere);
	}

}