package ca.mcgill.ecse223.kingdomino.features;

import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Kingdomino;
import ca.mcgill.ecse223.kingdomino.model.User;
import ca.mcgill.ecse223.kingdomino.controller.*;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
public class ProvideUserProfileStepDefinition {
	

Kingdomino kingdomino;
private Exception thrownException;
private String status;

@Given("the program is started and ready for providing user profile")
public void the_program_is_started_and_ready_for_providing_user_profile() {
    // Write code here that turns the phrase above into concrete actions
	// Intialize empty game
			kingdomino = new Kingdomino();
			Game game = new Game(48, kingdomino);
			game.setNumberOfPlayers(4);
			kingdomino.setCurrentGame(game);
			
//    throw new cucumber.api.PendingException();
}

@Given("there are no users exist")
public void there_are_no_users_exist() {
    // Write code here that turns the phrase above into concrete actions
	
	assertEquals(true, kingdomino.getUsers().isEmpty());
//    throw new cucumber.api.PendingException();
}

@When("I provide my username {string} and initiate creating a new user")
public void i_provide_my_username_and_initiate_creating_a_new_user(String string) {
    // Write code here that turns the phrase above into concrete actions
	try {
		
		KingdominoController.ProvideUserProfile(string, kingdomino);
		status="succeed";
	} catch (Exception e) {
		// TODO Auto-generated catch block
//		e.printStackTrace();
//		System.out.print(string);
//		fail();
//		the_user_creation_shall("fail");
		status = "fail";
	}
//    throw new cucumber.api.PendingException();
}

@Then("the user {string} shall be in the list of users")
public void the_user_shall_be_in_the_list_of_users(String string) {
    // Write code here that turns the phrase above into concrete actions
//	kingdomino.getUsers();
	boolean equals = false;
	for(User user : kingdomino.getUsers()) {
		if(user.getName().equals(string)) {
			assertEquals(null, thrownException);
			equals = true;
		}
	}
	if(!equals) {
		fail();
	}
	
//    throw new cucumber.api.PendingException();
}

@Given("the following users exist:")
public void the_following_users_exist(io.cucumber.datatable.DataTable dataTable) {
//	ArrayList<User> users = new ArrayList<User>(kingdomino.getUsers());
//	for(User user : users) {
//		user.delete();
//	}
	
	List<Map<String, String>> valueMaps = dataTable.asMaps();
	for (Map<String, String> map : valueMaps) {
		
		if(User.getWithName(map.get("name")) != null) {
			User.getWithName(map.get("name")).delete();
		}
		
		try {
			KingdominoController.ProvideUserProfile(map.get("name"), kingdomino);
//			i_provide_my_username_and_initiate_creating_a_new_user(map.get("name"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			fail();
		}
	
	}

}

@Then("the user creation shall {string}")
public void the_user_creation_shall(String string) {
//	ArrayList<User> users = new ArrayList<User>(kingdomino.getUsers());
//	for(User user : users) {
//		if(user.getName().equals(string)){
////			assertEquals(string, "suc);
//		}
//	}
	assertEquals(string,status);
}
//
ArrayList<User> arrayList;
@When("I initiate the browsing of all users")
public void i_initiate_the_browsing_of_all_users() {
    // Write code here that turns the phrase above into concrete actions
	arrayList = KingdominoController.ProvideUserProfile(kingdomino);
	
//    throw new cucumber.api.PendingException();
}

@Then("the users in the list shall be in the following alphabetical order:")
public void the_users_in_the_list_shall_be_in_the_following_alphabetical_order(io.cucumber.datatable.DataTable dataTable) {
   
	boolean ordered = false;
	List<Map<String, String>> valueMaps = dataTable.asMaps();
	for (Map<String, String> map : valueMaps) {
		String name = map.get("name");
		name = name.replace("\""," ");
		name = name.trim();
		int placeinlist = Integer.parseInt(map.get("placeinlist"));
		if(arrayList.get(placeinlist-1).getName().equals(name)) {
			ordered = true;
		}else {	
			ordered = false;
		}
	}
	assertEquals(true,ordered);
}
//
//@Given("the following users exist with their game statistics:")
//public void the_following_users_exist_with_their_game_statistics(io.cucumber.datatable.DataTable dataTable) {
//    // Write code here that turns the phrase above into concrete actions
//    // For automatic transformation, change DataTable to one of
//    // E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
//    // Map<K, List<V>>. E,K,V must be a String, Integer, Float,
//    // Double, Byte, Short, Long, BigInteger or BigDecimal.
//    //
//    // For other transformations you can register a DataTableType.
//    throw new cucumber.api.PendingException();
//}
//
//@When("I initiate querying the game statistics for a user {string}")
//public void i_initiate_querying_the_game_statistics_for_a_user(String string) {
//    // Write code here that turns the phrase above into concrete actions
//    throw new cucumber.api.PendingException();
//}
//
//@Then("the number of games played by and games won by the user shall be the following:")
//public void the_number_of_games_played_by_and_games_won_by_the_user_shall_be_the_following(io.cucumber.datatable.DataTable dataTable) {
//    // Write code here that turns the phrase above into concrete actions
//    // For automatic transformation, change DataTable to one of
//    // E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
//    // Map<K, List<V>>. E,K,V must be a String, Integer, Float,
//    // Double, Byte, Short, Long, BigInteger or BigDecimal.
//    //
//    // For other transformations you can register a DataTableType.
//    throw new cucumber.api.PendingException();
//}
}
