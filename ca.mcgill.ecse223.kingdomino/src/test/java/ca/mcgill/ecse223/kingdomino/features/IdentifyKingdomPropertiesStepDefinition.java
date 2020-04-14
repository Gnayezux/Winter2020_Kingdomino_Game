package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Property;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class IdentifyKingdomPropertiesStepDefinition {

	/**
	 * @author Kaichengwu
	 */
	@Given("the game is initialized for identify properties")
	public void the_game_is_initialized_for_identify_properties() {
		HelperClass.testSetup();
		KingdominoController.browseDominoPile();
		KingdominoController.generateInitialPlayerOrder();
	}

	/**
	 * @author Kaichengwu
	 */
	@When("the properties of the player are identified")
	public void the_properties_of_the_player_are_identified() {
		KingdominoController.identifyProperties();
	}

	/**
	 * @author Kaichengwu
	 */
	@Then("the player shall have the following properties:")
	public void the_player_shall_have_the_following_properties(io.cucumber.datatable.DataTable dataTable) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		List<Property> properties = game.getNextPlayer().getKingdom().getProperties();
		List<Map<String, String>> valueMaps = dataTable.asMaps();
		for (Map<String, String> map : valueMaps) {
			List<String> idList = new ArrayList<String>(Arrays.asList(map.get("dominoes").split(",")));
			HashSet<Integer> set = new HashSet<>();
			for (int i = 0; i < idList.size(); i++) {
				set.add(Integer.parseInt(idList.get(i)));
			}
			assertEquals(true, hasProperty(map.get("type"), set, properties));

		}
	}

	//HELPER METHODS
	
	private boolean hasProperty(String type, HashSet<Integer> dominosExpected, List<Property> properties) {
		boolean found = false;
		for (Property prop : properties) {
			if (prop.getLeftTile() == HelperClass.getTerrainType(type) && checkSet(dominosExpected, prop)) {
				found = true;
			}
		}
		return found;
	}

	private boolean checkSet(HashSet<Integer> set, Property prop) {
		boolean full = false;
		for (int i = 0; i < prop.getIncludedDominos().size(); i++) {
			if (set.contains(prop.getIncludedDomino(i).getId())) {
				full = true;
			}
		}
		return full;
	}
}
