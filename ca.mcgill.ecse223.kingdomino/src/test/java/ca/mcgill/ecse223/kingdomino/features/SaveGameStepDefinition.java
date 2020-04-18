package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
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

/**
 * @author Victoria Iannotti
 */
public class SaveGameStepDefinition {

	String fileName = "";
	@Given("the game is initialized for save game")
	public void the_game_is_initialized_for_save_game() {
		Kingdomino kingdomino = new Kingdomino();
		Game game = new Game(48, kingdomino);
		kingdomino.setCurrentGame(game);
		KingdominoController.setNumberOfPlayers(4);
		KingdominoController.createPlayersAndKingdoms();
//		for (int i = 0; i < 4; i++) {
//			KingdominoController.selectColor(PlayerColor.values()[i], i, kingdomino);
//		}
//		List<Player> players = kingdomino.getCurrentGame().getPlayers();
//		for (int i = 0; i < players.size(); i++) {
//			Player player = players.get(i);
//			Kingdom kingdom = new Kingdom(player);
//			new Castle(0, 0, kingdom, player);
//			player.setBonusScore(0);
//			player.setPropertyScore(0);
//			player.setDominoSelection(null);
//		}
		KingdominoController.setNextDraft();
		KingdominoController.setNextDraft();
		KingdominoController.createAllDominos();
		KingdominoApplication.setKingdomino(kingdomino);
	}

	@Given("the game is still in progress")
	public void the_game_is_still_in_progress() {
	}

	@Given("no file named {string} exists in the filesystem")
	public void no_file_named_exists_in_the_filesystem(String string) {
		File file  = new File(string);
		if (file.exists()) {
			file.delete();
		}
	}

	@When("the user initiates saving the game to a file named {string}")
	public void the_user_initiates_saving_the_game_to_a_file_named(String string) {
		fileName = string;
		KingdominoController.save(KingdominoApplication.getKingdomino(), string);
	}

	@Then("a file named {string} shall be created in the filesystem")
	public void a_file_named_shall_be_created_in_the_filesystem(String string) {
		File tempFile = new File(string);
		boolean exists = tempFile.exists();
		assertEquals(true, exists);
	}

	@Given("the file named {string} exists in the filesystem")
	public void the_file_named_exists_in_the_filesystem(String string) {
		File file = new File(string);
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@When("the user agrees to overwrite the existing file named {string}")
	public void the_user_agrees_to_overwrite_the_existing_file_named(String string) {
	//	KingdominoController.overWriteFile(string);
	}

	@Then("the file named {string} shall be updated in the filesystem")
	public void the_file_named_shall_be_updated_in_the_filesystem(String string) {
		File tempFile = new File(string);
		boolean exists = tempFile.exists();
		assertEquals(true, exists);
	}

}
