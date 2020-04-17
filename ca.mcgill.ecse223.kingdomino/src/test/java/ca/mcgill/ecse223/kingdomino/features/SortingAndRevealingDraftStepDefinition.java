package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SortingAndRevealingDraftStepDefinition {
	
	@Given("there is a next draft, face down")
	public void there_is_a_next_draft_face_down() {
		HelperClass.testSetup();
		KingdominoController.browseDominoPile();
		Gameplay g = new Gameplay();
		KingdominoApplication.setGameplay(g);
		
		KingdominoController.setNextDraft();
	}

	@Given("all dominoes in current draft are selected")
	public void all_dominoes_in_current_draft_are_selected() {
		List<String> choices = new ArrayList<String>(Arrays.asList("blue,green,pink,yellow".split(",")));
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

	@When("next draft is sorted")
	public void next_draft_is_sorted() {
		KingdominoApplication.getGameplay().setGamestatus("CreatingDraft");
	}

	@When("next draft is revealed")
	public void next_draft_is_revealed() {
		// At the same time as the sorting
	}

	@Then("the next draft shall be sorted")
	public void the_next_draft_shall_be_sorted() {
		Draft nextDraft = KingdominoApplication.getKingdomino().getCurrentGame().getNextDraft();
		List<Domino> dominos = nextDraft.getIdSortedDominos();
		for(int i = 0; i<dominos.size()-1; i++) {
			assertTrue(dominos.get(i).getId()<dominos.get(i+1).getId());
		}
	}

	@Then("the next draft shall be facing up")
	public void the_next_draft_shall_be_facing_up() {
		assertEquals(Draft.DraftStatus.FaceUp, KingdominoApplication.getKingdomino().getCurrentGame().getNextDraft().getDraftStatus());
	}

	@Then("it shall be the player's turn with the lowest domino ID selection")
	public void it_shall_be_the_player_s_turn_with_the_lowest_domino_ID_selection() {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Player player = game.getNextPlayer();
		Draft curDraft = game.getCurrentDraft();
		assertEquals(player, curDraft.getSelection(0).getPlayer());
	}
}
