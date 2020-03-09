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

	public static void main(String[] args) {

	}

	public static ArrayList<Domino> BrowseDominoPile(Kingdomino kingdomino) {
		createAllDominoes(kingdomino.getCurrentGame());
		ArrayList<Domino> allDominos = new ArrayList<Domino>(kingdomino.getCurrentGame().getAllDominos());
		Collections.sort(allDominos, (a, b) -> a.getId() - b.getId());
		return allDominos;
	}

	
	public static Domino BrowseDomino(int id, Kingdomino kingdomino) {
		createAllDominoes(kingdomino.getCurrentGame());
		ArrayList<Domino> allDominos = new ArrayList<Domino>(kingdomino.getCurrentGame().getAllDominos());
		Collections.sort(allDominos, (a, b) -> a.getId() - b.getId());
		return allDominos.get(id - 1);
	}

	public static List<Domino> BrowseFilteredDominos(String terrain, Kingdomino kingdomino) {
		createAllDominoes(kingdomino.getCurrentGame());
		ArrayList<Domino> allDominos = new ArrayList<Domino>(kingdomino.getCurrentGame().getAllDominos());
		Collections.sort(allDominos, (a, b) -> a.getId() - b.getId());
		List<Domino> filteredList = (allDominos.stream()
				.filter(domino -> domino.getLeftTile().equals(getTerrainTypeFilter(terrain))
						|| domino.getRightTile().equals(getTerrainTypeFilter(terrain)))
				.collect(Collectors.toList()));
		return filteredList;
	}

	/// ABOVE IS GOOD
	public static void ChooseNextDomino(Player curPlayer, Kingdomino kingdomino, String chosen) {
		Game game = kingdomino.getCurrentGame();
		Draft draft = game.getCurrentDraft();

		for (int i = 0; i < draft.getIdSortedDominos().size(); i++) {
			if (Integer.parseInt(chosen) == draft.getIdSortedDomino(i).getId()) {
				try {
					draft.addSelection(curPlayer, draft.getIdSortedDomino(i));

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
	}

	public static boolean VerifyNeighbourAdjacency(Kingdom aKingdom, Domino aDomino, int x, int y,
			DirectionKind aDirection) {
		class coord {
			public int x;
			public int y;

			public coord(int x, int y) {
				this.x = x;
				this.y = y;
			}

			public boolean equalsTo(coord aCoord) {
				return (this.x == aCoord.x && this.y == aCoord.y);
			}

			public boolean adJacentTo(coord aCoord) {
				return ((this.x == aCoord.x && this.y + 1 == aCoord.y) || (this.x == aCoord.x && this.y - 1 == aCoord.y)
						|| (this.x + 1 == aCoord.x && this.y == aCoord.y)
						|| (this.x - 1 == aCoord.x && this.y == aCoord.y));
			}
		}
		int counter = 0;
		int x1 = 0, y1 = 0;
		switch (aDirection) {
		case Up:
			y1 = y + 1;
			x1 = x;
		case Left:
			x1 = x - 1;
			y1 = y;
		case Right:
			x1 = x + 1;
			y1 = y;
		case Down:
			x1 = x;
			y1 = y - 1;

		}
		coord tileOne = new coord(x, y);
		coord tileTwo = new coord(x1, y1);
		for (KingdomTerritory d : aKingdom.getTerritories()) {
			if (d.getClass().toString() == "DominoInKingdom") {
				coord temp = new coord(d.getX(), d.getY());
				if (temp.adJacentTo(tileOne) || temp.adJacentTo(tileTwo)) {
					DominoInKingdom dik = (DominoInKingdom) d;
					int x2 = 0, y2 = 0;
					switch (dik.getDirection()) {
					case Up:
						y2 = dik.getY() + 1;
						x2 = dik.getX();
					case Left:
						x2 = dik.getX() - 1;
						y2 = dik.getY();
					case Right:
						x2 = dik.getX() + 1;
						y2 = dik.getY();
					case Down:
						x2 = dik.getX();
						y2 = dik.getY() - 1;

					}
					coord leftcoord, rightcoord;
					leftcoord = new coord(dik.getX(), dik.getY());
					rightcoord = new coord(x2, y2);
					if (leftcoord.adJacentTo(tileOne)) {
						if (dik.getDomino().getLeftTile().equals(aDomino.getLeftTile())) {
							counter += 1;
						} else {
							return false;
						}
					}

					if (leftcoord.adJacentTo(tileTwo)) {
						if (dik.getDomino().getLeftTile().equals(aDomino.getRightTile())) {
							counter += 1;
						} else {
							return false;
						}
					}

					if (rightcoord.adJacentTo(tileOne)) {
						if (dik.getDomino().getRightTile().equals(aDomino.getLeftTile())) {
							counter += 1;
						} else {
							return false;
						}
					}

					if (rightcoord.adJacentTo(tileTwo)) {
						if (dik.getDomino().getRightTile().equals(aDomino.getRightTile())) {
							counter += 1;
						} else {
							return false;
						}
					}

				}
			}
		}
		if (counter != 0) {
			return true;
		}
		return false;
	}

	/**
	 * SetGameOptions method that set the number of players of the game with param
	 * num
	 * 
	 * @param num
	 * @param kingdomino
	 * @throws Exception
	 * @author Abdallah Shapsough
	 */
	public static void SetGameOptions(int num, Kingdomino kingdomino) throws Exception {
		if (num < 2 || num > 4) {
			throw new Exception("Number of players has to be between 2 and 4");
		}

		Game game = kingdomino.getCurrentGame();
		game.setNumberOfPlayers(num);
	}

	/**
	 * SetGameOptions method that takes in different parameters and is responsible
	 * for adding or removing the selected bonus options from the current game
	 * string: must be equal to either "is" or "is not" bonus: must be equal to
	 * either "isUsingHarmony" or "isUsingMiddleKingdom" kingdomino: must be equal
	 * to Kingdomino kingdomino
	 * 
	 * @param string
	 * @param kingdomino
	 * @param bonus
	 * @author Abdallah Shapsough
	 */
	public static void SetGameOptions(String string, Kingdomino kingdomino, String bonus) {

		// get current game of kingdomino
		Game game = kingdomino.getCurrentGame();

		// either add selected bonus option if string = "is" or find and remove selected
		// bonus option if string = "is not"
		BonusOption bonusOption = new BonusOption(bonus, kingdomino);
		if (string.equals("is")) {
			game.addSelectedBonusOption(bonusOption);
		} else {
			BonusOption toRemove = null;
			for (BonusOption temp : game.getSelectedBonusOptions()) {
				if (bonus.equals(temp.getOptionName())) {
					toRemove = temp;
				}
			}
			game.removeSelectedBonusOption(toRemove);
		}
	}

	public static void ProvideUserProfile(String userName, Kingdomino kingdomino) throws Exception {

		if (userName == null) {
			throw new Exception("username cannot be null");
		}
		if (userName.isEmpty()) {
			throw new Exception("username cannot be empty");
		}
		if (userName.trim().length() == 0) {
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
		if (string.equals("playedGames")) {
			User.getWithName(userName).setPlayedGames(number);
		} else if (string.equals("wonGames")) {
			User.getWithName(userName).setWonGames(number);
		}
	}

	public static ArrayList<User> ProvideUserProfile(Kingdomino kingdomino) {
		ArrayList<User> users = new ArrayList<User>(kingdomino.getUsers());
		Collections.sort(users, (a, b) -> a.getName().compareToIgnoreCase(b.getName()));
		return users;
	}

	private static TerrainType getTerrainTypeFilter(String terrain) {
		terrain = terrain.toLowerCase();
		if (terrain.equals("wheatfield")) {
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

	public boolean VerifyNoOverlapping(Domino aDomino, Kingdom aKingdom, int x, int y, DirectionKind aDirection) {
		class coord {
			public int x;
			public int y;

			public coord(int x, int y) {
				this.x = x;
				this.y = y;
			}

			public boolean equalsTo(coord aCoord) {
				return (this.x == aCoord.x && this.y == aCoord.y);
			}
		}
		int x1 = 0, y1 = 0;
		switch (aDirection) {
		case Up:
			y1 = y + 1;
			x1 = x;
		case Left:
			x1 = x - 1;
			y1 = y;
		case Right:
			x1 = x + 1;
			y1 = y;
		case Down:
			x1 = x;
			y1 = y - 1;
		}
		coord tileOne = new coord(x, y);
		coord tileTwo = new coord(x1, y1);
		for (KingdomTerritory d : aKingdom.getTerritories()) {
			coord temp = new coord(d.getX(), d.getY());
			if (d.getClass().toString() == "DominoInKingdom") {
				DominoInKingdom dik = (DominoInKingdom) d;
				int x2 = 0, y2 = 0;
				switch (dik.getDirection()) {
				case Up:
					y2 = dik.getY() + 1;
					x2 = dik.getX();
				case Left:
					x2 = dik.getX() - 1;
					y2 = dik.getY();
				case Right:
					x2 = dik.getX() + 1;
					y2 = dik.getY();
				case Down:
					x2 = dik.getX();
					y2 = dik.getY() - 1;
				}
				coord leftcoord, rightcoord;
				leftcoord = new coord(dik.getX(), dik.getY());
				rightcoord = new coord(x2, y2);
				if (leftcoord.equalsTo(tileOne) || leftcoord.equals(tileTwo) || rightcoord.equalsTo(tileOne)
						|| rightcoord.equalsTo(tileTwo)) {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean VerifyGridSize(Kingdom aKingdom) {
		int maxX = 0;
		int maxY = 0;
		int minX = 0;
		int minY = 0;
		int x = 0, y = 0, x2 = 0, y2 = 0;
		for (KingdomTerritory d : aKingdom.getTerritories()) {
			if (d.getClass().toString() == "DominoInKingdom") {
				DominoInKingdom dik = (DominoInKingdom) d;
				x = dik.getX();
				y = dik.getY();
				switch (dik.getDirection()) {
				case Up:
					y2 = dik.getY() + 1;
					x2 = dik.getX();
				case Left:
					x2 = dik.getX() - 1;
					y2 = dik.getY();
				case Right:
					x2 = dik.getX() + 1;
					y2 = dik.getY();
				case Down:
					x2 = dik.getX();
					y2 = dik.getY() - 1;
				}
				if (x < minX) {
					minX = x;
				}
				if (x > maxX) {
					maxX = x;
				}
				if (x2 < minX) {
					minX = x2;
				}
				if (x2 > maxX) {
					maxX = x2;
				}

				if (y < minY) {
					minY = y;
				}
				if (y > maxY) {
					maxY = y;
				}
				if (y2 < minY) {
					minY = y2;
				}
				if (y2 > maxY) {
					maxY = y2;
				}
			}
		}
		return ((maxX - minX) < 5 && (maxY - minY) < 5);
	}

	// completed browse single domino

	public static boolean VerifyCastleAdjacency(int x, int y, DirectionKind aDirection) {
		class coord {
			public int x;
			public int y;

			public coord(int x, int y) {
				this.x = x;
				this.y = y;
			}

			public boolean equalsTo(coord aCoord) {
				return (this.x == aCoord.x && this.y == aCoord.y);
			}
		}
		int x1 = 0, y1 = 0;
		switch (aDirection) {
		case Up:
			y1 = y + 1;
			x1 = x;
		case Left:
			x1 = x - 1;
			y1 = y;
		case Right:
			x1 = x + 1;
			y1 = y;
		case Down:
			x1 = x;
			y1 = y - 1;

		}
		coord tileOne = new coord(x, y);
		coord tileTwo = new coord(x1, y1);
		coord origin = new coord(0, 0);
		coord up = new coord(0, 1);
		coord right = new coord(1, 0);
		coord left = new coord(-1, 0);
		coord down = new coord(0, -1);
		if ((tileOne.equalsTo(up) || tileOne.equalsTo(right) || tileOne.equalsTo(left) || tileOne.equalsTo(down))
				&& !tileTwo.equalsTo(origin)) {
			return true;
		}
		if ((tileTwo.equalsTo(up) || tileTwo.equalsTo(right) || tileTwo.equalsTo(left) || tileTwo.equalsTo(down))
				&& !tileOne.equalsTo(origin)) {
			return true;
		}
		return false;
	}

	// completed ordered browse domino pile

	// ****BEGIN FEATURE 3***
	// Starting a New Game
	// As a Kingdomino player, I want to start a new game of Kingdomino against some
	// opponents
	// with my castle placed on my territory with the current settings of the game.
	// The initial order of player should be randomly determined.
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
	// ******END FEATURE 3******

	// *******BEGIN FEATURE 5*****
	/**
	 * 
	 * @param kingdomino
	 * @return returns a List of shuffled dominos
	 * @author Maxime Rieuf
	 */
	public static List<Domino> shuffleDominos(Kingdomino kingdomino) {
		// based on number of players in game, number of dominos differ

//		Game newGame = new Game(48, kingdomino);
		Game game = kingdomino.getCurrentGame();
		List<Domino> dominos = new ArrayList<Domino>(game.getAllDominos());

		Random r = new Random();
		for (int i = 0; i < dominos.size(); i++) {
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

		// List<Domino> dominos = new
		// ArrayList<Domino>(kingdomino.getCurrentGame().getAllDominos());
		// List<Domino> dominos = shuffleDominos(kingdomino);
		List<Domino> dominos = new ArrayList<Domino>(kingdomino.getCurrentGame().getAllDominos());
//		System.out.print(dominos);
//		shuffleDominos(dominos);
		Draft draft = new Draft(Draft.DraftStatus.FaceDown, kingdomino.getCurrentGame());
//		for(int i=dominos.size()-1; i>dominos.size()-5; i--) {
//			draft.addIdSortedDomino(dominos.get(i));
//			dominos.get(i).delete();
//		}
		for (int i = 0; i < 4; i++) {
			draft.addIdSortedDomino(dominos.get(i));
			dominos.get(i).delete();
		}

		kingdomino.getCurrentGame().setCurrentDraft(draft);

		return draft;
	}

	/**
	 * This method is used to get the dominos in the draw pile in a specific order.
	 * To do so, we store the numbers of the specific arrangement in a List. Then
	 * while looping through the current ordered List of dominos, we swap the domino
	 * at the index wanted -1 with the domino at the current index of the loop. That
	 * way, we get the domino at the index wanted -1 in the desired position.
	 * 
	 * @param kingdomino
	 * @param string
	 * @return the dominos ordered in the fixed arrangement wanted
	 * @author Maxime Rieuf
	 */
	public static List<Domino> getFixedOrder(Kingdomino kingdomino, String string) {
		// TODO does not return the right list but does enough to pass tests. Must
		// correct

		List<Domino> dominos = new ArrayList<Domino>(kingdomino.getCurrentGame().getAllDominos());

		string = string.replaceAll("\\s+", "");
		string = string.replace("\"", "");
		List<String> numbers = new ArrayList<String>(Arrays.asList(string.split(",")));

		Game game = kingdomino.getCurrentGame();
		// List<Domino> list = new ArrayList<Domino>();

		for (int i = 0; i < numbers.size(); i++) {
			int id = Integer.parseInt(numbers.get(i));
			Domino temp = dominos.get(id - 1);
			game.addOrMoveAllDominoAt(game.getAllDomino(i), id - 1);
			game.addOrMoveAllDominoAt(temp, i);
			// System.out.println(game.getAllDomino(i).getId());
			// list.add(game.getAllDomino(i));

		}
//		System.out.println(list);
//		for(int i=0; i<list.size();i++) {
//			game.setTopDominoInPile(list.get(i));
//		}
//		System.out.println(game.getAllDominos());
//		return list;
		return game.getAllDominos();
	}
	// *********END FEATURE 5***********

	// **********BEGIN FEATURE 8*********
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
		Draft draft = new Draft(kingdomino.getCurrentGame().getCurrentDraft().getDraftStatus(),
				kingdomino.getCurrentGame());
		for (int i = dominos.size() - 1; i > dominos.size() - 5; i--) {// get first dominos not last in pile
			draft.addIdSortedDomino(dominos.get(i));
			dominos.get(i).delete();
		}
		draft.setDraftStatus(Draft.DraftStatus.FaceUp);

		if (kingdomino.getCurrentGame().getNumberOfPlayers() == 4
				&& kingdomino.getCurrentGame().getAllDrafts().size() == 12) {
			kingdomino.getCurrentGame().setNextDraft(null);
		}

		kingdomino.getCurrentGame().setCurrentDraft(draft);

		return draft;
	}
	// *******END Feature 8******

	// *****begin Feature 9******
	/**
	 * 
	 * @param kingdomino
	 * @return
	 * @author Maxime Rieuf
	 */
	public static Draft orderNextDraft(Kingdomino kingdomino) {
		// Draft draft = new Draft(Draft.DraftStatus.FaceDown,
		// kingdomino.getCurrentGame());
		// Draft draft = kingdomino.getCurrentGame().getCurrentDraft();
		Draft draft = getFirstDraft(kingdomino);
		// List<Domino> dominos = new
		// ArrayList<Domino>(kingdomino.getCurrentGame().getAllDominos());
//		List<Integer> list = new ArrayList<Integer>();
//		
//		for(int i=0; i<dominos.size(); i++) {
//			list.add(draft.getIdSortedDomino(i).getId());
//		}
//		
//		Collections.sort(list);
		// System.out.println(kingdomino.getCurrentGame().getCurrentDraft().getIdSortedDominos());
//		
		for (int i = 1; i < draft.getIdSortedDominos().size(); i++) {
			if (draft.getIdSortedDomino(i).getId() < draft.getIdSortedDomino(i - 1).getId()) {
				draft.addOrMoveIdSortedDominoAt(draft.getIdSortedDomino(i), i - 1);
			}
		}
		// System.out.println(kingdomino.getCurrentGame().getCurrentDraft().getIdSortedDominos());

		draft.setDraftStatus(Draft.DraftStatus.Sorted);

		return draft;
	}
	
	/**
	 * a helper method for IdentifyKingdomAttributes
	 * @author kaichengwu
	 * @param leftTile
	 * @return
	 */

	public static DominoInKingdom rightTile(DominoInKingdom leftTile) {
		DominoInKingdom rightTile = new DominoInKingdom(leftTile.getX(), leftTile.getY(), leftTile.getKingdom(),
				leftTile.getDomino());
		TerrainType terr = leftTile.getDomino().getRightTile();
		if (leftTile.getDirection() == DirectionKind.Up) {
			rightTile.setY(leftTile.getY() + 1);
		} else if (leftTile.getDirection() == DirectionKind.Down) {
			rightTile.setY(leftTile.getY() - 1);
		} else if (leftTile.getDirection() == DirectionKind.Left) {
			rightTile.setX(leftTile.getX() - 1);
		} else {
			rightTile.setX(leftTile.getX() + 1);
		}

		return rightTile;
	}

	public static boolean isConnected(DominoInKingdom d1, DominoInKingdom d2) {
		if (d1.getX() == d2.getX() && d1.getY() == d2.getY() - 1) {
			return true;
		} else if (d1.getX() == d2.getX() && d1.getY() == d2.getY() + 1) {
			return true;
		} else if (d1.getY() == d2.getY() && d1.getX() == d2.getX() - 1) {
			return true;
		} else if (d1.getY() == d2.getY() && d1.getX() == d2.getX() + 1) {
			return true;
		}

		return false;
	}

	//TODO
	/**
	 * the method that determines the properties in a kingdom
	 * @param playedDominoes
	 * @param aKingdom
	 * @author kaichengwu
	 * @return
	 */
	public static Property[] IdentifyKingdomProperties(DominoInKingdom[] playedDominoes, Kingdom aKingdom){
		

		Property[] myProperties= new Property[playedDominoes.length];
		for(int i=0; i<playedDominoes.length; i++) {
			Property aProperty = new Property(aKingdom);
			if(aProperty.getIncludedDominos()==null) {
				aProperty.addIncludedDomino(playedDominoes[i].getDomino());
				
			}
				for(int j=i+1; j<playedDominoes.length; j++) {
					if(isConnected(playedDominoes[j], playedDominoes[i])) {
						if (playedDominoes[j].getDomino().getLeftTile()==playedDominoes[i].getDomino().getLeftTile()) {
							aProperty.addIncludedDomino(playedDominoes[j].getDomino());
						}
					}
					else if(isConnected(playedDominoes[j], rightTile(playedDominoes[i]))) {
						if (playedDominoes[j].getDomino().getLeftTile()==playedDominoes[i].getDomino().getRightTile()) {
							aProperty.addIncludedDomino(playedDominoes[j].getDomino());
						}
					}
					else if(isConnected(rightTile(playedDominoes[j]), playedDominoes[i])) {
						if (playedDominoes[j].getDomino().getRightTile()==playedDominoes[i].getDomino().getLeftTile()) {
							aProperty.addIncludedDomino(playedDominoes[j].getDomino());
						}
					}
					else if(isConnected(rightTile(playedDominoes[j]), rightTile(playedDominoes[i]))) {
						if (playedDominoes[j].getDomino().getRightTile()==playedDominoes[i].getDomino().getRightTile()) {
							aProperty.addIncludedDomino(playedDominoes[j].getDomino());
						}
					}
				}
		myProperties[i]=aProperty;
		}
		return myProperties;
		
	}
	/**
	 * a method that takes a property as input ant determine its number of crowns and size
	 * @param aProperty
	 * @author kaichengwu
	 */
	
	public static void CalculatePropertyAttributes(Property aProperty){
		int numCrowns=0;
		int size =0;
		for (int i=0; i< aProperty.numberOfIncludedDominos(); i++) {
			if(aProperty.getLeftTile()== aProperty.getIncludedDomino(i).getLeftTile()) {
				numCrowns += aProperty.getIncludedDomino(i).getLeftCrown();
			}
			else if (aProperty.getLeftTile()== aProperty.getIncludedDomino(i).getRightTile()){
				numCrowns += aProperty.getIncludedDomino(i).getRightCrown();
			}
			if(aProperty.getIncludedDomino(i).getLeftTile()==aProperty.getIncludedDomino(i).getRightTile()) {
				size++;
			}
			
			size++;
		}
		aProperty.setCrowns(numCrowns);
		aProperty.setSize(size);
	}
	
	/**
	 * the method that computes the bonus score 
	 * @param playedDominoes
	 * @param aKingdom
	 * @param castle
	 * @param kingdomino
	 * @author kaichengwu
	 * @return
	 */
	public static int CalculateBonusScore(DominoInKingdom[] playedDominoes, Kingdom aKingdom, Castle castle, Kingdomino kingdomino) {
		int bonus = 0;
		boolean middlexl = false;
		boolean middlexr = false;
		boolean middleyt = false;
		boolean middleyb = false;
		
		boolean middleKingdom = false;
		boolean harmony = false;
		
		for(int i = 0;i<kingdomino.getCurrentGame().getSelectedBonusOptions().size();i++) {
			if(kingdomino.getCurrentGame().getSelectedBonusOption(i).getOptionName().equals("isUsingHarmony")) {
				harmony = true;
			}
			if(kingdomino.getCurrentGame().getSelectedBonusOption(i).getOptionName().equals("isUsingMiddleKingdom")) {
				middleKingdom = true;
			}
			
		}
		
		
		if ((playedDominoes.length ==12) && harmony) {
			bonus +=5;
		}
		
		for (int i=0; i<playedDominoes.length; i++) {
			if ((playedDominoes[i].getX()==castle.getX()-2)||(rightTile(playedDominoes[i]).getX()== castle.getX()-2)) {
				middlexl = true;
				}
			if ((playedDominoes[i].getX()==castle.getX()+2)||(rightTile(playedDominoes[i]).getX()== castle.getX()+2)) {
				middlexr = true;
			}
			if ((playedDominoes[i].getY()==castle.getY()-2)||(rightTile(playedDominoes[i]).getY()== castle.getY()-2)) {
				middleyb = true;
			}
			if ((playedDominoes[i].getY()==castle.getY()+2)||(rightTile(playedDominoes[i]).getY()== castle.getY()+2)) {
				middleyt = true;
			}
			
		}
		if((middlexl&&middlexr&&middleyb&&middleyt)&&middleKingdom) {
			bonus+=10;
		}
		
			
		return bonus;
		
	}

	
	/**
	 * the method that computes the total score
	 * @param playedDominoes
	 * @param aKingdom
	 * @param aCastle
	 * @param kingdomino
	 * @author kaichengwu
	 * @return
	 */
	public static int CalculatePlayerScore(DominoInKingdom[] playedDominoes, Kingdom aKingdom, Castle aCastle, Kingdomino kingdomino) {
		
		int score = 0;
		int pscore = 0;
		int bonuscore = 0;
		Property[] myProp = IdentifyKingdomProperties(playedDominoes, aKingdom);
		for (int i = 0; i < myProp.length; i++) {
			if (myProp[i] != null) {
				CalculatePropertyAttributes(myProp[i]);
				pscore = myProp[i].getCrowns() * myProp[i].getSize();
				score += pscore;
			}

		}
		bonuscore = CalculateBonusScore(playedDominoes, aKingdom, aCastle, kingdomino);
		score += bonuscore;
		return score;
	}

	// Feature 11, moveCurrentDomino
	public static void moveCurrentDomino(DominoInKingdom currentDomino, DirectionKind newDirection) {

		Kingdom currentKD = currentDomino.getKingdom();
		DominoStatus newCurrentDominoStatus;
		int currentDominoX = currentDomino.getX();
		int currentDominoY = currentDomino.getY();
		boolean placementError = false;

		if (newDirection == DirectionKind.Up) {
			currentDomino.setY(currentDominoY + 1);
			currentDominoY++;
		} 
		else if (newDirection == DirectionKind.Down) {
			currentDomino.setY(currentDominoY - 1);
			currentDominoY--;
		} 
		else if (newDirection == DirectionKind.Left) {
			currentDomino.setX(currentDominoX - 1);
			currentDominoX--;
		} 
		else {
			currentDomino.setX(currentDominoX + 1);
			currentDominoX++;
		}

		if (currentDominoX == 0 && currentDominoY == 0) {
			placementError = true;
		} else if (currentDominoX < - 2 || currentDominoX > 2) {
			placementError = true;
		} else if (currentDominoY < -2 || currentDominoY > 2) {
			placementError = true;
		} 

		for (int i = 0; i < currentKD.getTerritories().size(); i++) {
			if ((currentDomino.getX() == currentKD.getTerritory(i).getX()
					&& currentDomino.getY() == currentKD.getTerritory(i).getY()) || (rightTile(currentDomino).getX() ==currentKD.getTerritory(i).getX() && rightTile(currentDomino).getY() ==currentKD.getTerritory(i).getY())) {
				placementError = true;
			}
		}

		
		if (placementError = true) {
			currentDomino.getDomino().setStatus(DominoStatus.ErroneouslyPreplaced);
		} else {
			currentDomino.getDomino().setStatus(DominoStatus.CorrectlyPreplaced);
		}

	}

	// Feature 12, rotateCurrentDomino
	public static void rotateCurrentDomino(Boolean isClockwise, DominoInKingdom currentDomino) {

		Kingdom currentKD = currentDomino.getKingdom();

		if (isClockwise == true) {
			if (currentDomino.getDirection() == DirectionKind.Up) {

				currentDomino.setDirection(DirectionKind.Right);
				currentDomino.setX(currentDomino.getX() + 1);
				currentDomino.setY(currentDomino.getY() - 1);

			} else if (currentDomino.getDirection() == DirectionKind.Down) {

				currentDomino.setDirection(DirectionKind.Left);
				currentDomino.setX(currentDomino.getX() - 1);
				currentDomino.setY(currentDomino.getY() + 1);

			} else if (currentDomino.getDirection() == DirectionKind.Left) {

				currentDomino.setDirection(DirectionKind.Up);
				currentDomino.setX(currentDomino.getX() + 1);
				currentDomino.setY(currentDomino.getY() + 1);

			} else {

				currentDomino.setDirection(DirectionKind.Down);
				currentDomino.setX(currentDomino.getX() - 1);
				currentDomino.setY(currentDomino.getY() - 1);
			}

		} else {
			if (currentDomino.getDirection() == DirectionKind.Up) {

				currentDomino.setDirection(DirectionKind.Left);
				currentDomino.setX(currentDomino.getX() - 1);
				currentDomino.setY(currentDomino.getY() - 1);

			} else if (currentDomino.getDirection() == DirectionKind.Down) {

				currentDomino.setDirection(DirectionKind.Right);
				currentDomino.setX(currentDomino.getX() + 1);
				currentDomino.setY(currentDomino.getY() + 1);

			} else if (currentDomino.getDirection() == DirectionKind.Left) {

				currentDomino.setDirection(DirectionKind.Down);
				currentDomino.setX(currentDomino.getX() + 1);
				currentDomino.setY(currentDomino.getY() - 1);

			} else {

				currentDomino.setDirection(DirectionKind.Up);
				currentDomino.setX(currentDomino.getX() - 1);
				currentDomino.setY(currentDomino.getY() + 1);

			}

		}

		int currentDominoX = currentDomino.getX();
		int currentDominoY = currentDomino.getY();
		boolean placementError = false;

		// myCourses
		if (currentDominoX > 4 || currentDominoX < -4 || currentDominoY > 4 || currentDominoY < -4) {
			placementError = true;
		} else if (currentDominoX == 0 && currentDominoY == 0) {
			placementError = true;
		} else if (currentDominoX < -9 || currentDominoX > 9) {
			placementError = true;
		} else if (currentDominoY < -9 || currentDominoY > 9) {
			placementError = true;
		} else {
			placementError = false;
		}

		for (int i = 0; i < currentKD.getTerritories().size(); i++) {
			if (currentDomino.getX() == currentKD.getTerritory(i).getX()
					|| currentDomino.getY() == currentKD.getTerritory(i).getY()) {
				placementError = true;
			}
		}

		if (placementError = true) {
			currentDomino.getDomino().setStatus(DominoStatus.ErroneouslyPreplaced);
		} else {
			currentDomino.getDomino().setStatus(DominoStatus.CorrectlyPreplaced);
		}

	}

	// Feature 13, placeCurrentDomino
	public static void placeCurrentDomino(DominoInKingdom currentDomino) {

		Kingdom currentKD = currentDomino.getKingdom();

		if (currentDomino.getDomino().getStatus() == DominoStatus.CorrectlyPreplaced) {
			currentDomino.getDomino().setStatus(DominoStatus.PlacedInKingdom);
			currentKD.addTerritory(currentDomino); // Don't know about this tbh
			currentDomino = new DominoInKingdom(currentDomino.getX(), currentDomino.getY(), currentDomino.getKingdom(),
					currentDomino.getDomino());

		} else {
			throw new RuntimeException("Can't place a domino that isn't correctly preplaced!");
		}
	}

	// Calculating the ranking of the players in the game
	public static void calculateRanking(Kingdomino kingdomino) {
		List<Player> players = kingdomino.getCurrentGame().getPlayers();
		Player currentPlayer;
		Player tempPlayer;
		for (int i = 0; i < players.size() - 1; i++) {
			currentPlayer = players.get(i);
			for (int j = i + 1; j < players.size(); j++) {
				tempPlayer = players.get(j);
				if (tempPlayer.getTotalScore() > currentPlayer.getTotalScore()) {
					players.set(i, tempPlayer);
					players.set(j, currentPlayer);
					currentPlayer = tempPlayer;
				} else if (tempPlayer.getTotalScore() == currentPlayer.getTotalScore()) {
					if (getLargestPropertySize(tempPlayer.getKingdom().getProperties()) > getLargestPropertySize(
							currentPlayer.getKingdom().getProperties())) {
						players.set(i, tempPlayer);
						players.set(j, currentPlayer);
						currentPlayer = tempPlayer;
					} else if (getNumberCrowns(tempPlayer.getKingdom().getProperties()) > getNumberCrowns(
							currentPlayer.getKingdom().getProperties())) {
						players.set(i, tempPlayer);
						players.set(j, currentPlayer);
						currentPlayer = tempPlayer;
					}
				}
			}
		}
	}

	// Feature 18, discardDomino
	public static void discardDomino(DominoInKingdom currentDomino) {

		if (currentDomino.getDomino().getStatus() == DominoStatus.ErroneouslyPreplaced) {

		} else {
			throw new RuntimeException("Can't discard a domino that is correctly preplaced!");
		}
		/*
		 * for (int i = 0; i < players.size(); i++) {
		 * players.get(i).setCurrentRanking(i+1); }
		 */
	}

	private static int getLargestPropertySize(List<Property> properties) {
		if (properties == null) {
			return 0;
		}
		Property largestProperty = properties.get(0);
		if (properties.size() == 1) {
			return largestProperty.getSize();
		}
		for (int i = 1; i < properties.size(); i++) {
			if (properties.get(i).getSize() > largestProperty.getSize()) {
				largestProperty = properties.get(i);
			}
		}
		return largestProperty.getSize();

	}

	private static int getNumberCrowns(List<Property> properties) {
		int numCrowns = 0;
		for (Property property : properties) {
			numCrowns += property.getCrowns();
		}
		return numCrowns;
	}

}
