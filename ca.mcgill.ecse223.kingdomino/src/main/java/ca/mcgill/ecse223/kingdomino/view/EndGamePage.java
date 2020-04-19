package ca.mcgill.ecse223.kingdomino.view;

import java.awt.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.*;

public class EndGamePage extends JPanel{

	public EndGamePage(Game game) {
		int [] scores = new int [4];
		KingdominoController.startGameSetup();
		KingdominoController.setNumberOfPlayers(4);
		KingdominoController.createPlayersAndKingdoms();
		this.setBackground(Color.WHITE);
		this.setLayout(new GridLayout(3, 0));

		JPanel playerPanel = new JPanel();
		playerPanel.setBackground(Color.WHITE);
		playerPanel.setBorder(new EmptyBorder(100, 10, 10, 10));
		int winnerIndex = -1;
		for (int i = 0; i < scores.length; i++) {
			if (scores[i] > winnerIndex) {
				winnerIndex = i;
			}
		}
		for (int i = 0; i < game.getPlayers().size(); i++) {

			JLabel inputLabel = new JLabel();
			Font inputFont = new Font("Arial", Font.BOLD, 30);
			inputLabel.setText(game.getPlayer(i).getUser().getName());
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
			inputLabel.setText("Score: " + String.valueOf(scores[i]));
			inputLabel.setFont(inputFont);
			JPanel inputPanel = new JPanel();
			inputPanel.setBackground(Color.WHITE);
			inputPanel.add(inputLabel);
			inputPanel.setBorder(new EmptyBorder(10, 100, 10, 10));
			scorePanel.add(inputPanel);
		}

//		JButton newGameBtn = new JButton("New Game");
//		newGameBtn.setPreferredSize(new Dimension(200, 75));
//		newGameBtn.addActionListener(this);
//		Font inputFont = new Font("Arial", Font.BOLD, 30);
//		newGameBtn.setFont(inputFont);
//		JPanel newGamePanel = new JPanel();
//		newGamePanel.setBackground(Color.WHITE);
//		newGamePanel.add(newGameBtn);
//		newGamePanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		this.add(playerPanel);
		this.add(scorePanel);
//		this.add(newGamePanel);
	}
}
