package ca.mcgill.ecse223.kingdomino.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ca.mcgill.ecse223.kingdomino.model.*;
import ca.mcgill.ecse223.kingdomino.model.Domino.DominoStatus;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;

public class KingdomGrid extends JPanel {

	private static final int CLUSTER = 3;
	private static final int MAX_ROWS = 9;
	private static final int GAP = 1;
	private static final Color BG = Color.BLACK;
	public static final int TIMER_DELAY = 2 * 1000;
	private String [][] tiles;
	private Player player;

	public KingdomGrid(Player p) {
		this.player = p;
		initComponents();
	}

	private void initComponents() {
		tiles = new String[9][9];
		// JLabel[][] fieldGrid = new JLabel[MAX_ROWS][MAX_ROWS];
		this.setLayout(new GridLayout(9, 9));
		//this.setBorder(BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP));
		this.setBackground(BG);
		switch(player.getColor()) {
		case Pink:
			this.setBackground(Color.PINK);
			break;
		case Yellow:
			this.setBackground(Color.YELLOW);
			break;
		case Blue:
			this.setBackground(Color.BLUE);
			break;
		case Green:
			this.setBackground(Color.GREEN);
			break;
		}
		//
		initGrid();
		updateGrid();
//		JPanel[][] panels = new JPanel[CLUSTER][CLUSTER];
//		for (int i = 0; i < panels.length; i++) {
//			for (int j = 0; j < panels[i].length; j++) {
//				panels[i][j] = new JPanel(new GridLayout(CLUSTER, CLUSTER, 1, 1));
//				panels[i][j].setBackground(BG);
//				this.add(panels[i][j]);
//			}
//		}
//
//		for (int row = 0; row < fieldGrid.length; row++) {
//			for (int col = 0; col < fieldGrid[row].length; col++) {
//				fieldGrid[row][col] = createField();
//				if (row == 4 && col == 4) {
//					fieldGrid[row][col].setBackground(Color.CYAN);
//				}
//
//				int i = row / 3;
//				int j = col / 3;
//				panels[i][j].add(fieldGrid[row][col]);
//			}
//		}

		this.setPreferredSize(new Dimension(350, 320));
	}

	private void initGrid() {
		for (int i = 0; i < 9; i++) {
//			this.add(createField());
			for(int j = 0; j<9; j++) {
				tiles[i][j] = "src/main/resources/tiles/white.jpg";
			}
		}
	}

	private JLabel createField(String s) {
		BufferedImage myPicture = null;
		try {
			myPicture = ImageIO.read(new File(s));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Image newImage = myPicture.getScaledInstance(35, 33, Image.SCALE_DEFAULT);
		JLabel picLabel = new JLabel(new ImageIcon(newImage));

		return picLabel;
	}
	public void updateGrid() {
		this.removeAll();
		updateTiles();
		for (int i = 0; i < 9; i++) {
			for(int j = 0; j<9; j++) {
				this.add(createField(tiles[i][j]));
			}
		}
		this.setVisible(true);
	}
	public void updateTiles() {
		initGrid();
		List<KingdomTerritory> territories = player.getKingdom().getTerritories();
		for (int i = 0; i < territories.size(); i++) {
			if (territories.get(i) instanceof DominoInKingdom) {
				DominoInKingdom ter = (DominoInKingdom) territories.get(i);
				if (ter.getDomino().getStatus() != DominoStatus.Discarded) {
					String tileLeft = ter.getDomino().getLeftTile().toString().toLowerCase();
					tileLeft+=ter.getDomino().getLeftCrown();
					tiles[ter.getY()+4][ter.getX()+4] = "src/main/resources/tiles/"+tileLeft+".jpg";
					
					String tileRight = ter.getDomino().getRightTile().toString().toLowerCase();
					tileRight+=ter.getDomino().getRightCrown();
					switch(ter.getDirection()) {
					case Up:
						tiles[ter.getY()+4][ter.getX()+5] = "src/main/resources/tiles/"+tileRight+".jpg";
						break;
					case Down:
						tiles[ter.getY()+4][ter.getX()+3] = "src/main/resources/tiles/"+tileRight+".jpg";
						break;
					case Left:
						tiles[ter.getY()+3][ter.getX()+4] = "src/main/resources/tiles/"+tileRight+".jpg";
						break;
					case Right:
						tiles[ter.getY()+5][ter.getX()+4] = "src/main/resources/tiles/"+tileRight+".jpg";
						break;
					}
				}
			} else {
				tiles[territories.get(i).getY()+4][territories.get(i).getX()+4] = "src/main/resources/tiles/"+player.getColor()+".jpg";
			}
		}
	}
	
	public PlayerColor getColor() {
		return player.getColor();
	}
	String[] leftTile = { "WheatField", "Mountain", "Lake", "WheatField" };
	String[] rightTile = { "Grass", "Lake", "Grass", "Lake" };
	int[] leftCrown = { 0, 2, 1, 1 };
	int[] rightCrown = { 1, 0, 0, 1 };
	String[] directions = { "up", "down", "right", "left" };
	int[] xPos = { 2, 3, -2, -1 };
	int[] yPos = { 1, 3, -1, -2 };

//	private JPanel updateGrid(int index) {
//
//		// DominoInKingdom dom = (DominoInKingdom) player.getKingdom()
//		// .getTerritory(player.getKingdom().numberOfTerritories() - 1);
//
//		// dom.getDomino()
//
//		JLabel[][] fieldGrid = new JLabel[MAX_ROWS][MAX_ROWS];
//		JPanel mainPanel = grids.get(index);
//		mainPanel = new JPanel(new GridLayout(CLUSTER, CLUSTER));
//		mainPanel.setBorder(BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP));
//		mainPanel.setBackground(BG);
//		JPanel[][] panels = new JPanel[CLUSTER][CLUSTER];
//		for (int i = 0; i < panels.length; i++) {
//			for (int j = 0; j < panels[i].length; j++) {
//				panels[i][j] = new JPanel(new GridLayout(CLUSTER, CLUSTER, 1, 1));
//				panels[i][j].setBackground(BG);
//				mainPanel.add(panels[i][j]);
//			}
//		}
//
//		for (int k = 0; k < 4; k++) {
//			int rowLeft = yPos[k];
//			int colLeft = xPos[k];
//			int rowRight = 0;
//			int colRight = 0;
//			switch (directions[k]) {
//			case "left":
//				colRight = colLeft - 1;
//				rowRight = rowLeft;
//				break;
//			case "right":
//				colRight = colLeft + 1;
//				rowRight = rowLeft;
//				break;
//			case "up":
//				rowRight = rowLeft + 1;
//				colRight = colLeft;
//				break;
//			case "down":
//				rowRight = rowLeft - 1;
//				colRight = colLeft;
//				break;
//			}
//
//			if (rowLeft > 0) {
//				rowLeft = 4 - rowLeft;
//			} else {
//				rowLeft = Math.abs(rowLeft) + 4;
//			}
//
//			if (rowRight > 0) {
//				rowRight = 4 - rowRight;
//			} else {
//				rowRight = Math.abs(rowRight) + 4;
//			}
//
//			if (colLeft >= 0) {
//				colLeft = colLeft + 4;
//			} else {
//				colLeft = 4 - Math.abs(colLeft);
//			}
//
//			if (colRight >= 0) {
//				colRight = colRight + 4;
//			} else {
//				colRight = 4 - Math.abs(colRight);
//			}
//			BufferedImage myPicture = null;
//			try {
//				String path = "src/kingdomino/view/images/terrains/" + leftTile[k] + leftCrown[k] + ".jpg";
//				System.out.println(path);
//				myPicture = ImageIO.read(new File(path));
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//
//				e.printStackTrace();
//			}
//			Image newImage = myPicture.getScaledInstance(30, 30, Image.SCALE_DEFAULT);
//			JLabel leftPic = new JLabel(new ImageIcon(newImage));
//
//			myPicture = null;
//			try {
//				String path = "src/kingdomino/view/images/terrains/" + rightTile[k] + rightCrown[k] + ".jpg";
//				System.out.println(path);
//				myPicture = ImageIO.read(new File(path));
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//
//				e.printStackTrace();
//			}
//
//			newImage = myPicture.getScaledInstance(30, 30, Image.SCALE_DEFAULT);
//			JLabel rightPic = new JLabel(new ImageIcon(newImage));
//
//			fieldGrid[rowLeft][colLeft] = leftPic;
//			fieldGrid[rowRight][colRight] = rightPic;
////			fieldGrid[rowRight][colRight].setBackground(rightTile[k]);
////			fieldGrid[rowLeft][colLeft].setBackground(leftTile[k]);
//			System.out.print("loop");
//		}
//
//		for (int row = 0; row < fieldGrid.length; row++) {
//			for (int col = 0; col < fieldGrid[row].length; col++) {
//
//				int i = row / 3;
//				int j = col / 3;
//				if (fieldGrid[row][col] != null) {
//					panels[i][j].add(fieldGrid[row][col]);
//				} else {
//
//					fieldGrid[row][col] = createField();
//					if (row == 4 && col == 4) {
//						fieldGrid[row][col].setBackground(Color.CYAN);
//					}
//
//					panels[i][j].add(fieldGrid[row][col]);
//				}
//			}
//		}
//		mainPanel.setPreferredSize(new Dimension(350, 320));
//		grids.remove(mainPanel);
//		grids.add(index, mainPanel);
//		gamePage();
//		return mainPanel;
//	}
}
