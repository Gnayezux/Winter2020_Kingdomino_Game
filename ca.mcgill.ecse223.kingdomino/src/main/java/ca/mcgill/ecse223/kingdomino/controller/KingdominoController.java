package ca.mcgill.ecse223.kingdomino.controller;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.model.*;
import java.util.*;

public class KingdominoController {
	
	public KingdominoController() {
		
	}
	public static boolean VerifyGridSize(Kingdom aKingdom) {
		int maxX = 0;
		int maxY = 0;
		int minX = 0;
		int minY = 0;
		int x = 0,y = 0,x2 = 0,y2 = 0;
		for (KingdomTerritory d: aKingdom.getTerritories()) {
			if (d.getClass().toString() == "DominoInKingdom") {
				DominoInKingdom dik = (DominoInKingdom) d;
				x = dik.getX();
				y = dik.getY();
				switch (dik.getDirection()) {
				case Up:
					y2 = dik.getY()+1;
					x2 = dik.getX();
				case Left:
					x2 = dik.getX()-1;
					y2 = dik.getY();
				case Right:
					x2 = dik.getX()+1;
					y2 = dik.getY();
				case Down:
					x2 = dik.getX();
					y2 = dik.getY()-1;
					}
				if (x < minX) {
					minX = x;
				}
				if (x > maxX) {
					maxX = x;
				}
				if (x2 < minX) {
					minX = x2;
				}
				if (x2 > maxX) {
					maxX = x2;
				}
				
				if (y < minY) {
					minY = y;
				}
				if (y > maxY) {
					maxY = y;
				}
				if (y2 < minY) {
					minY = y2;
				}
				if (y2 > maxY) {
					maxY = y2;
				}

			}
	
		}
		return ((maxX - minX) < 5 && (maxY - minY) < 5);
	}
}
