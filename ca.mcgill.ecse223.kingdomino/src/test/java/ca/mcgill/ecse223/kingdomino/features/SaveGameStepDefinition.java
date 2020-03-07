package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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

public class SaveGameStepDefinition {

	@Given("the game is initialized for save game")
	public void the_game_is_initialized_for_save_game() {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	@Given("the game is still in progress")
	public void the_game_is_still_in_progress() {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	@Given("no file named {string} exists in the filesystem")
	public void no_file_named_exists_in_the_filesystem(String string) {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	@When("the user initiates saving the game to a file named {string}")
	public void the_user_initiates_saving_the_game_to_a_file_named(String string) {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	@Then("a file named {string} shall be created in the filesystem")
	public void a_file_named_shall_be_created_in_the_filesystem(String string) {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	@Given("the file named {string} exists in the filesystem")
	public void the_file_named_exists_in_the_filesystem(String string) {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	@When("the user agrees to overwrite the existing file")
	public void the_user_agrees_to_overwrite_the_existing_file() {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	@Then("the file named {string} shall be updated in the filesystem")
	public void the_file_named_shall_be_updated_in_the_filesystem(String string) {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

}
