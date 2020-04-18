package ca.mcgill.ecse223.kingdomino.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.*;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;

public class GamePage extends JPanel {

	private ArrayList<KingdomGrid> grids;
	private DraftPanel [] drafts;
	Game game;

	public GamePage(Game game) {
		drafts = new DraftPanel[2];
		grids = new ArrayList<KingdomGrid>();
		this.game = game;
		initPage();
	}

	public void initPage() {
		this.setBackground(Color.WHITE);
		this.setLayout(new GridLayout(0, 3));

		/******************/
		/******* LEFT *******/
		/******************/

		// Creating the left third
		JPanel left = new JPanel();
		left.setBackground(Color.WHITE);
		left.setLayout(new GridLayout(2, 0));
		left.setBorder(new EmptyBorder(10, 10, 10, 10));

		for (int i = 0; i < 2; i++) {
			JPanel part = new JPanel();
			part.setBackground(Color.WHITE);
			part.setBorder(new EmptyBorder(10, 10, 10, 10));

			JLabel label = new JLabel();
			Font labelFont = new Font("Arial", Font.BOLD, 20);
			
			String text = game.getPlayer(i).getUser().getName();
			if (text.length() < 8) {
				text = String.format("%-12s", text);
			}
			label.setText(text);
			label.setFont(labelFont);

			part.add(label);
			KingdomGrid grid = new KingdomGrid(game.getPlayer(i));
			part.add(grid);

			left.add(part);
		}

		this.add(left);

		/******************/
		/****** CENTER ******/
		/******************/
		JPanel center = new JPanel();
		center.setBackground(Color.WHITE);
		center.setLayout(new GridLayout(3, 0));
		center.setBorder(new EmptyBorder(10, 10, 10, 10));
		center.setPreferredSize(new Dimension(300, 800));
		/******************/
		/******** TOP *******/
		/******************/
		JPanel topPanel = new JPanel();
		topPanel.setBackground(Color.WHITE);
		topPanel.setLayout(new GridLayout(1, 0));
		topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		BufferedImage myPicture = null;
		try {
			myPicture = ImageIO.read(new File("src/main/resources/images/kingdomino.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Image image = myPicture.getScaledInstance(425, 200, Image.SCALE_DEFAULT);
		JLabel topLabel = new JLabel(new ImageIcon(image));
		//Font topLabelFont = new Font("Arial", Font.BOLD, 30);
		//topLabel.setFont(topLabelFont);

		JButton saveBtn = new JButton("Save & Quit");
		Font saveFont = new Font("Arial", Font.BOLD, 20);
		saveBtn.setFont(saveFont);
		saveBtn.setPreferredSize(new Dimension(150, 75));
		saveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO When the save and quit button is pressed
			}
		});

		JPanel savePanel = new JPanel();
		savePanel.setBackground(Color.WHITE);
		savePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		savePanel.add(saveBtn);

		JPanel titlePanel = new JPanel();
		titlePanel.setBackground(Color.WHITE);
		titlePanel.setLayout(new GridLayout(2, 0));

		titlePanel.add(topLabel);
		titlePanel.add(savePanel);

//		JPanel draftPanel = new JPanel();
//		draftPanel.setBackground(Color.WHITE);
//		draftPanel.setLayout(new GridLayout(1, 2));

		
//		draftPanel.add(p1);
//		draftPanel.add(p2);

//		JTextField playerColor = new JTextField();
//		playerColor.setEditable(false);
//		playerColor.setPreferredSize(new Dimension(75, 75));
//		playerColor.setBackground(Color.RED);
//
//		JPanel colorPanel = new JPanel();
//		colorPanel.setBackground(Color.WHITE);
//		colorPanel.setBorder(new EmptyBorder(10, 10, 10, 100));
//		colorPanel.add(playerColor);

		// draftPanel.add(colorPanel);

		topPanel.add(titlePanel);
		//topPanel.add(draftPanel);
		center.add(topPanel);

		/******************/
		/****** MIDDLE ******/
		/******************/

		JPanel middlePanel = new JPanel();
		middlePanel.setBackground(Color.WHITE);
		middlePanel.setLayout(new GridLayout(0, 2));

		DraftPanel p1 = new DraftPanel(true);
		DraftPanel p2 = new DraftPanel(false);
		drafts[0] = p1;
		drafts[1] = p2;
		
		middlePanel.add(p1);
		middlePanel.add(p2);
		
		JPanel leftPanel = new JPanel();
		middlePanel.setPreferredSize(new Dimension(300, 300));
		center.add(middlePanel);

		/******************/
		/****** BOTTOM ******/
		/******************/
		JPanel bottomPanel = new JPanel();
		bottomPanel.setBackground(Color.WHITE);
		bottomPanel.setLayout(new GridLayout(0, 2));

		JPanel movementPanel = new JPanel();
		movementPanel.setBackground(Color.WHITE);
		movementPanel.setLayout(new GridBagLayout());
		movementPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		BufferedImage uppic = null;
		try {
			uppic = ImageIO.read(new File("src/main/resources/images/MoveUP.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Image newImage = uppic.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
		JLabel picLabel = new JLabel(new ImageIcon(newImage));
		picLabel.setName("up");
		picLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
//				ActionEvent ae = new ActionEvent(e.getSource(), e.getID(), e.getComponent().getName());
//				actionPerformed(ae);
				// TODO When Click Up
			}
		});

		c.gridx = 1;
		c.gridy = 0;
		c.gridheight = 1;
		c.gridwidth = 1;
		movementPanel.add(picLabel, c);

		BufferedImage leftPic = null;
		try {
			leftPic = ImageIO.read(new File("src/main/resources/images/MoveLEFT.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		newImage = leftPic.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
		picLabel = new JLabel(new ImageIcon(newImage));
		picLabel.setName("left");
		picLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
//				ActionEvent ae = new ActionEvent(e.getSource(), e.getID(), e.getComponent().getName());
//				actionPerformed(ae);
				// TODO 
			}
		});

		c.gridx = 0;
		c.gridy = 1;
		c.gridheight = 1;
		c.gridwidth = 1;
		movementPanel.add(picLabel, c);

		BufferedImage rightPic = null;
		try {
			rightPic = ImageIO.read(new File("src/main/resources/images/MoveRIGHT.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		newImage = rightPic.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
		picLabel = new JLabel(new ImageIcon(newImage));
		picLabel.setName("right");
		picLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
//				ActionEvent ae = new ActionEvent(e.getSource(), e.getID(), e.getComponent().getName());
//				actionPerformed(ae);
				// TODO
			}
		});

		c.gridx = 2;
		c.gridy = 1;
		c.gridheight = 1;
		c.gridwidth = 1;
		movementPanel.add(picLabel, c);

		BufferedImage downPic = null;
		try {
			downPic = ImageIO.read(new File("src/main/resources/images/MoveDOWN.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		newImage = downPic.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
		picLabel = new JLabel(new ImageIcon(newImage));
		picLabel.setName("down");
		picLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
//				ActionEvent ae = new ActionEvent(e.getSource(), e.getID(), e.getComponent().getName());
//				actionPerformed(ae);
				// TODO
			}
		});

		c.gridx = 1;
		c.gridy = 2;
		c.gridheight = 1;
		c.gridwidth = 1;
		movementPanel.add(picLabel, c);

		BufferedImage counterPic = null;
		try {
			counterPic = ImageIO.read(new File("src/main/resources/images/RotateCounterClockwise.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		newImage = counterPic.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
		picLabel = new JLabel(new ImageIcon(newImage));
		picLabel.setName("counter");
		picLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
//				ActionEvent ae = new ActionEvent(e.getSource(), e.getID(), e.getComponent().getName());
//				actionPerformed(ae);
				// TODO
			}
		});

		c.gridx = 0;
		c.gridy = 3;
		c.gridheight = 1;
		c.gridwidth = 1;
		movementPanel.add(picLabel, c);

		BufferedImage clockPic = null;
		try {
			clockPic = ImageIO.read(new File("src/main/resources/images/RotateClockwise.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		newImage = clockPic.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
		picLabel = new JLabel(new ImageIcon(newImage));
		picLabel.setName("clockwise");
		picLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
//				ActionEvent ae = new ActionEvent(e.getSource(), e.getID(), e.getComponent().getName());
//				actionPerformed(ae);
				// TODO
			}
		});

		c.gridx = 2;
		c.gridy = 3;
		c.gridheight = 1;
		c.gridwidth = 1;
		movementPanel.add(picLabel, c);

		JPanel selectPanel = new JPanel();
		selectPanel.setBackground(Color.WHITE);
		selectPanel.setLayout(new GridLayout(3, 0));
		selectPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		String[] selectButtons = { "Place", "Discard", "Select" };
		
		
		JButton placeBtn = new JButton("Place");
		Font placeFont = new Font("Arial", Font.BOLD, 20);
		placeBtn.setFont(placeFont);
		placeBtn.setPreferredSize(new Dimension(125, 65));
		placeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO
			}
		});

		JPanel placePanel = new JPanel();
		placePanel.setBackground(Color.WHITE);
		placePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		placePanel.add(placeBtn);

		selectPanel.add(placePanel);
		
		JButton placeBtn2 = new JButton("Discard");
		placeBtn2.setFont(placeFont);
		placeBtn2.setPreferredSize(new Dimension(125, 65));
		placeBtn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO
			}
		});

		JPanel placePanel2 = new JPanel();
		placePanel2.setBackground(Color.WHITE);
		placePanel2.setBorder(new EmptyBorder(10, 10, 10, 10));
		placePanel2.add(placeBtn2);

		selectPanel.add(placePanel2);
		
		JButton placeBtn3 = new JButton("Select");
		placeBtn3.setFont(placeFont);
		placeBtn3.setPreferredSize(new Dimension(125, 65));
		placeBtn3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				KingdominoController.selectionComplete();
			}
		});

		JPanel placePanel3 = new JPanel();
		placePanel3.setBackground(Color.WHITE);
		placePanel3.setBorder(new EmptyBorder(10, 10, 10, 10));
		placePanel3.add(placeBtn3);

		selectPanel.add(placePanel3);

		bottomPanel.add(movementPanel);
		bottomPanel.add(selectPanel);
		center.add(bottomPanel);

		this.add(center);

		/******************/
		/******* RIGHT *******/
		/******************/
		JPanel right = new JPanel();
		right.setBackground(Color.WHITE);
		right.setLayout(new GridLayout(2, 0));
		right.setBorder(new EmptyBorder(10, 10, 10, 10));
		for (int i = 2; i < 4; i++) {
			JPanel part = new JPanel();
			part.setBackground(Color.WHITE);
			part.setBorder(new EmptyBorder(10, 10, 10, 10));

			JLabel label = new JLabel();
			Font labelFont = new Font("Arial", Font.BOLD, 20);

			String text = game.getPlayer(i).getUser().getName();
			if (text.length() < 9) {
				text = String.format("%-18s", text);
			}
			label.setText(text);
			label.setFont(labelFont);

			part.add(label);
			KingdomGrid grid = new KingdomGrid(game.getPlayer(i));
			part.add(grid);

			right.add(part);
		}
		this.add(right);



	}

	public void updateDrafts(Draft current, Draft next) {
		if(current == null) {
			//System.out.println(drafts[0]);
			drafts[0].updateEmpty();
		} else {
			drafts[0].update(current);
		}
		if (next == null) {
			drafts[1].updateEmpty();
		} else {
			drafts[1].update(next);
		}
		
	}
	
	public void setDominoSelectionEnabled(boolean enabled) {
		if(drafts[1] != null) {
			drafts[1].setDominoSelectionEnabled(enabled);
		}
	}
	
	public void changeButtonColor(PlayerColor color, int id) {
		drafts[1].changeButtonColor(color, id);
	}
}
