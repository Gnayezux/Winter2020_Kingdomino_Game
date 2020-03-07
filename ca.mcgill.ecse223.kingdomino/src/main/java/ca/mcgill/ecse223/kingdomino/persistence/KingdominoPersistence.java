package ca.mcgill.ecse223.kingdomino.persistence;

import ca.mcgill.ecse223.kingdomino.model.Kingdomino;

public class KingdominoPersistence {

	private static String filename = "data.kingdomino";

	public static void save(Kingdomino kingdomino) {
		PersistenceObjectStream.serialize(kingdomino);
	}

	public static Kingdomino load() {
		PersistenceObjectStream.setFilename(filename);
		Kingdomino kingdomino = (Kingdomino) PersistenceObjectStream.deserialize();
		// model cannot be loaded - create empty Kingdomino
		if (kingdomino == null) {
			kingdomino = new Kingdomino();
		} else {
			//kingdomino.reinitialize();
		}
		return kingdomino;
	}

	public static void setFilename(String newFilename) {
		filename = newFilename;
	}

}
