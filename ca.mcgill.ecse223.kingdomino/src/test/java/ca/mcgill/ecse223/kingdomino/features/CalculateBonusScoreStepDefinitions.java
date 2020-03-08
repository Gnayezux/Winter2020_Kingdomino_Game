//package ca.mcgill.ecse223.kingdomino.features;
//
//import static org.junit.Assert.assertEquals;
//
////mport static org.junit.Assert.assertEquals;
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.List;
//import java.util.Map;
//
//import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
//import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
//import ca.mcgill.ecse223.kingdomino.model.BonusOption;
//import ca.mcgill.ecse223.kingdomino.model.Castle;
//import ca.mcgill.ecse223.kingdomino.model.Domino;
//import ca.mcgill.ecse223.kingdomino.model.Domino.DominoStatus;
//import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom;
//import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
//import ca.mcgill.ecse223.kingdomino.model.Game;
//import ca.mcgill.ecse223.kingdomino.model.Kingdom;
//import ca.mcgill.ecse223.kingdomino.model.Kingdomino;
//import ca.mcgill.ecse223.kingdomino.model.Player;
//import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;
//import ca.mcgill.ecse223.kingdomino.model.TerrainType;
//import ca.mcgill.ecse223.kingdomino.model.User;
//import io.cucumber.java.en.Given;
//import io.cucumber.java.en.Then;
//import io.cucumber.java.en.When;
//
//public class CalculateBonusScoreStepDefinitions {
//
//	@Given("the game is initialized for calculate bonus scores")
//	public void the_game_is_initialized_for_calculate_bonus_scores() {
//		// Write code here that turns the phrase above into concrete actions
//		// throw new cucumber.api.PendingException();
//		// Intialize empty game
//		Kingdomino kingdomino = new Kingdomino();
//		Game game = new Game(48, kingdomino);
//		game.setNumberOfPlayers(4);
//		kingdomino.setCurrentGame(game);
//		// Populate game
//		addDefaultUsersAndPlayers(game);
//		createAllDominoes(game);
//		game.setNextPlayer(game.getPlayer(0));
//		KingdominoApplication.setKingdomino(kingdomino);
//	}
//
//	@Given("Middle Kingdom is selected as bonus option")
//	public void middle_Kingdom_is_selected_as_bonus_option() {
//		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
//		Kingdomino kingdomino = KingdominoApplication.getKingdomino();
//		BonusOption bonusOption = new BonusOption("MiddleKingdom", kingdomino);
//		game.addSelectedBonusOption(bonusOption);
//
//		// Write code here that turns the phrase above into concrete actions
//		throw new cucumber.api.PendingException();
//
//	}
//
//	@Given("the player's kingdom also includes the domino {int} at position {int}:{int} with the direction {string}")
//	public void the_player_s_kingdom_also_includes_the_domino_at_position_with_the_direction(Integer int1, Integer int2,
//			Integer int3, String string) {
//		Player player = KingdominoApplication.getKingdomino().getUser(0).getPlayerInGame(0);
//		Kingdom kingdom = new Kingdom(player);
//		Domino domino = getdominoByID(int1);
//		DominoInKingdom dominoinkingdom = new DominoInKingdom(int2, int3, kingdom, domino);
//		kingdom.addTerritory(dominoinkingdom);
//
//		// Write code here that turns the phrase above into concrete actions
//		//throw new cucumber.api.PendingException();
//	}
//
//	@When("calculate bonus score is initiated")
//	public void calculate_bonus_score_is_initiated() {
//		Player player = KingdominoApplication.getKingdomino().getUser(0).getPlayerInGame(0);
//		Kingdom kingdom = new Kingdom(player);
//		DominoInKingdom[] dominoinkingdom = new DominoInKingdom[0];
//		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
//		Kingdomino kingdomino = KingdominoApplication.getKingdomino();
//		Castle castle = new Castle(0, 0, kingdom, player);
//
//		KingdominoController.CalculateBonusScore(dominoinkingdom, kingdom, castle, kingdomino);
//		// Write code here that turns the phrase above into concrete actions
//	}
//
//	@Then("the bonus score should be {int}")
//	public void the_bonus_score_should_be(Integer int1) {
//		// Write code here that turns the phrase above into concrete actions
//		//throw new cucumber.api.PendingException();
//	}
//
//	@Given("Harmony is selected as bonus option")
//	public void harmony_is_selected_as_bonus_option() {
//		// Write code here that turns the phrase above into concrete actions
//		// throw new cucumber.api.PendingException();
//
//		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
//		Kingdomino kingdomino = KingdominoApplication.getKingdomino();
//		BonusOption bonusOption = new BonusOption("Harmony", kingdomino);
//		game.addSelectedBonusOption(bonusOption);
//	}
//
/////////////////////////////////////////
///// -----Private Helper Methods---- ///
/////////////////////////////////////////
//
//	private void addDefaultUsersAndPlayers(Game game) {
//		String[] userNames = { "User1", "User2", "User3", "User4" };
//		for (int i = 0; i < userNames.length; i++) {
//			User user = game.getKingdomino().addUser(userNames[i]);
//			Player player = new Player(game);
//			player.setUser(user);
//			player.setColor(PlayerColor.values()[i]);
//			Kingdom kingdom = new Kingdom(player);
//			new Castle(0, 0, kingdom, player);
//		}
//	}
//
//	private void createAllDominoes(Game game) {
//		try {
//			BufferedReader br = new BufferedReader(new FileReader("src/main/resources/alldominoes.dat"));
//			String line = "";
//			String delimiters = "[:\\+()]";
//			while ((line = br.readLine()) != null) {
//				String[] dominoString = line.split(delimiters); // {id, leftTerrain, rightTerrain, crowns}
//				int dominoId = Integer.decode(dominoString[0]);
//				TerrainType leftTerrain = getTerrainType(dominoString[1]);
//				TerrainType rightTerrain = getTerrainType(dominoString[2]);
//				int numCrown = 0;
//				if (dominoString.length > 3) {
//					numCrown = Integer.decode(dominoString[3]);
//				}
//				new Domino(dominoId, leftTerrain, rightTerrain, numCrown, game);
//			}
//			br.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//			throw new java.lang.IllegalArgumentException(
//					"Error occured while trying to read alldominoes.dat: " + e.getMessage());
//		}
//	}
//
//	private Domino getdominoByID(int id) {
//		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
//		for (Domino domino : game.getAllDominos()) {
//			if (domino.getId() == id) {
//				return domino;
//			}
//		}
//		throw new java.lang.IllegalArgumentException("Domino with ID " + id + " not found.");
//	}
//
//	private TerrainType getTerrainType(String terrain) {
//		switch (terrain) {
//		case "W":
//			return TerrainType.WheatField;
//		case "F":
//			return TerrainType.Forest;
//		case "M":
//			return TerrainType.Mountain;
//		case "G":
//			return TerrainType.Grass;
//		case "S":
//			return TerrainType.Swamp;
//		case "L":
//			return TerrainType.Lake;
//		default:
//			throw new java.lang.IllegalArgumentException("Invalid terrain type: " + terrain);
//		}
//	}
//
//	private DirectionKind getDirection(String dir) {
//		switch (dir) {
//		case "up":
//			return DirectionKind.Up;
//		case "down":
//			return DirectionKind.Down;
//		case "left":
//			return DirectionKind.Left;
//		case "right":
//			return DirectionKind.Right;
//		default:
//			throw new java.lang.IllegalArgumentException("Invalid direction: " + dir);
//		}
//	}
//
//	private DominoStatus getDominoStatus(String status) {
//		switch (status) {
//		case "inPile":
//			return DominoStatus.InPile;
//		case "excluded":
//			return DominoStatus.Excluded;
//		case "inCurrentDraft":
//			return DominoStatus.InCurrentDraft;
//		case "inNextDraft":
//			return DominoStatus.InNextDraft;
//		case "erroneouslyPreplaced":
//			return DominoStatus.ErroneouslyPreplaced;
//		case "correctlyPreplaced":
//			return DominoStatus.CorrectlyPreplaced;
//		case "placedInKingdom":
//			return DominoStatus.PlacedInKingdom;
//		case "discarded":
//			return DominoStatus.Discarded;
//		default:
//			throw new java.lang.IllegalArgumentException("Invalid domino status: " + status);
//		}
//	}
//
//}
