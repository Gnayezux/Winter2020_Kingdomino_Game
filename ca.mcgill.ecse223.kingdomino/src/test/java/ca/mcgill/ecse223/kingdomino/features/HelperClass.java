package ca.mcgill.ecse223.kingdomino.features;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.Domino;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.TerrainType;
import ca.mcgill.ecse223.kingdomino.model.Domino.DominoStatus;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;

public class HelperClass {
	public static void testSetup() {
		KingdominoController.startGameSetup();
		KingdominoController.setNumberOfPlayers(4);
		KingdominoController.createPlayersAndKingdoms();
	}

	public static TerrainType getTerrainType(String terrain) {
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

	public static PlayerColor getColor(String color) {
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

	public static DirectionKind getDirection(String dir) {
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

	public static DominoStatus getDominoStatus(String s) {
		String status = s.toLowerCase();
		switch (status) {
		case "inpile":
			return DominoStatus.InPile;
		case "excluded":
			return DominoStatus.Excluded;
		case "incurrentdraft":
			return DominoStatus.InCurrentDraft;
		case "innextdraft":
			return DominoStatus.InNextDraft;
		case "erroneouslypreplaced":
			return DominoStatus.ErroneouslyPreplaced;
		case "correctlypreplaced":
			return DominoStatus.CorrectlyPreplaced;
		case "placedinkingdom":
			return DominoStatus.PlacedInKingdom;
		case "discarded":
			return DominoStatus.Discarded;
		default:
			throw new java.lang.IllegalArgumentException("Invalid domino status: " + status);
		}
	}

	public static Domino getDominoByID(int id) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		if (!game.hasAllDominos()) {
			createAllDominos();
		}
		ArrayList<Domino> allDominos = new ArrayList<Domino>(game.getAllDominos());
		Collections.sort(allDominos, (a, b) -> a.getId() - b.getId());
		return allDominos.get(id - 1);
	}

	private static void createAllDominos() {
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

	public static String getState(String s) {
		switch (s) {
			case ("Initializing.SelectingFirstDomino"):
				return "selecting";
			case ("Playing.SelectingDomino"):
				return "selecting";
			case ("Playing.PlacingDomino"):
				return "placing";
			case ("Playing.EndingGame"):
				return "ending";
			default:
				return "";
		}
	}
}
