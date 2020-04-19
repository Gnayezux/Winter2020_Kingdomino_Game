package ca.mcgill.ecse223.kingdomino.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.*;
import ca.mcgill.ecse223.kingdomino.model.Domino.DominoStatus;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;

/**
 * This class contains the JPanel that is a domino in the draft
 * 4 DraftDominos for each draft
 * 
 * @author Victoria Iannotti
 */
public class DraftDomino extends JPanel{
	
	private List<Domino> dominos;
	private JButton selector;
	private int id;
	JPanel pan;
	public DraftDomino(int id, boolean isCurrentDraft) {
		this.id = id;
		dominos = new ArrayList<Domino>();
		createAllDominos();
		Domino domino = getDominoByID(id);
		this.setBackground(Color.WHITE);
		if(isCurrentDraft) {
			this.setPreferredSize(new Dimension(150, 75));
			this.setLayout(new GridLayout(1, 2));
			JLabel left = new JLabel();
			left.setBackground(Color.WHITE);
			String tileLeft = domino.getLeftTile().toString().toLowerCase();
			tileLeft+=domino.getLeftCrown();
			
			BufferedImage img = null;
			try {
			    img = ImageIO.read(new File("src/main/resources/tiles/"+tileLeft+".jpg"));
			} catch (IOException e) {
			    e.printStackTrace();
			}
			Image dimg = img.getScaledInstance(75, 65, Image.SCALE_SMOOTH);
			
			left.setIcon(new ImageIcon(dimg));
			JLabel right = new JLabel();
			right.setBackground(Color.WHITE);
			String tileRight = domino.getRightTile().toString().toLowerCase();
			tileRight+=domino.getRightCrown();
			
			 img = null;
			try {
			    img = ImageIO.read(new File("src/main/resources/tiles/"+tileRight+".jpg"));
			} catch (IOException e) {
			    e.printStackTrace();
			}
			dimg = img.getScaledInstance(75, 65, Image.SCALE_SMOOTH);
		
			right.setIcon(new ImageIcon(dimg));
			this.add(left);
			this.add(right);
			pan = new JPanel();
			pan.setBackground(Color.WHITE);
			pan.setPreferredSize(new Dimension(75, 75));
			this.add(pan);
		} else {
			this.setPreferredSize(new Dimension(150, 75));
			this.setLayout(new GridLayout(1, 2));
			JLabel left = new JLabel();
			left.setBackground(Color.WHITE);
			String tileLeft = domino.getLeftTile().toString().toLowerCase();
			tileLeft+=domino.getLeftCrown();
			
			BufferedImage img = null;
			try {
			    img = ImageIO.read(new File("src/main/resources/tiles/"+tileLeft+".jpg"));
			} catch (IOException e) {
			    e.printStackTrace();
			}
			Image dimg = img.getScaledInstance(75, 65, Image.SCALE_SMOOTH);
			
			left.setIcon(new ImageIcon(dimg));
			JLabel right = new JLabel();
			right.setBackground(Color.WHITE);
			String tileRight = domino.getRightTile().toString().toLowerCase();
			tileRight+=domino.getRightCrown();
			
			 img = null;
			try {
			    img = ImageIO.read(new File("src/main/resources/tiles/"+tileRight+".jpg"));
			} catch (IOException e) {
			    e.printStackTrace();
			}
			dimg = img.getScaledInstance(75, 65, Image.SCALE_SMOOTH);
		
			right.setIcon(new ImageIcon(dimg));
			this.add(left);
			this.add(right);
			pan = new JPanel();
			pan.setBackground(Color.WHITE);
			pan.setPreferredSize(new Dimension(75, 75));
			selector = new JButton("CHOOSE");
			selector.setFocusable(false);
			selector.setBackground(Color.WHITE);
			Font saveFont = new Font("Arial", Font.BOLD, 12);
			selector.setFont(saveFont);
			selector.setPreferredSize(new Dimension(75, 40));
			selector.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					KingdominoController.selectDomino(id);
//					if(KingdominoController.isSelectionValid()) {
//						selector.setColor(KingdominoApplication.getKingdomino().getCurrentGame().get)
//					}
					//selector.setEnabled(false);
					
				}
			});
			pan.add(selector);
			this.add(pan);
		}
	}
	
	public void createAllDominos() {
		Game game = new Game(4, new Kingdomino());
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
				this.dominos.add(new Domino(dominoId, leftTerrain, rightTerrain, numCrown, game));
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new java.lang.IllegalArgumentException(
					"Error occured while trying to read alldominoes.dat: " + e.getMessage());
		}
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
	public Domino getDominoByID(int id) {
//		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		if (dominos.size() == 0) {
			createAllDominos();
		}
		Collections.sort(dominos, (a, b) -> a.getId() - b.getId());
		return dominos.get(id - 1);
	}
	
	public void setDominoSelectionEnabled(boolean enabled) {
		selector.setEnabled(enabled);
	}
	
	public int getID() {
		return id;
	}
	
	public void setColor(PlayerColor color) {
		switch(color) {
		case Pink:
			pan.setBackground(Color.PINK);
			break;
		case Blue:
			pan.setBackground(Color.BLUE);
			break;
		case Green:
			pan.setBackground(Color.GREEN);
			break;
		case Yellow:
			pan.setBackground(Color.YELLOW);
			break;
		}
	}
}
