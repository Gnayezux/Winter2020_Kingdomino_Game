package ca.mcgill.ecse223.kingdomino.features;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.*;
import io.cucumber.java.en.*;
import static org.junit.Assert.*;
import java.util.*;

public class StartANewGameStepDefinitions {

	/**
	 * @author Maxime Rieuf
	 */
	@Given("the program is started and ready for starting a new game")
	public void the_program_is_started_and_ready_for_starting_a_new_game() {
		KingdominoController.startGameSetup();
	}

	/**
	 * @author Maxime Rieuf
	 */
	@Given("there are four selected players")
	public void there_are_four_selected_players() {
		KingdominoController.setNumberOfPlayers(4);
		KingdominoController.createPlayersAndKingdoms();
		List<Player> players = KingdominoApplication.getKingdomino().getCurrentGame().getPlayers();
		String[] userNames = { "User1", "User2", "User3", "User4" };
		int i = 0;
		for(Player p: players) {
			KingdominoController.selectUser(new User(userNames[i],KingdominoApplication.getKingdomino()), p.getColor().name());
			i++;
		}
	}

	/**
	 * @author Maxime Rieuf
	 */
	@Given("bonus options Harmony and MiddleKingdom are selected")
	public void bonus_options_Harmony_and_MiddleKingdom_are_selected() {
		KingdominoController.setBonusOption("Harmony", true);
		KingdominoController.setBonusOption("MiddleKingdom", true);
	}

	/**
	 * @author Maxime Rieuf
	 */
	@When("starting a new game is initiated")
	public void starting_a_new_game_is_initiated() {    
		KingdominoController.shuffleDominos();
		KingdominoController.setNextDraft();
	}

	/**
	 * @author Maxime Rieuf
	 */
	@When("reveal first draft is initiated")
	public void reveal_first_draft_is_initiated() {
		KingdominoController.orderNextDraft();
		KingdominoController.revealNextDraft();
	}

	/**
	 * @author Maxime Rieuf
	 */
	@Then("all kingdoms shall be initialized with a single castle")
	public void all_kingdoms_shall_be_initialized_with_a_single_castle() {
		for(int i = 0; i < KingdominoApplication.getKingdomino().getCurrentGame().getNumberOfPlayers(); i++) {			
			assertEquals(1, KingdominoApplication.getKingdomino().getCurrentGame().getPlayer(i).getKingdom().getTerritories().size());
		}
	}

	/**
	 * @author Maxime Rieuf
	 */
	@Then("all castle are placed at {int}:{int} in their respective kingdoms")
	public void all_castle_are_placed_at_in_their_respective_kingdoms(Integer int1, Integer int2) {
		for(int i = 0; i < KingdominoApplication.getKingdomino().getCurrentGame().getNumberOfPlayers(); i++) {			
			assertEquals(Integer.valueOf(int1), Integer.valueOf(KingdominoApplication.getKingdomino().getCurrentGame().getPlayer(i).getKingdom().getTerritory(0).getX()));
			assertEquals(Integer.valueOf(int2), Integer.valueOf(KingdominoApplication.getKingdomino().getCurrentGame().getPlayer(i).getKingdom().getTerritory(0).getY()));
		}
	}

	/**
	 * @author Maxime Rieuf
	 */
	@Then("the first draft of dominoes is revealed")
	public void the_first_draft_of_dominoes_is_revealed() {
		assertNotEquals(null, KingdominoApplication.getKingdomino().getCurrentGame().getNextDraft());
	}

	/**
	 * @author Maxime Rieuf
	 */
	@Then("all the dominoes form the first draft are facing up")
	public void all_the_dominoes_form_the_first_draft_are_facing_up() {
		assertEquals(Draft.DraftStatus.FaceUp, KingdominoApplication.getKingdomino().getCurrentGame().getNextDraft().getDraftStatus());
	}

	/**
	 * @author Maxime Rieuf
	 */
	@Then("all the players have no properties")
	public void all_the_players_have_no_properties() {
		for (int i = 0; i < KingdominoApplication.getKingdomino().getCurrentGame().getNumberOfPlayers(); i++) {
			assertEquals(null, KingdominoApplication.getKingdomino().getCurrentGame().getPlayer(i).getDominoSelection());
		}
		
	}

	/**
	 * @author Maxime Rieuf
	 */
	@Then("all player scores are initialized to zero")
	public void all_player_scores_are_initialized_to_zero() {
		for (int i = 0; i < KingdominoApplication.getKingdomino().getCurrentGame().getNumberOfPlayers(); i++) {
			assertEquals(0, KingdominoApplication.getKingdomino().getCurrentGame().getPlayer(i).getTotalScore());
		}
	}

}
