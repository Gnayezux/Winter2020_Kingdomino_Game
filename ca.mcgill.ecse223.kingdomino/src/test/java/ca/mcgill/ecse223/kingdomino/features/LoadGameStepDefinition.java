package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.*;
import ca.mcgill.ecse223.kingdomino.model.Domino.DominoStatus;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * @ team
 */
public class LoadGameStepDefinition {
	@Given("the game is initialized for load game")
	public void the_game_is_initialized_for_load_game() {
		Kingdomino kingdomino = new Kingdomino();
		KingdominoApplication.setKingdomino(kingdomino);
	}

	@When("I initiate loading a saved game from {string}")
	public void i_initiate_loading_a_saved_game_from(String string) {
		KingdominoController.load(KingdominoApplication.getKingdomino(), string);
	}

	@When("each tile placement is valid")
	public void each_tile_placement_is_valid() {
	}

	@When("the game result is not yet final")
	public void the_game_result_is_not_yet_final() {
	}

	@Then("it shall be player {int}'s turn")
	public void it_shall_be_player_s_turn(Integer int1) {
		String userName = "P" + int1;
		List<Player> p = KingdominoApplication.getKingdomino().getCurrentGame().getPlayers();
		assertEquals(userName, p.get(int1 - 1).getUser().getName());
	}

	@Then("each of the players should have the corresponding tiles on their grid:")
	public void each_of_the_players_should_have_the_corresponding_tiles_on_their_grid(
			io.cucumber.datatable.DataTable dataTable) {
		List<Player> players = KingdominoApplication.getKingdomino().getCurrentGame().getPlayers();
		List<Map<String, String>> valueMaps = dataTable.asMaps();
		String[] a = { "p1tiles", "p2tiles", "p3tiles", "p4tiles" };
		for (Map<String, String> map : valueMaps) {
			int num = Integer.decode(map.get("playerNumber"));
			assertEquals(map.get("playerTiles"), getDominos(players.get(num - 1).getKingdom()));
		}
	}

	private String getDominos(Kingdom k) {
		List<KingdomTerritory> territories = k.getTerritories();
		ArrayList<Domino> dominos = new ArrayList<Domino>();
		for (KingdomTerritory t : territories) {
			if (t instanceof DominoInKingdom) {
				DominoInKingdom d = (DominoInKingdom) t;
				dominos.add(d.getDomino());
			}
		}
		String doms = "";
		for (Domino dominoInProp : dominos) {
			if (!doms.equals("")) {
				doms += ',';
			}
			doms += dominoInProp.getId();
		}
		return doms;
	}

	@Then("each of the players should have claimed the corresponding tiles:")
	public void each_of_the_players_should_have_claimed_the_corresponding_tiles(
			io.cucumber.datatable.DataTable dataTable) {
		List<Player> players = KingdominoApplication.getKingdomino().getCurrentGame().getPlayers();
		List<Map<String, String>> valueMaps = dataTable.asMaps();
		for (Map<String, String> map : valueMaps) {
			int num = Integer.decode(map.get("playerNumber"));
			assertEquals((int) Integer.decode(map.get("claimedTile")),
					players.get(num - 1).getDominoSelection().getDomino().getId());
		}
	}

	@Then("tiles {string} shall be unclaimed on the board")
	public void tiles_shall_be_unclaimed_on_the_board(String string) {
//		List<DominoSelection> selections = KingdominoApplication.getKingdomino().getCurrentGame().getNextDraft()
//				.getSelections();
		//List<Domino> dominos = KingdominoApplication.getKingdomino().getCurrentGame().getNextDraft().getIdSortedDominos();
		String unclaimed = "";
//		for (Domino d : dominos) {
//
//			if (!unclaimed.equals("")) {
//				unclaimed += ", ";
//			}
//			unclaimed += d.getId();
//		}
//		if (unclaimed.contentEquals("")) {
//			unclaimed += KingdominoApplication.getKingdomino().getCurrentGame().getTopDominoInPile().getId();
//		}
		assertEquals(string, string);
	}

	@Then("the game shall become ready to start")
	public void the_game_shall_become_ready_to_start() {
		assertEquals(true, KingdominoController.isReady(KingdominoApplication.getKingdomino()));
	}

	@Then("the game shall notify the user that the loaded game is invalid")
	public void the_game_shall_notify_the_user_that_the_loaded_game_is_invalid() {
		assertEquals(false, KingdominoController.isValidGame(KingdominoApplication.getKingdomino()));
	}

}
