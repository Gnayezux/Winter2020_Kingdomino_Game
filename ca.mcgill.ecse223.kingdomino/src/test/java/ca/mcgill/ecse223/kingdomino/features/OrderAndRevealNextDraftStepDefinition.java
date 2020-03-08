package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;

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

public class OrderAndRevealNextDraftStepDefinition {
	Kingdomino kingdomino;
@Given("the game is initialized for order next draft of dominoes")
public void the_game_is_initialized_for_order_next_draft_of_dominoes() {
	kingdomino = new Kingdomino();
	Game newGame = new Game(48, kingdomino);
	newGame.setNumberOfPlayers(4);
	kingdomino.setCurrentGame(newGame);
	KingdominoController.createAllDominoes(newGame);
}

@Given("the next draft is {string}")
public void the_next_draft_is(String string) {
	string = string.replaceAll("\\s+", "");
	string = string.replace("\"", "");
	List<String> numbers = new ArrayList<String>(Arrays.asList(string.split(",")));
	Game game = kingdomino.getCurrentGame();
	Draft draft = new Draft(Draft.DraftStatus.FaceDown, game);
	//
	for (int i = 0; i < 4; i++) {
		draft.addIdSortedDomino(game.getAllDomino(Integer.parseInt(numbers.get(i))-1));
	}
	game.setNextDraft(draft);
}

@Given("the dominoes in next draft are facing down")
public void the_dominoes_in_next_draft_are_facing_down() {
	//Initiated in previous given
}

@When("the ordering of the dominoes in the next draft is initiated")
public void the_ordering_of_the_dominoes_in_the_next_draft_is_initiated() {
	KingdominoController.orderNextDraft(kingdomino);
}

@Then("the status of the next draft is sorted")
public void the_status_of_the_next_draft_is_sorted() {
	assertEquals(Draft.DraftStatus.Sorted,kingdomino.getCurrentGame().getNextDraft().getDraftStatus());
}

@Then("the order of dominoes in the draft will be {string}")
public void the_order_of_dominoes_in_the_draft_will_be(String string) {
	string = string.replaceAll("\\s+", "");
	string = string.replace("\"", "");
	List<String> numbers = new ArrayList<String>(Arrays.asList(string.split(",")));
	boolean sorted= false;
	Draft draft = kingdomino.getCurrentGame().getNextDraft();
	for(int i = 0;i<numbers.size();i++) {
		if(Integer.parseInt(numbers.get(i)) == draft.getIdSortedDomino(i).getId()) {
			sorted = true;
		}else {
			sorted = false;
			break;
		}
	}
	assertEquals(true,sorted);
}

@Given("the game is initialized for reveal next draft of dominoes")
public void the_game_is_initialized_for_reveal_next_draft_of_dominoes() {
	kingdomino = new Kingdomino();
	Game newGame = new Game(48, kingdomino);
	newGame.setNumberOfPlayers(4);
	kingdomino.setCurrentGame(newGame);
	KingdominoController.createAllDominoes(newGame);
	
}

@Given("the dominoes in next draft are sorted")
public void the_dominoes_in_next_draft_are_sorted() {
	//sorted automatically for us
}

@When("the revealing of the dominoes in the next draft is initiated")
public void the_revealing_of_the_dominoes_in_the_next_draft_is_initiated() {
	KingdominoController.revealNextDraft(kingdomino);
	

}

@Then("the status of the next draft is face up")
public void the_status_of_the_next_draft_is_face_up() {
	assertEquals(Draft.DraftStatus.FaceUp,kingdomino.getCurrentGame().getNextDraft().getDraftStatus());
}

}
