package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;
import java.util.List;
import java.util.Map;
import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Player;
import ca.mcgill.ecse223.kingdomino.model.Property;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CalculatePropertiesAttributeStepDefinition {

	/**
	 * @author Kaichengwu
	 */
	@Given("the game is initialized for calculate property attributes")
	public void the_game_is_initialized_for_calculate_property_attributes() {
		HelperClass.testSetup();
		KingdominoController.browseDominoPile();
		KingdominoController.generateInitialPlayerOrder();
	}

	/**
	 * @author Kaichengwu
	 */
	@When("calculate property attributes is initiated")
	public void calculate_property_attributes_is_initiated() {
		KingdominoController.identifyProperties();
//		for (Property prop : KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer().getKingdom().getProperties()) {
//			System.out.println(prop.getLeftTile() + ": " + getDominos(prop));
//		}
		KingdominoController.calculatePropertyAttributes();
	}

	/**
	 * @author Kaichengwu
	 */
	@Then("the player shall have a total of {int} properties")
	public void the_player_shall_have_a_total_of_properties(Integer int1) {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		assertEquals((int) int1, player.getKingdom().getProperties().size());
	}

	/**
	 * @author Kaichengwu
	 */
	@Then("the player shall have properties with the following attributes:")
	public void the_player_shall_have_properties_with_the_following_attributes(
			io.cucumber.datatable.DataTable dataTable) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		List<Property> properties = game.getNextPlayer().getKingdom().getProperties();
		List<Map<String, String>> valueMaps = dataTable.asMaps();
		for (Map<String, String> map : valueMaps) {
			assertEquals(true, hasProperty(map.get("type"), Integer.decode(map.get("size")),
					Integer.decode(map.get("crowns")), properties));

		}
	}

	//HELPER METHODS
	
	private boolean hasProperty(String type, int expectedSize, int expectedCrowns, List<Property> properties) {
		boolean found = false;
		for (Property prop : properties) {
			if (prop.getLeftTile() == HelperClass.getTerrainType(type) && expectedSize==prop.getSize() && expectedCrowns==prop.getCrowns()) {
				found = true;
			}
		}
		return found;
	}

}
