package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;
import java.util.*;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.*;
import io.cucumber.java.en.*;

public class BrowseDominoPileStepDefinition{
	List<Domino> dominoPile;
	Domino domino;
	List<Domino> filteredList;
	
	/**
	 * @author Abdallah Shapsough
	 */
	@Given("the program is started and ready for browsing dominoes")
	public void the_program_is_started_and_ready_for_browsing_dominoes() {
		HelperClass.testSetup();
	}
	/**
	 * @author Abdallah Shapsough
	 */
	@When("I initiate the browsing of all dominoes")
	public void i_initiate_the_browsing_of_all_dominoes() {
		// Returns a sorted ArrayList to dominoPile
		dominoPile = KingdominoController.browseDominoPile();
	}
	
	/**
	 * @author Abdallah Shapsough
	 */
	@Then("all the dominoes are listed in increasing order of identifiers")
	public void all_the_dominoes_are_listed_in_increasing_order_of_identifiers() {
		boolean ordered = true;
		for (int i = 0; i < dominoPile.size() - 1; i++) {
			if (dominoPile.get(i).getId() > dominoPile.get(i + 1).getId()) {
				ordered = false;
			}
		}
		assertEquals(true, ordered);
	}

	/**
	 * @author Abdallah Shapsough
	 */
	@When("I provide a domino ID {int}")
	public void i_provide_a_domino_ID(Integer int1) {
		domino = HelperClass.getDominoByID(int1);
	}

	/**
	 * @author Abdallah Shapsough
	 */
	@Then("the listed domino has {string} left terrain")
	public void the_listed_domino_has_left_terrain(String string) {
		assertEquals(HelperClass.getTerrainType(string), domino.getLeftTile());
	}

	/**
	 * @author Abdallah Shapsough
	 */
	@Then("the listed domino has {string} right terrain")
	public void the_listed_domino_has_right_terrain(String string) {
		assertEquals(HelperClass.getTerrainType(string), domino.getRightTile());
	}

	/**
	 * @author Abdallah Shapsough
	 */
	@Then("the listed domino has {int} crowns")
	public void the_listed_domino_has_crowns(Integer int1) {
		assertEquals(int1, Integer.valueOf(domino.getLeftCrown() + domino.getRightCrown()));
	}

	/**
	 * @author Abdallah Shapsough
	 */
	@When("I initiate the browsing of all dominoes of {string} terrain type")
	public void i_initiate_the_browsing_of_all_dominoes_of_terrain_type(String string) {
		filteredList = KingdominoController.browseFilteredDominos(string);
	}

	/**
	 * @author Abdallah Shapsough
	 */
	@Then("list of dominoes with IDs {string} should be shown")
	public void list_of_dominoes_with_IDs_should_be_shown(String string) {
		List<String> idList = new ArrayList<String>(Arrays.asList(string.split(",")));
		boolean filtered = true;
		for (int i = 0; i < idList.size(); i++) {
			if (filteredList.get(i).getId() != Integer.parseInt(idList.get(i))) {
				filtered = false;
			}
		}
		assertEquals(true, filtered);
	}
}
