package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;
import java.util.*;
import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.*;
import io.cucumber.java.en.*;

public class OrderAndRevealNextDraftStepDefinition {
	
	/**
	 * @author Maxime Rieuf
	 */
	@Given("the game is initialized for order next draft of dominoes")
	public void the_game_is_initialized_for_order_next_draft_of_dominoes() {
		HelperClass.testSetup();
		KingdominoController.browseDominoPile();
	}

	/**
	 * @author Maxime Rieuf
	 */
	@Given("the next draft is {string}")
	public void the_next_draft_is(String string) {
		string = string.replaceAll("\\s+", "");
		string = string.replace("\"", "");
		List<String> numbers = new ArrayList<String>(Arrays.asList(string.split(",")));
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Draft draft = new Draft(Draft.DraftStatus.FaceDown, game);
		//
		for (int i = 0; i < 4; i++) {
			draft.addIdSortedDomino(game.getAllDomino(Integer.parseInt(numbers.get(i)) - 1));
		}
		game.setNextDraft(draft);
	}

	/**
	 * @author Maxime Rieuf
	 */
	@Given("the dominoes in next draft are facing down")
	public void the_dominoes_in_next_draft_are_facing_down() {
		// Initiated in previous given
	}

	/**
	 * @author Maxime Rieuf
	 */
	@When("the ordering of the dominoes in the next draft is initiated")
	public void the_ordering_of_the_dominoes_in_the_next_draft_is_initiated() {
		KingdominoController.orderNextDraft();
	}

	/**
	 * @author Maxime Rieuf
	 */
	@Then("the status of the next draft is sorted")
	public void the_status_of_the_next_draft_is_sorted() {
		assertEquals(Draft.DraftStatus.Sorted, KingdominoApplication.getKingdomino().getCurrentGame().getNextDraft().getDraftStatus());
	}

	/**
	 * @author Maxime Rieuf
	 */
	@Then("the order of dominoes in the draft will be {string}")
	public void the_order_of_dominoes_in_the_draft_will_be(String string) {
		string = string.replaceAll("\\s+", "");
		string = string.replace("\"", "");
		List<String> numbers = new ArrayList<String>(Arrays.asList(string.split(",")));
		boolean sorted = false;
		Draft draft = KingdominoApplication.getKingdomino().getCurrentGame().getNextDraft();
		for (int i = 0; i < numbers.size(); i++) {
			if (Integer.parseInt(numbers.get(i)) == draft.getIdSortedDomino(i).getId()) {
				sorted = true;
			} else {
				sorted = false;
				break;
			}
		}
		assertEquals(true, sorted);
	}

	/**
	 * @author Maxime Rieuf
	 */
	@Given("the game is initialized for reveal next draft of dominoes")
	public void the_game_is_initialized_for_reveal_next_draft_of_dominoes() {
		HelperClass.testSetup();
		KingdominoController.browseDominoPile();
	}

	/**
	 * @author Maxime Rieuf
	 */
	@Given("the dominoes in next draft are sorted")
	public void the_dominoes_in_next_draft_are_sorted() {
		KingdominoController.setNextDraft();
		KingdominoController.orderNextDraft();
	}

	/**
	 * @author Maxime Rieuf
	 */
	@When("the revealing of the dominoes in the next draft is initiated")
	public void the_revealing_of_the_dominoes_in_the_next_draft_is_initiated() {
		KingdominoController.revealNextDraft();

	}

	/**
	 * @author Maxime Rieuf
	 */
	@Then("the status of the next draft is face up")
	public void the_status_of_the_next_draft_is_face_up() {
		assertEquals(Draft.DraftStatus.FaceUp, KingdominoApplication.getKingdomino().getCurrentGame().getNextDraft().getDraftStatus());
	}

}
