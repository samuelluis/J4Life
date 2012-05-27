package schema;

import models.Category;

public class Seed {
	public static void seed(){
		//Categories
		Category.create("4Life Transfer Factor");
		Category.create("Enumi Cuidado Personal");
		Category.create("Respaldo General y Bienestar General");
		Category.create("4Life Control de Peso");
	}
}
