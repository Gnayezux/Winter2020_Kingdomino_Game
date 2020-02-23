package ca.mcgill.ecse223.kingdomino;

import ca.mcgill.ecse223.kingdomino.model.Kingdomino;

public class KingdominoApplication {
	
	private static Kingdomino kingdomino; 
	
    public static void main(String[] args) {
		kingdomino = new Kingdomino();
	}
    
	public static Kingdomino getKingdomino() {
		if (kingdomino == null) {
			kingdomino = new Kingdomino();
		}
 		return kingdomino;
	}
}
