package ca.mcgill.ecse223.kingdomino.features;

import ca.mcgill.ecse223.kingdomino.model.*;
import ca.mcgill.ecse223.kingdomino.model.Domino.DominoStatus;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.*;
import io.cucumber.java.en.*;
import static org.junit.Assert.*;
import java.util.*;

public class PlaceDominoStepDefinition {

	/**
	 * @author Mathieu-Joseph Magri
	 */
	@Given("the {string}'s kingdom has the following dominoes:")
	public void the_s_kingdom_has_the_following_dominoes(String string, io.cucumber.datatable.DataTable dataTable) {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		List<Map<String, String>> valueMaps = dataTable.asMaps();
		for (Map<String, String> map : valueMaps) {
			// Get values from cucumber table
			Integer id = Integer.decode(map.get("domino"));
			DirectionKind dir = HelperClass.getDirection(map.get("dominodir"));
			Integer posx = Integer.decode(map.get("posx"));
			Integer posy = Integer.decode(map.get("posy"));

			// Add the domino to a player's kingdom
			Domino dominoToPlace = HelperClass.getDominoByID(id);
			Kingdom kingdom = player.getKingdom();

			DominoInKingdom domInKingdom = new DominoInKingdom(posx, posy, kingdom, dominoToPlace);
			domInKingdom.setDirection(dir);
			dominoToPlace.setStatus(DominoStatus.PlacedInKingdom);
		}
	}

	/**
	 * @author Mathieu-Joseph Magri
	 */
	@Given("domino {int} is in {string} status")
	public void domino_is_in_status(Integer int1, String string) {
		Domino dom = HelperClass.getDominoByID(int1);
		dom.setStatus(HelperClass.getDominoStatus(string));
	}

	/**
	 * @author Mathieu-Joseph Magri
	 */
	@When("{string} requests to place the selected domino {int}")
	public void requests_to_place_the_selected_domino(String string, Integer int1) {
	    KingdominoController.placeDomino();
	}

	/**
	 * @author Mathieu-Joseph Magri
	 */
	@Then("{string}'s kingdom should now have domino {int} at position {int}:{int} with direction {string}")
	public void s_kingdom_should_now_have_domino_at_position_with_direction(String string, Integer int1, Integer int2, Integer int3, String string2) {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		DominoInKingdom dom = (DominoInKingdom) player.getKingdom()
				.getTerritory(player.getKingdom().numberOfTerritories() - 1);
		assertEquals((int) int1, dom.getDomino().getId());
		assertEquals((int) int2, dom.getX());
		assertEquals((int) int3, dom.getY());
		assertEquals(HelperClass.getColor(string), player.getColor());
		assertEquals(HelperClass.getDirection(string2), dom.getDirection());
		assertEquals(DominoStatus.PlacedInKingdom, dom.getDomino().getStatus());
	}
	
}
