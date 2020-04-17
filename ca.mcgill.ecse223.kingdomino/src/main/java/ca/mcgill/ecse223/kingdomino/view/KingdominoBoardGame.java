package ca.mcgill.ecse223.kingdomino.view;

import ca.mcgill.ecse223.kingdomino.controller.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class KingdominoBoardGame extends JFrame implements ActionListener {

	private KingdominoController controller;
	String page = "homepage";
	
	public KingdominoBoardGame(KingdominoController controller) {
		this.controller = controller;
		initComponents();
	}

	private void initComponents() {
		this.setTitle("Kingdomino");
		this.setSize(1000, 700);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		homePage();
		
	}	
	
	public void homePage() {
		JPanel mainPanel = new JPanel();
		// main panel details
		mainPanel.setBackground(Color.WHITE);
		mainPanel.setLayout(new GridLayout(3, 1));

		// kingdomino image
		BufferedImage myPicture = null;
		try {
			myPicture = ImageIO.read(new File("src/main/resources/images/kingdomino.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Image newImage = myPicture.getScaledInstance(600, 200, Image.SCALE_DEFAULT);
		JLabel picLabel = new JLabel(new ImageIcon(newImage));

		JPanel p1 = new JPanel();
		p1.setLayout(new GridLayout(0, 1, 0, 10));
		p1.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
		p1.setBackground(Color.WHITE);
		String[] homePageBtn = { "New Game", "Load Game", "Rules" };
		for (String name : homePageBtn) {
			JButton btn = new JButton(name);
			btn.setPreferredSize(new Dimension(200, 75));
			btn.addActionListener(this);
			Font font = new Font("Arial", Font.BOLD, 25);
			btn.setFont(font);
			JPanel btnPanel = new JPanel();
			btnPanel.setBackground(Color.WHITE);
			btnPanel.add(btn);
			p1.add(btnPanel);
		}

		// add image and button panel to main panel, then add main panel to frame
		mainPanel.add(picLabel);
		mainPanel.add(p1);
		this.setContentPane(mainPanel);
		this.invalidate();
		this.validate();
	}

	ArrayList<JTextField> playerInput = new ArrayList<>();

	public void newGamePage() {
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setLayout(new GridLayout(0, 3));

		JButton btn = new JButton("Back");
		btn.setPreferredSize(new Dimension(200, 75));
		btn.addActionListener(this);
		Font font = new Font("Arial", Font.BOLD, 25);
		btn.setFont(font);
		JPanel btnPanel = new JPanel();
		btnPanel.setBackground(Color.WHITE);
		btnPanel.add(btn);
		btnPanel.setBorder(new EmptyBorder(100, 10, 10, 10));
		panel.add(btnPanel);

		JPanel middle = new JPanel();
		middle.setBackground(Color.WHITE);
		middle.setLayout(new GridLayout(0, 1));

		JLabel label = new JLabel();
		Font labelFont = new Font("Arial", Font.BOLD, 35);
		label.setText("New Game");
		label.setFont(labelFont);
		JPanel labelPanel = new JPanel();
		labelPanel.setBackground(Color.WHITE);
		labelPanel.add(label);
		labelPanel.setBorder(new EmptyBorder(50, 10, 10, 10));
		middle.add(labelPanel);

		JLabel para = new JLabel();
		Font paraFont = new Font("Arial", Font.BOLD, 10);
		para.setText("Please input player names below. You can add a minimum of 2 names and a maximum of 4 names");
		para.setFont(paraFont);
		JPanel paraPanel = new JPanel();
		paraPanel.setBackground(Color.WHITE);
		paraPanel.add(para);
		paraPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		String[] players = { "Player 1", "Player 2", "Player 3", "Player 4" };
		int index = 0;
		for (String name : players) {
			JLabel inputLabel = new JLabel();
			Font inputFont = new Font("Arial", Font.BOLD, 20);
			inputLabel.setText(name);
			inputLabel.setFont(inputFont);
			JPanel inputPanel = new JPanel();
			inputPanel.setBackground(Color.WHITE);
			inputPanel.add(inputLabel);
			inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

			if (playerInput.size() == 4) {
				inputPanel.add(playerInput.get(index));
			} else {
				JTextField userText = new JTextField(20);
				userText.setPreferredSize(new Dimension(200, 50));
				playerInput.add(userText);
				inputPanel.add(userText);
			}

			middle.add(inputPanel);
			index++;
		}

		JPanel panelBtn = new JPanel();
		panelBtn.setBackground(Color.WHITE);
		panelBtn.setLayout(new GridLayout(1, 0));

		JPanel startGamePanel = new JPanel();
		startGamePanel.setBackground(Color.WHITE);

		JButton startGameBtn = new JButton("Start Game");
		startGameBtn.setPreferredSize(new Dimension(200, 75));
		startGameBtn.addActionListener(this);
		Font startGameFont = new Font("Arial", Font.BOLD, 25);
		btn.setFont(startGameFont);
		startGamePanel.add(startGameBtn);

		JButton browseDominoBtn = new JButton("Browse Dominos");
		browseDominoBtn.setPreferredSize(new Dimension(200, 75));
		browseDominoBtn.addActionListener(this);
		Font browseDominoFont = new Font("Arial", Font.BOLD, 25);
		btn.setFont(browseDominoFont);
		startGamePanel.add(browseDominoBtn);

		middle.add(startGamePanel);

		panel.add(middle);
		this.setContentPane(panel);
		this.invalidate();
		this.validate();
	}

	public void loadGame() {

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setLayout(new GridLayout(0, 3));

		JButton btn = new JButton("Back");
		btn.setPreferredSize(new Dimension(200, 75));
		btn.addActionListener(this);
		Font font = new Font("Arial", Font.BOLD, 25);
		btn.setFont(font);
		JPanel btnPanel = new JPanel();
		btnPanel.setBackground(Color.WHITE);
		btnPanel.add(btn);
		btnPanel.setBorder(new EmptyBorder(100, 10, 10, 10));
		panel.add(btnPanel);

		JPanel middle = new JPanel();
		middle.setBackground(Color.WHITE);
		middle.setLayout(new GridLayout(3, 0));

		JLabel label = new JLabel();
		Font labelFont = new Font("Arial", Font.BOLD, 35);
		label.setText("Load Game");
		label.setFont(labelFont);
		JPanel labelPanel = new JPanel();
		labelPanel.setBackground(Color.WHITE);
		labelPanel.add(label);
		labelPanel.setBorder(new EmptyBorder(80, 10, 10, 10));
		middle.add(labelPanel);

		JRadioButton birdButton = new JRadioButton("bird");
		birdButton.setMnemonic(KeyEvent.VK_B);
		birdButton.setActionCommand("bird");
		birdButton.setSelected(true);

		JRadioButton catButton = new JRadioButton("cat");
		catButton.setMnemonic(KeyEvent.VK_C);
		catButton.setActionCommand("cat");

		JRadioButton dogButton = new JRadioButton("dog");
		dogButton.setMnemonic(KeyEvent.VK_D);
		dogButton.setActionCommand("dog");

		JRadioButton rabbitButton = new JRadioButton("rabbit");
		rabbitButton.setMnemonic(KeyEvent.VK_R);
		rabbitButton.setActionCommand("rabbit");

		JRadioButton pigButton = new JRadioButton("pig");
		pigButton.setMnemonic(KeyEvent.VK_P);
		pigButton.setActionCommand("pig");

		// Group the radio buttons.
		ButtonGroup group = new ButtonGroup();
		group.add(birdButton);
		group.add(catButton);
		group.add(dogButton);
		group.add(rabbitButton);
		group.add(pigButton);

		// Register a listener for the radio buttons.
		birdButton.addActionListener(this);
		catButton.addActionListener(this);
		dogButton.addActionListener(this);
		rabbitButton.addActionListener(this);
		pigButton.addActionListener(this);

		JPanel radiopanel = new JPanel();
		radiopanel.setBackground(Color.WHITE);
		radiopanel.setLayout(new GridLayout(0, 1));

		JPanel scrollablepanel = new JPanel();
		scrollablepanel.setBackground(Color.WHITE);
		scrollablepanel.setLayout(new GridLayout(0, 1));

		JScrollPane scrollableTextArea = new JScrollPane();

		scrollableTextArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		scrollablepanel.add(scrollableTextArea);
		middle.add(scrollablepanel);

		JButton continueBtn = new JButton("Continue");
		continueBtn.setPreferredSize(new Dimension(200, 75));
		continueBtn.addActionListener(this);
		continueBtn.setFont(font);
		JPanel continuePanel = new JPanel();
		continuePanel.setBackground(Color.WHITE);
		continuePanel.add(continueBtn);
		continuePanel.setBorder(new EmptyBorder(100, 10, 10, 10));

		panel.add(middle);
		panel.add(continuePanel);

		this.setContentPane(panel);
		this.invalidate();
		this.validate();
	}

	public void rulesPage() {
		JPanel pane = new JPanel();
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		BufferedImage myPicture = null;
		try {
			myPicture = ImageIO.read(new File("src/kingdomino/view/images/rules.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Image newImage = myPicture.getScaledInstance(1100, 700, Image.SCALE_DEFAULT);
		JLabel picLabel = new JLabel(new ImageIcon(newImage));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.0;
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy = 3;
		pane.add(picLabel, c);

		JButton btn = new JButton("Back");
		btn.setPreferredSize(new Dimension(200, 75));
		btn.addActionListener(this);
		Font font = new Font("Arial", Font.BOLD, 25);
		btn.setFont(font);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 0; // reset to default
		c.weighty = 1.0; // request any extra vertical space
		c.anchor = GridBagConstraints.PAGE_END; // bottom of space
		c.gridx = 0; // aligned with button 2
		c.gridwidth = 1; // 2 columns wide
		c.gridy = 0; // third row
		pane.add(btn, c);
		pane.setBackground(Color.WHITE);

		this.setContentPane(pane);
		this.invalidate();
		this.validate();

	}

	public void browseDominos() {
		JPanel pane = new JPanel();
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		BufferedImage myPicture = null;
		try {
			myPicture = ImageIO.read(new File("src/kingdomino/view/images/dominos.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Image newImage = myPicture.getScaledInstance(800, 600, Image.SCALE_DEFAULT);
		JLabel picLabel = new JLabel(new ImageIcon(newImage));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.0;
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy = 3;
		pane.add(picLabel, c);

		JButton btn = new JButton("Back");
		btn.setPreferredSize(new Dimension(200, 75));
		btn.addActionListener(this);
		Font font = new Font("Arial", Font.BOLD, 25);
		btn.setFont(font);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 0; // reset to default
		c.weighty = 1.0; // request any extra vertical space
		c.anchor = GridBagConstraints.PAGE_END; // bottom of space
		c.gridx = 0; // aligned with button 2
		c.gridwidth = 1; // 2 columns wide
		c.gridy = 0; // third row
		pane.add(btn, c);
		pane.setBackground(Color.WHITE);

		JLabel inputLabel = new JLabel();
		Font inputFont = new Font("Arial", Font.BOLD, 30);
		inputLabel.setText("Browse Dominos");
		inputLabel.setFont(inputFont);
		JPanel inputPanel = new JPanel();
		inputPanel.setBackground(Color.WHITE);
		inputPanel.add(inputLabel);
		inputPanel.setBorder(new EmptyBorder(10, 100, 10, 10));
		pane.add(inputPanel);
		pane.setBorder(new EmptyBorder(10, 10, 100, 10));

		this.setContentPane(pane);
		this.invalidate();
		this.validate();
	}

	String[] players = { "Abdallah", "Maxime", "Kaicheng", "Victoria" };
	int[] scores = { 40, 2, 10, 200 };

	public void scoresPage() {
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setLayout(new GridLayout(3, 0));

		JPanel playerPanel = new JPanel();
		playerPanel.setBackground(Color.WHITE);
		playerPanel.setBorder(new EmptyBorder(100, 10, 10, 10));
		int winnerIndex = -1;
		for (int i = 0; i < scores.length; i++) {
			if (scores[i] > winnerIndex) {
				winnerIndex = i;
			}
		}
		for (int i = 0; i < players.length; i++) {

			JLabel inputLabel = new JLabel();
			Font inputFont = new Font("Arial", Font.BOLD, 30);
			inputLabel.setText(players[i]);
			inputLabel.setFont(inputFont);
			JPanel inputPanel = new JPanel();
			inputPanel.setBackground(Color.WHITE);
			inputPanel.add(inputLabel);
			inputPanel.setBorder(new EmptyBorder(10, 100, 10, 10));
			if (i == winnerIndex) {
				JLabel winnerLabel = new JLabel();
				winnerLabel.setText("Winner!");
				winnerLabel.setFont(inputFont);
				winnerLabel.setForeground(Color.RED);
				inputPanel.add(winnerLabel);
				inputPanel.setLayout(new GridLayout(0, 1));
				inputPanel.setBorder(new EmptyBorder(50, 100, 10, 10));

			}

			playerPanel.add(inputPanel);

		}

		JPanel scorePanel = new JPanel();
		scorePanel.setBackground(Color.WHITE);
		scorePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		for (int i = 0; i < scores.length; i++) {
			JLabel inputLabel = new JLabel();
			Font inputFont = new Font("Arial", Font.BOLD, 30);
			inputLabel.setText("Score:" + String.valueOf(scores[i]));
			inputLabel.setFont(inputFont);
			JPanel inputPanel = new JPanel();
			inputPanel.setBackground(Color.WHITE);
			inputPanel.add(inputLabel);
			inputPanel.setBorder(new EmptyBorder(10, 100, 10, 10));
			scorePanel.add(inputPanel);
		}

		JButton newGameBtn = new JButton("New Game");
		newGameBtn.setPreferredSize(new Dimension(200, 75));
		newGameBtn.addActionListener(this);
		Font inputFont = new Font("Arial", Font.BOLD, 30);
		newGameBtn.setFont(inputFont);
		JPanel newGamePanel = new JPanel();
		newGamePanel.setBackground(Color.WHITE);
		newGamePanel.add(newGameBtn);
		newGamePanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		panel.add(playerPanel);
		panel.add(scorePanel);
		panel.add(newGamePanel);
		this.setContentPane(panel);
		this.invalidate();
		this.validate();
	}
	//

	boolean isCreated = false;

	public void gameBoardComponents() {
		JPanel gamePanel = new JPanel();
		gamePanel.setBackground(Color.WHITE);
		gamePanel.setLayout(new GridLayout(0, 3));

		String[] players = { "abdallah", "hani", "maxime", "victoria" };
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// left

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
			String text = players[i];
			if (text.length() < 8) {
				text = String.format("%-12s", text);
			}
			label.setText(text);
			label.setFont(labelFont);

			part.add(label);

			if (isCreated) {
				part.add(grids.get(i));
			} else {
				part.add(createGrid());
			}

			left.add(part);
		}

		gamePanel.add(left);

		// CENTER
		JPanel center = new JPanel();
		center.setBackground(Color.WHITE);
		center.setLayout(new GridLayout(3, 0));
		center.setBorder(new EmptyBorder(10, 10, 10, 10));

		// TOP

		JPanel topPanel = new JPanel();
		topPanel.setBackground(Color.WHITE);
		topPanel.setLayout(new GridLayout(2, 0));
		topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		JLabel topLabel = new JLabel("Kingdomino");
		Font topLabelFont = new Font("Arial", Font.BOLD, 30);
		topLabel.setFont(topLabelFont);

		JButton saveBtn = new JButton("Save & Quit");
		Font saveFont = new Font("Arial", Font.BOLD, 20);
		saveBtn.setFont(saveFont);
		saveBtn.setPreferredSize(new Dimension(150, 75));
		saveBtn.addActionListener(this);

		JPanel savePanel = new JPanel();
		savePanel.setBackground(Color.WHITE);
		savePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		savePanel.add(saveBtn);

		JPanel titlePanel = new JPanel();
		titlePanel.setBackground(Color.WHITE);
		titlePanel.setLayout(new GridLayout(1, 0));

		titlePanel.add(topLabel);
		titlePanel.add(savePanel);

		JPanel draftPanel = new JPanel();
		draftPanel.setBackground(Color.WHITE);
		draftPanel.setLayout(new GridLayout(1, 0));

		JLabel draftNext = new JLabel("Up Next: " + players[1]);
		Font draftFont = new Font("Arial", Font.BOLD, 20);
		draftNext.setFont(draftFont);
		draftPanel.add(draftNext);

		JTextField playerColor = new JTextField();
		playerColor.setEditable(false);
		playerColor.setPreferredSize(new Dimension(75, 75));
		playerColor.setBackground(Color.RED);

		JPanel colorPanel = new JPanel();
		colorPanel.setBackground(Color.WHITE);
		colorPanel.setBorder(new EmptyBorder(10, 10, 10, 100));
		colorPanel.add(playerColor);

		draftPanel.add(colorPanel);

		topPanel.add(titlePanel);
		topPanel.add(draftPanel);
		center.add(topPanel);

		// MIDDLE
		JPanel middlePanel = new JPanel();
		middlePanel.setBackground(Color.WHITE);
		middlePanel.setLayout(new GridLayout(0, 2));

		JPanel leftPanel = new JPanel();
		leftPanel.setBackground(Color.WHITE);
		leftPanel.setLayout(new GridLayout(4, 0));
		for (int i = 0; i < 4; i++) {
			JTextField domino = new JTextField();
			domino.setEditable(false);
			domino.setPreferredSize(new Dimension(150, 75));
			domino.setBackground(Color.BLUE);
			domino.setName("prevDraft" + String.valueOf(i));
			domino.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					ActionEvent ae = new ActionEvent(e.getSource(), e.getID(), e.getComponent().getName());
					actionPerformed(ae);
				}
			});

			JPanel dominoPanel = new JPanel();
			dominoPanel.setBackground(Color.WHITE);
			dominoPanel.add(domino);
			domino.setBorder(BorderFactory.createLineBorder(Color.GREEN, 6));
			leftPanel.add(dominoPanel);

		}
		middlePanel.add(leftPanel);

		JPanel rightPanel = new JPanel();
		rightPanel.setBackground(Color.WHITE);
		rightPanel.setLayout(new GridLayout(4, 0));
		for (int i = 0; i < 4; i++) {
			JTextField domino = new JTextField();
			domino.setEditable(false);
			domino.setPreferredSize(new Dimension(150, 75));
			domino.setBackground(Color.BLUE);
			domino.setName("curDraft" + String.valueOf(i));
			domino.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					ActionEvent ae = new ActionEvent(e.getSource(), e.getID(), e.getComponent().getName());
					actionPerformed(ae);
				}
			});

			JPanel dominoPanel = new JPanel();
			dominoPanel.setBackground(Color.WHITE);
			dominoPanel.add(domino);
			domino.setBorder(BorderFactory.createLineBorder(Color.GREEN, 6));
			rightPanel.add(dominoPanel);

		}
		middlePanel.add(rightPanel);

		center.add(middlePanel);

		// BOTTOM
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
			uppic = ImageIO.read(new File("src/kingdomino/view/images/MoveUP.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Image newImage = uppic.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
		JLabel picLabel = new JLabel(new ImageIcon(newImage));
		picLabel.setName("up");
		picLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ActionEvent ae = new ActionEvent(e.getSource(), e.getID(), e.getComponent().getName());
				actionPerformed(ae);
			}
		});

		c.gridx = 1;
		c.gridy = 0;
		c.gridheight = 1;
		c.gridwidth = 1;
		movementPanel.add(picLabel, c);

		BufferedImage leftPic = null;
		try {
			leftPic = ImageIO.read(new File("src/kingdomino/view/images/MoveLEFT.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		newImage = leftPic.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
		picLabel = new JLabel(new ImageIcon(newImage));
		picLabel.setName("left");
		picLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ActionEvent ae = new ActionEvent(e.getSource(), e.getID(), e.getComponent().getName());
				actionPerformed(ae);

			}
		});

		c.gridx = 0;
		c.gridy = 1;
		c.gridheight = 1;
		c.gridwidth = 1;
		movementPanel.add(picLabel, c);

		BufferedImage rightPic = null;
		try {
			rightPic = ImageIO.read(new File("src/kingdomino/view/images/MoveRIGHT.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		newImage = rightPic.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
		picLabel = new JLabel(new ImageIcon(newImage));
		picLabel.setName("right");
		picLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ActionEvent ae = new ActionEvent(e.getSource(), e.getID(), e.getComponent().getName());
				actionPerformed(ae);
			}
		});

		c.gridx = 2;
		c.gridy = 1;
		c.gridheight = 1;
		c.gridwidth = 1;
		movementPanel.add(picLabel, c);

		BufferedImage downPic = null;
		try {
			downPic = ImageIO.read(new File("src/kingdomino/view/images/MoveDOWN.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		newImage = downPic.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
		picLabel = new JLabel(new ImageIcon(newImage));
		picLabel.setName("down");
		picLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ActionEvent ae = new ActionEvent(e.getSource(), e.getID(), e.getComponent().getName());
				actionPerformed(ae);
			}
		});

		c.gridx = 1;
		c.gridy = 2;
		c.gridheight = 1;
		c.gridwidth = 1;
		movementPanel.add(picLabel, c);

		BufferedImage counterPic = null;
		try {
			counterPic = ImageIO.read(new File("src/kingdomino/view/images/RotateCounterClockwise.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		newImage = counterPic.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
		picLabel = new JLabel(new ImageIcon(newImage));
		picLabel.setName("counter");
		picLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ActionEvent ae = new ActionEvent(e.getSource(), e.getID(), e.getComponent().getName());
				actionPerformed(ae);
			}
		});

		c.gridx = 0;
		c.gridy = 3;
		c.gridheight = 1;
		c.gridwidth = 1;
		movementPanel.add(picLabel, c);

		BufferedImage clockPic = null;
		try {
			clockPic = ImageIO.read(new File("src/kingdomino/view/images/RotateClockwise.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		newImage = clockPic.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
		picLabel = new JLabel(new ImageIcon(newImage));
		picLabel.setName("clockwise");
		picLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ActionEvent ae = new ActionEvent(e.getSource(), e.getID(), e.getComponent().getName());
				actionPerformed(ae);
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
		for (String select : selectButtons) {
			JButton placeBtn = new JButton(select);
			Font placeFont = new Font("Arial", Font.BOLD, 20);
			placeBtn.setFont(placeFont);
			placeBtn.setPreferredSize(new Dimension(125, 65));
			placeBtn.addActionListener(this);

			JPanel placePanel = new JPanel();
			placePanel.setBackground(Color.WHITE);
			placePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
			placePanel.add(placeBtn);

			selectPanel.add(placePanel);
		}

		bottomPanel.add(movementPanel);
		bottomPanel.add(selectPanel);
		center.add(bottomPanel);

		//
		gamePanel.add(center);

		// right
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

			String text = players[i];
			if (text.length() < 9) {
				text = String.format("%-18s", text);
			}
			label.setText(text);
			label.setFont(labelFont);

			part.add(label);
			if (isCreated) {
				part.add(grids.get(i));
			} else {
				part.add(createGrid());
			}

			right.add(part);
		}
		gamePanel.add(right);

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		//

		isCreated = true;
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		this.setContentPane(gamePanel);
		this.invalidate();
		this.validate();
	}

	private static final int CLUSTER = 3;
	private static final int MAX_ROWS = 9;
	private static final int GAP = 3;
	private static final Color BG = Color.BLACK;
	public static final int TIMER_DELAY = 2 * 1000;
	ArrayList<JPanel> grids = new ArrayList<>();

	private JPanel createGrid() {

		// DominoInKingdom dom = (DominoInKingdom) player.getKingdom()
		// .getTerritory(player.getKingdom().numberOfTerritories() - 1);

		// dom.getDomino()

		JLabel[][] fieldGrid = new JLabel[MAX_ROWS][MAX_ROWS];
		JPanel mainPanel = new JPanel(new GridLayout(CLUSTER, CLUSTER));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP));
		mainPanel.setBackground(BG);
		JPanel[][] panels = new JPanel[CLUSTER][CLUSTER];
		for (int i = 0; i < panels.length; i++) {
			for (int j = 0; j < panels[i].length; j++) {
				panels[i][j] = new JPanel(new GridLayout(CLUSTER, CLUSTER, 1, 1));
				panels[i][j].setBackground(BG);
				mainPanel.add(panels[i][j]);
			}
		}

		for (int row = 0; row < fieldGrid.length; row++) {
			for (int col = 0; col < fieldGrid[row].length; col++) {
				fieldGrid[row][col] = createField();
				if (row == 4 && col == 4) {
					fieldGrid[row][col].setBackground(Color.CYAN);
				}

				int i = row / 3;
				int j = col / 3;
				panels[i][j].add(fieldGrid[row][col]);
			}
		}
		mainPanel.setPreferredSize(new Dimension(350, 320));
		grids.add(mainPanel);

		return mainPanel;
	}

	String[] leftTile = { "WheatField", "Mountain", "Lake", "WheatField" };
	String[] rightTile = { "Grass", "Lake", "Grass", "Lake" };
	int[] leftCrown = { 0, 2, 1, 1 };
	int[] rightCrown = { 1, 0, 0, 1 };
	String[] directions = { "up", "down", "right", "left" };
	int[] xPos = { 2, 3, -2, -1 };
	int[] yPos = { 1, 3, -1, -2 };

	private JPanel updateGrid(int index) {

		// DominoInKingdom dom = (DominoInKingdom) player.getKingdom()
		// .getTerritory(player.getKingdom().numberOfTerritories() - 1);

		// dom.getDomino()

		JLabel[][] fieldGrid = new JLabel[MAX_ROWS][MAX_ROWS];
		JPanel mainPanel = grids.get(index);
		mainPanel = new JPanel(new GridLayout(CLUSTER, CLUSTER));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP));
		mainPanel.setBackground(BG);
		JPanel[][] panels = new JPanel[CLUSTER][CLUSTER];
		for (int i = 0; i < panels.length; i++) {
			for (int j = 0; j < panels[i].length; j++) {
				panels[i][j] = new JPanel(new GridLayout(CLUSTER, CLUSTER, 1, 1));
				panels[i][j].setBackground(BG);
				mainPanel.add(panels[i][j]);
			}
		}

		for (int k = 0; k < 4; k++) {
			int rowLeft = yPos[k];
			int colLeft = xPos[k];
			int rowRight = 0;
			int colRight = 0;
			switch (directions[k]) {
			case "left":
				colRight = colLeft - 1;
				rowRight = rowLeft;
				break;
			case "right":
				colRight = colLeft + 1;
				rowRight = rowLeft;
				break;
			case "up":
				rowRight = rowLeft + 1;
				colRight = colLeft;
				break;
			case "down":
				rowRight = rowLeft - 1;
				colRight = colLeft;
				break;
			}

			if (rowLeft > 0) {
				rowLeft = 4 - rowLeft;
			} else {
				rowLeft = Math.abs(rowLeft) + 4;
			}

			if (rowRight > 0) {
				rowRight = 4 - rowRight;
			} else {
				rowRight = Math.abs(rowRight) + 4;
			}

			if (colLeft >= 0) {
				colLeft = colLeft + 4;
			} else {
				colLeft = 4 - Math.abs(colLeft);
			}

			if (colRight >= 0) {
				colRight = colRight + 4;
			} else {
				colRight = 4 - Math.abs(colRight);
			}
			BufferedImage myPicture = null;
			try {
				String path = "src/kingdomino/view/images/terrains/" + leftTile[k] + leftCrown[k] + ".jpg";
				System.out.println(path);
				myPicture = ImageIO.read(new File(path));
			} catch (IOException e) {
				// TODO Auto-generated catch block

				e.printStackTrace();
			}
			Image newImage = myPicture.getScaledInstance(30, 30, Image.SCALE_DEFAULT);
			JLabel leftPic = new JLabel(new ImageIcon(newImage));

			myPicture = null;
			try {
				String path = "src/kingdomino/view/images/terrains/" + rightTile[k] + rightCrown[k] + ".jpg";
				System.out.println(path);
				myPicture = ImageIO.read(new File(path));
			} catch (IOException e) {
				// TODO Auto-generated catch block

				e.printStackTrace();
			}

			newImage = myPicture.getScaledInstance(30, 30, Image.SCALE_DEFAULT);
			JLabel rightPic = new JLabel(new ImageIcon(newImage));

			fieldGrid[rowLeft][colLeft] = leftPic;
			fieldGrid[rowRight][colRight] = rightPic;
//			fieldGrid[rowRight][colRight].setBackground(rightTile[k]);
//			fieldGrid[rowLeft][colLeft].setBackground(leftTile[k]);
			System.out.print("loop");
		}

		for (int row = 0; row < fieldGrid.length; row++) {
			for (int col = 0; col < fieldGrid[row].length; col++) {

				int i = row / 3;
				int j = col / 3;
				if (fieldGrid[row][col] != null) {
					panels[i][j].add(fieldGrid[row][col]);
				} else {

					fieldGrid[row][col] = createField();
					if (row == 4 && col == 4) {
						fieldGrid[row][col].setBackground(Color.CYAN);
					}

					panels[i][j].add(fieldGrid[row][col]);
				}
			}
		}
		mainPanel.setPreferredSize(new Dimension(350, 320));
		grids.remove(mainPanel);
		grids.add(index, mainPanel);
		gameBoardComponents();
		return mainPanel;
	}

	private JLabel createField() {
		BufferedImage myPicture = null;
		try {
			myPicture = ImageIO.read(new File("src/kingdomino/view/images/terrains/white.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Image newImage = myPicture.getScaledInstance(30, 30, Image.SCALE_DEFAULT);
		JLabel picLabel = new JLabel(new ImageIcon(newImage));

		return picLabel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "up") {
			System.out.print("hi");
			updateGrid(0);
		}
		// TODO Auto-generated method stub

		if (e.getActionCommand() == "New Game") {
			newGamePage();
			page = "newgame";
		} else if (e.getActionCommand() == "Browse Dominos") {
			browseDominos();
			page = "browsedominos";
		} else if (e.getActionCommand() == "Load Game") {
			loadGame();
			page = "loadgame";
		} else if (e.getActionCommand() == "Rules") {
			rulesPage();
			page = "rules";
		} else if (e.getActionCommand() == "Start Game") {
			gameBoardComponents();
			page = "startgame";
		} else if (e.getActionCommand() == "Back") {
			if (page.equals("browsedominos")) {
				newGamePage();
				page = "newgame";
			} else {
				homePage();
				page = "homepage";
			}

		}
	}

}
