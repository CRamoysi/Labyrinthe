import java.awt.Color;
/**
 * 
 */
/**
 * @author rc117
 *
 */
public interface DataL {
	/*
	 * Texte de la fenetre
	 */
	public static final String TITRE = "Labyrinthe";
	//CONSTANT IHM
	//MENU
	public static final String FILE = "Fichier";
	public static final String NEW = "Nouveau";
	public static final String LOAD = "Chargement";
	public static final String SAVE = "Sauvegader";
	public static final String SAVEIMG = "Generer image";
	public static final String QUIT = "Quitter";
	public static final String INTERO = "?";
	public static final String VERSION = "Version";
	public static final String IHM_QUITTER = "Etes-Vous sur(e) de vouloir quitter ?";
	public static final String IHM_NOUVEAU = "Nouveau";
	public static final String IHM_VERSION = "Version 1.8\nPar Yayilkan Nurbanu et Civel Romain\n\nTous droits réservés";
	//POUTIL
	public static final String GENERATE = "Generation";
	public static final String TO_GENERATE = "Generer";
	public static final String GENERATE_FUSION = "Fusion de chemin";
	public static final String GENERATE_EXPLORATION = "Exploration exhaustive";
	public static final String REGENERATE = "Regenerer";
	
	public static final String LABY_WIDTH = "Largeur";
	public static final String LABY_HEIGHT = "Hauteur";
	public static final String LABY_NB_OUTPUT = "Nombre de sorties";
	public static final String LABY_NB_MINOTAURE = "Nombre de minotaures";
	
	public static final String RESOLVE = "Resolution";
	public static final String TO_RESOLVE = "Resoudre";
	public static final String DFS = "DFS";
	public static final String DIJKSTRA = "Dijkstra";
	public static final String LEFT_HAND = "Main gauche";
	public static final String RIGHT_HAND = "Main droite";
	public static final String ALEA = "Au hasard";
	//LEGENDE
	public static final String L_LEGEND = "Legende";
	
	public static final String L_WAY_DFS = "Chemin DFS";
	public static final String L_WAY_DIJKSTRA = "Plus court chemin";
	public static final String L_VISITED = "Visitée";
	public static final String L_MINOTAURE = "Minotaures";
	public static final String L_OUTPUT = "Sorties";
	public static final String L_INPUT = "Entrée";
	public static final String L_THESEE = "Thésée";
	
	public static final Color L_C_WAY_DFS = Color.YELLOW;
	public static final Color L_C_WAY_DIJKSTRA = Color.PINK;
	public static final Color L_C_VISITED = Color.LIGHT_GRAY;
	public static final Color L_C_MINOTAURE = Color.GREEN;
	public static final Color L_C_OUTPUT = Color.BLUE;
	public static final Color L_C_INPUT = Color.RED;
	public static final Color L_C_THESEE = Color.BLACK;
	
	public static final Color C_WALL = Color.BLACK;
	public static final Color C_BACKGROUND = Color.white;
		
	/*
	 * Format fichier
	 */
	//Fichier labyrinthe
	public static final String F_FORMAT = ".lb";
	public static final String F_TYPE = "Labyrinthe(*.lb)";
	
	public static final String F_DELIM = ":";
	public static final String F_DIMENSION = "dimension";
	public static final String F_INPUT = "input";
	public static final String F_OUTPUT = "output";
	public static final String F_THESEE = "thesee";
	public static final String F_MINOTAURE = "minotaure";
	public static final String F_TRANSITION = "transition";
	
	public static final String F_ERROR_FORMAT = "Error: Format du fichier incompatible";
	
	//Fichier Image
	public static final String F_IFORMAT= ".png";
	public static final String F_ITYPE = "Image PNG (*.png)";
	
	public static final int F_I_C_WIDTH = 12;
	public static final int F_I_C_HEIGHT = 12;
	//couleur rgba
	public static final int[] F_I_C_BACKGROUND = {255, 255, 255, 255};
	public static final int[] F_I_C_WALL = {0, 0, 0, 255};
	public static final int[] F_I_C_INPUT = {255, 0, 0, 255};
	public static final int[] F_I_C_OUTPUT = {0, 0, 255, 255};
	public static final int[] F_I_C_MINOTAURES = {0, 255, 0, 255};
	public static final int[] F_I_C_THESEE = {0, 155, 155, 255};
	public static final int[] F_I_C_TEST = {255, 0, 0, 255};
	
	
	
	/*
	 * Taille affichage
	 */
	public static final int WCASE = 12;//Largeur des cases
	public static final int HCASE = 12;//Hauteur des cases
	public static final int FXCASE = 5;//Position en X de la premiere case
	public static final int FYCASE = 5;//Position en Y de la premiere case	
}
