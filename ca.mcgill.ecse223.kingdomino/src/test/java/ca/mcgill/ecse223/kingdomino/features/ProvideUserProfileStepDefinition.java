package ca.mcgill.ecse223.kingdomino.features;

import ca.mcgill.ecse223.kingdomino.model.*;
import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.*;
import io.cucumber.java.en.*;
import static org.junit.Assert.*;
import java.util.*;

public class ProvideUserProfileStepDefinition {

	private boolean userAdded;
	List<User> listAllUsers;
	int gamesWon;
	int gamesPlayed;

	/**
	 * @author Abdallah Shapsough
	 */
	@Given("the program is started and ready for providing user profile")
	public void the_program_is_started_and_ready_for_providing_user_profile() {
		Kingdomino kingdomino = new Kingdomino();
		Game game = new Game(48, kingdomino);
		game.setNumberOfPlayers(4);
		kingdomino.setCurrentGame(game);
		KingdominoApplication.setKingdomino(kingdomino);
	}

	/**
	 * @author Abdallah Shapsough
	 */
	@Given("there are no users exist")
	public void there_are_no_users_exist() {
	}

	/**
	 * @author Abdallah Shapsough
	 */
	@When("I provide my username {string} and initiate creating a new user")
	public void i_provide_my_username_and_initiate_creating_a_new_user(String string) {
		userAdded = KingdominoController.createNewUser(string, KingdominoApplication.getKingdomino());
	}

	/**
	 * @author Abdallah Shapsough
	 */
	@Then("the user {string} shall be in the list of users")
	public void the_user_shall_be_in_the_list_of_users(String string) {
		boolean hasUser = false;
		for (User user : KingdominoApplication.getKingdomino().getUsers()) {
			if (user.getName().equalsIgnoreCase(string)) {
				hasUser = true;
			}
		}
		assertEquals(true, hasUser);
	}

	/**
	 * @author Abdallah Shapsough
	 */
	@Given("the following users exist:")
	public void the_following_users_exist(io.cucumber.datatable.DataTable dataTable) {		
		List<Map<String, String>> valueMaps = dataTable.asMaps();
		for (Map<String, String> map : valueMaps) {
			KingdominoApplication.getKingdomino().addUser(map.get("name"));
		}
	}

	/**
	 * @author Abdallah Shapsough
	 */
	@Then("the user creation shall {string}")
	public void the_user_creation_shall(String string) {
		if (string.contentEquals("succeed")) {
			assertEquals(true, userAdded);
		} else {
			assertEquals(false, userAdded);
		}
	}

	/**
	 * @author Abdallah Shapsough
	 */
	@When("I initiate the browsing of all users")
	public void i_initiate_the_browsing_of_all_users() {
		listAllUsers = KingdominoController.browseAllUsers(KingdominoApplication.getKingdomino());
	}

	/**
	 * @author Abdallah Shapsough
	 */
	@Then("the users in the list shall be in the following alphabetical order:")
	public void the_users_in_the_list_shall_be_in_the_following_alphabetical_order(
			io.cucumber.datatable.DataTable dataTable) {
		boolean ordered = true;
		List<Map<String, String>> valueMaps = dataTable.asMaps();
		for (Map<String, String> map : valueMaps) {
			if (!map.get("name").equals(listAllUsers.get(Integer.parseInt(map.get("placeinlist"))-1).getName())) {
				ordered = false;
			}
		}
		assertEquals(true, ordered);
	}

	/**
	 * @author Abdallah Shapsough
	 */
	@Given("the following users exist with their game statistics:")
	public void the_following_users_exist_with_their_game_statistics(io.cucumber.datatable.DataTable dataTable) {
		List<Map<String, String>> valueMaps = dataTable.asMaps();
		for (Map<String, String> map : valueMaps) {
			User user = new User(map.get("name"), KingdominoApplication.getKingdomino());
			user.setPlayedGames(Integer.decode(map.get("playedGames")));
			user.setWonGames(Integer.decode(map.get("wonGames")));
		}
	}

	/**
	 * @author Abdallah Shapsough
	 */
	@When("I initiate querying the game statistics for a user {string}")
	public void i_initiate_querying_the_game_statistics_for_a_user(String string) {
		gamesWon = KingdominoController.getUserGamesWon(string, KingdominoApplication.getKingdomino());
		gamesPlayed = KingdominoController.getUserGamesPlayed(string, KingdominoApplication.getKingdomino());
	}

	/**
	 * @author Abdallah Shapsough
	 */
	@Then("the number of games played by {string} shall be {int}")
	public void the_number_of_games_played_by_shall_be(String string, Integer int1) {
		assertEquals((int) int1, gamesPlayed);
	}

	/**
	 * @author Abdallah Shapsough
	 */
	@Then("the number of games won by {string} shall be {int}")
	public void the_number_of_games_won_by_shall_be(String string, Integer int1) {
		assertEquals((int) int1, gamesWon);
	}
}
