package ca.mcgill.ecse223.kingdomino.view;

import ca.mcgill.ecse223.kingdomino.controller.*;
import ca.mcgill.ecse223.kingdomino.model.*;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class KingdominoBoardGame extends JFrame {

	private String page = "homepage";
	private boolean isGridCreated = false;
	private String[] usernames = {"v", "m", "a", "k"};
	private static final int CLUSTER = 3;
	private static final int MAX_ROWS = 9;
	private static final int GAP = 3;
	private static final Color BG = Color.BLACK;
	public static final int TIMER_DELAY = 2 * 1000;
	private ArrayList<JPanel> grids = new ArrayList<>();
	
	GamePage gamePage;
	public KingdominoBoardGame() {
		initComponents();
	}

	private void initComponents() {
		this.setTitle("Kingdomino");
		this.setSize(1400, 850);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		homePage();
	}
	
	public void startGame(Game game) {
		gamePage = new GamePage(game);
		this.setContentPane(gamePage);
		this.invalidate();
		this.validate();
	}
	
	public void homePage() {
		HomePage p = new HomePage();
		this.setContentPane(p);
		this.invalidate();
		this.validate();
	}
	
	public void newGamePage() {
		NewGamePage p = new NewGamePage();
		this.setContentPane(p);
		this.invalidate();
		this.validate();
	}
	
	public void rulesPage() {
		RulesPage p = new RulesPage();
		this.setContentPane(p);
		this.invalidate();
		this.validate();
	}
	
	public void browseDominos() {
		BrowseDominosPage p = new BrowseDominosPage();
		this.setContentPane(p);
		this.invalidate();
		this.validate();
	}
	
	public void updateDrafts(Draft current, Draft next) {
		gamePage.updateDrafts(current, next);
		this.invalidate();
		this.validate();
	}
	
	public void notifyCurrentPlayer(Player player) {
		JOptionPane.showMessageDialog(this,
			    "The current player is " + player.getColor() + ".",
			    "Message",
			    JOptionPane.PLAIN_MESSAGE);
	}
	
	public void sendMessage(String s) {
		JOptionPane.showMessageDialog(this,
			    s ,
			    "Message",
			    JOptionPane.PLAIN_MESSAGE);
	}
	
	public void setDominoSelectionEnabled(boolean enabled) {
		gamePage.setDominoSelectionEnabled(enabled);
	}
	
	public void changeButtonColor(PlayerColor color, int id) {
		gamePage.changeButtonColor(color, id);
	}
	
	public void setPlacementEnabled(boolean enabled) {
		gamePage.setPlacementEnabled(enabled);
	}
	
	public void setDiscardEnabled(boolean enabled) {
		gamePage.setDiscardEnabled(enabled);
	}
	
	public void setSelectionEnabled(boolean enabled) {
		gamePage.setSelectionEnabled(enabled);
	}
	
	public void updateGrid() {
		gamePage.updateGrid();
		this.invalidate();
		this.validate();
	}
	public void setMovementEnabled(boolean enabled) {
		gamePage.setMovementEnabled(enabled);
	}
	
	public void endingGame(Game game) {
		EndGamePage p = new EndGamePage(game);
		this.setContentPane(p);
		this.invalidate();
		this.validate();
	}
//	
//	public void loadGame() {
//
//		JPanel panel = new JPanel();
//		panel.setBackground(Color.WHITE);
//		panel.setLayout(new GridLayout(0, 3));
//
//		JButton btn = new JButton("Back");
//		btn.setPreferredSize(new Dimension(200, 75));
//		btn.addActionListener(this);
//		Font font = new Font("Arial", Font.BOLD, 25);
//		btn.setFont(font);
//		JPanel btnPanel = new JPanel();
//		btnPanel.setBackground(Color.WHITE);
//		btnPanel.add(btn);
//		btnPanel.setBorder(new EmptyBorder(100, 10, 10, 10));
//		panel.add(btnPanel);
//
//		JPanel middle = new JPanel();
//		middle.setBackground(Color.WHITE);
//		middle.setLayout(new GridLayout(3, 0));
//
//		JLabel label = new JLabel();
//		Font labelFont = new Font("Arial", Font.BOLD, 35);
//		label.setText("Load Game");
//		label.setFont(labelFont);
//		JPanel labelPanel = new JPanel();
//		labelPanel.setBackground(Color.WHITE);
//		labelPanel.add(label);
//		labelPanel.setBorder(new EmptyBorder(80, 10, 10, 10));
//		middle.add(labelPanel);
//
//		JRadioButton birdButton = new JRadioButton("bird");
//		birdButton.setMnemonic(KeyEvent.VK_B);
//		birdButton.setActionCommand("bird");
//		birdButton.setSelected(true);
//
//		JRadioButton catButton = new JRadioButton("cat");
//		catButton.setMnemonic(KeyEvent.VK_C);
//		catButton.setActionCommand("cat");
//
//		JRadioButton dogButton = new JRadioButton("dog");
//		dogButton.setMnemonic(KeyEvent.VK_D);
//		dogButton.setActionCommand("dog");
//
//		JRadioButton rabbitButton = new JRadioButton("rabbit");
//		rabbitButton.setMnemonic(KeyEvent.VK_R);
//		rabbitButton.setActionCommand("rabbit");
//
//		JRadioButton pigButton = new JRadioButton("pig");
//		pigButton.setMnemonic(KeyEvent.VK_P);
//		pigButton.setActionCommand("pig");
//
//		// Group the radio buttons.
//		ButtonGroup group = new ButtonGroup();
//		group.add(birdButton);
//		group.add(catButton);
//		group.add(dogButton);
//		group.add(rabbitButton);
//		group.add(pigButton);
//
//		// Register a listener for the radio buttons.
//		birdButton.addActionListener(this);
//		catButton.addActionListener(this);
//		dogButton.addActionListener(this);
//		rabbitButton.addActionListener(this);
//		pigButton.addActionListener(this);
//
//		JPanel radiopanel = new JPanel();
//		radiopanel.setBackground(Color.WHITE);
//		radiopanel.setLayout(new GridLayout(0, 1));
//
//		JPanel scrollablepanel = new JPanel();
//		scrollablepanel.setBackground(Color.WHITE);
//		scrollablepanel.setLayout(new GridLayout(0, 1));
//
//		JScrollPane scrollableTextArea = new JScrollPane();
//
//		scrollableTextArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//
//		scrollablepanel.add(scrollableTextArea);
//		middle.add(scrollablepanel);
//
//		JButton continueBtn = new JButton("Continue");
//		continueBtn.setPreferredSize(new Dimension(200, 75));
//		continueBtn.addActionListener(this);
//		continueBtn.setFont(font);
//		JPanel continuePanel = new JPanel();
//		continuePanel.setBackground(Color.WHITE);
//		continuePanel.add(continueBtn);
//		continuePanel.setBorder(new EmptyBorder(100, 10, 10, 10));
//
//		panel.add(middle);
//		panel.add(continuePanel);
//
//		this.setContentPane(panel);
//		this.invalidate();
//		this.validate();
//	}
//
//	
//
//	//String[] players = { "Abdallah", "Maxime", "Kaicheng", "Victoria" };
//	int[] scores = { 40, 2, 10, 200 };
//
//	public void scoresPage(ArrayList<Player> p) {
//		ArrayList<Player> players = p;
//		JPanel panel = new JPanel();
//		panel.setBackground(Color.WHITE);
//		panel.setLayout(new GridLayout(3, 0));
//
//		JPanel playerPanel = new JPanel();
//		playerPanel.setBackground(Color.WHITE);
//		playerPanel.setBorder(new EmptyBorder(100, 10, 10, 10));
//		int winnerIndex = -1;
//		for (int i = 0; i < scores.length; i++) {
//			if (scores[i] > winnerIndex) {
//				winnerIndex = i;
//			}
//		}
//		for (int i = 0; i < players.size(); i++) {
//
//			JLabel inputLabel = new JLabel();
//			Font inputFont = new Font("Arial", Font.BOLD, 30);
//			inputLabel.setText(players.get(i).getUser().getName());
//			inputLabel.setFont(inputFont);
//			JPanel inputPanel = new JPanel();
//			inputPanel.setBackground(Color.WHITE);
//			inputPanel.add(inputLabel);
//			inputPanel.setBorder(new EmptyBorder(10, 100, 10, 10));
//			if (i == winnerIndex) {
//				JLabel winnerLabel = new JLabel();
//				winnerLabel.setText("Winner!");
//				winnerLabel.setFont(inputFont);
//				winnerLabel.setForeground(Color.RED);
//				inputPanel.add(winnerLabel);
//				inputPanel.setLayout(new GridLayout(0, 1));
//				inputPanel.setBorder(new EmptyBorder(50, 100, 10, 10));
//
//			}
//
//			playerPanel.add(inputPanel);
//
//		}
//
//		JPanel scorePanel = new JPanel();
//		scorePanel.setBackground(Color.WHITE);
//		scorePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
//		for (int i = 0; i < scores.length; i++) {
//			JLabel inputLabel = new JLabel();
//			Font inputFont = new Font("Arial", Font.BOLD, 30);
//			inputLabel.setText("Score:" + String.valueOf(scores[i]));
//			inputLabel.setFont(inputFont);
//			JPanel inputPanel = new JPanel();
//			inputPanel.setBackground(Color.WHITE);
//			inputPanel.add(inputLabel);
//			inputPanel.setBorder(new EmptyBorder(10, 100, 10, 10));
//			scorePanel.add(inputPanel);
//		}
//
//		JButton newGameBtn = new JButton("New Game");
//		newGameBtn.setPreferredSize(new Dimension(200, 75));
//		newGameBtn.addActionListener(this);
//		Font inputFont = new Font("Arial", Font.BOLD, 30);
//		newGameBtn.setFont(inputFont);
//		JPanel newGamePanel = new JPanel();
//		newGamePanel.setBackground(Color.WHITE);
//		newGamePanel.add(newGameBtn);
//		newGamePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
//
//		panel.add(playerPanel);
//		panel.add(scorePanel);
//		panel.add(newGamePanel);
//		this.setContentPane(panel);
//		this.invalidate();
//		this.validate();
//	}
//	//
//
//	
//
//	
//
//	
//
//	
//
//
//
//	

}
