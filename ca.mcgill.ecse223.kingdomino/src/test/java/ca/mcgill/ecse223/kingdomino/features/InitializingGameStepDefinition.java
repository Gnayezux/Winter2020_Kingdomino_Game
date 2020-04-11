package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.Castle;
import ca.mcgill.ecse223.kingdomino.model.Domino;
import ca.mcgill.ecse223.kingdomino.model.Draft;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Gameplay;
import ca.mcgill.ecse223.kingdomino.model.Kingdom;
import ca.mcgill.ecse223.kingdomino.model.Kingdomino;
import ca.mcgill.ecse223.kingdomino.model.Player;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;
import io.cucumber.java.en.*;

public class InitializingGameStepDefinition {
	@Given("the game has not been started")
	public void the_game_has_not_been_started() {
		Kingdomino kingdomino = new Kingdomino();
		Game game = new Game(48, kingdomino);
		kingdomino.setCurrentGame(game);
		KingdominoController.setNumberOfPlayers(4, kingdomino);
		for (int i = 0; i < 4; i++) {
			KingdominoController.selectColor(PlayerColor.values()[i], i, kingdomino);
		}
		List<Player> players = kingdomino.getCurrentGame().getPlayers();
		for (int i = 0; i < players.size(); i++) {
			Player player = players.get(i);
			Kingdom kingdom = new Kingdom(player);
			new Castle(0, 0, kingdom, player);
			player.setBonusScore(0);
			player.setPropertyScore(0);
			player.setDominoSelection(null);
			game.setNextPlayer(players.get(0));
		}
		KingdominoController.createAllDominos(kingdomino.getCurrentGame());
		KingdominoApplication.setKingdomino(kingdomino);
		Gameplay g = new Gameplay();
		KingdominoApplication.setGameplay(g);
	}

	@When("start of the game is initiated")
	public void start_of_the_game_is_initiated() {
		System.out.println(KingdominoApplication.getGameplay());
		KingdominoApplication.getGameplay().setGamestatus("CreatingFirstDraft");
		System.out.println(KingdominoApplication.getGameplay().getGamestatusFullName());
	}

	@Then("the pile shall be shuffled")
	public void the_pile_shall_be_shuffled() {
		List<Domino> dominoPile = KingdominoApplication.getKingdomino().getCurrentGame().getAllDominos();
		boolean ordered = true;
		for (int i = 0; i < dominoPile.size() - 1; i++) {
			if (dominoPile.get(i).getId() > dominoPile.get(i + 1).getId()) {
				ordered = false;
			}
		}
		assertEquals(false, ordered);
	}

	@Then("the first draft shall be on the table")
	public void the_first_draft_shall_be_on_the_table() {
		assertNotEquals(KingdominoApplication.getKingdomino().getCurrentGame().getNextDraft(), null);
		Draft nextDraft = KingdominoApplication.getKingdomino().getCurrentGame().getNextDraft();
		assertNotEquals(null, nextDraft);
	}

	@Then("the first draft shall be revealed")
	public void the_first_draft_shall_be_revealed() {
		Draft nextDraft = KingdominoApplication.getKingdomino().getCurrentGame().getCurrentDraft();
		assertEquals(Draft.DraftStatus.FaceUp, nextDraft.getDraftStatus());
	}

	@Then("the initial order of players shall be determined")
	public void the_initial_order_of_players_shall_be_determined() {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		List<Player> players = game.getPlayers();
		assertEquals(game.getNumberOfPlayers(), players.size());
	}

	@Then("the first player shall be selecting his\\/her first domino of the game")
	public void the_first_player_shall_be_selecting_his_her_first_domino_of_the_game() {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		List<Player> players = game.getPlayers();
		assertEquals(game.getNextPlayer().getColor(), players.get(0).getColor());
	}

	@Then("the second draft shall be on the table, face down")
	public void the_second_draft_shall_be_on_the_table_face_down() {
		Draft nextDraft = KingdominoApplication.getKingdomino().getCurrentGame().getNextDraft();
		assertEquals(Draft.DraftStatus.FaceDown, nextDraft.getDraftStatus());
	}


}
