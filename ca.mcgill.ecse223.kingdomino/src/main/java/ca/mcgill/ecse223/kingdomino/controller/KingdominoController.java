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
		// Set the next player here
		Domino dom = player.getDominoSelection().getDomino();
		dom.setStatus(DominoStatus.ErroneouslyPreplaced);
		DominoInKingdom domIn = new DominoInKingdom(0, 0, player.getKingdom(), dom);
		domIn.setDirection(DirectionKind.Right);
		player.getKingdom().addTerritory(domIn);
	}

	public static void moveDomino(Kingdomino kingdomino, String movement) {
		Player player = kingdomino.getCurrentGame().getNextPlayer();
		List<KingdomTerritory> territories = player.getKingdom().getTerritories();
		DominoInKingdom ter = (DominoInKingdom) territories.get(territories.size() - 1);
		switch (movement) {
		case "left":
			ter.setX(ter.getX() - 1);
			if (!verifyGridSize(player.getKingdom())) {
				ter.setX(ter.getX() + 1);
			}
			break;
		case "right":
			ter.setX(ter.getX() + 1);
			if (!verifyGridSize(player.getKingdom())) {
				ter.setX(ter.getX() - 1);
			}
			break;
		case "up":
			ter.setY(ter.getY() + 1);
			if (!verifyGridSize(player.getKingdom())) {
				ter.setX(ter.getX() - 1);
			}
			break;
		case "down":
			ter.setY(ter.getY() - 1);
			if (!verifyGridSize(player.getKingdom())) {
				ter.setX(ter.getX() + 1);
			}
			break;
		}
		Domino dom = player.getDominoSelection().getDomino();
		resetDominoStatus(ter, kingdomino);
	}

	private static void resetDominoStatus(DominoInKingdom dom, Kingdomino kingdomino) {
		Player player = kingdomino.getCurrentGame().getNextPlayer();
		boolean castleAdjacency = verifyCastleAdjacency(dom.getX(), dom.getY(), dom.getDirection());
		System.out.print(castleAdjacency);
		boolean neighborAdjacency = verifyNeighborAdjacency(player.getKingdom(), dom.getDomino(), dom.getX(), dom.getY(), dom.getDirection());
		boolean noOverlapping = verifyNoOverlapping(dom.getDomino(), player.getKingdom(), dom.getX(), dom.getY(), dom.getDirection());
		if( (castleAdjacency || neighborAdjacency)&&noOverlapping) {
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

	private static void rotateDomino(Kingdomino kingdomino, String rotation) {
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
		if(dom.getStatus( )== DominoStatus.CorrectlyPreplaced) {
			dom.setStatus(DominoStatus.PlacedInKingdom);
			//Set the next player here
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
		int counter = 0;
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
			if (d.getClass().toString() == "DominoInKingdom") {
				Coord temp = new Coord(d.getX(), d.getY());
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
					Coord leftcoord, rightcoord;
					leftcoord = new Coord(dik.getX(), dik.getY());
					rightcoord = new Coord(x2, y2);
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

	/******************
	 * * Feature 16 * *
	 ******************/

	// {Verify no overlapping}
	// As a player, I want the Kingdomino app to automatically check that my current
	// domino is not overlapping with existing dominos

	public static boolean verifyNoOverlapping(Domino aDomino, Kingdom aKingdom, int x, int y, DirectionKind aDirection) {

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

	/******************
	 * * Feature 17 * *
	 ******************/

	// {Verify kingdom grid size}
	// As a player, I want the Kingdomino app to automatically check if the grid of
	// my kingdom has not yet exceeded a square of 5x5 tiles (including my castle)

	public static boolean verifyGridSize(Kingdom aKingdom) {
		int maxX = 0;
		int maxY = 0;
		int minX = 0;
		int minY = 0;
		int x = 0, y = 0, x2 = 0, y2 = 0;
		for (KingdomTerritory d : aKingdom.getTerritories()) {
			
//			if (d.getClass().toString() == "KingdomTerritoy") {
				System.out.print("hi");
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
//			}
		}
		System.out.print((maxY - minY) < 5);
		System.out.print(maxY);
		System.out.print(minY);
		System.out.print((maxX - minX) < 5);
		System.out.print(maxX);
		System.out.print(minX);
		return ((maxX - minX) < 5 && (maxY - minY) < 5);
	}

	/******************
	 * * Feature 18 * *
	 ******************/

	// {Discard domino}
	// As a player, I wish to discard a domino if it cannot be placed to my kingdom
	// in a valid way

	/******************
	 * * Feature 19 * *
	 ******************/

	// {Identify kingdom properties}
	// As a player, I want the Kingdomino app to automatically determine each
	// properties of my kingdom so that my score can be calculated

	/******************
	 * * Feature 20 * *
	 ******************/

	// {Calculate property score}
	// As a player, I want the Kingdomino app to automatically calculate the score
	// for each of my property based upon the size of that property and the number
	// of crowns

	/******************
	 * * Feature 21 * *
	 ******************/

	// { Calculate bonus scores}
	// As a player, I want the Kingdomino app to automatically calculate the bonus
	// scores (for Harmony and middle Kingdom) if those bonus scores were selected
	// as a game option

	/******************
	 * * Feature 22 * *
	 ******************/

	// {Calculate player score}
	// As a player, I want the Kingdomino app to automatically calculate the score
	// for each player by summing up their property scores and their bonus scores

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

	// ****************************************************************************************

	public DominoInKingdom rightTile(DominoInKingdom leftTile) {
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

	public boolean isConnected(DominoInKingdom d1, DominoInKingdom d2) {
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

	public static void CalculatePropertyAttributes(Property aProperty) {
		int numCrowns = 0;
		int size = 0;
		for (int i = 0; i < aProperty.numberOfIncludedDominos(); i++) {
			if (aProperty.getLeftTile() == aProperty.getIncludedDomino(i).getLeftTile()) {
				numCrowns += aProperty.getIncludedDomino(i).getLeftCrown();
			} else if (aProperty.getLeftTile() == aProperty.getIncludedDomino(i).getRightTile()) {
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


	public static int CalculatePlayerScore(DominoInKingdom[] playedDominoes, Kingdom aKingdom, Castle aCastle) {
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
		bonuscore = CalculateBonusScore(playedDominoes, aKingdom, aCastle);
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
			currentDomino.setDirection(DirectionKind.Up);
			currentDomino.setY(currentDominoY + 1);
			currentDominoY++;
		} else if (newDirection == DirectionKind.Down) {
			currentDomino.setDirection(DirectionKind.Down);
			currentDomino.setY(currentDominoY - 1);
			currentDominoY--;
		} else if (newDirection == DirectionKind.Left) {
			currentDomino.setDirection(DirectionKind.Left);
			currentDomino.setX(currentDominoX - 1);
			currentDominoX--;
		} else {
			currentDomino.setDirection(DirectionKind.Right);
			currentDomino.setX(currentDominoX + 1);
			currentDominoX++;
		}

		if (currentDominoX == 0 && currentDominoY == 0) {
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

}
