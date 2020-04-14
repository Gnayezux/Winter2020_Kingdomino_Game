package ca.mcgill.ecse223.kingdomino.features;

import java.util.*;
import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.*;
import ca.mcgill.ecse223.kingdomino.model.Kingdomino;
import io.cucumber.java.en.*;
import static org.junit.Assert.*;

public class ShuffleDominosStepDefinitions {

	/**
	 * @author Maxime Rieuf
	 */
	@Given("the game is initialized for shuffle dominoes")
	public void the_game_is_initialized_for_shuffle_dominoes() {
	    KingdominoController.startGameSetup();
	    KingdominoController.setNumberOfPlayers(4);
	}

	/**
	 * @author Maxime Rieuf
	 */
	@Given("there are {int} players playing")
	public void there_are_players_playing(Integer int1) {
		KingdominoApplication.getKingdomino().getCurrentGame().setNumberOfPlayers(int1);
	}

	/**
	 * @author Maxime Rieuf
	 */
	@When("the shuffling of dominoes is initiated")
	public void the_shuffling_of_dominoes_is_initiated() {
		KingdominoController.shuffleDominos();
		KingdominoController.setNextDraft();
		//System.out.println(KingdominoApplication.getKingdomino().getCurrentGame().getNextDraft().hasIdSortedDominos());
	}

	/**
	 * @author Maxime Rieuf
	 */
	@Then("the first draft shall exist")
	public void the_first_draft_shall_exist() {
		Draft draft = KingdominoApplication.getKingdomino().getCurrentGame().getNextDraft();
		assertTrue(draft.hasIdSortedDominos());
	}

	/**
	 * @author Maxime Rieuf
	 */
	@Then("the first draft should have {int} dominoes on the board face down")
	public void the_first_draft_should_have_dominoes_on_the_board_face_down(Integer int1) {
		// first assertEquals checks the number of dominoes in the draft
		// second assertEquals checks that the status of the draft is face down
		assertEquals((int)int1,
				(int)KingdominoApplication.getKingdomino().getCurrentGame().getNextDraft().getIdSortedDominos().size());
		assertEquals(Draft.DraftStatus.FaceDown, KingdominoApplication.getKingdomino().getCurrentGame().getNextDraft().getDraftStatus());

	}

	/**
	 * @author Maxime Rieuf
	 */
	@Then("there should be {int} dominoes left in the draw pile")
	public void there_should_be_dominoes_left_in_the_draw_pile(Integer int1) {
		// compares the size of the remaining drawpile with the integer given
		assertEquals(Integer.valueOf(int1), Integer.valueOf(KingdominoApplication.getKingdomino().getCurrentGame().getAllDominos().size()));
	}

	/**
	 * @author Maxime Rieuf
	 */
	@When("I initiate to arrange the domino in the fixed order {string}")
	public void i_initiate_to_arrange_the_domino_in_the_fixed_order(String string) {
		KingdominoController.setPileOrder(string);
		KingdominoController.setNextDraft();
	}

	/**
	 * @author Maxime Rieuf
	 */
	@Then("the draw pile should consist of everything in {string} except the first {int} dominoes with their order preserved")
	public void the_draw_pile_should_consist_of_everything_in_except_the_first_dominoes_with_their_order_preserved(String string, Integer int1) {

		List<Domino> dominos = new ArrayList<Domino>(KingdominoApplication.getKingdomino().getCurrentGame().getAllDominos());
		string = string.replaceAll("\\s+", "");
		string = string.replace("\"", "");

		Draft draft = KingdominoApplication.getKingdomino().getCurrentGame().getNextDraft();
		boolean sorted = false;
		for(int i = 0;i<4;i++) {
		
			if(dominos.contains(draft.getIdSortedDomino(i))) {
				sorted= false;
				break;
			}else {
				sorted = true;
			}
		}
		assertEquals(true,sorted);
	}

}
