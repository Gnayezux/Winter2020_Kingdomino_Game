package ca.mcgill.ecse223.kingdomino.controller;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.model.*;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class KingdominoController {
	
	public KingdominoController() {
		
	}
	public static void main(String[] args) throws Exception {

		Kingdomino kingdomino = new Kingdomino();
		Game game = new Game(48, kingdomino);
		game.setNumberOfPlayers(4);
		kingdomino.setCurrentGame(game);

		
		SetGameOptions("is",kingdomino,"isUsingHarmony");
		SetGameOptions("is",kingdomino,"isUsingMiddleKingdom");
		
		String[] userNames = { "User1", "User2", "User3", "User4" };
		for (int i = 0; i < userNames.length; i++) {
			User user = game.getKingdomino().addUser(userNames[i]);
			Player player = new Player(game);
			player.setUser(user);
			player.setColor(PlayerColor.values()[i]);
//			Kingdom kingdom = new Kingdom(player);
//			new Castle(0, 0, kingdom, player);
		}
		
		
		
	}
	
	/**
	 * SetGameOptions method that set the number of players of the game with param num
	 * @param num
	 * @param kingdomino
	 * @throws Exception
	 * @author Abdallah Shapsough
	 */
	public static void SetGameOptions(int num, Kingdomino kingdomino) throws Exception {
		if(num<2||num>4) {
			throw new Exception("Number of players has to be between 2 and 4");
		}
		
		Game game = kingdomino.getCurrentGame();
		game.setNumberOfPlayers(num);
	}
	

	/**
	 * SetGameOptions method that takes in different parameters and is responsible for adding or removing the 
	 * selected bonus options from the current game
	 * string: must be equal to either "is" or "is not"
	 * bonus: must be equal to either "isUsingHarmony" or "isUsingMiddleKingdom"
	 * kingdomino: must be equal to Kingdomino kingdomino
	 * @param string
	 * @param kingdomino
	 * @param bonus
	 * @author Abdallah Shapsough
	 */
	public static void SetGameOptions(String string, Kingdomino kingdomino, String bonus) {
		
		//get current game of kingdomino
		Game game = kingdomino.getCurrentGame();
		
		//either add selected bonus option if string = "is" or find and remove selected bonus option if string = "is not"
			BonusOption bonusOption= new BonusOption(bonus, kingdomino);
			if(string.equals("is")) {
				game.addSelectedBonusOption(bonusOption);
			}else {
				BonusOption toRemove = null;
				for(BonusOption temp : game.getSelectedBonusOptions()) {
					if(bonus.equals(temp.getOptionName())) {
						toRemove = temp;
					}
				}
				game.removeSelectedBonusOption(toRemove);
			}
	}
	

	
	// Calculating the ranking of the players in the game
	public static void calculateRanking(Kingdomino kingdomino) {
		//Kingdomino kingdomino = KingdominoApplication.getKingdomino();
		List <Player> players = kingdomino.getCurrentGame().getPlayers();

		for (int i = 1; i < players.size(); i++) {
			Player currentPlayer = players.get(i);
			int j = i - 1;
			while (j>=0 && currentPlayer.getTotalScore() < players.get(j).getTotalScore()) {
				players.set(j+1, players.get(j));
				j--;
			}
			players.set(j+1, currentPlayer);
			players.get(i-1).setCurrentRanking(i);
		}
	}
	
	//****BEGIN FEATURE 3***
	//Starting a New Game
	//As a Kingdomino player, I want to start a new game of Kingdomino against some opponents
	//with my castle placed on my territory with the current settings of the game. 
	//The initial order of player should be randomly determined.
	public static void startNewGame(Kingdomino kingdomino) {
		for (int i = 0; i < kingdomino.getCurrentGame().getNumberOfPlayers(); i++) {
			Player player = kingdomino.getCurrentGame().getPlayer(i);
			Kingdom kingdom = new Kingdom(player);
			new Castle(0, 0, kingdom, player);
			player.setBonusScore(0);
			player.setPropertyScore(0);	
			player.setDominoSelection(null);
			
		}
		createAllDominoes(kingdomino.getCurrentGame());
		
		
		
	}
	public static void createAllDominoes(Game game) {
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
	private static TerrainType getTerrainType(String terrain) {
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
//
//	public static void startNewGame(Kingdomino kingdomino) {
//		//should kingdomino be an input or initialized in method
//		
////		Boolean kingdominoIsValid = !kingdomino.equals(null);
////
////		if (!kingdominoIsValid) {
////			throw new IllegalArgumentException("This Kingdomino already contains a game, or the Kingdomino is null");
////		}
//		
//		Game newGame = new Game(48, kingdomino);
//		
////		if(kingdomino.getUsers().size() < 2) {
////			throw new RuntimeException("There needs to be at least 2 users.");
////		}
//		try {
//			SetGameOptions(4, kingdomino);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		newGame.setNumberOfPlayers(4);					
//		
//		
//		//add players and dominos and castle
//		Player player1 = newGame.getPlayer(0);
//		Player player2 = newGame.getPlayer(1);
//		Player player3 = newGame.getPlayer(2);
//		Player player4 = newGame.getPlayer(3);
//		
//		newGame.setNextPlayer(player1);//should be randomly determined
//		newGame.setNextPlayer(player2);
//		newGame.setNextPlayer(player3);
//		newGame.setNextPlayer(player4);
//		
//		
//		
//		Kingdom kingdom1 = new Kingdom(player1);
//		Kingdom kingdom2 = new Kingdom(player2);
//		Kingdom kingdom3 = new Kingdom(player3);
//		Kingdom kingdom4 = new Kingdom(player4);
//		
//		Castle castle1 = new Castle(0, 0, kingdom1, player1);
//		Castle castle2 = new Castle(0, 0, kingdom2, player2);
//		Castle castle3 = new Castle(0, 0, kingdom3, player3);
//		Castle castle4 = new Castle(0, 0, kingdom4, player4);//check which coordonates this corresponds to
//		
//		
//		
//		//current settings of the game
//		kingdomino.setCurrentGame(newGame);
//		KingdominoApplication.setKingdomino(kingdomino);
//	}
	//******END FEATURE 3******
	
	//*******BEGIN FEATURE 5*****
	public static List<Domino> shuffleDominos(Kingdomino kingdomino) {
		//based on number of players in game, number of dominos differ
		
//		Game newGame = new Game(48, kingdomino);
		Game game = kingdomino.getCurrentGame();
		List<Domino> dominos = new ArrayList<Domino>(game.getAllDominos());
		
		Random r = new Random();
		for (int i=0; i<dominos.size(); i++) {
		    int randomPosition = r.nextInt(dominos.size());
		    Domino d = dominos.get(i);
		    Domino temp = dominos.get(randomPosition);
		    dominos.set(randomPosition, d);
		    dominos.set(i, temp);		    
		}
		
		
		return dominos;
	}
	
	public static Draft getFirstDraft(Kingdomino kingdomino) {
		
		
		
		List<Domino> dominos = new ArrayList<Domino>(kingdomino.getCurrentGame().getAllDominos());
//		System.out.print(dominos);
//		shuffleDominos(dominos);
		Draft draft = new Draft(Draft.DraftStatus.FaceDown, kingdomino.getCurrentGame());
		for(int i=dominos.size()-1; i>dominos.size()-5; i--) {
			draft.addIdSortedDomino(dominos.get(i));
			dominos.get(i).delete();
		}

		kingdomino.getCurrentGame().setCurrentDraft(draft);
				
		
		return draft;
	}
	
	public static List<Domino> getFixedOrder(Kingdomino kingdomino, String string){
		
		
		List<Domino> dominos = new ArrayList<Domino>(kingdomino.getCurrentGame().getAllDominos());;
		
		string=string.replaceAll("\\s+", "");
		string = string.replace("\"", "");
		List<String> numbers = new ArrayList<String>(Arrays.asList(string.split(",")));
		
		Game game = kingdomino.getCurrentGame();
		
//		for(int i = 0;i<numbers.size();i++) {
//			int id = Integer.parseInt(numbers.get(i));
//			game.addOrMoveAllDominoAt(game.getAllDomino(id-1), i);
////			game.addOrMoveAllDominoAt(aAllDomino, index)
//		}
		
		
		
//		for(int i = 0;i<numbers.size();i++) {
//			int id = Integer.parseInt(numbers.get(i));
//			Domino temp = dominos.get(i);
//			dominos.set(i,dominos.get(id-1));
//			dominos.set(id-1, temp);
//		}
		
		System.out.print(dominos);
		return dominos;
	}
	//*********END FEATURE 5***********
	
	//**********BEGIN FEATURE 8*********
	public static List<Domino> revealNextDraft(Kingdomino kingdomino) {
		Draft draft = new Draft(kingdomino.getCurrentGame().getCurrentDraft().getDraftStatus(), kingdomino.getCurrentGame());
		List<Domino> dominos = new ArrayList<Domino>(kingdomino.getCurrentGame().getAllDominos());;
		
		System.out.println(kingdomino.getCurrentGame().getAllDominos().size()/kingdomino.getCurrentGame().getNumberOfPlayers());
		kingdomino.getCurrentGame().setTopDominoInPile(kingdomino.getCurrentGame().getAllDomino(0));
//		for(int i=0; i<kingdomino.getCurrentGame().getAllDominos().size(); i++) {
//			kingdomino.getCurrentGame().setTopDominoInPile(kingdomino.getCurrentGame().getAllDomino(i));
//		}
		getFirstDraft(kingdomino);
		for(int i=dominos.size()-1; i>dominos.size()-5; i--) {
			draft.addIdSortedDomino(dominos.get(i));
			dominos.get(i).delete();
		}

		kingdomino.getCurrentGame().setCurrentDraft(draft);
		
		Boolean hasNext = kingdomino.getCurrentGame().hasNextDraft();
		if(kingdomino.getCurrentGame().getAllDrafts().size() == kingdomino.getCurrentGame().getAllDominos().size()/kingdomino.getCurrentGame().getNumberOfPlayers()) {
			hasNext=false;
		}
		return dominos;
	}
	
}
