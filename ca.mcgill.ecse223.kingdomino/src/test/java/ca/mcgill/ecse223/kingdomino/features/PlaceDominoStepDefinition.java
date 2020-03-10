package ca.mcgill.ecse223.kingdomino.features;

import ca.mcgill.ecse223.kingdomino.model.*;
import ca.mcgill.ecse223.kingdomino.model.Domino.DominoStatus;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;
import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.*;
import io.cucumber.java.en.*;
import static org.junit.Assert.*;
import java.util.*;

public class PlaceDominoStepDefinition {

	@Given("the {string}'s kingdom has the following dominoes:")
	public void the_s_kingdom_has_the_following_dominoes(String string, io.cucumber.datatable.DataTable dataTable) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		List<Player> players = KingdominoApplication.getKingdomino().getCurrentGame().getPlayers();
	    for (Player p : players) {
			if (p.getColor() == getColor(string)) {
				game.setNextPlayer(p);
				break;
			}
		}
	}

	@Given("domino {int} is in {string} status")
	public void domino_is_in_status(Integer int1, String string) {
		Domino dom = getdominoByID(int1);
		dom.setStatus(getDominoStatus(string));
	}

	@When("{string} requests to place the selected domino {int}")
	public void requests_to_place_the_selected_domino(String string, Integer int1) {
	    KingdominoController.placeDomino(KingdominoApplication.getKingdomino());
	}

	@Then("{string}'s kingdom should now have domino {int} at position {int}:{int} with direction {string}")
	public void s_kingdom_should_now_have_domino_at_position_with_direction(String string, Integer int1, Integer int2, Integer int3, String string2) {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		DominoInKingdom dom = (DominoInKingdom) player.getKingdom()
				.getTerritory(player.getKingdom().numberOfTerritories() - 1);
		assertEquals((int) int1, dom.getDomino().getId());
		assertEquals((int) int2, dom.getX());
		assertEquals((int) int3, dom.getY());
		assertEquals(getColor(string), player.getColor());
		assertEquals(getDirection(string2), dom.getDirection());
		assertEquals(DominoStatus.PlacedInKingdom, dom.getDomino().getStatus());
	}
	
	/**********************
	 * * Helper Methods * *
	 **********************/

	private Domino getdominoByID(int id) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		for (Domino domino : game.getAllDominos()) {
			if (domino.getId() == id) {
				return domino;
			}
		}
		throw new java.lang.IllegalArgumentException("Domino with ID " + id + " not found.");
	}
	
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
}
