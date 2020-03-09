package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.Castle;
import ca.mcgill.ecse223.kingdomino.model.Domino;
import ca.mcgill.ecse223.kingdomino.model.Domino.DominoStatus;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom;
import ca.mcgill.ecse223.kingdomino.model.DominoSelection;
import ca.mcgill.ecse223.kingdomino.model.Draft;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Kingdom;
import ca.mcgill.ecse223.kingdomino.model.KingdomTerritory;
import ca.mcgill.ecse223.kingdomino.model.Kingdomino;
import ca.mcgill.ecse223.kingdomino.model.Player;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;
import ca.mcgill.ecse223.kingdomino.model.TerrainType;
import ca.mcgill.ecse223.kingdomino.model.User;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class MoveCurrentDominoStepDefinition {
	

	@Given("the game is initialized for move current domino")
	public void the_game_is_initialized_for_move_current_domino() {
		// Write code here that turns the phrase above into concrete actions
//		throw new cucumber.api.PendingException();
		Kingdomino kingdomino = new Kingdomino();
		Game game = new Game(48, kingdomino);
		
		kingdomino.setCurrentGame(game);
		KingdominoController.setNumberOfPlayers(4,kingdomino);
		for(int i =0;i<4;i++) {
			KingdominoController.selectColor(PlayerColor.values()[i], i, kingdomino);
		}

//		KingdominoController.startNewGame(kingdomino);
		// TODO Randomly order the players
		List<Player> players = kingdomino.getCurrentGame().getPlayers();
		for (int i = 0; i < players.size(); i++) {
			Player player = players.get(i);
			Kingdom kingdom = new Kingdom(player);
			new Castle(0, 0, kingdom, player);
			player.setBonusScore(0);
			player.setPropertyScore(0);
			player.setDominoSelection(null);
		}
		if (kingdomino.getCurrentGame().getAllDominos().size() == 0) {
			KingdominoController.createAllDominos(kingdomino.getCurrentGame());
		}
		
		KingdominoApplication.setKingdomino(kingdomino);
		
	}


	@Given("it is {string}'s turn")
	public void it_is_s_turn(String string) {
		// Write code here that turns the phrase above into concrete actions
//		throw new cucumber.api.PendingException();
//		System.out.print(KingdominoApplication.getKingdomino().getCurrentGame().getPlayers());
//		KingdominoApplication.getKingdomino().getCurrentGame()
//		System.out.print(string);
		int index = -1;
		for(int i =0;i<4;i++) {
			if(KingdominoApplication.getKingdomino().getCurrentGame().getPlayer(i).getColor().toString().toLowerCase().equals(string)) {
				index = i;
			}
		}
		KingdominoApplication.getKingdomino().getCurrentGame().setNextPlayer(KingdominoApplication.getKingdomino().getCurrentGame().getPlayer(index));
		
	}

	@Given("{string} has selected domino {int}")
	public void has_selected_domino(String string, Integer int1) {
		// Write code here that turns the phrase above into concrete actions
//		throw new cucumber.api.PendingException();
		Draft draft = new Draft(Draft.DraftStatus.FaceUp, KingdominoApplication.getKingdomino().getCurrentGame());
		int index = -1;
		for(int i =0;i<4;i++) {
			if(KingdominoApplication.getKingdomino().getCurrentGame().getPlayer(i).getColor().toString().toLowerCase().equals(string)) {
				index = i;
			}
		}
		
		DominoSelection selection = new DominoSelection(KingdominoApplication.getKingdomino().getCurrentGame().getPlayer(index),KingdominoApplication.getKingdomino().getCurrentGame().getAllDomino(int1-1), draft);
		
	}

	@When("{string} removes his king from the domino {int}")
	public void removes_his_king_from_the_domino(String string, Integer int1) {
		// Write code here that turns the phrase above into concrete actions
//		throw new cucumber.api.PendingException();
//		System.out.print(int1);
//		System.out.print(KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer().getDominoSelection().getDomino().getId());
		KingdominoController.removeKing(KingdominoApplication.getKingdomino());
	}

	@Then("domino {int} should be tentative placed at position {int}:{int} of {string}'s kingdom with ErroneouslyPreplaced status")
	public void domino_should_be_tentative_placed_at_position_of_s_kingdom_with_ErroneouslyPreplaced_status(
			Integer int1, Integer int2, Integer int3, String string) {
		// Write code here that turns the phrase above into concrete actions
//		throw new cucumber.api.PendingException();
		int index = -1;
		for(int i =0;i<4;i++) {
			if(KingdominoApplication.getKingdomino().getCurrentGame().getPlayer(i).getColor().toString().toLowerCase().equals(string)) {
				index = i;
			}
		}
		
//		System.out.print(domino.getDomino().getStatus());
		int terIndex=-1;
		for(int i =1;i< KingdominoApplication.getKingdomino().getCurrentGame().getPlayer(index).getKingdom().getTerritories().size();i++) {
			DominoInKingdom domino = (DominoInKingdom) KingdominoApplication.getKingdomino().getCurrentGame().getPlayer(index).getKingdom().getTerritory(i);
			if(domino.getDomino().getId()==int1) {
				terIndex = i;
			}
		}
		DominoInKingdom domino = (DominoInKingdom) KingdominoApplication.getKingdomino().getCurrentGame().getPlayer(index).getKingdom().getTerritory(terIndex);
		assertEquals((int)int1,domino.getDomino().getId());
		assertEquals((int)int2,domino.getX());
		assertEquals((int)int3,domino.getY());
		assertEquals(string,domino.getDomino().getDominoSelection().getPlayer().getColor().toString().toLowerCase());
		assertEquals(Domino.DominoStatus.ErroneouslyPreplaced,domino.getDomino().getStatus());
	}

	@Given("{string}'s kingdom has following dominoes:")
	public void s_kingdom_has_following_dominoes(String string, io.cucumber.datatable.DataTable dataTable) {
		// Write code here that turns the phrase above into concrete actions
		// For automatic transformation, change DataTable to one of
		// E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
		// Map<K, List<V>>. E,K,V must be a String, Integer, Float,
		// Double, Byte, Short, Long, BigInteger or BigDecimal.
		//
		// For other transformations you can register a DataTableType.
//		throw new cucumber.api.PendingException();
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		List<Map<String, String>> valueMaps = dataTable.asMaps();
		for (Map<String, String> map : valueMaps) {
			// Get values from cucumber table
			Integer id = Integer.decode(map.get("id"));
			DirectionKind dir = getDirection(map.get("dir"));
			Integer posx = Integer.decode(map.get("posx"));
			Integer posy = Integer.decode(map.get("posy"));

			// Add the domino to a player's kingdom
			Domino dominoToPlace = getdominoByID(id);
			Kingdom kingdom = game.getNextPlayer().getKingdom();
			DominoInKingdom domInKingdom = new DominoInKingdom(posx, posy, kingdom, dominoToPlace);
			domInKingdom.setDirection(dir);
			dominoToPlace.setStatus(DominoStatus.PlacedInKingdom);
		}
	}

	@Given("domino {int} is tentatively placed at position {int}:{int} with direction {string}")
	public void domino_is_tentatively_placed_at_position_with_direction(Integer int1, Integer int2, Integer int3,
			String string) {
		// Write code here that turns the phrase above into concrete actions
//		throw new cucumber.api.PendingException();
//		System.out.print(KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer().getKingdom().getTerritories());
//		System.out.print(int1);
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		// Set the next player here
		Domino dom = player.getDominoSelection().getDomino();
		dom.setStatus(DominoStatus.ErroneouslyPreplaced);
		DominoInKingdom domIn = new DominoInKingdom(int2, int3, player.getKingdom(), dom);
		domIn.setDirection(getDirection(string));
		player.getKingdom().addTerritory(domIn);
	}

	@When("{string} requests to move the domino {string}")
	public void requests_to_move_the_domino(String string, String string2) {
		// Write code here that turns the phrase above into concrete actions
//		throw new cucumber.api.PendingException();
		KingdominoController.moveDomino(KingdominoApplication.getKingdomino(), string2);
	}

	@Then("the domino {int} should be tentatively placed at position {int}:{int} with direction {string}")
	public void the_domino_should_be_tentatively_placed_at_position_with_direction(Integer int1, Integer int2,
			Integer int3, String string) {
		// Write code here that turns the phrase above into concrete actions
//		throw new cucumber.api.PendingException();
		int i = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer().getKingdom().getTerritories().size()-1;
		DominoInKingdom domino = (DominoInKingdom) KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer().getKingdom().getTerritory(i);
		assertEquals((int)int1,domino.getDomino().getId());
		assertEquals((int)int2,domino.getX());
		assertEquals((int)int3,domino.getY());
		assertEquals(getDirection(string),domino.getDirection());
		
	}

	@Then("the new status of the domino is {string}")
	public void the_new_status_of_the_domino_is(String string) {
		// Write code here that turns the phrase above into concrete actions
//		throw new cucumber.api.PendingException();
		int i = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer().getKingdom().getTerritories().size()-1;
		DominoInKingdom domino = (DominoInKingdom) KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer().getKingdom().getTerritory(i);
		System.out.print(domino.getX());
		System.out.print(domino.getY());
		System.out.print(KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer().getKingdom().getTerritories());
		assertEquals(getDominoStatus(string),domino.getDomino().getStatus());
	}

	@Given("domino {int} has status {string}")
	public void domino_has_status(Integer int1, String string) {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	@Then("the domino {int} is still tentatively placed at position {int}:{int}")
	public void the_domino_is_still_tentatively_placed_at_position(Integer int1, Integer int2, Integer int3) {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	@Then("the domino should still have status {string}")
	public void the_domino_should_still_have_status(String string) {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}
	
	
	
	private Domino getdominoByID(int id) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		for (Domino domino : game.getAllDominos()) {
			if (domino.getId() == id) {
				return domino;
			}
		}
		throw new java.lang.IllegalArgumentException("Domino with ID " + id + " not found.");
	}
	
	private DirectionKind getDirection(String dir) {
		switch (dir) {
		case "up":
			return DirectionKind.Up;
		case "down":
			return DirectionKind.Down;
		case "left":
			return DirectionKind.Left;
		case "right":
			return DirectionKind.Right;
		default:
			throw new java.lang.IllegalArgumentException("Invalid direction: " + dir);
		}
	}
	
	private DominoStatus getDominoStatus(String status) {
		switch (status) {
		case "inPile":
			return DominoStatus.InPile;
		case "excluded":
			return DominoStatus.Excluded;
		case "inCurrentDraft":
			return DominoStatus.InCurrentDraft;
		case "inNextDraft":
			return DominoStatus.InNextDraft;
		case "ErroneouslyPreplaced":
			return DominoStatus.ErroneouslyPreplaced;
		case "CorrectlyPreplaced":
			return DominoStatus.CorrectlyPreplaced;
		case "placedInKingdom":
			return DominoStatus.PlacedInKingdom;
		case "discarded":
			return DominoStatus.Discarded;
		default:
			throw new java.lang.IllegalArgumentException("Invalid domino status: " + status);
		}
	}
}
