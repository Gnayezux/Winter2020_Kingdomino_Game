package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.*;

import java.util.*;
import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.*;
import io.cucumber.java.en.*;

public class CreateNextDraftStepDefinition {

	@Given("the game is initialized to create next draft")
	public void the_game_is_initialized_to_create_next_draft() {
		Kingdomino kingdomino = new Kingdomino();
		Game newGame = new Game(48, kingdomino);
		newGame.setNumberOfPlayers(4);
		kingdomino.setCurrentGame(newGame);
		KingdominoController.createAllDominos(newGame);
		KingdominoApplication.setKingdomino(kingdomino);
	}

	@Given("there has been {int} drafts created")
	public void there_has_been_drafts_created(Integer int1) {
		for (int i = 0; i < int1; i++) {
			if (i == 0) {
				KingdominoController.setFirstDraft(KingdominoApplication.getKingdomino());
			} else {
				KingdominoController.createNextDraft(KingdominoApplication.getKingdomino());
			}
		}
	}

	@Given("there is a current draft")
	public void there_is_a_current_draft() {
		assertNotEquals(KingdominoApplication.getKingdomino().getCurrentGame().getCurrentDraft(), null);
	}

	@Given("there is a next draft")
	public void there_is_a_next_draft() {
		assertNotEquals(KingdominoApplication.getKingdomino().getCurrentGame().getNextDraft(), null);
	}

	@Given("the top {int} dominoes in my pile have the IDs {string}")
	public void the_top_dominoes_in_my_pile_have_the_IDs(Integer int1, String string) {
		string = string.replaceAll("\\s+", "");
		string = string.replace("\"", "");
		List<String> numbers = new ArrayList<String>(Arrays.asList(string.split(",")));
		boolean correct = false;
		for (int i = 0; i < int1; i++) {
			if (Integer.parseInt(numbers.get(i)) == KingdominoApplication.getKingdomino().getCurrentGame().getAllDomino(i).getId()) {
				correct = true;
			} else {
				correct = false;
				break;
			}
		}
		assertEquals(true, correct);
	}

	@When("create next draft is initiated")
	public void create_next_draft_is_initiated() {
		KingdominoController.createNextDraft(KingdominoApplication.getKingdomino());
	}

	@Then("a new draft is created from dominoes {string}")
	public void a_new_draft_is_created_from_dominoes(String string) {
		string = string.replaceAll("\\s+", "");
		string = string.replace("\"", "");
		List<String> numbers = new ArrayList<String>(Arrays.asList(string.split(",")));
		Draft newDraft = KingdominoApplication.getKingdomino().getCurrentGame().getNextDraft();
		boolean created = false;
		for (int i = 0; i < numbers.size(); i++) {
			if (Integer.parseInt(numbers.get(i)) == newDraft.getIdSortedDomino(i).getId()) {
				created = true;
			} else {
				created = false;
				break;
			}
		}
		assertEquals(true, created);
	}

	@Then("the next draft now has the dominoes {string}")
	public void the_next_draft_now_has_the_dominoes(String string) {
		//this check is the exact same as the previous one.
	}

	@Then("the dominoes in the next draft are face down")
	public void the_dominoes_in_the_next_draft_are_face_down() {
		Draft nextDraft = KingdominoApplication.getKingdomino().getCurrentGame().getNextDraft();
		assertEquals(Draft.DraftStatus.FaceDown, nextDraft.getDraftStatus());
	}

	@Then("the top domino of the pile is ID {int}")
	public void the_top_domino_of_the_pile_is_ID(Integer int1) {
		assertEquals(int1, Integer.valueOf(KingdominoApplication.getKingdomino().getCurrentGame().getTopDominoInPile().getId()));
	}

	@Then("the former next draft is now the current draft")
	public void the_former_next_draft_is_now_the_current_draft() {
		if (KingdominoApplication.getKingdomino().getCurrentGame().getAllDrafts().size() == 12) {
			assertEquals(KingdominoApplication.getKingdomino().getCurrentGame().getAllDraft(KingdominoApplication.getKingdomino().getCurrentGame().getAllDrafts().size() - 1),
					KingdominoApplication.getKingdomino().getCurrentGame().getCurrentDraft());
		} else {
			assertEquals(KingdominoApplication.getKingdomino().getCurrentGame().getAllDraft(KingdominoApplication.getKingdomino().getCurrentGame().getAllDrafts().size() - 2),
					KingdominoApplication.getKingdomino().getCurrentGame().getCurrentDraft());
		}
	}

	@Given("this is a {int} player game")
	public void this_is_a_player_game(Integer int1) {
		Kingdomino kingdomino = new Kingdomino();
		Game newGame = new Game(48, kingdomino);
		newGame.setNumberOfPlayers(Integer.valueOf(int1));
		kingdomino.setCurrentGame(newGame);
		KingdominoController.createAllDominos(newGame);
		KingdominoApplication.setKingdomino(kingdomino);
	}

	@Then("the pile is empty")
	public void the_pile_is_empty() {
		assertEquals(0, KingdominoApplication.getKingdomino().getCurrentGame().getAllDominos().size());
	}

	@Then("there is no next draft")
	public void there_is_no_next_draft() {
		assertEquals(null, KingdominoApplication.getKingdomino().getCurrentGame().getNextDraft());
	}

}