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

public class CalculateRankingStepDefinition {
	@Given("the game is initialized for calculate ranking")
	public void the_game_is_initialized_for_calculate_ranking() {
		Kingdomino kingdomino = new Kingdomino();
		Game game = new Game(48, kingdomino);
		game.setNumberOfPlayers(4);
		kingdomino.setCurrentGame(game);
		createAllDominoes(game);
		KingdominoApplication.setKingdomino(kingdomino);
	}

	@Given("the players have the following two dominoes in their respective kingdoms:")
	public void the_players_have_the_following_two_dominoes_in_their_respective_kingdoms(
			io.cucumber.datatable.DataTable dataTable) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		
		String[] userNames = { "User1", "User2", "User3", "User4" };
		List<Map<String, String>> valueMaps = dataTable.asMaps();
		int i = 0;
		for (Map<String, String> map : valueMaps) {
			//Adding a Player
			User user = game.getKingdomino().addUser(userNames[i]);
			Player player = new Player(game);
			player.setUser(user);
			player.setColor(getColor(map.get("player")));
			Kingdom kingdom = new Kingdom(player);
			new Castle(0, 0, kingdom, player);
			//Adding their first domino
			int posx = Integer.decode(map.get("posx1"));
			int posy = Integer.decode(map.get("posy1"));
			int id = Integer.decode(map.get("domino1"));
			DominoInKingdom dom = new DominoInKingdom(posx, posy,kingdom, getdominoByID(id));
			dom.setDirection(getDirection(map.get("dominodir1")));
			//Adding their second domino
			posx = Integer.decode(map.get("posx2"));
			posy = Integer.decode(map.get("posy2"));
			id = Integer.decode(map.get("domino2"));
			dom = new DominoInKingdom(posx, posy,kingdom, getdominoByID(id));
			dom.setDirection(getDirection(map.get("dominodir2")));
			i++;
		}
	}

	@Given("the players have no tiebreak")
	
	public void the_players_have_no_tiebreak() {
		/*Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		List<Player> players = game.getPlayers();
		for(int i = 0; i<players.size()/2; i++) {
			for(int j = 0; j<players.size(); j++) {
				if(players.get(i).getTotalScore()==players.get(j).getTotalScore()) {
					throw new java.lang.IllegalArgumentException("There is a tie");
				}
			}
		}*/
	}

	@When("calculate ranking is initiated")
	public void calculate_ranking_is_initiated() {
		//KingdominoController.calculatePropertyAttributes(KingdominoApplication.getKingdomino());
		KingdominoController.calculateRanking(KingdominoApplication.getKingdomino());
	}

	@Then("player standings shall be the followings:")
	public void player_standings_shall_be_the_followings(io.cucumber.datatable.DataTable dataTable) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		List<Player> players = game.getPlayers();
		List<Map<String, String>> valueMaps = dataTable.asMaps();
		for (Map<String, String> map : valueMaps) {
			int actualRanking = getPlayer(players, getColor(map.get("player"))).getCurrentRanking();
			int expectedRanking = Integer.decode(map.get("standing"));
			assertEquals(expectedRanking, actualRanking);
		}
	}

	private Player getPlayer (List<Player> players, PlayerColor col) {
		for(Player p: players) {
			if(p.getColor()==col) {
				return p;
			}
		}
		throw new java.lang.IllegalArgumentException("Inexistant player of color: " + col);
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
	
	private PlayerColor getColor(String color) {
		switch (color) {
		case "blue":
			return PlayerColor.Blue;
		case "green":
			return PlayerColor.Green;
		case "yellow":
			return PlayerColor.Yellow;
		case "pink":
			return PlayerColor.Pink;
		default:
			throw new java.lang.IllegalArgumentException("Invalid color: " + color);
		}
	}
	
	private void createAllDominoes(Game game) {
		try {
			BufferedReader br = new BufferedReader(new FileReader("src/main/resources/alldominoes.dat"));
			String line = "";
			String delimiters = "[:\\+()]";
			while ((line = br.readLine()) != null) {
				String[] dominoString = line.split(delimiters); // {id, leftTerrain, rightTerrain, crowns}
				int dominoId = Integer.decode(dominoString[0]);
				TerrainType leftTerrain = getTerrainType(dominoString[1]);
				TerrainType rightTerrain = getTerrainType(dominoString[2]);
				int numCrown = 0;
				if (dominoString.length > 3) {
					numCrown = Integer.decode(dominoString[3]);
				}
				new Domino(dominoId, leftTerrain, rightTerrain, numCrown, game);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new java.lang.IllegalArgumentException(
					"Error occured while trying to read alldominoes.dat: " + e.getMessage());
		}
	}

	private Domino getdominoByID(int id) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		for (Domino domino : game.getAllDominos()) {
			if (domino.getId() == id) {
				return domino;
			}
		}
		throw new java.lang.IllegalArgumentException("Domino with ID " + id + " not found.");
	}

	private TerrainType getTerrainType(String terrain) {
		switch (terrain) {
		case "W":
			return TerrainType.WheatField;
		case "F":
			return TerrainType.Forest;
		case "M":
			return TerrainType.Mountain;
		case "G":
			return TerrainType.Grass;
		case "S":
			return TerrainType.Swamp;
		case "L":
			return TerrainType.Lake;
		default:
			throw new java.lang.IllegalArgumentException("Invalid terrain type: " + terrain);
		}
	}

}
