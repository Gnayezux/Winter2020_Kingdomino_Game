package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.Castle;
import ca.mcgill.ecse223.kingdomino.model.Draft;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Gameplay;
import ca.mcgill.ecse223.kingdomino.model.Kingdom;
import ca.mcgill.ecse223.kingdomino.model.Kingdomino;
import ca.mcgill.ecse223.kingdomino.model.Player;
import ca.mcgill.ecse223.kingdomino.model.User;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;

public class SelectingFirstDominoStepDefinitions {

	@Given("the game has been initialized for selecting first domino")
	public void the_game_has_been_initialized_for_selecting_first_domino() {
		Kingdomino kingdomino = new Kingdomino();
		Game game = new Game(48, kingdomino);
		game.setNumberOfPlayers(4);
		kingdomino.setCurrentGame(game); 
		KingdominoController.createAllDominos(game);
		game.setNextPlayer(game.getPlayer(0));
		KingdominoApplication.setKingdomino(kingdomino);
		Gameplay g = new Gameplay();
		KingdominoApplication.setGameplay(g);
	}

	@Given("the initial order of players is {string}")
	public void the_initial_order_of_players_is(String string) {
		String[] order = string.split(",");
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		String[] userNames = { "User1", "User2", "User3", "User4" };
		for (int i = 0; i < userNames.length; i++) {
			User user = game.getKingdomino().addUser(userNames[i]);
			Player player = new Player(game);
			player.setUser(user);
			player.setColor(getColor(order[i]));
			Kingdom kingdom = new Kingdom(player);
			new Castle(0, 0, kingdom, player);
		}
	}

	@Given("the current draft has the dominoes with ID {string}")
	public void the_current_draft_has_the_dominoes_with_ID(String string) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		List<String> dominoString = new ArrayList<String>(Arrays.asList(string.split(",")));
		Draft draft = new Draft(Draft.DraftStatus.Sorted, game);
		for (String s : dominoString) {
			draft.addIdSortedDomino(game.getAllDomino(Integer.parseInt(s) - 1));
		}
		game.setCurrentDraft(draft);
		KingdominoController.orderNextDraft(KingdominoApplication.getKingdomino());
	}

	@Given("player's first domino selection of the game is {string}")
	public void player_s_first_domino_selection_of_the_game_is(String string) {
		List<String> choices = new ArrayList<String>(Arrays.asList(string.split(",")));
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		List<Player> players = game.getPlayers();
		Draft draft = game.getCurrentDraft();
		for(String choice: choices) {
			if(choice.equalsIgnoreCase("none")) {
				break;
			} else {
				int j = 0;
				for(Player p: players) {
					if(p.getColor() == getColor(choice)) {
						draft.getSelection(j).setPlayer(p);
						break;
					}
					j++;
				}
			}
		}
	}

	@Given("the {string} player is selecting his\\/her domino with ID {int}")
	public void the_player_is_selecting_his_her_domino_with_ID(String string, Integer int1) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		List<Player> players = game.getPlayers();
		Draft draft = game.getCurrentDraft();
		for(Player p: players) {
			if(p.getColor() == getColor(string)) {
				game.setNextPlayer(p);
				break;
			}
		}
	}

	@Given("the {string} player is selecting his\\/her first domino with ID {int}")
	public void the_player_is_selecting_his_her_first_domino_with_ID(String string, Integer int1) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		List<Player> players = game.getPlayers();
		Draft draft = game.getCurrentDraft();
		for(Player p: players) {
			if(p.getColor() == getColor(string)) {
				game.setNextPlayer(p);
				break;
			}
		}
	}
	
	// We use the annotation @And to signal precondition check instead of
	// initialization (which is done in @Given methods)
	@And("the validation of domino selection returns {string}")
	public void the_validation_of_domino_selection_returns(String expectedValidationResultString) {
		boolean expectedValidationResult = true;
		if ("success".equalsIgnoreCase(expectedValidationResultString.trim())) {
			expectedValidationResult = true;
		} else if ("error".equalsIgnoreCase(expectedValidationResultString.trim())) {
			expectedValidationResult = false;
		} else {
			throw new IllegalArgumentException(
					"Unknown validation result string \"" + expectedValidationResultString + "\"");
		}
		boolean actualValidationResult = false;

		// TODO call here the guard function from the statemachine and store the result
		// actualValidationResult = gameplay.isSelectionValid();

		// Check the precondition prescribed by the scenario
		assertEquals(expectedValidationResult, actualValidationResult);
	}

	/**********************
	 * * Helper Methods * *
	 **********************/
	
	private PlayerColor getColor(String color) {
		switch (color) {
		case "pink":
			return PlayerColor.Pink;
		case "green":
			return PlayerColor.Green;
		case "blue":
			return PlayerColor.Blue;
		case "yellow":
			return PlayerColor.Yellow;
		default:
			throw new java.lang.IllegalArgumentException("Invalid color: " + color);
		}
	}
	
}
