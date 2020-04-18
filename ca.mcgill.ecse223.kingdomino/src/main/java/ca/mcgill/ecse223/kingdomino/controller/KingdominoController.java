package ca.mcgill.ecse223.kingdomino.controller;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.model.*;
import ca.mcgill.ecse223.kingdomino.model.Domino.DominoStatus;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;
import ca.mcgill.ecse223.kingdomino.view.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class KingdominoController {

	private static KingdominoBoardGame boardGame;

	public KingdominoController() {
		boardGame = new KingdominoBoardGame();
	}

	public static void newGame() {
		boardGame.newGamePage();
	}

	// Method that is called to initiate the start of the game
	public static void startGame(String[] users) {
		startGameSetup();
		setNumberOfPlayers(4);
		createPlayersAndKingdoms();
		List<Player> players = KingdominoApplication.getKingdomino().getCurrentGame().getPlayers();
		String[] userNames = users;
		int i = 0;
		for (Player p : players) {
			p.setUser(new User(userNames[i], KingdominoApplication.getKingdomino()));
			// KingdominoController.selectUser(new
			// User(userNames[i],KingdominoApplication.getKingdomino()),
			// p.getColor().name());
			i++;
		}
		Gameplay gameplay = new Gameplay();
		KingdominoApplication.setGameplay(gameplay);
		boardGame.startGame(KingdominoApplication.getKingdomino().getCurrentGame()); // Starting the game in the view
		gameplay.start();
	}

	public static List<Player> getPlayers() {
		return KingdominoApplication.getKingdomino().getCurrentGame().getPlayers();
	}

	public static void shuffleDominos() {
		Kingdomino kingdomino = KingdominoApplication.getKingdomino();
		Game game = kingdomino.getCurrentGame();
		if (!game.hasAllDominos()) {
			createAllDominos();
		}
		List<Domino> dominos = new ArrayList<Domino>(game.getAllDominos());
		Random r = new Random();
		for (int i = 0; i < dominos.size(); i++) {
			int randomPosition = r.nextInt(dominos.size());
			Domino d = dominos.get(i);
			Domino temp = dominos.get(randomPosition);
			dominos.set(randomPosition, d);
			dominos.set(i, temp);
		}

		for (int i = 0; i < dominos.size(); i++) {
			game.addOrMoveAllDominoAt(dominos.get(i), i);
		}
		game.setTopDominoInPile(dominos.get(0));
	}

	public static void setPileOrder(String string) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		if (!game.hasAllDominos()) {
			createAllDominos();
		}
		List<Domino> dominos = new ArrayList<Domino>(game.getAllDominos());
		string = string.replaceAll("\\s+", "");
		string = string.replace("\"", "");
		List<String> numbers = new ArrayList<String>(Arrays.asList(string.split(",")));

		/*
		 * Here, while we loop through the desired arrangement indexes, we get the
		 * domino at the specific index in the fixed arrangement and place it at index
		 * i. That way we get our fixed order of dominos.
		 */
		for (int i = 0; i < dominos.size(); i++) {
			game.addOrMoveAllDominoAt(dominos.get(Integer.parseInt(numbers.get(i)) - 1), i);
		}
		game.setTopDominoInPile(game.getAllDomino(0));
	}

	public static void createAllDominos() {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
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
		terrain = terrain.toUpperCase();
		if (terrain.equals("WHEATFIELD") || terrain.equals("WHEAT")) {
			terrain = "W";
		} else if (terrain.equals("FOREST")) {
			terrain = "F";
		} else if (terrain.equals("MOUNTAIN")) {
			terrain = "M";
		} else if (terrain.equals("GRASS")) {
			terrain = "G";
		} else if (terrain.equals("SWAMP")) {
			terrain = "S";
		} else if (terrain.equals("LAKE")) {
			terrain = "L";
		}
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

	private static Draft createNewDraft() {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		List<Domino> dominos = new ArrayList<Domino>(game.getAllDominos());
		Draft draft = new Draft(Draft.DraftStatus.FaceDown, game);
		if (game.getTopDominoInPile() == null) {
			return null;
		}
		// It takes the first 4 dominos in the pile
		for (int i = 0; i < 4; i++) {
			draft.addIdSortedDomino(dominos.get(i));
			dominos.get(i).delete(); // Removes the domino from the domino pile
		}
		// Sets what the top domino in the pile is
		if (game.hasAllDominos()) {
			game.setTopDominoInPile(game.getAllDomino(0));
		} else {
			game.setTopDominoInPile(null); // Null will indicate that the last turn
		}
		return draft;
	}

	public static void orderNextDraft() {
		// Sorting the next draft of the game in order of Domino IDs
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Draft draft = game.getNextDraft();
		List<Domino> draftDominos = new ArrayList<Domino>(game.getNextDraft().getIdSortedDominos());
		Collections.sort(draftDominos, (a, b) -> a.getId() - b.getId());
		for (int i = 0; i < draftDominos.size(); i++) {
			draft.addOrMoveIdSortedDominoAt(draftDominos.get(i), i);
		}
		draft.setDraftStatus(Draft.DraftStatus.Sorted);
	}

	public static void setNextDraft() {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		// Gets a new draft from the pile
		Draft draft = createNewDraft(); // Current status of the new draft is FaceDown
		// The next draft becomes the current draft
		// and the new draft becomes the next draft
		game.setCurrentDraft(game.getNextDraft());
		game.setNextDraft(draft);
		if (boardGame != null) {
			boardGame.updateDrafts(game.getCurrentDraft(), game.getNextDraft());
		}

	}

	public static void revealNextDraft() {
		// Setting the status to FaceUp
		Draft draft = KingdominoApplication.getKingdomino().getCurrentGame().getNextDraft();
		draft.setDraftStatus(Draft.DraftStatus.FaceUp);
		System.out.println("before");
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		if (boardGame != null) {
			boardGame.updateDrafts(game.getCurrentDraft(), game.getNextDraft());
		}
	}

	public static void generateInitialPlayerOrder() {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		boolean taken;
		while (numbers.size() < 4) {
			Integer temp = (int) (Math.random() * ((3 - 0) + 1));
			taken = false;
			for (int j = 0; j < numbers.size(); j++) {
				if (numbers.get(j).compareTo(temp) == 0) {
					taken = true;
				}
			}
			if (!taken) {
				numbers.add(temp);
			}
		}
		List<Player> players = game.getPlayers();
		for (int i = 0; i < players.size(); i++) {
			game.addOrMovePlayerAt(players.get(i), numbers.get(i).intValue());
		}
		game.setNextPlayer(game.getPlayer(0));
		if (boardGame != null) {
			boardGame.sendMessage("For your first turn, choose a domino. \nTo lock in your choice, click <select>.");
			boardGame.notifyCurrentPlayer(game.getNextPlayer());
		}
	}

	/******************************************/

	public static void selectDomino(int selectedDominoID) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Draft draft = game.getNextDraft();
		for (Domino d : draft.getIdSortedDominos()) {
			if (d.getId() == selectedDominoID) {
				if (d.hasDominoSelection()) {
					break;
				} else {
					new DominoSelection(game.getNextPlayer(), d, draft);
					break;
				}
			}
		}
		if (boardGame != null) {
			boardGame.setDominoSelectionEnabled(false);
		}
	}

	public static boolean isSelectionValid() {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		if (player.getDominoSelection() != null) {
			return true;
		} else {
			return false;
		}
	}

	public static void selectionComplete() {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		if (isSelectionValid()) {
			if (boardGame != null) {
				boardGame.changeButtonColor(player.getColor(), player.getDominoSelection().getDomino().getId());
			}
		}
		if (boardGame != null) {
			boardGame.setDominoSelectionEnabled(true);
		}
		KingdominoApplication.getGameplay().endOfTurn();
		KingdominoApplication.getGameplay().selectionComplete();

	}

	public static void nextPlayer() {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Player nextPlayer = game.getNextPlayer();
		List<Player> players = game.getPlayers();
		for (int i = 0; i < players.size(); i++) {
			if (nextPlayer.equals(players.get(i))) {
				if (i + 1 != players.size()) {
					game.setNextPlayer(players.get(i + 1));
					if (boardGame != null) {
						boardGame.notifyCurrentPlayer(game.getNextPlayer());
					}
				}
				break;
			}
		}
		
	}

	public static boolean isCurrentPlayerTheLastInTurn() {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Player nextPlayer = game.getNextPlayer();
		List<Player> players = game.getPlayers();
		for (int i = 0; i < players.size() - 1; i++) {
			if (nextPlayer.equals(players.get(i))) {
				return false;
			}
		}
		// TODO end of the turn
		return true;
	}

	public static void generatePlayerOrder() {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Draft draft = game.getCurrentDraft();
		ArrayList<DominoSelection> selections = new ArrayList<DominoSelection>(draft.getSelections());
		int i = 0;
		for (DominoSelection selection : selections) {
			game.addOrMovePlayerAt(selection.getPlayer(), i);
			i++;
		}
		game.setNextPlayer(game.getPlayer(0));
	}

	/******************************************/

	public static void startGameSetup() {
		Kingdomino kingdomino = new Kingdomino();
		KingdominoApplication.setKingdomino(kingdomino);
	}

	public static void setNumberOfPlayers(int numPlayers) {
		Kingdomino kingdomino = KingdominoApplication.getKingdomino();
		Game game = new Game(48, kingdomino);
		kingdomino.setCurrentGame(game);
		game.setNumberOfPlayers(numPlayers);
	}

	public static void setBonusOption(String bonus, boolean selected) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		BonusOption bonusOption = new BonusOption(bonus, KingdominoApplication.getKingdomino());
		if (selected) {
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

	public static void createPlayersAndKingdoms() {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		for (int i = 0; i < game.getNumberOfPlayers(); i++) {
			Player player = new Player(game);
			Kingdom kingdom = new Kingdom(player);
			player.setColor(PlayerColor.values()[i]);
			new Castle(0, 0, kingdom, player);
			player.setBonusScore(0);
			player.setPropertyScore(0);
			player.setDominoSelection(null);
		}
	}

	// TODO --> Game set ups
	// Handling users (storing and loading the profiles and data for users)
	public static boolean selectUser(User user, String color) {
		List<Player> players = KingdominoApplication.getKingdomino().getCurrentGame().getPlayers();
		for (Player p : players) {
			if (p.getColor().equals(getColor(color.toLowerCase()))) {
				p.setUser(user);
				return true;
			}
		}
		return false;
	}

	// This method is good
	private static PlayerColor getColor(String color) {
		color = color.toLowerCase();
		switch (color) {
		case "pink":
			return PlayerColor.Pink;
		case "green":
			return PlayerColor.Green;
		case "blue":
			return PlayerColor.Blue;
		case "yellow":
			return PlayerColor.Yellow;
		default:
			throw new java.lang.IllegalArgumentException("Invalid color: " + color);
		}
	}

	public static boolean createNewUser(String userName) {
		Kingdomino kingdomino = KingdominoApplication.getKingdomino();
		if (User.getWithName(userName) != null) {
			return false;
		}
		if (userName == null) {
			return false;
		}
		if (userName.isEmpty()) {
			return false;
		}
		if (userName.trim().length() == 0) {
			return false;
		}
		for (int i = 0; i < userName.length(); i++) {
			if (!Character.isLetterOrDigit(userName.charAt(i))) {
				return false;
			}
		}
		for (User user : kingdomino.getUsers()) {
			if (userName.equalsIgnoreCase(user.getName())) {
				return false;
			}
		}
		kingdomino.addUser(userName);
		return true;
	}

	public static void clearUsers() {
		Kingdomino kingdomino = KingdominoApplication.getKingdomino();
		for (User user : kingdomino.getUsers()) {
			kingdomino.removeUser(user);
		}
	}

	public static List<User> browseAllUsers() {
		Kingdomino kingdomino = KingdominoApplication.getKingdomino();
		ArrayList<User> users = new ArrayList<User>(kingdomino.getUsers());
		Collections.sort(users, (a, b) -> a.getName().compareToIgnoreCase(b.getName()));
		return users;
	}

	public static int getUserGamesWon(String userName) {
		return User.getWithName(userName).getWonGames();
	}

	public static int getUserGamesPlayed(String userName) {
		return User.getWithName(userName).getPlayedGames();
	}

	/******************************************/

	public static List<Domino> browseDominoPile() {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		if (!game.hasAllDominos()) {
			createAllDominos();
		}
		// Sorts all 48 dominos in order
		ArrayList<Domino> allDominos = new ArrayList<Domino>(game.getAllDominos());
		Collections.sort(allDominos, (a, b) -> a.getId() - b.getId());
		game.setTopDominoInPile(allDominos.get(0));
		return allDominos;
	}

	public static List<Domino> browseFilteredDominos(String terrain) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		if (!game.hasAllDominos()) {
			createAllDominos();
		}
		ArrayList<Domino> allDominos = new ArrayList<Domino>(game.getAllDominos());
		Collections.sort(allDominos, (a, b) -> a.getId() - b.getId());
		List<Domino> filteredList = (allDominos.stream()
				.filter(domino -> domino.getLeftTile().equals(getTerrainType(terrain))
						|| domino.getRightTile().equals(getTerrainType(terrain)))
				.collect(Collectors.toList()));
		return filteredList;
	}

	/******************************************/

	public static void removeKing() {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		Domino dom = player.getDominoSelection().getDomino();
		dom.setStatus(DominoStatus.ErroneouslyPreplaced);
		new DominoInKingdom(0, 0, player.getKingdom(), dom); // Placing domino in kingdom
	}

	public static void moveDomino(String movement) {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		DominoInKingdom dom = (DominoInKingdom) player.getKingdom()
				.getTerritory(player.getKingdom().numberOfTerritories() - 1); // Get last domino
		int x = dom.getX();
		int y = dom.getY();
		String dir = null;
		if (verifyGridSize(player.getKingdom())) { // if the grid size of a kingdom respects rules
			switch (movement) {
			case "left":
				if (dom.setX(x - 1)) {
					dir = "left";
				}
				break;
			case "right":
				if (dom.setX(x + 1)) {
					dir = "right";
				}
				break;
			case "up":
				if (dom.setY(y + 1)) {
					dir = "up";
				}
				break;
			case "down":
				if (dom.setY(y - 1)) {
					dir = "down";
				}
				break;
			}
		}
		if (dir != null) {
			if (!verifyGridSize(player.getKingdom())) { // if the grid size of a kingdom does not respect the rules
				if (dom.getDomino().getStatus().equals(Domino.DominoStatus.CorrectlyPreplaced)) {
					switch (dir) {
					case "left":
						dom.setX(dom.getX() + 1);
						break;
					case "right":
						dom.setX(dom.getX() - 1);
						break;
					case "up":
						dom.setY(dom.getY() - 1);
						break;
					case "down":
						dom.setY(dom.getY() + 1);
						break;
					}
				}
			}
		}
		resetDominoStatus(dom);
	}

	public static void resetDominoStatus(DominoInKingdom dom) {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		boolean castleAdjacency = verifyCastleAdjacency(dom.getX(), dom.getY(), dom.getDirection());

		boolean neighborAdjacency = verifyNeighborAdjacency(player.getKingdom(), dom.getDomino(), dom.getX(),
				dom.getY(), dom.getDirection());
		boolean noOverlapping = verifyNoOverlapping(dom.getDomino(), player.getKingdom(), dom.getX(), dom.getY(),
				dom.getDirection());
		if ((castleAdjacency || neighborAdjacency) && noOverlapping) {
			dom.getDomino().setStatus(DominoStatus.CorrectlyPreplaced);
		} else {
			// System.out.println("here");
			dom.getDomino().setStatus(DominoStatus.ErroneouslyPreplaced);
		}
	}

	public static void rotateDomino(String rotation) {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		List<KingdomTerritory> territories = player.getKingdom().getTerritories();
		DominoInKingdom ter = (DominoInKingdom) territories.get(territories.size() - 1);
		DirectionKind dir = ter.getDirection();
		if (rotation.equalsIgnoreCase("clockwise")) {
			switch (dir) {
			case Up:
				ter.setDirection(DirectionKind.Right);
				if (!verifyGridSize(player.getKingdom())) {
					ter.setDirection(DirectionKind.Up);
				}
				break;
			case Down:
				ter.setDirection(DirectionKind.Left);
				if (!verifyGridSize(player.getKingdom())) {
					ter.setDirection(DirectionKind.Down);
				}
				break;
			case Right:
				ter.setDirection(DirectionKind.Down);
				if (!verifyGridSize(player.getKingdom())) {
					ter.setDirection(DirectionKind.Right);
				}
				break;
			case Left:
				ter.setDirection(DirectionKind.Up);
				if (!verifyGridSize(player.getKingdom())) {
					ter.setDirection(DirectionKind.Left);
				}
				break;
			}
		} else {
			switch (dir) {
			case Up:
				ter.setDirection(DirectionKind.Left);
				if (!verifyGridSize(player.getKingdom())) {
					ter.setDirection(DirectionKind.Up);
				}
				break;
			case Down:
				ter.setDirection(DirectionKind.Right);
				if (!verifyGridSize(player.getKingdom())) {
					ter.setDirection(DirectionKind.Down);
				}
				break;
			case Right:
				ter.setDirection(DirectionKind.Up);
				if (!verifyGridSize(player.getKingdom())) {
					ter.setDirection(DirectionKind.Right);
				}
				break;
			case Left:
				ter.setDirection(DirectionKind.Down);
				if (!verifyGridSize(player.getKingdom())) {
					ter.setDirection(DirectionKind.Left);
				}
				break;
			}
		}
		resetDominoStatus(ter);
	}

	public static void placeDomino() {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		List<KingdomTerritory> territories = player.getKingdom().getTerritories();
		DominoInKingdom ter = (DominoInKingdom) territories.get(territories.size() - 1);
		Domino dom = ter.getDomino();
		// Placing the domino in the kingdom
		dom.setStatus(DominoStatus.PlacedInKingdom);
		player.setDominoSelection(null);
	}

	public static boolean isDominoCorrectlyPreplaced() {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		List<KingdomTerritory> territories = player.getKingdom().getTerritories();
		DominoInKingdom ter = (DominoInKingdom) territories.get(territories.size() - 1);
		Domino dom = ter.getDomino();
		if (dom.getStatus() == DominoStatus.CorrectlyPreplaced) {
			return true;
		}
		return false;
	}

	public static void placing() {
		KingdominoApplication.getGameplay().readyToPlace();
		KingdominoApplication.getGameplay().placeLast();
		KingdominoApplication.getGameplay().endGame();
	}

	public static boolean verifyCastleAdjacency(int x, int y, DirectionKind aDirection) {
		int x1 = 0, y1 = 0;

		switch (aDirection) {
		case Up:
			y1 = y + 1;
			x1 = x;
			break;
		case Left:
			x1 = x - 1;
			y1 = y;
			break;
		case Right:
			x1 = x + 1;
			y1 = y;
			break;
		case Down:
			x1 = x;
			y1 = y - 1;
			break;
		}

		Coord tileOne = new Coord(x, y);
		Coord tileTwo = new Coord(x1, y1);
		Coord origin = new Coord(0, 0);
		Coord up = new Coord(0, 1);
		Coord right = new Coord(1, 0);
		Coord left = new Coord(-1, 0);
		Coord down = new Coord(0, -1);

		if ((tileOne.equalsTo(up) || tileOne.equalsTo(right) || tileOne.equalsTo(left) || tileOne.equalsTo(down))
				&& !tileTwo.equalsTo(origin)) {
			return true;
		} else if ((tileTwo.equalsTo(up) || tileTwo.equalsTo(right) || tileTwo.equalsTo(left) || tileTwo.equalsTo(down))
				&& !tileOne.equalsTo(origin)) {
			return true;
		}
		return false;
	}

	public static boolean verifyNeighborAdjacency(Kingdom aKingdom, Domino aDomino, int x, int y,
			DirectionKind aDirection) {

		int x1 = 0, y1 = 0;
		switch (aDirection) {
		case Up:
			y1 = y + 1;
			x1 = x;
			break;
		case Left:
			x1 = x - 1;
			y1 = y;
			break;
		case Right:
			x1 = x + 1;
			y1 = y;
			break;
		case Down:
			x1 = x;
			y1 = y - 1;
			break;

		}
		Coord tileOneCoord = new Coord(x, y);
		Coord tileTwoCoord = new Coord(x1, y1);

		for (KingdomTerritory d : aKingdom.getTerritories()) {
			if (d instanceof DominoInKingdom) {
				DominoInKingdom ter = (DominoInKingdom) d;
				if (ter.getDomino() != aDomino) {
					Coord tempOneCoord = new Coord(ter.getX(), ter.getY());
					int tempX = 0, tempY = 0;
					switch (ter.getDirection()) {
					case Up:
						tempY = ter.getY() + 1;
						tempX = ter.getX();
						break;
					case Left:
						tempX = ter.getX() - 1;
						tempY = ter.getY();
						break;
					case Right:
						tempX = ter.getX() + 1;
						tempY = ter.getY();
						break;
					case Down:
						tempX = ter.getX();
						tempY = ter.getY() - 1;
						break;

					}
					Coord tempTwoCoord = new Coord(tempX, tempY);
					if (tileOneCoord.adJacentTo(tempOneCoord)) {
						if (ter.getDomino().getLeftTile() == aDomino.getLeftTile()) {
							return true;
						}
					}
					if (tileOneCoord.adJacentTo(tempTwoCoord)) {
						if (ter.getDomino().getRightTile() == aDomino.getLeftTile()) {
							return true;
						}
					}
					if (tileTwoCoord.adJacentTo(tempOneCoord)) {
						if (ter.getDomino().getLeftTile() == aDomino.getRightTile()) {
							return true;
						}
					}
					if (tileTwoCoord.adJacentTo(tempTwoCoord)) {
						if (ter.getDomino().getRightTile() == aDomino.getRightTile()) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public static boolean verifyNoOverlapping(Domino aDomino, Kingdom aKingdom, int x, int y,
			DirectionKind aDirection) {
		int x1 = 0, y1 = 0;
		switch (aDirection) {
		case Up:
			y1 = y + 1;
			x1 = x;
			break;
		case Left:
			x1 = x - 1;
			y1 = y;
			break;
		case Right:
			x1 = x + 1;
			y1 = y;
			break;
		case Down:
			x1 = x;
			y1 = y - 1;
			break;
		}
		Coord tileOne = new Coord(x, y);
		Coord tileTwo = new Coord(x1, y1);
		for (KingdomTerritory d : aKingdom.getTerritories()) {
			if (d instanceof DominoInKingdom) {
				DominoInKingdom dik = (DominoInKingdom) d;
				if (!dik.getDomino().equals(aDomino)) {
					int x2 = 0, y2 = 0;
					switch (dik.getDirection()) {
					case Up:
						y2 = dik.getY() + 1;
						x2 = dik.getX();
						break;
					case Left:
						x2 = dik.getX() - 1;
						y2 = dik.getY();
						break;
					case Right:
						x2 = dik.getX() + 1;
						y2 = dik.getY();
						break;
					case Down:
						x2 = dik.getX();
						y2 = dik.getY() - 1;
						break;
					}
					Coord leftcoord, rightcoord;
					leftcoord = new Coord(dik.getX(), dik.getY());
					rightcoord = new Coord(x2, y2);

					boolean leftOne = ((leftcoord.x == tileOne.x) && (leftcoord.y == tileOne.y));
					boolean leftTwo = ((leftcoord.x == tileTwo.x) && (leftcoord.y == tileTwo.y));
					boolean rightOne = ((rightcoord.x == tileOne.x) && (rightcoord.y == tileOne.y));
					boolean rightTwo = ((rightcoord.x == tileTwo.x) && (rightcoord.y == tileTwo.y));

					if (leftOne || leftTwo || rightOne || rightTwo) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public static boolean verifyGridSize(Kingdom aKingdom) {
		int maxX = -5;
		int maxY = -5;
		int minX = 5;
		int minY = 5;
		int x = 0, y = 0, x2 = 0, y2 = 0;
		for (KingdomTerritory d : aKingdom.getTerritories()) {

			if (d instanceof DominoInKingdom) {

				DominoInKingdom dik = (DominoInKingdom) d;

				x = dik.getX();
				y = dik.getY();

				switch (dik.getDirection()) {
				case Up:
					y2 = y + 1;
					x2 = x;
					break;
				case Left:
					x2 = x - 1;
					y2 = y;
					break;
				case Right:
					x2 = x + 1;
					y2 = y;
					break;
				case Down:
					x2 = x;
					y2 = y - 1;
					break;
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
			if (d instanceof Castle) {
				if (d.getX() > maxX) {
					maxX = d.getX();
				}
				if (d.getX() < minX) {
					minX = d.getX();
				}
				if (d.getY() > maxY) {
					maxY = d.getY();
				}
				if (d.getY() < minY) {
					minY = d.getY();
				}
			}
		}
		boolean validity = ((maxX - minX) < 5 && (maxY - minY) < 5);
		return validity;
	}

	/******************************************/

	public static void discardDomino() {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		if (isPossibleToPlace()) {
			player.getDominoSelection().getDomino().setStatus(DominoStatus.ErroneouslyPreplaced);
		} else {
			player.getDominoSelection().getDomino().setStatus(DominoStatus.Discarded);
		}
	}

	public static void discarding() {
		KingdominoApplication.getGameplay().discardLast();
		KingdominoApplication.getGameplay().discard();
		KingdominoApplication.getGameplay().endGame();
	}

	public static boolean isPossibleToPlace() {
		Kingdom kingdom = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer().getKingdom();
		if (kingdom.getTerritory(kingdom.getTerritories().size() - 1) instanceof DominoInKingdom) {
			DominoInKingdom dom = (DominoInKingdom) kingdom.getTerritory(kingdom.getTerritories().size() - 1);
			int originalX = dom.getX();
			int originalY = dom.getY();
			boolean castleAdjacency;
			boolean neighborAdjacency;
			boolean noOverlapping;
			boolean validGridSize;
			for (int i = -5; i <= 5; i++) {
				for (int j = -5; j < 5; j++) {
					for (DirectionKind dir : DirectionKind.values()) {
						dom.setX(i);
						dom.setY(j);
						castleAdjacency = verifyCastleAdjacency(dom.getX(), dom.getY(), dir);
						neighborAdjacency = verifyNeighborAdjacency(kingdom, dom.getDomino(), dom.getX(), dom.getY(),
								dir);
						noOverlapping = verifyNoOverlapping(dom.getDomino(), kingdom, dom.getX(), dom.getY(), dir);
						validGridSize = verifyGridSize(kingdom);
						if ((castleAdjacency || neighborAdjacency) && noOverlapping && validGridSize) {
							dom.setX(originalX);
							dom.setY(originalY);
							return true;
						}
					}
				}
			}
			dom.setX(originalX);
			dom.setY(originalY);
		}
		return false;
	}

	public static boolean isCurrentTurnTheLastInGame() {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		if (game.getTopDominoInPile() == null) {
			return true;
		} else {
			return false;
		}
	}

	/******************************************/

	public static void updateScore() {
		System.out.println("updating");
		KingdominoController.identifyProperties();
		KingdominoController.calculatePropertyAttributes();
		KingdominoController.calculateBonusScore();
		KingdominoController.calculatePlayerScore();
	}

	public static void identifyProperties() {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		List<KingdomTerritory> territories = player.getKingdom().getTerritories();
		List<Property> properties = player.getKingdom().getProperties();
		boolean isMatchL;
		boolean isMatchR;
		// Going through all of the territories in the kingdom
		for (int i = 0; i < territories.size(); i++) {
			// Only care about the territories that are DominoInKingdoms
			if (territories.get(i) instanceof DominoInKingdom) {
				// Working with the DominoInKingdom
				DominoInKingdom ter = (DominoInKingdom) territories.get(i);
				System.out.println(ter.getDomino().getStatus());
				if (ter.getDomino().getStatus() != DominoStatus.Discarded) {
					isMatchL = false;
					// Looking at the left tile
					for (int j = 0; j < properties.size(); j++) {
						// If this property type matches the left tile type
						if (ter.getDomino().getLeftTile() == properties.get(j).getLeftTile()) {
							// If the domino isn't already present in the property
							if (!propertyContains(ter, properties.get(j))) {
								// If the domino left tile is adjacent to another square of a domino in property
								// of same type
								if (isLeftMatch(ter, properties.get(j), player.getKingdom())) {
									properties.get(j).addIncludedDomino(ter.getDomino());
									isMatchL = true;
								}
							} else {
								isMatchL = true;
							}
						}

					}
					if (!isMatchL) {
						Property prop = new Property(player.getKingdom());
						prop.addIncludedDomino(ter.getDomino());
						prop.setLeftTile(ter.getDomino().getLeftTile());
						player.getKingdom().addProperty(prop);
					}
					// Looking at the right tile
					isMatchR = false;
					// Looking at the left tile
					for (int j = 0; j < properties.size(); j++) {
						// If this property type matches the left tile type
						if (ter.getDomino().getRightTile() == properties.get(j).getLeftTile()) {
							// If the domino isn't already present in the property
							if (!propertyContains(ter, properties.get(j))) {
								// If the domino left tile is adjacent to another square of a domino in property
								// of same type
								if (isRightMatch(ter, properties.get(j), player.getKingdom())) {
									properties.get(j).addIncludedDomino(ter.getDomino());
									isMatchR = true;
								}
							} else {
								isMatchR = true;
							}
						}

					}
					if (!isMatchR) {
						Property prop = new Property(player.getKingdom());
						prop.addIncludedDomino(ter.getDomino());
						prop.setLeftTile(ter.getDomino().getRightTile());
						player.getKingdom().addProperty(prop);
					}
				}
			}
		}
		checkForConnected(player.getKingdom().getProperties(), player.getKingdom());
		List<Property> props = player.getKingdom().getProperties();
		for (int i = 0; i < props.size(); i++) {
			if (props.get(i).getLeftTile() == null) {
				props.get(i).delete();
				i--;
			}
		}
	}

	private static void checkForConnected(List<Property> properties, Kingdom k) {
		ArrayList<Property> propsOfType = new ArrayList<Property>();
		for (TerrainType type : TerrainType.values()) {
			for (Property prop : properties) {
				if (prop.getLeftTile() == type) {
					propsOfType.add(prop);
				}
			}
			if (propsOfType.size() > 1) {
				handleDuplicates(propsOfType, k);
			}
			if (!propsOfType.isEmpty()) {
				propsOfType.clear();
			}
		}
	}

	private static void handleDuplicates(ArrayList<Property> propsOfType, Kingdom k) {
		HashMap<Integer, Integer> map = new HashMap<>();
		ArrayList<Integer> indexes = new ArrayList<>();
		for (int i = 0; i < propsOfType.size(); i++) {
			for (int j = 0; j < propsOfType.get(i).getIncludedDominos().size(); j++) {
				if (!map.containsKey(propsOfType.get(i).getIncludedDomino(j).getId())) {
					map.put(propsOfType.get(i).getIncludedDomino(j).getId(), i);
				} else {
					int index = map.get(propsOfType.get(i).getIncludedDomino(j).getId());
					if (!indexes.contains(index)) {
						indexes.add(index);
					}
					if (!indexes.contains(i)) {
						indexes.add(i);
					}
				}
			}
		}

		Property prop = new Property(k);
		for (int i : indexes) {
			prop.setLeftTile(propsOfType.get(i).getLeftTile());
			for (int j = 0; j < propsOfType.get(i).getIncludedDominos().size(); j++) {
				prop.addIncludedDomino(propsOfType.get(i).getIncludedDomino(j));

			}
		}

		List<Domino> dominos = new ArrayList<Domino>(prop.getIncludedDominos());

		Collections.sort(dominos, (a, b) -> a.getId() - b.getId());

		for (int i = 0; i < dominos.size(); i++) {
			prop.addOrMoveIncludedDominoAt(dominos.get(i), i);

		}

		List<Property> temp = new ArrayList<Property>(k.getProperties());
		for (Property todelete : temp) {
			for (int j : indexes) {
				if (todelete.equals(propsOfType.get(j))) {
					todelete.delete();
				}
			}
		}

	}

	private static boolean propertyContains(DominoInKingdom dom, Property prop) {
		for (Domino domInProperty : prop.getIncludedDominos()) {
			if (dom.getDomino() == domInProperty) {
				return true;
			}
		}
		return false;
	}

	private static boolean isLeftMatch(DominoInKingdom dom, Property prop, Kingdom kingdom) {
		boolean isMatch = false;
		// Comparing with all of the dominos in the property
		for (Domino domInProperty : prop.getIncludedDominos()) {
			DominoInKingdom tempTer = null;
			// Getting the DominoInKingdom object with that domino
			for (KingdomTerritory t : kingdom.getTerritories()) {
				if (t instanceof DominoInKingdom) {
					if (((DominoInKingdom) t).getDomino() == domInProperty) {
						tempTer = (DominoInKingdom) t;
						break;
					}
				}
			}
			Coord curCoord = new Coord(dom.getX(), dom.getY());
			Coord toCompareCoord = new Coord(tempTer.getX(), tempTer.getY());
			if (dom.getDomino().getLeftTile() == tempTer.getDomino().getLeftTile()
					&& curCoord.adJacentTo(toCompareCoord)) {
				isMatch = true;
			}
			switch (tempTer.getDirection()) {
			case Up:
				toCompareCoord = new Coord(tempTer.getX(), tempTer.getY() + 1);
				break;
			case Down:
				toCompareCoord = new Coord(tempTer.getX(), tempTer.getY() - 1);
				break;
			case Left:
				toCompareCoord = new Coord(tempTer.getX() - 1, tempTer.getY());
				break;
			case Right:
				toCompareCoord = new Coord(tempTer.getX() + 1, tempTer.getY());
				break;
			}
			if (dom.getDomino().getLeftTile() == tempTer.getDomino().getRightTile()
					&& curCoord.adJacentTo(toCompareCoord)) {
				isMatch = true;
			}
		}
		return isMatch;
	}

	private static boolean isRightMatch(DominoInKingdom dom, Property prop, Kingdom kingdom) {
		boolean isMatch = false;
		// Comparing with all of the dominos in the property
		for (Domino domInProperty : prop.getIncludedDominos()) {
			DominoInKingdom tempTer = null;
			// Getting the DominoInKingdom object with that domino
			for (KingdomTerritory t : kingdom.getTerritories()) {
				if (t instanceof DominoInKingdom) {
					if (((DominoInKingdom) t).getDomino() == domInProperty) {
						tempTer = (DominoInKingdom) t;
						break;
					}
				}
			}
			Coord curCoord = null;
			switch (dom.getDirection()) {
			case Up:
				curCoord = new Coord(dom.getX(), dom.getY() + 1);
				break;
			case Down:
				curCoord = new Coord(dom.getX(), dom.getY() - 1);
				break;
			case Left:
				curCoord = new Coord(dom.getX() - 1, dom.getY());
				break;
			case Right:
				curCoord = new Coord(dom.getX() + 1, dom.getY());
				break;
			}
			Coord toCompareCoord = new Coord(tempTer.getX(), tempTer.getY());
			if (dom.getDomino().getRightTile() == tempTer.getDomino().getLeftTile()
					&& curCoord.adJacentTo(toCompareCoord)) {
				isMatch = true;
			}
			switch (tempTer.getDirection()) {
			case Up:
				toCompareCoord = new Coord(tempTer.getX(), tempTer.getY() + 1);
				break;
			case Down:
				toCompareCoord = new Coord(tempTer.getX(), tempTer.getY() - 1);
				break;
			case Left:
				toCompareCoord = new Coord(tempTer.getX() - 1, tempTer.getY());
				break;
			case Right:
				toCompareCoord = new Coord(tempTer.getX() + 1, tempTer.getY());
				break;
			}
			if (dom.getDomino().getRightTile() == tempTer.getDomino().getRightTile()
					&& curCoord.adJacentTo(toCompareCoord)) {
				isMatch = true;
			}
		}
		return isMatch;
	}

	public static void calculatePropertyAttributes() {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		List<Property> properties = player.getKingdom().getProperties();
		for (Property p : properties) {
			int inc = 0;
			for (Domino d : p.getIncludedDominos()) {
				if (d.getLeftTile() == p.getLeftTile()) {
					p.setCrowns(p.getCrowns() + d.getLeftCrown());
					inc++;
				}
				if (d.getRightTile() == p.getLeftTile()) {
					p.setCrowns(p.getCrowns() + d.getRightCrown());
					inc++;
				}
			}
			p.setSize(inc);
			p.setScore(p.getSize() * p.getCrowns());
		}
	}

	public static void calculateBonusScore() {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		int bonusScore = 0;
		for (BonusOption b : KingdominoApplication.getKingdomino().getCurrentGame().getSelectedBonusOptions()) {

			if (b.getOptionName().equals("Harmony")) {

				bonusScore += calculateHarmony(player);
			}

			if (b.getOptionName().equals("MiddleKingdom")) {

				bonusScore += calculateMiddleKingdom(player);
			}
		}
		player.setBonusScore(bonusScore);
	}

	// if there are 13 terriotories in kingtom => harmony is achieved
	private static int calculateHarmony(Player player) {
		if (player.getKingdom().getTerritories().size() == 13) {
			return 5;
		} else {
			return 0;
		}
	}

	// if the castle is in the middle => middlekingdom is achieved
	private static int calculateMiddleKingdom(Player player) {
		List<KingdomTerritory> territories = player.getKingdom().getTerritories();

		int minX = territories.get(0).getX();
		int minY = territories.get(0).getY();
		int maxX = territories.get(0).getX();
		int maxY = territories.get(0).getY();

		for (KingdomTerritory d : territories) {
			if (d instanceof DominoInKingdom) {
				DominoInKingdom dik = (DominoInKingdom) d;
				int x = dik.getX();
				int y = dik.getY();
				int x2 = x, y2 = y;
				switch (dik.getDirection()) {
				case Up:
					y2 = y + 1;
					x2 = x;
					break;
				case Left:
					x2 = x - 1;
					y2 = y;
					break;
				case Right:
					x2 = x + 1;
					y2 = y;
					break;
				case Down:
					x2 = x;
					y2 = y - 1;
					break;
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
			if (d instanceof Castle) {
				if (d.getX() > maxX) {
					maxX = d.getX();
				}
				if (d.getX() < minX) {
					minX = d.getX();
				}
				if (d.getY() > maxY) {
					maxY = d.getY();
				}
				if (d.getY() < minY) {
					minY = d.getY();
				}
			}
		}
		if (minX == (-1) * maxX && minY == (-1) * maxY) {
			return 10;
		} else {

			return 0;
		}
	}

	public static void calculatePlayerScore() {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		int propscore = 0;
		List<Property> myprop = player.getKingdom().getProperties();
		for (int i = 0; i < myprop.size(); i++) {
			propscore += myprop.get(i).getScore();
			System.out.println(myprop.get(i).getCrowns());
			System.out.println(myprop.get(i).getSize());
			System.out.println(myprop.get(i).getScore());
			System.out.println(propscore);
		}

		player.setPropertyScore(propscore);

	}

	public static void calculateRanking() {
		ArrayList<Player> players = new ArrayList<Player>(
				KingdominoApplication.getKingdomino().getCurrentGame().getPlayers());

		Player currentPlayer;
		Player tempPlayer;
		boolean tied = true;
		for (int i = 0; i < players.size() - 1; i++) {
			currentPlayer = players.get(i);
			for (int j = i + 1; j < players.size(); j++) {
				tempPlayer = players.get(j);
				if (tempPlayer.getTotalScore() == currentPlayer.getTotalScore()) {

					switch (resolveTiebreak(tempPlayer, currentPlayer)) {
					case 1:

						players.set(i, tempPlayer);
						players.set(j, currentPlayer);
						currentPlayer = tempPlayer;
						tied = false;
						break;
					case 2:
						players.set(i, tempPlayer);
						players.set(j, currentPlayer);
						currentPlayer = tempPlayer;
						tied = false;
						break;
					case -1:
						players.set(i, tempPlayer);
						players.set(j, currentPlayer);
						currentPlayer = tempPlayer;
						tied = false;
						break;
					}
				} else if (tempPlayer.getTotalScore() > currentPlayer.getTotalScore()) {
					players.set(i, tempPlayer);
					players.set(j, currentPlayer);
					currentPlayer = tempPlayer;
				}
			}
		}
		int tie = -1;
		for (int i = 1; i < players.size(); i++) {
			if (players.get(i).getTotalScore() == players.get(i - 1).getTotalScore() && tied) {
				tie = i - 1;
			}
		}
		boolean checked = false;
		for (int i = 0; i < players.size(); i++) {

			if (tie == i) {
				players.get(i).setCurrentRanking(i + 1);
				players.get(i + 1).setCurrentRanking(i + 1);
				checked = true;
				i++;
			}
			if (checked) {
				players.get(i).setCurrentRanking(i);
			} else {
				players.get(i).setCurrentRanking(i + 1);
			}

		}

	}

	private static int resolveTiebreak(Player p1, Player p2) {
		if (getLargestPropertySize(p1.getKingdom().getProperties()) > getLargestPropertySize(
				p2.getKingdom().getProperties())) {
			return 1;
		} else if (getLargestPropertySize(p1.getKingdom().getProperties()) < getLargestPropertySize(
				p2.getKingdom().getProperties())) {
			return 2;
		}
		if (getNumberCrowns(p1.getKingdom().getProperties()) > getNumberCrowns(p2.getKingdom().getProperties())) {
			return 1;
		} else if (getNumberCrowns(p1.getKingdom().getProperties()) < getNumberCrowns(
				p2.getKingdom().getProperties())) {
			return 2;
		}
		return 0;
	}

	private static int getLargestPropertySize(List<Property> properties) {
		int largestSize = 0;
		if (properties == null) {
			return 0;
		}
		for (Property p : properties) {
			if (p.getSize() > largestSize) {
				largestSize = p.getSize();
			}
		}
		return largestSize;
	}

	private static int getNumberCrowns(List<Property> properties) {
		int numCrowns = 0;
		for (Property property : properties) {
			numCrowns += property.getCrowns();
		}
		return numCrowns;
	}

}
