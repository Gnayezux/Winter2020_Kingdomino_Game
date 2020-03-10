package ca.mcgill.ecse223.kingdomino.features;

import java.io.*;
import java.util.*;
import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.*;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;
import io.cucumber.java.en.*;
import static org.junit.Assert.assertEquals;

public class ChooseNextDominoStepDefinition {
	/**
	 * @author Abdallah Shapsough
	 */
	@Given("the game is initialized for choose next domino")
	public void the_game_is_initialized_for_choose_next_domino() {
		Kingdomino kingdomino = new Kingdomino();
		Game game = new Game(48, kingdomino);
		game.setNumberOfPlayers(4);
		kingdomino.setCurrentGame(game);
		addDefaultUsersAndPlayers(game);
		KingdominoController.createAllDominos(game);
		game.setNextPlayer(game.getPlayer(0));
		KingdominoApplication.setKingdomino(kingdomino);
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
			draft.addIdSortedDomino(game.getAllDomino(Integer.parseInt(s) - 1));
		}
		game.setNextDraft(draft);
		KingdominoController.orderNextDraft(KingdominoApplication.getKingdomino());
	}

	/**
	 * @author Abdallah Shapsough
	 */
	@Given("player's domino selection {string}")
	public void player_s_domino_selection(String string) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Draft draft = game.getNextDraft();
		string = string.replaceAll("\\s+", "");
		List<String> selection = new ArrayList<String>(Arrays.asList(string.split(",")));
		int index = 0;
		for (String select : selection) {
			for (Player player : game.getPlayers()) {
				if (select.equals(player.getColor().toString().toLowerCase())) {
					draft.addSelection(player, draft.getIdSortedDomino(index));
				}
			}
			index++;
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
			}
		}
		game.setNextPlayer(curPlayer);
	}

	/**
	 * @author Abdallah Shapsough
	 */
	@When("current player chooses to place king on {int}")
	public void current_player_chooses_to_place_king_on(Integer int1) {
		KingdominoController.ChooseNextDomino(KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer(),
				KingdominoApplication.getKingdomino(), (int)int1);
	}

	/**
	 * @author Abdallah Shapsough
	 */
	@Then("current player king now is on {string}")
	public void current_player_king_now_is_on(String string) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Draft draft = game.getNextDraft();
		int id = -1;
		for (DominoSelection selection : draft.getSelections()) {
			if (game.getNextPlayer().equals(selection.getPlayer())) {
				id = selection.getDomino().getId();
			}

		}
		assertEquals(Integer.parseInt(string), id);
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

	/**********************
	 * * Helper Methods * *
	 **********************/
	
	private static void addDefaultUsersAndPlayers(Game game) {
		String[] userNames = { "User1", "User2", "User3", "User4" };
		for (int i = 0; i < userNames.length; i++) {
			User user = game.getKingdomino().addUser(userNames[i]);
			Player player = new Player(game);
			player.setUser(user);
			player.setColor(PlayerColor.values()[i]);
			Kingdom kingdom = new Kingdom(player);
			new Castle(0, 0, kingdom, player);
		}
	}

}