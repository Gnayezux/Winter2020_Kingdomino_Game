package ca.mcgill.ecse223.kingdomino.view;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

import ca.mcgill.ecse223.kingdomino.model.*;
import ca.mcgill.ecse223.kingdomino.model.Draft.DraftStatus;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;

/**
 * A Draft Panel contains the draftdominos
 * They are updated differently depending whether it is the "current" draft or the "next" draft 
 * @author victoria_iannotti
 *
 */
public class DraftPanel extends JPanel{

	private boolean isCurrent;
	private ArrayList<DraftDomino> dominos;
	
	public DraftPanel(boolean isCurrent) {
		this.isCurrent = isCurrent;
		initComponents();
	}

	private void initComponents() {
		GridLayout layout = new GridLayout(4,1);
		layout.setVgap(8);
		this.setPreferredSize(new Dimension(150, 75));
		this.setLayout(layout);
//		for(int i = 0; i<4; i++) {
//			DraftDomino d = new DraftDomino(1, "FaceDown");
//			d.setPreferredSize(new Dimension(75, 50));
//			this.add(d);
//		}
		updateEmpty();
	}
	
	public DraftPanel update(Draft draft) {
		dominos = new ArrayList<DraftDomino>();
		this.removeAll();
		this.setBackground(Color.WHITE);
		for(int i = 0; i<4; i++) {
			DraftDomino d;
			if(isCurrent) {
				d = new DraftDomino(draft.getIdSortedDominos().get(i).getId(), true);
				d.setColor(draft.getGame().getPlayer(i).getColor());
			} else {
				d = new DraftDomino(draft.getIdSortedDominos().get(i).getId(), false);
			}
			
			d.setPreferredSize(new Dimension(75, 75));
			dominos.add(d);
			this.add(d);
		}
		this.setVisible(true);
		return this;
	}
	
	public void updateEmpty() {
		this.removeAll();
		this.setBackground(Color.WHITE);
		for(int i = 0; i<4; i++) {
			JPanel pan = new JPanel();
			pan.setPreferredSize(new Dimension(150, 75));
			this.add(pan);
		}
		this.setVisible(true);
	}
	
	public void setDominoSelectionEnabled(boolean enabled) {
		for(DraftDomino d: dominos) {
			d.setDominoSelectionEnabled(enabled);
		}
	}
	public void changeButtonColor(PlayerColor color, int id) {
		for(DraftDomino d: dominos) {
			if(d.getID() == id) {
				d.setColor(color);
				break;
			}
		}
	}

}
