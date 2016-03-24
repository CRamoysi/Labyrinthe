import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
/**
 * 
 */
/**
 * @author rc117
 * 
 */
public class ImgGenerator implements DataL {


	/*
	 * Constructor
	 */
	public ImgGenerator(Labyrinthe l){

		this.l = l;
	}

	public void generate(String filename) throws IOException{
		//Largeur et hauteur du fichier image
		int wsize = this.l.nbColonne * (F_I_C_WIDTH + 1) +1; 
		int hsize = this.l.nbLigne * (F_I_C_HEIGHT + 1) +1;

		BufferedImage img = new BufferedImage(wsize, hsize, BufferedImage.TYPE_INT_ARGB );
		File f = new File(filename);
		
		int currColor = (F_I_C_BACKGROUND[3] << 24) | (F_I_C_BACKGROUND[0] << 16) | (F_I_C_BACKGROUND[1] << 8) | F_I_C_BACKGROUND[2], cx, cy, c;
		for(int x = 0; x < wsize; x++){
			for(int y = 0; y < hsize; y++){
				
				if(x==0 || x==wsize-1 || y==0 || y==hsize-1){//bord
					currColor = (F_I_C_WALL[3] << 24) | (F_I_C_WALL[0] << 16) | (F_I_C_WALL[1] << 8) | F_I_C_WALL[2];
				}else if(x%(F_I_C_WIDTH+1)==0 && y%(F_I_C_HEIGHT+1)==0){//l'intersection est forcement un mur
					currColor = (F_I_C_WALL[3] << 24) | (F_I_C_WALL[0] << 16) | (F_I_C_WALL[1] << 8) | F_I_C_WALL[2];
				}else if(x%(F_I_C_WIDTH+1)==0){//cas ou on pourrait trouver un mur vertical
					//on va verifier si il y a une transition, si non, il y a un mur
					/*cherchons la case courrante et la suivante
						on est forcement au debut de la case suivante, soit: c = (x/(F_I_C_WIDTH+1))
						il faut donc trouver la transition entre c-1 et c
					*/
					cx =  x/(F_I_C_WIDTH+1);
					cy = (int)Math.floor(y/(F_I_C_HEIGHT+1));
					c = cy*this.l.nbColonne + cx;
					currColor = (F_I_C_WALL[3] << 24) | (F_I_C_WALL[0] << 16) | (F_I_C_WALL[1] << 8) | F_I_C_WALL[2];
					for(int i = 0; i < this.l.transitions.get(c).size(); i++){
						if(this.l.transitions.get(c).get(i).getY() == cy && this.l.transitions.get(c).get(i).getX() == cx-1){
							currColor = (F_I_C_BACKGROUND[3] << 24) | (F_I_C_BACKGROUND[0] << 16) | (F_I_C_BACKGROUND[1] << 8) | F_I_C_BACKGROUND[2];
						}
					}
				
				}else if(y%(F_I_C_HEIGHT+1)==0){//cas ou on pourrait trouver un mur horizontal
					//on va verifier si il y a une transition, si non, il y a un mur
					/*cherchons la case courrante et la suivante
						on est forcement au debut de la case suivante, soit: c = (y/(F_I_C_HEIGHT+1))
						il faut donc trouver la transition entre c-this.l.nbColonne et c
					*/
				
					cx =  (int)Math.floor(x/(F_I_C_WIDTH+1));
					cy = y/(F_I_C_HEIGHT+1);
					c = cy*this.l.nbColonne + cx;
					currColor = (F_I_C_WALL[3] << 24) | (F_I_C_WALL[0] << 16) | (F_I_C_WALL[1] << 8) | F_I_C_WALL[2];
					for(int i = 0; i < this.l.transitions.get(c).size(); i++){
						if(this.l.transitions.get(c).get(i).getY() == cy-1 && this.l.transitions.get(c).get(i).getX() == cx){
							currColor = (F_I_C_BACKGROUND[3] << 24) | (F_I_C_BACKGROUND[0] << 16) | (F_I_C_BACKGROUND[1] << 8) | F_I_C_BACKGROUND[2];
						}
					}

				}else{
					currColor = (F_I_C_BACKGROUND[3] << 24) | (F_I_C_BACKGROUND[0] << 16) | (F_I_C_BACKGROUND[1] << 8) | F_I_C_BACKGROUND[2];
				}
				img.setRGB(x, y, currColor);
			}
		}
		
		//Ajout de l'entrée
		currColor = (F_I_C_INPUT[3] << 24) | (F_I_C_INPUT[0] << 16) | (F_I_C_INPUT[1] << 8) | F_I_C_INPUT[2];
		for(int x = (int)this.l.input.getX()*(F_I_C_WIDTH+1)+1; x<((int)this.l.input.getX()+1)*(F_I_C_WIDTH+1); x++ ){
			for(int y = (int)this.l.input.getY()*(F_I_C_HEIGHT+1)+1; y<((int)this.l.input.getY()+1)*(F_I_C_HEIGHT+1); y++ ){
				img.setRGB(x, y, currColor);
			}
		}
		//Ajout des sorties
		currColor = (F_I_C_OUTPUT[3] << 24) | (F_I_C_OUTPUT[0] << 16) | (F_I_C_OUTPUT[1] << 8) | F_I_C_OUTPUT[2];
		for(int o = 0; o < this.l.output.size(); o++){
			for(int x = (int)this.l.output.get(o).getX()*(F_I_C_WIDTH+1)+1; x<((int)this.l.output.get(o).getX()+1)*(F_I_C_WIDTH+1); x++ ){
				for(int y = (int)this.l.output.get(o).getY()*(F_I_C_HEIGHT+1)+1; y<((int)this.l.output.get(o).getY()+1)*(F_I_C_HEIGHT+1); y++ ){
					img.setRGB(x, y, currColor);
				}
			}
		}
		//Ajout de minotaures
		currColor = (F_I_C_MINOTAURES[3] << 24) | (F_I_C_MINOTAURES[0] << 16) | (F_I_C_MINOTAURES[1] << 8) | F_I_C_MINOTAURES[2];
		for(int m = 0; m < this.l.minotaures.size(); m++){
			for(int x = (int)this.l.minotaures.get(m).getX()*(F_I_C_WIDTH+1)+1; x<((int)this.l.minotaures.get(m).getX()+1)*(F_I_C_WIDTH+1); x++ ){
				for(int y = (int)this.l.minotaures.get(m).getY()*(F_I_C_HEIGHT+1)+1; y<((int)this.l.minotaures.get(m).getY()+1)*(F_I_C_HEIGHT+1); y++ ){
					img.setRGB(x, y, currColor);
				}
			}
		}
		//Ajout de Thesée
		currColor = (F_I_C_THESEE[3] << 24) | (F_I_C_THESEE[0] << 16) | (F_I_C_THESEE[1] << 8) | F_I_C_THESEE[2];
		for(int x = (int)this.l.thesee.getX()*(F_I_C_WIDTH+1)+1; x<((int)this.l.thesee.getX()+1)*(F_I_C_WIDTH+1); x++ ){
			for(int y = (int)this.l.thesee.getY()*(F_I_C_HEIGHT+1)+1; y<((int)this.l.thesee.getY()+1)*(F_I_C_HEIGHT+1); y++ ){
				img.setRGB(x, y, currColor);
			}
		}
		
		ImageIO.write(img, "PNG", f);
		

	}







	private Labyrinthe l;



}
