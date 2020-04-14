package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;
import java.util.*;
import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.*;
import io.cucumber.java.en.*;

public class SelectingDominoStepDefinition {
	@Given("the game has been initialized for selecting domino")
	public void the_game_has_been_initialized_for_selecting_domino() {
		HelperClass.testSetup();
		KingdominoController.browseDominoPile();
		Gameplay g = new Gameplay();
		KingdominoApplication.setGameplay(g);
		KingdominoApplication.getGameplay().setGamestatus("SelectingFirstDomino");
	}

	@Given("the order of players is {string}")
	public void the_order_of_players_is(String string) {
		String[] order = string.split(",");
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		List<Player> players = game.getPlayers();
		for (int i = 0; i < players.size(); i++) {
			players.get(i).setColor(HelperClass.getColor(order[i]));
		}
	}

	@Given("the next draft has the dominoes with ID {string}")
	public void the_next_draft_has_the_dominoes_with_ID(String string) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		List<String> dominoString = new ArrayList<String>(Arrays.asList(string.split(",")));
		Draft draft = new Draft(Draft.DraftStatus.Sorted, game);
		for (String s : dominoString) {
			draft.addIdSortedDomino(KingdominoController.getDominoByID(Integer.parseInt(s)));
		}
		game.setNextDraft(draft); // Hardcode what the draft is depending on the test
	}

	@Given("the {string} is selecting his\\/her domino with ID {int}")
	public void the_is_selecting_his_her_domino_with_ID(String string, Integer int1) {
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



	@Given("the {string} player is selecting his\\/her first domino of the game with ID {int}")
	public void the_player_is_selecting_his_her_first_domino_of_the_game_with_ID(String string, Integer int1) {

	}

	


}
