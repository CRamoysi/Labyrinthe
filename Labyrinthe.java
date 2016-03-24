import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

/**
 * 
 */
/**
 * @author rc117)
 *
 */
public class Labyrinthe {
	/*
	 * Constructeurs
	 */
	public Labyrinthe(int nbColonne, int nbLigne){
		this.nbColonne = nbColonne;
		this.nbLigne = nbLigne;
		//instanciation de la matrice de transition
		minotaures = new ArrayList<Point>();
		//creation et instanciation du ou des sorties du labyrinthe
		output = new ArrayList<Point>();
		//creation du tableau de transitions
		transitions = new ArrayList<ArrayList<Point>>();
		for(int i = 0; i < nbColonne*nbLigne; i++){
			transitions.add(new ArrayList<Point>());
		}	
	}
	public Labyrinthe(int nbColonne, int nbLigne, int nbOutput, int nbMinotaure){
		this.nbColonne = nbColonne;
		this.nbLigne = nbLigne;
		transitions = new ArrayList<ArrayList<Point>>();
		for(int i = 0; i < nbColonne*nbLigne; i++){
			transitions.add(new ArrayList<Point>());
		}
		//instanciation de la matrice de transition
		//creation d'un generateur de nombre aleatoire
		Random ran = new Random();
		//creation et instanciation de l'entrée du labyrinthe. C'est aussi la position de Dedale qui part a la recherche de Thésee
		/*
		  * On ajoute une entrée
		  */
		{
			int x = ran.nextInt(2), y;
			if(x == 0){//entrée sur un axe horizontal
				 y = ran.nextInt(2);
				 if(y == 0){//a gauche
					input = new Point(ran.nextInt(nbColonne), 0); 
				 }else{//a droite
					 input = new Point(ran.nextInt(nbColonne), this.nbLigne-1); 
				 }
			}else{//entrée sur axe vertical
				 x = ran.nextInt(2);
				 if(x == 0){//en haut
					input = new Point( 0, ran.nextInt(nbLigne)); 
				 }else{//en bas
					 input = new Point(nbColonne-1, ran.nextInt(nbLigne)); 
				 }
			}
		}
		//creation et instanciation de Thesee qui est perdu dans le labyrinthe
		do{
			thesee = new Point(ran.nextInt(nbColonne), ran.nextInt(nbLigne));
		}while((thesee.x == input.x) && (thesee.y == input.y));
		//creation et instanciation du ou des minotaures
		minotaures = new ArrayList<Point>();
		for(int i = 0; i < nbMinotaure; i++){
			Point p = new Point(ran.nextInt(nbColonne), ran.nextInt(nbLigne));
			while(((p.x == thesee.x) && (p.y == thesee.y)) || ((p.x == input.x) && (p.y == input.y))){
				p = new Point(ran.nextInt(nbColonne), ran.nextInt(nbLigne));
			}
			minotaures.add(p);
		}
		//creation et instanciation du ou des sorties du labyrinthe
		output = new ArrayList<Point>();
		for(int i = 0; i < nbOutput; i++){
			Point p;
			do{
				int x = ran.nextInt(2), y;
				if(x == 0){//entrée sur un axe horizontal
					 y = ran.nextInt(2);
					 if(y == 0){//a gauche
						p = new Point(ran.nextInt(nbColonne),0); 
					 }else{//a droite
						p = new Point(ran.nextInt(nbColonne),nbLigne-1); 
					 }
				 }else{//entrée sur axe vertical
					 x = ran.nextInt(2);
					 if(x == 0){//en haut
						p = new Point(0, ran.nextInt(nbLigne)); 
					 }else{//en bas
						p = new Point(nbColonne-1, ran.nextInt(nbLigne)); 
					 }
				 }
			}while(((p.x == thesee.x) && (p.y == thesee.y)) || ((p.x == input.x) && (p.y == input.y)));
			output.add(p);
		}
	}
	/*
	 * generation du labyrinthe par un algorithme dit de fusion de chemins
	 */
	public void generate_fusion(){
		int count = 0;
		int[][] valeur = new int[nbLigne][nbColonne];
		for(int i = 0; i < nbLigne; i++){
			for(int j = 0; j < nbColonne; j++){
				valeur[i][j] = j + nbColonne*i;
			}
		}
		Random alea = new Random();
		Point p1;
		ArrayList<Point> succ = new ArrayList<Point>();
		int choix;
		boolean ok;
		while(count < nbColonne*nbLigne - 1){//tant qu'on a pas fini
			succ.clear();
			p1 = new Point(alea.nextInt(nbColonne), alea.nextInt(nbLigne));//recuperation d'un point au hasard
			//on recupere les cases autour
			if(p1.x > 0){
				succ.add(new Point(p1.x-1, p1.y));
			}
			if(p1.x < nbColonne-1){
				succ.add(new Point(p1.x+1, p1.y));
			}
			if(p1.y > 0){
				succ.add(new Point(p1.x, p1.y-1));
			}
			if(p1.y < nbLigne-1){
				succ.add(new Point(p1.x, p1.y+1));
			}
			ok = false;
			while(succ.size() > 0 && !ok){//tant qu'on ne peux pas casser de mur ou qu'il reste un mur adjacent a tester
				choix = alea.nextInt(succ.size());
				int lV = valeur[succ.get(choix).y][succ.get(choix).x];//valeur de la case a verifié
				if(valeur[p1.y][p1.x] != lV){//si on peux casser le mur
					for(int r = 0; r < nbLigne; r++){//on redefini tout les id
						for(int c = 0; c < nbColonne; c++){
							if(valeur[r][c] == lV){
								valeur[r][c] = valeur[p1.y][p1.x];
							}
						}
					}
					//rajout dans la matrice de transition
					transitions.get(p1.x + (p1.y)*nbColonne).add(new Point(succ.get(choix).x, succ.get(choix).y));
					transitions.get(succ.get(choix).x + (succ.get(choix).y)*nbColonne).add(new Point(p1.x, p1.y));
					ok = true;
					count++;
				}else{//sinon on peux deja acceder a cette case donc on la jette
					succ.remove(choix);
				}
			}
		}
	}
	/*
	 * Generation du labyrinthe par la methode d'exploration exhaustive
	 */
	public void generate_explo(){
		// variables locales
		int choix;
		boolean[][] visited = new boolean[nbLigne][nbColonne];
		// un historique pour revenir sur l'element precedent
		// dans le cas où une case nbLigne'aura plus de case voisine a explorer
		ArrayList<Point> historique = new ArrayList<Point>();
		// on commence par choisir une case parmi toutes les cases du labyrinthe et on indique qu'elle est visitée
		// on utilise pour cela un random qui va choisir une case du labyrinthe au hasard
		Random aleatoire=new Random(); 
		// on stocke la valeur pour la colonne et la ligne
		//input = new Point(0,0);
		//output = new Point(this.nbLigne-1,this.nbColonne-1);
		int ligne =  input.x;//(int) aleatoire.nextInt(this.nbLigne);
		int colonne =  input.y;//(int) aleatoire.nextInt(this.nbColonne);
		// ensuite on marque la case choisit comme visité
		visited[ligne][colonne] = true;
		// et on l'ajoute dans l'historique
		historique.add(new Point(ligne, colonne));
		// dans un autre tableau on va stocker les cases voisines et qui ne sont pas encore visitées
		// on cree la list des cases voisine
		ArrayList<Point> voisine = new ArrayList<Point>();
		//ArrayList<Integer> direction = new ArrayList<Integer>();
		do{
			// NORD
			if ( ligne >0  && !visited[ligne-1][colonne] ){
				voisine.add( new Point(ligne-1,colonne) );
			}
			// OUEST
			if ( colonne >0  && !visited[ligne][colonne-1] ){
				voisine.add( new Point(ligne, colonne-1) );
			}
			// SUD
			if ( ligne < (this.nbLigne-1)  && !visited[ligne+1][colonne] ){
				voisine.add( new Point(ligne+1, colonne) );
			}
			// EST
			if ( colonne < (this.nbColonne-1)  && !visited[ligne][colonne+1] ){
				voisine.add( new Point(ligne, colonne+1) );
			}
			// s'il existe au moins une case a visiter
			if( voisine.size() > 0 ){
				// on pioche au hasard une case voisine
				choix = aleatoire.nextInt(voisine.size());
				// on recupere la case se trouvant a la choix'ieme place dans la liste voisine
				// et on indique true pour visited
				visited[voisine.get(choix).x][voisine.get(choix).y] = true;
				// ensuite on ajoute l'arc correspond dans la matrice
				this.transitions.get(voisine.get(choix).x + voisine.get(choix).y*this.nbColonne).add(new Point(ligne, colonne));
				this.transitions.get(ligne + colonne*this.nbColonne).add(new Point(voisine.get(choix).x, voisine.get(choix).y ));
				// on change les coordonnees de la ligne et de la colonne avec celle de la case choisie
				ligne = voisine.get(choix).x;
				colonne = voisine.get(choix).y;
				// on ajoute la case a l'historique
				historique.add(new Point(voisine.get(choix).x, voisine.get(choix).y));
			}else{
				// sinon on enleve le dernier element de l'historique
				historique.remove( historique.size()-1);
				// on remet dans ligne et colonne l'element se trouvant a la derniere position
				if( historique.size() >0 ){
					ligne = historique.get(historique.size()-1).x;
					colonne = historique.get(historique.size()-1).y;
				}
			}
			// et on reeffectue une recherche de cases voisines tant que l'historique nbLigne'est pas vide
			// pour cela on vide d'abord la liste des case voisine pour chaque nouvelle recherche
			voisine.clear();
			//direction.clear();
		}
		while(  historique.size() > 0);
	}	
	/*
	 * toString
	 */
	public String toString(){
		String l = "";
		return l;
	}
	/*
	 * Variables de class
	 */
	ArrayList<ArrayList<Point>> transitions;
	Point input;// l'entrée du labyrinthe
	ArrayList<Point> output;//sorties du labyrinthe
	Point thesee;
	ArrayList<Point> minotaures;//les minotaure dans le labyrinthe
	int nbColonne, nbLigne;//taille du labyrinthe
}
