import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import javax.swing.JPanel;
/**
 * 
 */

/**
 * @author rc117
 *
 */
public class PanelLabyrinthe extends JPanel implements DataL {
	/*
	 * Constructeurs
	 */
	public PanelLabyrinthe(){
		super();
		this.choixResolution="";    
		this.cheminDFS = new ArrayList<Point>();
		this.cheminPCC = new ArrayList<Point>();
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		this.setBackground(Color.white);

		if( l!=null ){
			printLabyrinthe(g);
			
			if( this.cheminDFS.size() > 0 ){
				for(int i=0 ; i < this.cheminDFS.size(); i++){
					this.drawCase(this.cheminDFS.get(i), L_C_WAY_DFS, g);
				}
			}
			if( this.cheminPCC.size() > 0 ){
				for(int i=0 ; i < this.cheminPCC.size(); i++){
					this.drawCase(this.cheminPCC.get(i), L_C_WAY_DIJKSTRA, g);
				}
			}
		}
	}
	
	
	/*
	 * Generation
	 */
	public void generation(String typeOfGen, int x, int y, int nbOutput, int nbMinotaure){
		l = new Labyrinthe(x,y, nbOutput, nbMinotaure);
		if(typeOfGen.equals(GENERATE_FUSION)){
			l.generate_fusion();
			this.setPreferredSize(new Dimension(l.nbColonne*WCASE+FYCASE+FYCASE, l.nbLigne*HCASE+FXCASE+FXCASE));
		}else if(typeOfGen.equals(GENERATE_EXPLORATION)){
			l.generate_explo();
			this.setPreferredSize(new Dimension(l.nbColonne*WCASE+FYCASE+FYCASE, l.nbLigne*HCASE+FXCASE+FXCASE));
		}
	}
	/*
	 * Resolution
	 */
	public void resolution(String typeOfRes){
		if(typeOfRes.equals(DFS)){
			boolean[][] visited = new boolean[l.nbLigne][l.nbColonne];
			ArrayList<Point> th = new ArrayList<Point>();
			th.add(l.thesee);
			dfs(l.input, th, visited);
			visited = new boolean[l.nbLigne][l.nbColonne];
			dfs(l.thesee, l.output, visited);
		}else if(typeOfRes.equals(DIJKSTRA)){
			int[][] valeur = new int[l.nbLigne][l.nbColonne];
			/*
			 * Thesee vers la sortie
			 */
			for(int i = 0; i < l.nbColonne; i++){
				for(int j = 0; j < l.nbLigne; j++){
					valeur[j][i] = l.nbColonne*l.nbLigne + 100;
				}
			}
			valeur[l.thesee.y][l.thesee.x] = 0;
			dijkstra(l.thesee, valeur, 0);
			printDijkstra(valeur, l.output);
			/*
			 * Entree vers thesee
			 */
			for(int i = 0; i < l.nbColonne; i++){
				for(int j = 0; j < l.nbLigne; j++){
					valeur[j][i] = l.nbColonne*l.nbLigne + 100;
				}
			}
			valeur[l.input.y][l.input.x] = 0;
			dijkstra(l.input, valeur, 0);
			ArrayList<Point> t = new ArrayList<Point>();
			t.add(l.thesee);
			printDijkstra(valeur, t);
		}
	}
	/*
	 * Affichage du labyrinthe
	 */
	public void printLabyrinthe(Graphics g){
		//clean du panel
		g.setColor(C_BACKGROUND);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		for(int i = 0; i <= l.nbColonne; i++){
			this.drawLine(new Point(i, 0), new Point(i, l.nbLigne), C_WALL, g);
		}
		for(int i = 0; i <= l.nbLigne; i++){
			this.drawLine(new Point(0, i), new Point(l.nbColonne, i), C_WALL, g);
		}
		int dx, dy;
		for(int i = 0; i < l.transitions.size(); i++){
			for(int j = 0; j < l.transitions.get(i).size(); j++){
				dx = (int)i%l.nbColonne;
				dy = (int)Math.floor(i/l.nbColonne);
				//creation des x du mur a blanchir
				if(dx > l.transitions.get(i).get(j).x || dy > l.transitions.get(i).get(j).y){
					this.drawLine(new Point(dx, dy), new Point(l.transitions.get(i).get(j).x+1, l.transitions.get(i).get(j).y+1), C_BACKGROUND, g);
				}else{
					this.drawLine(new Point(dx+1, dy+1), new Point(l.transitions.get(i).get(j).x, l.transitions.get(i).get(j).y), C_BACKGROUND, g);
				}
			}
		}
		//Affichage de l'entrée
		this.drawCase(l.input, L_C_INPUT, g);
		//Affichage des sorties
		for(int i = 0; i < l.output.size(); i++){
			this.drawCase(l.output.get(i), L_C_OUTPUT, g);
		}
		//Affichage des minotaures
		for(int i = 0; i < l.minotaures.size(); i++){
			this.drawCase(l.minotaures.get(i), L_C_MINOTAURE, g);
		}
		this.drawCase(l.thesee, L_C_THESEE, g);
	}	
	/*
	 * Affiche une ligne entre deux cases
	 */
	public void drawLine(Point a, Point b, Color typeOfLine, Graphics g){
		g.setColor(typeOfLine);
		g.drawRect(FXCASE + a.x*WCASE, FYCASE+ a.y*HCASE, (b.x-a.x)*WCASE, (b.y-a.y)*HCASE);
	}
	/*
	 * Affichage du contenu d'une case
	 */
	public void drawCase(Point a, Color typeOfCase, Graphics g){
		g.setColor(typeOfCase);
		g.fillRect(FXCASE+1 + WCASE*a.x, FYCASE+1 + HCASE*a.y, WCASE-1, HCASE - 1);
	}
	/*
	 * Pour eviter de reecrire par dessus entrée, sorties, minotaures...
	 */
	public boolean occuped(Point p){
		if(p.equals(l.input) || l.output.contains(p) || l.minotaures.contains(p) || p.equals(l.thesee)){
			return true;
		}
		return false;
	}
	/*
	 * Pour savoir si il y a un minotaures en face
	 */
	public boolean minotaure(Point p){
		if(l.minotaures.contains(p)){
			return true;
		}
		return false;
	}
	/*
	 * DFS
	 */
	boolean dfs(Point debut, ArrayList<Point> fin, boolean[][]visited){
		if(fin.contains(debut)){
			return true;
		}
		try{
			ArrayList<Point> succ = new ArrayList<Point>(l.transitions.get(debut.x + debut.y*l.nbColonne));
			while(!(succ.isEmpty())){
				if(!minotaure(succ.get(0)) && !(visited[succ.get(0).y][succ.get(0).x])){	
					visited[succ.get(0).y][succ.get(0).x]= true; 
					if(dfs(succ.get(0), fin, visited)){
						if(!this.occuped(succ.get(0))){
							this.cheminDFS.add(succ.get(0));
						}
						return true;
					}
				}
				succ.remove(0);
			}
			succ.clear();
		}
		catch(StackOverflowError e){
			System.out.println("Le chemin ne peut etre affiché en entier à cause d'un Overflow :  " + e.getMessage());
			return true;
		}
		return false;
	}

	/*
	 *  LE PLUS COURT CHEMIN PAR DIJKSTRA 
	 */
	public int dijkstra(Point debut, int[][]valeur, int etape){
		if(etape >= (l.nbColonne*l.nbLigne-1)){
			return etape;
		}
		try{
			ArrayList<Point> succ = new ArrayList<Point>(l.transitions.get(debut.x + debut.y*l.nbColonne));
			while(!(succ.isEmpty())){
				if(!minotaure(succ.get(0)) && (valeur[succ.get(0).y][succ.get(0).x] > 1+valeur[debut.y][debut.x])){
					valeur[succ.get(0).y][succ.get(0).x] = 1+valeur[debut.y][debut.x];
					if((etape=dijkstra(succ.get(0), valeur, etape+1)) >= (l.nbColonne*l.nbLigne-1)){
						return etape;
					}
				}else if(minotaure(succ.get(0))){
					etape++;
				}
				
				succ.remove(0);
			}
		}catch(StackOverflowError e){
			System.out.println("Le chemin ne peut etre affiché à cause d'un Overflow :  " + e.getMessage());
		}
		return etape;
	}
	/*
	 * Affiche le plus court chemin
	 */
	public void printDijkstra(int[][]valeur, ArrayList<Point> last){

			Point mLast = last.get(0);
			for(int i = 1; i < last.size(); i++){
				if(valeur[mLast.y][mLast.x] > valeur[last.get(i).y][last.get(i).x]){
					mLast = last.get(i);
				}
			}
			Point pNext = new Point(mLast.x, mLast.y);
			ArrayList<Point> succ;
			boolean suite = true;;
			while(suite && valeur[pNext.y][pNext.x] > 0){
				succ = new ArrayList<Point>(l.transitions.get(pNext.x + pNext.y*l.nbColonne));
				suite = false;
				while(!(succ.isEmpty())){
					if(valeur[pNext.y][pNext.x] > valeur[succ.get(0).y][succ.get(0).x]){
						pNext = new Point(succ.get(0));
						suite = true;
					}
					succ.remove(0);
				}
				if(!this.occuped(pNext)){
					this.cheminPCC.add(pNext);
				}	
			}
		
	}
	/*
	 * Variables
	 */
	Labyrinthe l = null;
	private static final long serialVersionUID = 117L;
	ArrayList<Point> cheminDFS;
	ArrayList<Point> cheminPCC;
	Point pSource, pTarget;
	String choixResolution;
}
