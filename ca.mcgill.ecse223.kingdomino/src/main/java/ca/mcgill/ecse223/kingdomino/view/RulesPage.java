package ca.mcgill.ecse223.kingdomino.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class RulesPage extends JPanel {

	KingdominoBoardGame frame;
	public RulesPage(KingdominoBoardGame frame) {
		this.frame = frame;
		initComponents();
	}

	private void initComponents() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		BufferedImage myPicture = null;
		try {
			myPicture = ImageIO.read(new File("src/main/resources/images/rules.png"));
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
		this.add(picLabel, c);

		JButton btn = new JButton("Back");
		btn.setPreferredSize(new Dimension(200, 75));
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.homePage();
			}
		});
		Font font = new Font("Arial", Font.BOLD, 25);
		btn.setFont(font);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 0; // reset to default
		c.weighty = 1.0; // request any extra vertical space
		c.anchor = GridBagConstraints.PAGE_END; // bottom of space
		c.gridx = 0; // aligned with button 2
		c.gridwidth = 1; // 2 columns wide
		c.gridy = 0; // third row
		this.add(btn, c);
		this.setBackground(Color.WHITE);
	}
}
