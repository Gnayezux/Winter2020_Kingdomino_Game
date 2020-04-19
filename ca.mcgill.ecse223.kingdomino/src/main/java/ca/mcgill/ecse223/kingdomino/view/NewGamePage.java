package ca.mcgill.ecse223.kingdomino.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;

/**
 * Panel for the setup of users for a new game
 * @author Zeyang Xu
 *
 */
public class NewGamePage extends JPanel {

	ArrayList<JTextField> playerInput = new ArrayList<>();
	KingdominoBoardGame frame;
	public NewGamePage(KingdominoBoardGame frame) {
		this.frame = frame;
		initComponents();
	}

	private void initComponents() {
		this.setBackground(Color.WHITE);
		this.setLayout(new GridLayout(0, 3));

		JButton btn = new JButton("Back");
		btn.setPreferredSize(new Dimension(200, 75));
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.newGamePage();
			}
		});
		Font font = new Font("Arial", Font.BOLD, 25);
		btn.setFont(font);
		JPanel btnPanel = new JPanel();
		btnPanel.setBackground(Color.WHITE);
		btnPanel.add(btn);
		btnPanel.setBorder(new EmptyBorder(100, 10, 10, 10));
		this.add(btnPanel);

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

			JTextField userText = new JTextField(20);
			userText.setPreferredSize(new Dimension(200, 50));
			playerInput.add(userText);
			inputPanel.add(userText);

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
		startGameBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				KingdominoController.startGame(getUsernames());
			}
		});
		Font startGameFont = new Font("Arial", Font.BOLD, 25);
		btn.setFont(startGameFont);
		startGamePanel.add(startGameBtn);

		JButton browseDominoBtn = new JButton("Browse Dominos");
		browseDominoBtn.setPreferredSize(new Dimension(200, 75));
		browseDominoBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.browseDominos();
			}
		});
		Font browseDominoFont = new Font("Arial", Font.BOLD, 25);
		btn.setFont(browseDominoFont);
		startGamePanel.add(browseDominoBtn);

		middle.add(startGamePanel);

		this.add(middle);
	}
	
	private String[] getUsernames() {
		String [] users = new String[4];
		int index = 0;
		for(JTextField t: playerInput) {
			users[index] = t.getText();
			index++;
		}
		return users;
	}

}
