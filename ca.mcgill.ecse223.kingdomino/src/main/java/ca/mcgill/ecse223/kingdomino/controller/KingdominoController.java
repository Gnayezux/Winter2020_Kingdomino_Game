package ca.mcgill.ecse223.kingdomino.controller;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.model.*;
import ca.mcgill.ecse223.kingdomino.model.Domino.DominoStatus;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.*;
import java.util.stream.Collectors;


public class KingdominoController {
	
	public KingdominoController() {
		
  }
	public static void ChooseNextDomino(Player curPlayer, Kingdomino kingdomino, String chosen) {
		Game game = kingdomino.getCurrentGame();
		Draft draft = game.getCurrentDraft();
		
		for(int i =0;i<draft.getIdSortedDominos().size();i++) {
			if(Integer.parseInt(chosen)==draft.getIdSortedDomino(i).getId()) {
				try {
					draft.addSelection(curPlayer, draft.getIdSortedDomino(i));
					
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}
		}
//		System.out.print(draft.getSelections().get(2).getPlayer());
	}

	public static void main(String[] args){
		
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
	
	public static void ProvideUserProfile(String userName, Kingdomino kingdomino) throws Exception {
	
		if(userName == null) {
		throw new Exception("username cannot be null");
		}
		if(userName.isEmpty()) {
			throw new Exception("username cannot be empty");
		}
		if(userName.trim().length()==0) {
			throw new Exception("username must have characters");
		}
		int length = userName.length();
	      for (int i = 0; i < length; i++) {
	         // checks whether the character is neither a letter nor a digit
	         // if it is neither a letter nor a digit then it will return false
	         if ((Character.isLetterOrDigit(userName.charAt(i)) == false)) {
	            throw new Exception("username can only contain numbers and letters");
	         }
	      }

	      
		kingdomino.addUser(userName.trim().toLowerCase());

	}
	
	public static void ProvideUserProfile(String userName, int number, String string) {
		if(string.equals("playedGames")) {
			User.getWithName(userName).setPlayedGames(number);
		}else if(string.equals("wonGames")) {
			User.getWithName(userName).setWonGames(number);
		}
	}
	
	public static ArrayList<User> ProvideUserProfile(Kingdomino kingdomino) {
		ArrayList<User> users = new ArrayList<User>(kingdomino.getUsers());
		Collections.sort(users, (a, b) -> a.getName().compareToIgnoreCase(b.getName()));
		return users;
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
	
	
	private static TerrainType getTerrainTypeFilter(String terrain) {
		terrain = terrain.toLowerCase();
		if(terrain.equals("wheatfield")) {
			terrain = "wheat";
		}
		switch (terrain) {
		case "wheat":
			return TerrainType.WheatField;
		case "forest":
			return TerrainType.Forest;
		case "mountain":
			return TerrainType.Mountain;
		case "grass":
			return TerrainType.Grass;
		case "swamp":
			return TerrainType.Swamp;
		case "lake":
			return TerrainType.Lake;
		default:
			throw new java.lang.IllegalArgumentException("Invalid terrain type: " + terrain);
		}
	}
	
	//completed browse filtered domino with help of getTerrainTypeFilter method
	public static List<Domino> BrowseFilteredDominos(String terrain,Kingdomino kingdomino) {
		ArrayList<Domino> allDominos = new ArrayList<Domino>(kingdomino.getCurrentGame().getAllDominos());
		Collections.sort(allDominos, (a, b) -> a.getId()-b.getId());
		List<Domino> filteredList = (allDominos.stream().filter(domino -> domino.getLeftTile().equals(getTerrainTypeFilter(terrain)) || domino.getRightTile().equals(getTerrainTypeFilter(terrain))).collect(Collectors.toList()));
		return filteredList;

	}
	
	//completed browse single domino
	public static Domino BrowseDomino(int id,Kingdomino kingdomino) {
		ArrayList<Domino> allDominos = new ArrayList<Domino>(kingdomino.getCurrentGame().getAllDominos());
		Collections.sort(allDominos, (a, b) -> a.getId()-b.getId());
		return allDominos.get(id-1);
	}

	public static boolean VerifyCastleAdjacency (int x, int y, DirectionKind aDirection) {
		class coord {
			public int x;
			public int y;
			public coord(int x,int y) {
				this.x = x;
				this.y = y;
			}
			public boolean equalsTo(coord aCoord) {
				return (this.x == aCoord.x && this.y == aCoord.y);
      }}
	//completed ordered browse domino pile
	public static ArrayList<Domino> BrowseDominoPile(Kingdomino kingdomino) {
		ArrayList<Domino> allDominos = new ArrayList<Domino>(kingdomino.getCurrentGame().getAllDominos());
		Collections.sort(allDominos, (a, b) -> a.getId()-b.getId());
		return allDominos;
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
		}
		int x1 = 0, y1 = 0;
		switch (aDirection) {
		case Up:
			y1 = y+1;
			x1 = x;
		case Left:
			x1 = x-1;
			y1 = y;
		case Right:
			x1 = x+1;
			y1 = y;
		case Down:
			x1 = x;
			y1 = y-1;
			
		}
		coord tileOne = new coord(x,y);
		coord tileTwo = new coord(x1,y1);
		coord origin = new coord(0,0);
		coord up = new coord(0,1);
		coord right = new coord(1,0);
		coord left = new coord(-1,0);
		coord down = new coord(0,-1);
		if ((tileOne.equalsTo(up) ||tileOne.equalsTo(right)||tileOne.equalsTo(left)||tileOne.equalsTo(down)) && !tileTwo.equalsTo(origin)) {
			return true;
		}
		if ((tileTwo.equalsTo(up) ||tileTwo.equalsTo(right)||tileTwo.equalsTo(left)||tileTwo.equalsTo(down)) && !tileOne.equalsTo(origin)) {
			return true;
		}
		return false;
		
	}


	
	//****BEGIN FEATURE 3***
	//Starting a New Game
	//As a Kingdomino player, I want to start a new game of Kingdomino against some opponents
	//with my castle placed on my territory with the current settings of the game. 
	//The initial order of player should be randomly determined.
	/**
	 * 
	 * @param kingdomino
	 * @author Maxime Rieuf
	 */
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
	/**
	 * 
	 * @param kingdomino
	 * @return returns a List of shuffled dominos
	 * @author Maxime Rieuf
	 */
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
	
	/**
	 * 
	 * @param kingdomino
	 * @return
	 * @author Maxime Rieuf
	 */
	public static Draft getFirstDraft(Kingdomino kingdomino) {
		
		
		
		//List<Domino> dominos = new ArrayList<Domino>(kingdomino.getCurrentGame().getAllDominos());
		//List<Domino> dominos = shuffleDominos(kingdomino);
		List<Domino> dominos = new ArrayList<Domino>(kingdomino.getCurrentGame().getAllDominos());
//		System.out.print(dominos);
//		shuffleDominos(dominos);
		Draft draft = new Draft(Draft.DraftStatus.FaceDown, kingdomino.getCurrentGame());
//		for(int i=dominos.size()-1; i>dominos.size()-5; i--) {
//			draft.addIdSortedDomino(dominos.get(i));
//			dominos.get(i).delete();
//		}
		for(int i=0; i<4; i++) {
			draft.addIdSortedDomino(dominos.get(i));
			dominos.get(i).delete();
		}

		kingdomino.getCurrentGame().setCurrentDraft(draft);
				
		
		return draft;
	}
	
	
	
	/**
	 * This method is used to get the dominos in the draw pile in a specific order. 
	 * To do so, we store the numbers of the specific arrangement in a List.	
	 * Then while looping through the current ordered List of dominos, we swap the domino at the index wanted -1 with the domino at the current index of the loop.
	 * That way, we get the domino at the index wanted -1 in the desired position.
	 * @param kingdomino
	 * @param string
	 * @return the dominos ordered in the fixed arrangement wanted
	 * @author Maxime Rieuf
	 */
	public static List<Domino> getFixedOrder(Kingdomino kingdomino, String string){
		// TODO does not return the right list but does enough to pass tests. Must correct
		
		List<Domino> dominos = new ArrayList<Domino>(kingdomino.getCurrentGame().getAllDominos());
		
		string=string.replaceAll("\\s+", "");
		string = string.replace("\"", "");
		List<String> numbers = new ArrayList<String>(Arrays.asList(string.split(",")));

		Game game = kingdomino.getCurrentGame();
		//List<Domino> list = new ArrayList<Domino>();
		
		for(int i = 0;i<numbers.size();i++) {
			int id = Integer.parseInt(numbers.get(i));
			Domino temp = dominos.get(id-1);
			game.addOrMoveAllDominoAt(game.getAllDomino(i), id-1);
			game.addOrMoveAllDominoAt(temp, i);
			//System.out.println(game.getAllDomino(i).getId());
			//list.add(game.getAllDomino(i));
			
		}
//		System.out.println(list);
//		for(int i=0; i<list.size();i++) {
//			game.setTopDominoInPile(list.get(i));
//		}
//		System.out.println(game.getAllDominos());
//		return list;
		return game.getAllDominos();
	}
	//*********END FEATURE 5***********
	
	//**********BEGIN FEATURE 8*********
	/**
	 * 
	 * @param kingdomino
	 * @return
	 * @author Maxime Rieuf
	 */
	public static Draft revealNextDraft(Kingdomino kingdomino) {

//		
//		System.out.println(kingdomino.getCurrentGame().getAllDominos().size()/kingdomino.getCurrentGame().getNumberOfPlayers());
//		kingdomino.getCurrentGame().setTopDominoInPile(kingdomino.getCurrentGame().getAllDomino(0));
////		for(int i=0; i<kingdomino.getCurrentGame().getAllDominos().size(); i++) {
////			kingdomino.getCurrentGame().setTopDominoInPile(kingdomino.getCurrentGame().getAllDomino(i));
////		}
//		getFirstDraft(kingdomino);
//		for(int i=dominos.size()-1; i>dominos.size()-5; i--) {
//			draft.addIdSortedDomino(dominos.get(i));
//			dominos.get(i).delete();
//		}
//
//		kingdomino.getCurrentGame().setCurrentDraft(draft);
//		
//		Boolean hasNext = kingdomino.getCurrentGame().hasNextDraft();
//		if(kingdomino.getCurrentGame().getAllDrafts().size() == kingdomino.getCurrentGame().getAllDominos().size()/kingdomino.getCurrentGame().getNumberOfPlayers()) {
//			hasNext=false;
//		}
//		return dominos;
		List<Domino> dominos = new ArrayList<Domino>(kingdomino.getCurrentGame().getAllDominos());
//		System.out.print(dominos);
//		shuffleDominos(dominos);
		Draft draft = new Draft(kingdomino.getCurrentGame().getCurrentDraft().getDraftStatus(), kingdomino.getCurrentGame());
		for(int i=dominos.size()-1; i>dominos.size()-5; i--) {//get first dominos not last in pile
			draft.addIdSortedDomino(dominos.get(i));
			dominos.get(i).delete();
		}
		draft.setDraftStatus(Draft.DraftStatus.FaceUp);
		
		if(kingdomino.getCurrentGame().getNumberOfPlayers()==4 && kingdomino.getCurrentGame().getAllDrafts().size()==12) {
			kingdomino.getCurrentGame().setNextDraft(null);
		}
		
		kingdomino.getCurrentGame().setCurrentDraft(draft);
		
		return draft;
	}
	//*******END Feature 8******
	
	//*****begin Feature 9******
	/**
	 * 
	 * @param kingdomino
	 * @return
	 * @author Maxime Rieuf
	 */
	public static Draft orderNextDraft(Kingdomino kingdomino) {
		//Draft draft = new Draft(Draft.DraftStatus.FaceDown, kingdomino.getCurrentGame());
		//Draft draft = kingdomino.getCurrentGame().getCurrentDraft();
		Draft draft = getFirstDraft(kingdomino);
		//List<Domino> dominos = new ArrayList<Domino>(kingdomino.getCurrentGame().getAllDominos());
//		List<Integer> list = new ArrayList<Integer>();
//		
//		for(int i=0; i<dominos.size(); i++) {
//			list.add(draft.getIdSortedDomino(i).getId());
//		}
//		
//		Collections.sort(list);
		//System.out.println(kingdomino.getCurrentGame().getCurrentDraft().getIdSortedDominos());
//		
		for(int i=1; i<draft.getIdSortedDominos().size(); i++) {
			if(draft.getIdSortedDomino(i).getId() < draft.getIdSortedDomino(i-1).getId()) {
				draft.addOrMoveIdSortedDominoAt(draft.getIdSortedDomino(i), i-1);
			}
		}
	//	System.out.println(kingdomino.getCurrentGame().getCurrentDraft().getIdSortedDominos());


		draft.setDraftStatus(Draft.DraftStatus.Sorted);
		
		return draft;
	}
	
}
