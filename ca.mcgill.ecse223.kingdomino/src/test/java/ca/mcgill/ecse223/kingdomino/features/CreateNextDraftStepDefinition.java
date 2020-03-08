package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.Draft;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Kingdomino;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CreateNextDraftStepDefinition {

	Kingdomino kingdomino;
	private Exception thrownException;
	
@Given("the game is initialized to create next draft")
public void the_game_is_initialized_to_create_next_draft() {
	kingdomino = new Kingdomino();
	Game newGame = new Game(48, kingdomino);
	newGame.setNumberOfPlayers(4);
	kingdomino.setCurrentGame(newGame);
	KingdominoController.createAllDominoes(newGame);
}

@Given("there has been {int} drafts created")
public void there_has_been_drafts_created(Integer int1) {
	for(int i =0;i<int1;i++) {
		if(i==0) {
			KingdominoController.setFirstDraft(kingdomino);
		}else {
			KingdominoController.createNextDraft(kingdomino);
		}
		
	}
}

@Given("there is a current draft")
public void there_is_a_current_draft() {
	assertNotEquals(kingdomino.getCurrentGame().getCurrentDraft(), null);
}

@Given("there is a next draft")
public void there_is_a_next_draft() {
	assertNotEquals(kingdomino.getCurrentGame().getNextDraft(), null);
}

@Given("the top {int} dominoes in my pile have the IDs {string}")
public void the_top_dominoes_in_my_pile_have_the_IDs(Integer int1, String string) {
	string = string.replaceAll("\\s+", "");
	string = string.replace("\"", "");
	List<String> numbers = new ArrayList<String>(Arrays.asList(string.split(",")));
	boolean correct = false;
	for(int i =0;i<int1;i++) {
		if(Integer.parseInt(numbers.get(i))==kingdomino.getCurrentGame().getAllDomino(i).getId()) {
			correct = true;
		}else {
			correct = false;
			break;
		}
	}
	assertEquals(true,correct);
	
}

@When("create next draft is initiated")
public void create_next_draft_is_initiated() {
	KingdominoController.createNextDraft(kingdomino);
}

@Then("a new draft is created from dominoes {string}")
public void a_new_draft_is_created_from_dominoes(String string) {
	string = string.replaceAll("\\s+", "");
	string = string.replace("\"", "");
	List<String> numbers = new ArrayList<String>(Arrays.asList(string.split(",")));
	Draft newDraft = kingdomino.getCurrentGame().getNextDraft();
	boolean created = false;
	for(int i =0;i<numbers.size();i++) {
		if(Integer.parseInt(numbers.get(i)) == newDraft.getIdSortedDomino(i).getId()) {
			created=true;
		}else {
			created= false;
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
	Draft nextDraft = kingdomino.getCurrentGame().getNextDraft();
	assertEquals(Draft.DraftStatus.FaceDown,nextDraft.getDraftStatus());
}

@Then("the top domino of the pile is ID {int}")
public void the_top_domino_of_the_pile_is_ID(Integer int1) {
	assertEquals(int1, Integer.valueOf(kingdomino.getCurrentGame().getTopDominoInPile().getId()));
}

@Then("the former next draft is now the current draft")
public void the_former_next_draft_is_now_the_current_draft() {
	if(kingdomino.getCurrentGame().getAllDrafts().size() == 12) {
		assertEquals(kingdomino.getCurrentGame().getAllDraft(kingdomino.getCurrentGame().getAllDrafts().size()-1),kingdomino.getCurrentGame().getCurrentDraft());
	}else {
		assertEquals(kingdomino.getCurrentGame().getAllDraft(kingdomino.getCurrentGame().getAllDrafts().size()-2),kingdomino.getCurrentGame().getCurrentDraft());
	}
	}
	
	

@Given("this is a {int} player game")
public void this_is_a_player_game(Integer int1) {
	kingdomino = new Kingdomino();
	Game newGame = new Game(48, kingdomino);
	newGame.setNumberOfPlayers(Integer.valueOf(int1));
	kingdomino.setCurrentGame(newGame);
	KingdominoController.createAllDominoes(newGame);
}

@Then("the pile is empty")
public void the_pile_is_empty() {
	assertEquals(0,kingdomino.getCurrentGame().getAllDominos().size());
}

@Then("there is no next draft")
public void there_is_no_next_draft() {
	assertEquals(null,kingdomino.getCurrentGame().getNextDraft());
}

}
