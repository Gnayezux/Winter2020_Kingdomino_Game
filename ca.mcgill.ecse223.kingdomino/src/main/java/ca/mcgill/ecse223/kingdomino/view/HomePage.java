package ca.mcgill.ecse223.kingdomino.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;

/**
 * This is the homepage, the first panel that the user interacts with 
 * @author Mathieu-Joseph Magri
 */
public class HomePage extends JPanel {

	private KingdominoBoardGame frame;
	public HomePage(KingdominoBoardGame frame) {
		this.frame = frame;
		initComponents();
	}

	private void initComponents() {
		this.setBackground(Color.WHITE);
		this.setLayout(new GridLayout(3, 1));

		// kingdomino image
		BufferedImage myPicture = null;
		try {
			myPicture = ImageIO.read(new File("src/main/resources/images/kingdomino.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Image newImage = myPicture.getScaledInstance(600, 200, Image.SCALE_DEFAULT);
		JLabel picLabel = new JLabel(new ImageIcon(newImage));

		JPanel p1 = new JPanel();
		p1.setLayout(new GridLayout(0, 1, 0, 10));
		p1.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
		p1.setBackground(Color.WHITE);
		
		JButton btn1 = new JButton("New Game");
		btn1.setPreferredSize(new Dimension(200, 75));
		btn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				KingdominoController.newGame();
			}
		});
		Font font = new Font("Arial", Font.BOLD, 25);
		btn1.setFont(font);
		JPanel btnPanel = new JPanel();
		btnPanel.setBackground(Color.WHITE);
		btnPanel.add(btn1);
		p1.add(btnPanel);
		
		JButton btn2 = new JButton("Load Game");
		btn2.setPreferredSize(new Dimension(200, 75));
		btn2.setEnabled(false);
		btn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO
			}
		});
		btn2.setFont(font);
		JPanel btnPanel2 = new JPanel();
		btnPanel2.setBackground(Color.WHITE);
		btnPanel2.add(btn2);
		p1.add(btnPanel2);
		
		JButton btn3 = new JButton("Rules");
		btn3.setPreferredSize(new Dimension(200, 75));
		btn3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.rulesPage();
			}
		});
		btn3.setFont(font);
		JPanel btnPanel3 = new JPanel();
		btnPanel3.setBackground(Color.WHITE);
		btnPanel3.add(btn3);
		p1.add(btnPanel3);

		// add image and button panel to main panel, then add main panel to frame
		this.add(picLabel);
		this.add(p1);
		
	}

}
