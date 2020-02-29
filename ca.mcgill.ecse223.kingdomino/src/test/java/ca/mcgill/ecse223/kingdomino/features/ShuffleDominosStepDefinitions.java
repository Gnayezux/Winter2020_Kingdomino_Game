package ca.mcgill.ecse223.kingdomino.features;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.Domino;
import ca.mcgill.ecse223.kingdomino.model.Draft;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Kingdomino;
import ca.mcgill.ecse223.kingdomino.model.TerrainType;
import ca.mcgill.ecse223.kingdomino.model.User;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.junit.Assert.assertEquals;

public class ShuffleDominosStepDefinitions {
	
	Kingdomino kingdomino;
	@Given("the game is initialized for shuffle dominoes")
	public void the_game_is_initialized_for_shuffle_dominoes() {
	    // Write code here that turns the phrase above into concrete actions
		kingdomino = new Kingdomino();
		Game newGame = new Game(48, kingdomino);
		newGame.setNumberOfPlayers(4);
		kingdomino.setCurrentGame(newGame);
//	    throw new cucumber.api.PendingException();
		//KingdominoController.startNewGame(kingdomino);
		createAllDominoes(newGame);
		
	}

	@Given("there are {int} players playing")
	public void there_are_players_playing(Integer int1) {
	    // Write code here that turns the phrase above into concrete actions
//		Game newGame = new Game(48,kingdomino);
//		newGame.setNumberOfPlayers(int1);
//		assertEquals(Integer.valueOf(int1), Integer.valueOf(kingdomino.getCurrentGame().getNumberOfPlayers()));
		Game game = kingdomino.getCurrentGame();
		game.setNumberOfPlayers(int1);
	    //throw new cucumber.api.PendingException();
	}

	@When("the shuffling of dominoes is initiated")
	public void the_shuffling_of_dominoes_is_initiated() {
	    //here we shuffle the dominoes and get the first draft from the shuffled pile
		KingdominoController.shuffleDominos(kingdomino);
		KingdominoController.getFirstDraft(kingdomino);
	    //throw new cucumber.api.PendingException();
	}

	@Then("the first draft shall exist")
	public void the_first_draft_shall_exist() {
	    //reasonning: if the size of the draft equals a real number, it must exist.
		assertEquals(4, kingdomino.getCurrentGame().getCurrentDraft().getIdSortedDominos().size());

	}

	@Then("the first draft should have {int} dominoes on the board face down")
	public void the_first_draft_should_have_dominoes_on_the_board_face_down(Integer int1) {
		//first assertEquals checks the number of dominoes in the draft
		//second assertEquals checks that the status of the draft is face down
		assertEquals(Integer.valueOf(int1), Integer.valueOf(kingdomino.getCurrentGame().getCurrentDraft().getIdSortedDominos().size()));
		assertEquals(Draft.DraftStatus.FaceDown, kingdomino.getCurrentGame().getCurrentDraft().getDraftStatus());

	}

	@Then("there should be {int} dominoes left in the draw pile")
	public void there_should_be_dominoes_left_in_the_draw_pile(Integer int1) {
	    //compare the size of the remaining drawpile with the integer given
		assertEquals(Integer.valueOf(int1), Integer.valueOf(kingdomino.getCurrentGame().getAllDominos().size()));

	}

	@When("I initiate to arrange the domino in the fixed order {string}")
	public void i_initiate_to_arrange_the_domino_in_the_fixed_order(String string) {
	    // Write code here that turns the phrase above into concrete actions
//		Comparator<Domino> c = Comparator.comparing(Domino::toString);
//		Collections.sort(kingdomino.getCurrentGame().getAllDominos(), c);
//		System.out.println(kingdomino.getCurrentGame().getAllDominos());
//		System.out.println("HELOOOOOOOOOO");
		KingdominoController.getFixedOrder(kingdomino, string);
	    //throw new cucumber.api.PendingException();
	}

	@Then("the draw pile should consist of everything in {string} except the first {int} dominoes with their order preserved")
	public void the_draw_pile_should_consist_of_everything_in_except_the_first_dominoes_with_their_order_preserved(String string, Integer int1) {
	    // Write code here that turns the phrase above into concrete actions
	    throw new cucumber.api.PendingException();
	}
	
	
	//helper methods to create dominos
	private void createAllDominoes(Game game) {
		try {
			BufferedReader br = new BufferedReader(new FileReader("src/main/resources/alldominoes.dat"));
			String line = "";
			String delimiters = "[:\\+()]";
			while ((line = br.readLine()) != null) {
				String[] dominoString = line.split(delimiters); // {id, leftTerrain, rightTerrain, crowns}
				int dominoId = Integer.decode(dominoString[0]);
				TerrainType leftTerrain = getTerrainType(dominoString[1]);
				TerrainType rightTerrain = getTerrainType(dominoString[2]);
				int numCrown = 0;
				if (dominoString.length > 3) {
					numCrown = Integer.decode(dominoString[3]);
				}
				new Domino(dominoId, leftTerrain, rightTerrain, numCrown, game);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new java.lang.IllegalArgumentException(
					"Error occured while trying to read alldominoes.dat: " + e.getMessage());
		}
	}


	private TerrainType getTerrainType(String terrain) {
		switch (terrain) {
		case "W":
			return TerrainType.WheatField;
		case "F":
			return TerrainType.Forest;
		case "M":
			return TerrainType.Mountain;
		case "G":
			return TerrainType.Grass;
		case "S":
			return TerrainType.Swamp;
		case "L":
			return TerrainType.Lake;
		default:
			throw new java.lang.IllegalArgumentException("Invalid terrain type: " + terrain);
		}
	}

}
