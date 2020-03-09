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

	/*****************
	 * * Feature 1 * *
	 *****************/
	// {Set game options}
	// As a player, I want to configure the designated options of the Kingdomino
	// game including the number of players (2, 3 or 4) and the bonus scoring
	// options

	public static void setGameOptions(Kingdomino kingdomino) {
		Game game = new Game(48, kingdomino);
		kingdomino.setCurrentGame(game);

	}

	public static void setNumberOfPlayers(int numPlayers, Kingdomino kingdomino) {
		Game game = kingdomino.getCurrentGame();
		game.setNumberOfPlayers(numPlayers);
		for (int i = 0; i < numPlayers; i++) {
			Player player = new Player(game);
		}
	}

	public static void setBonusOption(String bonus, Kingdomino kingdomino, boolean selected) {
		Game game = kingdomino.getCurrentGame();
		BonusOption bonusOption = new BonusOption(bonus, kingdomino);
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

	public static void selectUser(User user, int num, Kingdomino kingdomino) {
		kingdomino.getCurrentGame().getPlayer(num).setUser(user);
	}

	public static void selectColor(PlayerColor color, int num, Kingdomino kingdomino) {
		kingdomino.getCurrentGame().getPlayer(num).setColor(color);
	}

	/*****************
	 * * Feature 2 * *
	 *****************/
	// {Provide user profile}
	// As a player, I wish to use my unique user name in when a game starts. I also
	// want the Kingdomino app to maintain my game statistics (e.g. number of games
	// played, won, etc.).

	public static boolean createNewUser(String userName, Kingdomino kingdomino) {
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

	public static void clearUsers(Kingdomino kingdomino) {
		for (User user : kingdomino.getUsers()) {
			kingdomino.removeUser(user);
		}
	}

	public static List<User> browseAllUsers(Kingdomino kingdomino) {
		ArrayList<User> users = new ArrayList<User>(kingdomino.getUsers());
		Collections.sort(users, (a, b) -> a.getName().compareToIgnoreCase(b.getName()));
		return users;
	}

	public static int getUserGamesWon(String userName, Kingdomino kingdomino) {
		return User.getWithName(userName).getWonGames();
	}

	public static int getUserGamesPlayed(String userName, Kingdomino kingdomino) {
		return User.getWithName(userName).getPlayedGames();
	}

	/*****************
	 * * Feature 3 * *
	 *****************/

	// {Start a new game}
	// As a Kingdomino player, I want to start a new game of Kingdomino against some
	// opponents with my castle placed on my territory with the current settings of
	// the game. The initial order of player should be randomly determined.

	public static void startNewGame(Kingdomino kingdomino) {
		// TODO Randomly order the players
		List<Player> players = kingdomino.getCurrentGame().getPlayers();
		for (int i = 0; i < players.size(); i++) {
			Player player = players.get(i);
			Kingdom kingdom = new Kingdom(player);
			new Castle(0, 0, kingdom, player);
			player.setBonusScore(0);
			player.setPropertyScore(0);
			player.setDominoSelection(null);
		}
		if (kingdomino.getCurrentGame().getAllDominos().size() == 0) {
			createAllDominos(kingdomino.getCurrentGame());
		}
		shuffleDominos(kingdomino);
	}

	public static void createAllDominos(Game game) {
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

	/*****************
	 * * Feature 4 * *
	 *****************/

	// {Browse domino pile}
	// As a player, I wish to browse the set of all dominos in increasing order of
	// numbers prior to playing the game so that I can adjust my strategy

	public static ArrayList<Domino> browseDominoPile(Kingdomino kingdomino) {
		if (kingdomino.getCurrentGame().getAllDominos().size() == 0) {
			createAllDominos(kingdomino.getCurrentGame());
		}
		ArrayList<Domino> allDominos = new ArrayList<Domino>(kingdomino.getCurrentGame().getAllDominos());
		Collections.sort(allDominos, (a, b) -> a.getId() - b.getId());
		return allDominos;
	}

	public static Domino getDomino(int id, Kingdomino kingdomino) {
		ArrayList<Domino> allDominos = new ArrayList<Domino>(kingdomino.getCurrentGame().getAllDominos());
		Collections.sort(allDominos, (a, b) -> a.getId() - b.getId());
		return allDominos.get(id - 1);
	}

	public static List<Domino> browseFilteredDominos(String terrain, Kingdomino kingdomino) {
		ArrayList<Domino> allDominos = new ArrayList<Domino>(kingdomino.getCurrentGame().getAllDominos());
		Collections.sort(allDominos, (a, b) -> a.getId() - b.getId());
		List<Domino> filteredList = (allDominos.stream()
				.filter(domino -> domino.getLeftTile().equals(getTerrainTypeFilter(terrain))
						|| domino.getRightTile().equals(getTerrainTypeFilter(terrain)))
				.collect(Collectors.toList()));
		return filteredList;
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

	/*****************
	 * * Feature 5 * *
	 *****************/

	// {Shuffle domino pile}
	// As a player, I want to have a randomly shuffled pile of dominos so that every
	// game becomes unique

	public static void shuffleDominos(Kingdomino kingdomino) {
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

		for (int i = 0; i < dominos.size(); i++) {
			game.addOrMoveAllDominoAt(dominos.get(i), i);
		}
		setFirstDraft(kingdomino);
	}

	public static Draft setFirstDraft(Kingdomino kingdomino) {
		Game game = kingdomino.getCurrentGame();
		List<Domino> dominos = new ArrayList<Domino>(game.getAllDominos());
		Draft draft = new Draft(Draft.DraftStatus.FaceDown, game);
		for (int i = 0; i < 4; i++) {
			draft.addIdSortedDomino(dominos.get(i));
			dominos.get(i).delete();
		}
		game.setCurrentDraft(null);
		game.setNextDraft(draft);
		game.setTopDominoInPile(game.getAllDomino(0));
		return draft;
	}

	public static void getFixedOrder(Kingdomino kingdomino, String string) {
		Game game = kingdomino.getCurrentGame();
		List<Domino> dominos = new ArrayList<Domino>(game.getAllDominos());
		string = string.replaceAll("\\s+", "");
		string = string.replace("\"", "");
		List<String> numbers = new ArrayList<String>(Arrays.asList(string.split(",")));

		for (int i = 0; i < dominos.size(); i++) {
			game.addOrMoveAllDominoAt(dominos.get(Integer.parseInt(numbers.get(i)) - 1), i);
		}
		setFirstDraft(kingdomino);
	}

	/*****************
	 * * Feature 6 * *
	 *****************/

	// {Load game}
	// As a player, I want to load a previously played game so that I can continue
	// it from the last position

	/*****************
	 * * Feature 7 * *
	 *****************/

	// {Save game}
	// As a player, I want to save the current game if the game has not yet been
	// finished so that I can continue it later

	/*****************
	 * * Feature 8 * *
	 *****************/

	// {Reveal next draft of dominos}
	// As a player, I want the Kingdomino app to automatically reveal the next four
	// dominos once the previous round is finished

	public static void createNextDraft(Kingdomino kingdomino) {

		Game game = kingdomino.getCurrentGame();
		List<Domino> dominos = new ArrayList<Domino>(game.getAllDominos());
		if (game.getAllDominos().size() > 0) {

			Draft draft = new Draft(Draft.DraftStatus.FaceDown, game);
			for (int i = 0; i < 4; i++) {
				draft.addIdSortedDomino(dominos.get(i));
				dominos.get(i).delete();
			}
			game.setCurrentDraft(game.getNextDraft());
			game.setNextDraft(draft);

			if (game.getAllDominos().size() > 0) {
				game.setTopDominoInPile(game.getAllDomino(0));
			} else {
				game.setTopDominoInPile(null);
			}

		} else {
			game.setCurrentDraft(game.getNextDraft());
			game.setNextDraft(null);
			game.setTopDominoInPile(null);
		}

	}

	public static void revealNextDraft(Kingdomino kingdomino) {
		Draft draft = kingdomino.getCurrentGame().getNextDraft();
		draft.setDraftStatus(Draft.DraftStatus.FaceUp);
	}

	/*****************
	 * * Feature 9 * *
	 *****************/

	// {Order next draft of dominos}
	// As a player, I want the Kingdomino app to automatically order the revealed
	// next draft of dominos in increasing order with respect to their numbers so
	// that I know which are the more valuable dominos

	public static void orderNextDraft(Kingdomino kingdomino) {
		Draft draft = kingdomino.getCurrentGame().getNextDraft();
		List<Domino> draftDominos = new ArrayList<Domino>(
				kingdomino.getCurrentGame().getNextDraft().getIdSortedDominos());
		Collections.sort(draftDominos, (a, b) -> a.getId() - b.getId());
		for (int i = 0; i < draftDominos.size(); i++) {
			draft.addOrMoveIdSortedDominoAt(draftDominos.get(i), i);
		}
		draft.setDraftStatus(Draft.DraftStatus.Sorted);
	}

	/******************
	 * * Feature 10 * *
	 ******************/

	// {Choose next domino}
	// As a player, I wish to be able to choose a designated domino from the next
	// draft assuming that this domino has not yet been chosen by any other players

	public static boolean ChooseNextDomino(Player curPlayer, Kingdomino kingdomino, int chosen) {
		Game game = kingdomino.getCurrentGame();
		Draft draft = game.getNextDraft();
		for (int i = 0; i < draft.getIdSortedDominos().size(); i++) {
			if (!draft.getIdSortedDomino(i).hasDominoSelection() && (chosen == draft.getIdSortedDomino(i).getId())) {
				DominoSelection selection = new DominoSelection(curPlayer, draft.getIdSortedDomino(i), draft);
				draft.addSelection(selection);
				curPlayer.setDominoSelection(selection);
				return true;
			}
		}
		return false;
	}

	/******************
	 * * Feature 11 * *
	 ******************/

	// {Move current domino}
	// As a player, I wish to evaluate a provisional placement of my current domino
	// by moving the domino around into my kingdom (up, down, left, right)

	public static void removeKing(Kingdomino kingdomino) {
		Player player = kingdomino.getCurrentGame().getNextPlayer();
		Domino dom = player.getDominoSelection().getDomino();
		dom.setStatus(DominoStatus.ErroneouslyPreplaced);
		DominoInKingdom domIn = new DominoInKingdom(0, 0, player.getKingdom(), dom);
		domIn.setDirection(DirectionKind.Right);
		player.getKingdom().addTerritory(domIn);
	}

	public static void moveDomino(Kingdomino kingdomino, String movement) {
		Player player = kingdomino.getCurrentGame().getNextPlayer();
		DominoInKingdom dom = (DominoInKingdom) player.getKingdom()
				.getTerritory(player.getKingdom().numberOfTerritories() - 1);
		int x = dom.getX();
		int y = dom.getY();
		String dir = null;
		if (verifyGridSize(player.getKingdom())) {
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
			if (!verifyGridSize(player.getKingdom())) {
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
		resetDominoStatus(dom, kingdomino);
	}

	private static void resetDominoStatus(DominoInKingdom dom, Kingdomino kingdomino) {
		Player player = kingdomino.getCurrentGame().getNextPlayer();
		boolean castleAdjacency = verifyCastleAdjacency(dom.getX(), dom.getY(), dom.getDirection());
		boolean neighborAdjacency = verifyNeighborAdjacency(player.getKingdom(), dom.getDomino(), dom.getX(),
				dom.getY(), dom.getDirection());
		boolean noOverlapping = verifyNoOverlapping(dom.getDomino(), player.getKingdom(), dom.getX(), dom.getY(),
				dom.getDirection());
		if ((castleAdjacency || neighborAdjacency) && noOverlapping) {
			dom.getDomino().setStatus(DominoStatus.CorrectlyPreplaced);
		} else {
			dom.getDomino().setStatus(DominoStatus.ErroneouslyPreplaced);
		}
	}

	/******************
	 * * Feature 12 * *
	 ******************/

	// {Rotate current domino}
	// As a player, I wish to evaluate a provisional placement of my current domino
	// in my kingdom by rotating it (clockwise or counter-clockwise)

	public static void rotateDomino(Kingdomino kingdomino, String rotation) {
		Player player = kingdomino.getCurrentGame().getNextPlayer();
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
		resetDominoStatus(ter, kingdomino);
	}

	/******************
	 * * Feature 13 * *
	 ******************/

	// {Place domino}
	// As a player, I wish to place my selected domino to my kingdom. If I am
	// satisfied with its placement, and its current position respects the adjacency
	// rules, I wish to finalize the placement

	public static boolean placeDomino(Kingdomino kingdomino) {
		Player player = kingdomino.getCurrentGame().getNextPlayer();
		List<KingdomTerritory> territories = player.getKingdom().getTerritories();
		DominoInKingdom ter = (DominoInKingdom) territories.get(territories.size() - 1);
		Domino dom = ter.getDomino();
		if (dom.getStatus() == DominoStatus.CorrectlyPreplaced) {
			dom.setStatus(DominoStatus.PlacedInKingdom);
			// Set the next player here
			return true;
		}
		return false;
	}

	/******************
	 * * Feature 14 * *
	 ******************/

	// {Verify castle adjacency}
	// As a player, I want the Kingdomino app to automatically check if my current
	// domino is placed next to my castle

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

	/******************
	 * * Feature 15 * *
	 ******************/

	// {Verify neighbor adjacency}
	// As a player, I want the Kingdomino app to automatically check if my current
	// domino is placed to an adjacent territory

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

	/******************
	 * * Feature 16 * *
	 ******************/

	// {Verify no overlapping}
	// As a player, I want the Kingdomino app to automatically check that my current
	// domino is not overlapping with existing dominos

	public static boolean verifyNoOverlapping(Domino aDomino, Kingdom aKingdom, int x, int y,
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
		}

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
		coord tileOne = new coord(x, y);
		coord tileTwo = new coord(x1, y1);
		int index = 0;
		for (KingdomTerritory d : aKingdom.getTerritories()) {
			coord temp = new coord(d.getX(), d.getY());
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
					coord leftcoord, rightcoord;
					leftcoord = new coord(dik.getX(), dik.getY());
					rightcoord = new coord(x2, y2);

					boolean leftOne = ((leftcoord.x == tileOne.x) && (leftcoord.y == tileOne.y));
					boolean leftTwo = ((leftcoord.x == tileTwo.x) && (leftcoord.y == tileTwo.y));
					boolean rightOne = ((rightcoord.x == tileOne.x) && (rightcoord.y == tileOne.y));
					boolean rightTwo = ((rightcoord.x == tileTwo.x) && (rightcoord.y == tileTwo.y));

					if (leftOne || leftTwo || rightOne || rightTwo) {
						return false;
					}
				}
			}
			index++;
		}
		return true;
	}

	/******************
	 * * Feature 17 * *
	 ******************/

	// {Verify kingdom grid size}
	// As a player, I want the Kingdomino app to automatically check if the grid of
	// my kingdom has not yet exceeded a square of 5x5 tiles (including my castle)

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
		return ((maxX - minX) < 5 && (maxY - minY) < 5);
	}

	/******************
	 * * Feature 18 * *
	 ******************/

	// {Discard domino}
	// As a player, I wish to discard a domino if it cannot be placed to my kingdom
	// in a valid way

	public static boolean discardDomino(Kingdomino kingdomino) {
		Player player = kingdomino.getCurrentGame().getNextPlayer();
		if (canStillPlay(player.getKingdom())) {
			player.getDominoSelection().getDomino().setStatus(DominoStatus.ErroneouslyPreplaced);
			return false;
		} else {
			player.getDominoSelection().getDomino().setStatus(DominoStatus.Discarded);
			return true;
		}
	}

	public static boolean canStillPlay(Kingdom kingdom) {
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
					neighborAdjacency = verifyNeighborAdjacency(kingdom, dom.getDomino(), dom.getX(), dom.getY(), dir);
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
		return false;
	}

	/******************
	 * * Feature 19 * *
	 ******************/

	// {Identify kingdom properties}
	// As a player, I want the Kingdomino app to automatically determine each
	// properties of my kingdom so that my score can be calculated

	public static void identifyProperties(Kingdomino kingdomino) {
		
		Player player = kingdomino.getCurrentGame().getNextPlayer();
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
		checkForConnected(player.getKingdom().getProperties(), player.getKingdom());
		List<Property> props = player.getKingdom().getProperties();
		for(int i = 0; i< props.size(); i++) {
			if(props.get(i).getLeftTile() == null) {
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

	private static String getDominos(Property property) {
		List<Domino> dominos = property.getIncludedDominos();
		String doms = "";
		for (Domino dominoInProp : dominos) {
			if (!doms.equals("")) {
				doms += ',';
			}
			doms += dominoInProp.getId();
		}
		return doms;
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
		TerrainType type = prop.getLeftTile();
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
		TerrainType type = prop.getLeftTile();
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

//	private static void checkForConnected(List<Property> properties, Kingdom k) {
//		ArrayList<Property> propsOfType = new ArrayList<Property>();
//		for (TerrainType type : TerrainType.values()) {
//			for (Property prop : properties) {
//				if (prop.getLeftTile() == type) {
//					propsOfType.add(prop);
//				}
//			}
//			if (propsOfType.size() > 1) {
//				handleDuplicates(propsOfType, k);
//			}
//			if (!propsOfType.isEmpty()) {
//				propsOfType.clear();
//			}
//		}
//	}

//	private static void handleDuplicates(List<Property> propsOfType, Kingdom k) {
//		boolean duplicate = false;
//		Kingdom kingdom = k;
//		ArrayList<Property> toDelete = new ArrayList<Property>();
//		do {
//			duplicate = false;
//			for (int i = 0; i < propsOfType.size() - 1; i++) {
//				for (int j = i; j < propsOfType.size(); j++) {
//					if (matchingDomino(propsOfType.get(i), propsOfType.get(j))) {
//						System.out.println("GAFGAFGADGDFGS");
//						System.out.println(propsOfType.get(i).getLeftTile());
//						kingdom = propsOfType.get(i).getKingdom();
//						Property newProp = combineProperties(propsOfType.get(i), propsOfType.get(j));
//						toDelete.add(propsOfType.get(i));
//						toDelete.add(propsOfType.get(j));
//						propsOfType.get(i).delete();
//						propsOfType.get(j).delete();
//						kingdom.addProperty(newProp);
//						duplicate = true;
//					}
//				}
//				for (Property p : toDelete) {
//					deleteProperty(kingdom, p);
//				}
//			}
//		} while (duplicate);
//
//	}

//	private static void deleteProperty(Kingdom kingdom, Property p) {
//		if (kingdom.hasProperties()) {
//			for (Property prop : kingdom.getProperties()) {
//				if (p.getIncludedDominos() == prop.getIncludedDominos()) {
//					prop.delete();
//					break;
//				}
//			}
//		}
//	}
//
//	private static boolean matchingDomino(Property p1, Property p2) {
//		ArrayList<Integer> ids = new ArrayList<Integer>();
//		for (Domino dom : p1.getIncludedDominos()) {
//			ids.add(dom.getId());
//		}
//		for (Domino dom : p2.getIncludedDominos()) {
//			if (ids.contains(dom.getId())) {
//				return true;
//			}
//		}
//		return false;
//	}
////
//	private static Property combineProperties(Property p1, Property p2) {
//		ArrayList<Domino> dominos = new ArrayList<Domino>();
//		;
//		Property p = new Property(p1.getKingdom());
//		for (Domino dom : p1.getIncludedDominos()) {
//			dominos.add(dom);
//			p.addIncludedDomino(dom);
//		}
//		for (Domino dom : p2.getIncludedDominos()) {
//			if (!dominos.contains(dom)) {
//				dominos.add(dom);
//				p.addIncludedDomino(dom);
//			}
//		}
//		p.setLeftTile(p1.getLeftTile());
//		return p;
//	}
	/******************
	 * * Feature 20 * *
	 ******************/

	// {Calculate property score}
	// As a player, I want the Kingdomino app to automatically calculate the score
	// for each of my property based upon the size of that property and the number
	// of crowns

	public static void calculatePropertyAttributes(Kingdomino kingdomino) {
		Player player = kingdomino.getCurrentGame().getNextPlayer();
		List<Property> properties = player.getKingdom().getProperties();
		for (Property p : properties) {
			int inc = 0;
			for (Domino d : p.getIncludedDominos()) {
				if(d.getLeftTile()==p.getLeftTile()) {
					p.setCrowns(p.getCrowns() + d.getLeftCrown());
					inc ++;
				}
				if(d.getRightTile()==p.getLeftTile()) {
					p.setCrowns(p.getCrowns() + d.getRightCrown());
					inc++;
				}
			}
			p.setSize(inc);
		}
	}

	/******************
	 * * Feature 21 * *
	 ******************/

	// { Calculate bonus scores}
	// As a player, I want the Kingdomino app to automatically calculate the bonus
	// scores (for Harmony and middle Kingdom) if those bonus scores were selected
	// as a game option

	public static void calculateBonusScore(Kingdomino kingdomino) {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		int bonusScore = 0;
		for (BonusOption b : kingdomino.getCurrentGame().getSelectedBonusOptions()) {
			if (b.getOptionName().equals("Harmony")) {
				
				bonusScore += calculateHarmony(player);
			}
			if (b.getOptionName().equals("MiddleKingdom")) {
				
				bonusScore += calculateMiddleKingdom(player);
			}
		}
		player.setBonusScore(bonusScore);
	}

	private static int calculateHarmony(Player player) {
		if (player.getKingdom().getTerritories().size() == 13) {
			return 5;
		} else {
			return 0;
		}
	}

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

	/******************
	 * * Feature 22 * *
	 ******************/

	// {Calculate player score}
	// As a player, I want the Kingdomino app to automatically calculate the score
	// for each player by summing up their property scores and their bonus scores
	
	public static void calculatePlayerScore(Kingdomino kingdomino, Player s) {
		//identifyProperties(kingdomino);
		//calculatePropertyAttributes(kingdomino);
		//Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		Player player = kingdomino.getCurrentGame().getNextPlayer();
		int playerScore = 0; 
		List<Property> props = player.getKingdom().getProperties();
		
		System.out.println(player.getKingdom().getProperty(0));
		System.out.println(props.size());
//		System.out.println(props.get(1).getCrowns());
//		System.out.println(props.get(2).getCrowns());
		
		//System.out.println();
		for(Property p : props) {
			
			playerScore += (p.getKingdom().getTerritories().size()) * (p.getCrowns());
			System.out.println(playerScore);
		}
//		for(Property p : props) {
//			playerScore += (p.getKingdom().getTerritories().size()) * (p.getCrowns());
//			System.out.println(playerScore);
//		}
//		for(int i=0; i<props.size()) {
//			
//		}
		//System.out.println(playerScore);
		//calculateBonusScore(kingdomino);
		player.setPropertyScore(playerScore);
	}

	/******************
	 * * Feature 23 * *
	 ******************/

	// { Calculate ranking}
	// As a player, I want the Kingdomino app to automatically calculate the ranking
	// in order to know the winner of a finished game

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

	/******************
	 * * Feature 24 * *
	 ******************/

	// {Resolve tiebreak}
	// As a player, I want the Kingdomino app to automatically resolve a potential
	// tiebreak (i.e. equal score between players) by evaluating the most extended
	// (largest) property and then the total number of crowns

	// Handled in feature 23
	// ****************************************************************************************
}