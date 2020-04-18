package ca.mcgill.ecse223.kingdomino.controller;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.model.*;
import ca.mcgill.ecse223.kingdomino.model.Domino.DominoStatus;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
import ca.mcgill.ecse223.kingdomino.model.Draft.DraftStatus;
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

	/**
	 * This method starts the game off
	 * 
	 * @param users takes in an array of the usernames
	 * @author Abdallah Shapsough
	 */
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

	/**
	 * This method returns the list of players in the game
	 * 
	 * @return list of players in the game
	 * @author team
	 */
	public static List<Player> getPlayers() {
		return KingdominoApplication.getKingdomino().getCurrentGame().getPlayers();
	}

	/**
	 * Shuffling the pile of dominos so that each game will be unique And the order
	 * of the dominos is unknown
	 * 
	 */
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

	/**
	 * This method allows to set the pile in a specific order (useful primarily for
	 * testing)
	 * 
	 * @param string the order of the pile
	 * @author team
	 */
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

	/**
	 * Creates all of the dominos in the game
	 */
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

	/**
	 * Converting the terrain
	 * 
	 * @param terrain a string
	 * @return the terrain in an enum type
	 */
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

	/**
	 * Creates a new draft to be used for the setNextDraft method
	 * 
	 * @return a new draft
	 */
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

	/**
	 * Sorts the dominos in the draft in order of smallest to largest id
	 */
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

	/**
	 * Sets the next draft as a new draft and Sets the current draft as the old next
	 * draft
	 */
	public static void setNextDraft() {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		// Gets a new draft from the pile
		Draft draft = createNewDraft(); // Current status of the new draft is FaceDown
		// The next draft becomes the current draft
		// and the new draft becomes the next draft
		game.setCurrentDraft(game.getNextDraft());
		game.setNextDraft(draft);
//		if (boardGame != null) {
//			boardGame.updateDrafts(game.getCurrentDraft(), game.getNextDraft());
//		}

	}

	/**
	 * This method sets all of the dominos faceup so that they can be seen by the
	 * users
	 */
	public static void revealNextDraft() {
		// Setting the status to FaceUp
		Draft draft = KingdominoApplication.getKingdomino().getCurrentGame().getNextDraft();
		draft.setDraftStatus(Draft.DraftStatus.FaceUp);
		// System.out.println("before");
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		if (boardGame != null) {
			boardGame.updateDrafts(game.getCurrentDraft(), game.getNextDraft());
			// System.out.println(KingdominoApplication.getGameplay().getGamestatusFullName());
			switch (KingdominoApplication.getGameplay().getGamestatusFullName()) {
			case ("Initializing.SelectingFirstDomino"):
				boardGame.setDominoSelectionEnabled(true);
				break;
			case ("Playing.PlacingDomino"):
				boardGame.setDominoSelectionEnabled(false);
				boardGame.setMovementEnabled(true);
				break;
			}
			boardGame.updateGrid();

		}
	}

	/**
	 * This method generates the initial player order The order is chosen at random
	 * at the beginning of each game
	 * 
	 * @author Victoria Iannotti
	 */
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

	/**
	 * Attempting to select a domino, if successful, creates a new dominoSelection
	 * 
	 * @param selectedDominoID the id of the domino that the current player is
	 *                         trying to select
	 * 
	 * @author team
	 */
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
			switch (KingdominoApplication.getGameplay().getGamestatusFullName()) {
			case ("Initializing.SelectingFirstDomino"):
				boardGame.setDominoSelectionEnabled(false);
				break;
			}
		}
	}

	/**
	 * The method checks if the selection attempt has been successful
	 * 
	 * @return true or false depending on the validity of the selection
	 */
	public static boolean isSelectionValid() {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		if (player.getDominoSelection() != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This method is called when the selection process is complete
	 */
	public static void selectionComplete() {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		if (isSelectionValid()) {
			if (boardGame != null) {
				boardGame.changeButtonColor(player.getColor(), player.getDominoSelection().getDomino().getId());
			}
		}
		if (boardGame != null) {
			switch (KingdominoApplication.getGameplay().getGamestatusFullName()) {
			case ("Initializing.SelectingFirstDomino"):
				boardGame.setDominoSelectionEnabled(true);
				break;
			}

		}
		KingdominoApplication.getGameplay().endOfTurn();
		KingdominoApplication.getGameplay().selectionComplete();

	}

	/**
	 * Changes the turn to the next player When the current player is done their
	 * actions
	 * 
	 * @author team
	 */
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

	/**
	 * Checking if the player is the last in the turn This will instigate a change
	 * in rounds, a new draft...
	 * 
	 * @return true or false depending on where the player lies in the list
	 * @author team
	 */
	public static boolean isCurrentPlayerTheLastInTurn() {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Player nextPlayer = game.getNextPlayer();
		List<Player> players = game.getPlayers();
		for (int i = 0; i < players.size() - 1; i++) {
			if (nextPlayer.equals(players.get(i))) {
				return false;
			}
		}
		if (boardGame != null) {
			switch (KingdominoApplication.getGameplay().getGamestatusFullName()) {
			case ("Initializing.SelectingFirstDomino"):
				boardGame.setPlacementEnabled(true);
				boardGame.setDominoSelectionEnabled(false);
				boardGame.setSelectionEnabled(false);
				boardGame.sendMessage(
						"Place your domino in your kingdom with the controls. \nTo confirm your placement, click <place>. \nIf it is impossible to place, click <discard>.");

				break;
			}
		}
		return true;
	}

	/**
	 * Generate the player order This is needed to set the player order that
	 * corresponds to the selections in the draft
	 * 
	 * @author team
	 */
	public static void generatePlayerOrder() {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Draft draft = game.getCurrentDraft();
		ArrayList<DominoSelection> selections = new ArrayList<DominoSelection>(draft.getSelections());
		Collections.sort(selections, (a, b) -> a.getDomino().getId() - b.getDomino().getId());
		for (int i = 0; i < selections.size(); i++) {
			draft.addOrMoveSelectionAt(selections.get(i), i);
		}

		int i = 0;
		for (DominoSelection selection : selections) {
			game.addOrMovePlayerAt(selection.getPlayer(), i);
			System.out.println(selection.getPlayer().getColor());
			i++;
		}
		game.setNextPlayer(game.getPlayer(0));
		if (boardGame != null) {
			boardGame.notifyCurrentPlayer(game.getNextPlayer());
			removeKing();
			System.out.println(KingdominoApplication.getGameplay().getGamestatusFullName());
			switch (KingdominoApplication.getGameplay().getGamestatusFullName()) {
			case ("Initializing.SelectingFirstDomino"):

				break;
			}
		}

	}

	/**
	 * Creating the Kingdomino for this execution of the app
	 * 
	 * @author team
	 */
	public static void startGameSetup() {
		Kingdomino kingdomino = new Kingdomino();
		KingdominoApplication.setKingdomino(kingdomino);
	}

	/**
	 * This method sets the number of players in the game For this project, only the
	 * 4 player version is implemented
	 * 
	 * @param numPlayers the number of players that there will be in the game
	 */
	public static void setNumberOfPlayers(int numPlayers) {
		Kingdomino kingdomino = KingdominoApplication.getKingdomino();
		Game game = new Game(48, kingdomino);
		kingdomino.setCurrentGame(game);
		game.setNumberOfPlayers(numPlayers);
	}

	/**
	 * Setting the bonus options that will be used in the game
	 * 
	 * @param bonus    the type of bonus
	 * @param selected whether it is selected or not
	 */
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

	/**
	 * Creating the players and the kingdoms that are to be used in the game
	 * 
	 * 
	 */
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

	/**
	 * 
	 * Let's us get a user and set it to a player within a kingdomino game.
	 * 
	 * 
	 * 
	 * @param user       Selected use that will set to a player.
	 * 
	 * @param num        Number of a player that will be set User user.
	 * 
	 * @param kingdomino The kingdomino application we wish to analyze.
	 * 
	 * @return void
	 * 
	 * @author Abdallah Shapsough
	 * 
	 * @gherkin SetGameOptions.feature
	 * 
	 */

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

	/**
	 * This method is used to go from a string with the name of the color to a
	 * PlayerColor enum type
	 * 
	 * @author team
	 */
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

	/**
	 * 
	 * Let's us create a new user that can be used for multiple kingdomino games.
	 * 
	 * 
	 * 
	 * @param userName
	 * 
	 * @param kingdomino
	 * 
	 * @return boolean
	 * 
	 * @author Abdallah Shapsough
	 * 
	 * @gherkin ProvideUserProfile.feature
	 * 
	 */
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

	/**
	 * 
	 * Let's us browse all users in a sorted manner.
	 * 
	 * 
	 * 
	 * @param kingdomino The kingdomino instance that is used.
	 * 
	 * @return List<User> A sorted list of users from a kingdomno instance is
	 * 
	 *         returned.
	 * 
	 * @author Abdallah Shapsough
	 * 
	 * @gherkin ProvideUserProfile.feature
	 * 
	 */
	public static void clearUsers() {
		Kingdomino kingdomino = KingdominoApplication.getKingdomino();
		for (User user : kingdomino.getUsers()) {
			kingdomino.removeUser(user);
		}
	}

	/**
	 * 
	 * Let's us view the amount of games won by a specific user.
	 * 
	 * 
	 * 
	 * @param userName   Specific user we wish to view.
	 * 
	 * @param kingdomino The kingdomino instance that is used.
	 * 
	 * @return int A number of games won by a specific user.
	 * 
	 * @author Abdallah Shapsough
	 * 
	 * @gherkin ProvideUserProfile.feature
	 * 
	 */
	public static List<User> browseAllUsers() {
		Kingdomino kingdomino = KingdominoApplication.getKingdomino();
		ArrayList<User> users = new ArrayList<User>(kingdomino.getUsers());
		Collections.sort(users, (a, b) -> a.getName().compareToIgnoreCase(b.getName()));
		return users;
	}

	/**
	 * 
	 * Let's us view the amount of games played by a specific user.
	 * 
	 * 
	 * 
	 * @param userName   Specific user we wish to view.
	 * 
	 * @param kingdomino The kingdomino instance that is used.
	 * 
	 * @return int A number of played games by a specific user is returned.
	 * 
	 * @author Abdallah Shapsough
	 * 
	 * @gherkin ProvideUserProfile.feature
	 * 
	 */
	public static int getUserGamesWon(String userName) {
		return User.getWithName(userName).getWonGames();
	}

	/**
	 * This is used for the retrieval of stats
	 * 
	 * @param userName takes in the username
	 * @return returns their games played
	 */
	public static int getUserGamesPlayed(String userName) {
		return User.getWithName(userName).getPlayedGames();
	}

	/**
	 * This method is used for browsing dominos
	 * 
	 * @return the list of the dominos ordered
	 */
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

	/**
	 * Browsing dominos of only a particular terrain type
	 * 
	 * @param terrain the terrain type to be filtered
	 * @return a filtered list of dominos
	 */
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

	/**
	 * This method is used for the initial placement of a domino in the kingdom
	 * 
	 */
	public static void removeKing() {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		Domino dom = player.getDominoSelection().getDomino();
		dom.setStatus(DominoStatus.ErroneouslyPreplaced);
		new DominoInKingdom(0, 0, player.getKingdom(), dom); // Placing domino in kingdom

	}

	/**
	 * Move the domino not yet placed according to a particular movement
	 * 
	 * @param movement
	 */
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

	/**
	 * This method reevaluates the status of the domino within the kingdom
	 * 
	 * @param dom takes as input the domino we are looking to evalutate
	 */
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
		if (boardGame != null) {
			boardGame.updateGrid();
		}
	}

	/**
	 * Rotating the domino so that there is a different orientation in the kingdom
	 * 
	 * @param rotation
	 */
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

	/**
	 * 
	 * Takes a CorrectlyPreplaced domino and places it in a player's kingdom.
	 * 
	 * 
	 * 
	 * @param kingdomino The kingdomino application from which we get our current
	 * 
	 *                   game to analyze
	 * 
	 * @return boolean A boolean value which indicates whether the domino has been
	 * 
	 *         successfully added (true) into a kingdom or not (false).
	 * 
	 * @author Mathieu-Joseph Magri
	 * 
	 * @gherkin PlaceDomino.feature
	 * 
	 */

	public static void placeDomino() {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		List<KingdomTerritory> territories = player.getKingdom().getTerritories();
		DominoInKingdom ter = (DominoInKingdom) territories.get(territories.size() - 1);
		Domino dom = ter.getDomino();
		// Placing the domino in the kingdom
		dom.setStatus(DominoStatus.PlacedInKingdom);
		player.setDominoSelection(null);
	}

	/**
	 * This method checks if the domino is correctly preplaced
	 * 
	 * @return true or false depending on the status of the domino
	 * @author team
	 */
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

	/**
	 * This method is called when the action of placing a domino is occuring
	 * 
	 * @author team
	 */
	public static void placing() {
		KingdominoApplication.getGameplay().readyToPlace();
		KingdominoApplication.getGameplay().placeLast();
		KingdominoApplication.getGameplay().endGame();
	}

	/**
	 * 
	 * Verifies if a domino is placed next to a casle
	 * 
	 * 
	 * 
	 * @param x
	 * 
	 * @param y
	 * 
	 * @param aDirection
	 * 
	 * @return
	 * 
	 * @author Zeyang Xu
	 * 
	 * @gherkin VerifyCastleAdjacency.feature
	 * 
	 */

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

	/**
	 * 
	 * Verifies if a domino is adjacent to its neighbor to make sure a domino can be
	 * 
	 * placed on that location on the grid
	 * 
	 * 
	 * 
	 * @param aKingdom
	 * 
	 * @param aDomino
	 * 
	 * @param x
	 * 
	 * @param y
	 * 
	 * @param aDirection
	 * 
	 * @return
	 * 
	 * @author Zeyang Xu
	 * 
	 * @gherkin VerifyNeightborAdjacency.feature
	 * 
	 */

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

	/**
	 * 
	 * Verifies that when a domino is on the board, the domino does not overlap
	 * 
	 * another domino or castle
	 * 
	 * 
	 * 
	 * @param aDomino
	 * 
	 * @param aKingdom
	 * 
	 * @param x
	 * 
	 * @param y
	 * 
	 * @param aDirection
	 * 
	 * @return
	 * 
	 * @author Zeyang Xu
	 * 
	 * @gherkin VerifyNoOverlapping.feature
	 * 
	 */

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

	/**
	 * 
	 * Verifies that the domino placed is within the size of the grid where the
	 * 
	 * dominos on the territory can form of up to 5x5 grid size
	 * 
	 * 
	 * 
	 * @param aKingdom
	 * 
	 * @return
	 * 
	 * @author Zeyang Xu
	 * 
	 * @gherkin VerifyGridSize.feature
	 * 
	 */

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

	/**
	 * 
	 * If a player can't place a domino in a his own domino in any maner possible,
	 * 
	 * that domino gets discarded from the game.
	 * 
	 * 
	 * 
	 * @param kingdomino The kingdomino application from which we get our current
	 * 
	 *                   game to analyze.
	 * 
	 * @return boolean A boolean value which indicates whether the domino has been
	 * 
	 *         successfully discarded (true) from a kingdom or not (false).
	 * 
	 * @author Mathieu-Joseph Magri
	 * 
	 * @gherkin DiscardDomino.feature
	 * 
	 */

	public static void discardDomino() {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		if (isPossibleToPlace()) {
			player.getDominoSelection().getDomino().setStatus(DominoStatus.ErroneouslyPreplaced);
		} else {
			player.getDominoSelection().getDomino().setStatus(DominoStatus.Discarded);
		}
	}

	/**
	 * This method is called when the action of discarding a domino is being
	 * executed
	 * 
	 * @author team
	 */
	public static void discarding() {
		KingdominoApplication.getGameplay().discardLast();
		KingdominoApplication.getGameplay().discard();
		KingdominoApplication.getGameplay().endGame();
	}

	/**
	 * 
	 * Verifies whether or not a domino can still be placed in a player's kingdom.
	 * 
	 * 
	 * 
	 * @param kingdom The kingdomino application from which we get our current game
	 * 
	 *                to analyze.
	 * 
	 * @return boolean A boolean value which indicates whether the domino can still
	 * 
	 *         be placed within a player's kingdom or not.
	 * 
	 * @author Mathieu-Joseph Magri
	 * 
	 * @gherkin DiscardDomino.feature
	 * 
	 */

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

	/**
	 * This method is checking if we have reached the end of the game
	 * 
	 * @return true or false depending on the state of the game
	 * 
	 * @author team
	 */
	public static boolean isCurrentTurnTheLastInGame() {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		if (game.getTopDominoInPile() == null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This method is used to update the score the player in question
	 * 
	 * @author team
	 */
	public static void updateScore() {
		KingdominoController.identifyProperties();
		KingdominoController.calculatePropertyAttributes();
		KingdominoController.calculateBonusScore();
		KingdominoController.calculatePlayerScore();
	}

	/**
	 * 
	 * @gherkin IdentifyProperties.feature
	 * 
	 * @author kaichengwu
	 * 
	 * @param kingdomino
	 * 
	 * @return void this method identifies the kingdom properties
	 * 
	 */
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
				// System.out.println(ter.getDomino().getStatus());
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

	// Helper method
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

	// Helper method
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

	// Helper method
	private static boolean propertyContains(DominoInKingdom dom, Property prop) {
		for (Domino domInProperty : prop.getIncludedDominos()) {
			if (dom.getDomino() == domInProperty) {
				return true;
			}
		}
		return false;
	}

	// Helper method
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

	// Helper method
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

	/**
	 * 
	 * this methods calculate the attributes of each property (num of crowns & size
	 * 
	 * of property), and sets them up for furthur use
	 * 
	 * 
	 * 
	 * @author kaichengwu
	 * 
	 * @gherkin CalculatePropertyAttributes.feature
	 * 
	 * @param kingdomino
	 * 
	 * @return void
	 * 
	 */
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

	/**
	 * 
	 * this feature calculates the bonus score of the player if the bonus feature
	 * 
	 * harmony/middle kingdom were selected at the beginning of the game
	 * 
	 * 
	 * 
	 * @param kingdomino
	 * 
	 * @return void
	 * 
	 * @author kaichengwu
	 * 
	 * @gherkin CalculateBonusScores.feature
	 * 
	 */

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

	/**
	 * 
	 * @author kaichengwu
	 * 
	 * @param kingdomino
	 * 
	 * @return void this features calculate the total score of the player by summing
	 * 
	 *         up the score of each individual property =======
	 * 
	 * @return void
	 * 
	 * @gherkin CalculatePlayerScore.feature this features calculate the total score
	 * 
	 *          of the player by summing up the score of each individual property
	 * 
	 * 
	 */
	public static void calculatePlayerScore() {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		int propscore = 0;
		List<Property> myprop = player.getKingdom().getProperties();
		for (int i = 0; i < myprop.size(); i++) {
			propscore += myprop.get(i).getScore();
//			System.out.println(myprop.get(i).getCrowns());
//			System.out.println(myprop.get(i).getSize());
//			System.out.println(myprop.get(i).getScore());
//			System.out.println(propscore);
		}

		player.setPropertyScore(propscore);

	}

	/**
	 * 
	 * Calculating the ranking of the players in the kingdomino game
	 * 
	 * @param kingdomino
	 * 
	 * @author Victoria Iannotti
	 * 
	 */
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

	/**
	 * 
	 * Method for resolving ties between players
	 * 
	 * @param p1 first player
	 * 
	 * @param p2 second player
	 * 
	 * @return int depending on the outcome of the resolution
	 * 
	 */
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

	// Helper method
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

	// Helper method
	private static int getNumberCrowns(List<Property> properties) {
		int numCrowns = 0;
		for (Property property : properties) {
			numCrowns += property.getCrowns();
		}
		return numCrowns;
	}

	/**
	 * This method is used for loading the game
	 * 
	 * @param kingdomino
	 * @param string
	 */
	public static void load(Kingdomino kingdomino, String string) {
		File file = new File(string);
		try {
			Scanner reader = new Scanner(file);
			String data = reader.nextLine();
			// System.out.print(data);

			Game game = new Game(48, kingdomino);
			kingdomino.setCurrentGame(game);
			setNumberOfPlayers(4);
			createPlayersAndKingdoms();
//			for (int i = 0; i < 4; i++) {
//				game.getPlayer(0).setColor(PlayerColor.values()[i]);
//			}
			List<Player> players = kingdomino.getCurrentGame().getPlayers();
			for (int i = 0; i < players.size(); i++) {
				Player player = players.get(i);
				User user = new User("P" + (i + 1), kingdomino);
				player.setUser(user);
				player.setColor(PlayerColor.values()[i]);
				game.setNextPlayer(player);
			}
			createAllDominos();
			KingdominoApplication.setKingdomino(kingdomino);

			ArrayList<DominoSelection> sels = new ArrayList<DominoSelection>();
			Draft draft = new Draft(DraftStatus.FaceUp, game);
			String[] ids = data.split(" ");
			for (int i = 1; i < ids.length; i++) {
				ids[i].replaceAll("[^0-9]", "");
				ids[i].trim();
				String id = ids[i];
				if (ids[i].charAt(ids[i].length() - 1) == ',') {
					id = ids[i].substring(0, ids[i].length() - 1);
				}
				draft.addIdSortedDomino(getDominoByID(Integer.parseInt(id)));
				DominoSelection s = new DominoSelection(players.get(i - 1), getDominoByID(Integer.parseInt(id)), draft);
				sels.add(s);
				// draft.addSelection(s);
			}
			game.setCurrentDraft(draft);
			game.setNextDraft(draft);

//			for (int i = 0; i < sels.size(); i++) {
//				players.get(i).setDominoSelection(sels.get(i));
//			}

			data = reader.nextLine();
			ids = data.split(" ");
			String id = ids[1];
			if (ids[1].charAt(ids[1].length() - 1) == ',') {
				id = ids[1].substring(0, ids[1].length() - 1);
			}
			game.setTopDominoInPile(getDominoByID(Integer.parseInt(id)));

			draft = new Draft(DraftStatus.FaceUp, game);
			for (int i = 1; i < ids.length; i++) {
				ids[i].replaceAll("[^0-9]", "");
				ids[i].trim();
				String id2 = ids[i];
				if (ids[i].charAt(ids[i].length() - 1) == ',') {
					id2 = ids[i].substring(0, ids[i].length() - 1);
				}
				draft.addIdSortedDomino(getDominoByID(Integer.parseInt(id)));
			}
			game.setNextDraft(draft);
			game.setCurrentDraft(draft);
			// System.out.println(draft.getIdSortedDominos());
			int playerCounter = 0;
			while (reader.hasNextLine()) {
				data = reader.nextLine();
				String[] elements = data.split(" ");
				for (int i = 1; i < elements.length; i++) {
					addLoadDomino(elements[i], players.get(playerCounter), kingdomino);
				}
				playerCounter++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Helper method for loading
	 * 
	 * @param kingdomino The kingdomino instance that is used
	 * @return boolean if ready
	 */
	public static boolean isReady(Kingdomino kingdomino) {
		return (kingdomino.getCurrentGame() != null);
	}

	// Helper method
	public static boolean isValidGame(Kingdomino kingdomino) {
		return false;
	}

	// Helper method
	private static void addLoadDomino(String element, Player player, Kingdomino kingdomino) {
		String[] sections = element.split("@");
		Domino domino = KingdominoApplication.getKingdomino().getCurrentGame()
				.getAllDomino(Integer.parseInt(sections[0]) - 1);
		String[] elements = sections[1].substring(1, sections[1].length()).split(",");
		int x = Integer.parseInt(elements[0]);
		int y = Integer.parseInt(elements[1]);
		DirectionKind dir = null;
		elements[2] = elements[2].substring(0, 1);
		switch (elements[2]) {

		case "R":
			dir = DirectionKind.Right;
			break;
		case "L":
			dir = DirectionKind.Left;
			break;
		case "U":
			dir = DirectionKind.Up;
			break;
		case "D":
			dir = DirectionKind.Down;
			break;
		}
		Kingdom kingdom = player.getKingdom();
		if (x < 5) {
			DominoInKingdom domInKingdom = new DominoInKingdom(x, y, kingdom, domino);
			domInKingdom.setDirection(dir);
			domino.setStatus(DominoStatus.PlacedInKingdom);

		}
	}

	/**
	 * Helper method for getting a domino with a particular id
	 * 
	 * @param id the id of the domino we want
	 * @return the domino in question
	 */
	public static Domino getDominoByID(int id) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		if (!game.hasAllDominos()) {
			createAllDominosLoading();
		}
		ArrayList<Domino> allDominos = new ArrayList<Domino>(game.getAllDominos());
		Collections.sort(allDominos, (a, b) -> a.getId() - b.getId());
		return allDominos.get(id - 1);
	}

	// Helper method
	private static void createAllDominosLoading() {
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

	/**
	 * This method is used for saving the game
	 * 
	 * @param kingdomino
	 * @param string
	 */
	public static void save(Kingdomino kingdomino, String string) {
		File file = new File(string);
		if (file.exists()) {
			overWriteFile(string);
			helpsavegame(file, kingdomino);

		} else {
			helpsavegame(file, kingdomino);
		}

	}

	/**
	 * Helper method that save the game to a file if needed
	 * 
	 * @param file
	 * @param kingdomino
	 */
	public static void helpsavegame(File file, Kingdomino kingdomino) {
		try {
			file.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			String currentsel = "C: ";
			List<Player> players = kingdomino.getCurrentGame().getPlayers();
			for (Player i : players) {
				currentsel = currentsel + i.getDominoSelection().getDomino().getId() + ", ";
			}
			currentsel = currentsel.substring(0, currentsel.length() - 2);
			out.write(currentsel);
			out.newLine();
//			String unused = "U: ";
//			List<Domino> dominos = kingdomino.getCurrentGame().getCurrentDraft().getIdSortedDominos();
//			for (int i = 0; i<dominos.size();i++) {
//				unused = unused+dominos.get(i).getId() + ", ";
//			}
//			
//			unused = unused.substring(0, unused.length()-2);
//			out.write(unused);
//			out.newLine();

			for (Player i : players) {
				String playerdominos = i.getUser().getName() + ": ";
				for (KingdomTerritory kt : i.getKingdom().getTerritories()) {
					if (!kt.getClass().equals(Castle.class)) {
						DominoInKingdom dik = (DominoInKingdom) kt;
						playerdominos = playerdominos + dik.getDomino().getId() + "@(" + kt.getX() + "," + kt.getY()
								+ "," + dik.getDirection().toString().substring(0, 1) + ")" + ", ";
					} else {
						continue;
					}
				}
				playerdominos = playerdominos.substring(0, playerdominos.length() - 2);
				out.write(playerdominos);
				out.newLine();
			}

			out.flush();
			out.close();
		}

		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Helper method that overwrites a file if needed
	 * 
	 * @param string
	 */
	public static void overWriteFile(String string) {
		File file = new File(string);
		file.delete();
		File newFile = new File(string);
		try {
			newFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
