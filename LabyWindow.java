import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
/**
 * 
 */
/**
 * @author rc117
 * 
 */
public class LabyWindow extends JFrame implements DataL {
		/*
		 * Constructor
		 */
		public LabyWindow(){
			super(TITRE);//Title of the Window 
			//Redimensionne la fenetre (plein ecran)
			this.pack();
			LabyWindow.setDefaultLookAndFeelDecorated(true);
			this.setExtendedState(Frame.MAXIMIZED_BOTH);
			//Fermeture de la fenetre
			this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			//Initialisation de la fenetre
			this.initialiseMenu();
			//Fenetre visible
			this.initAffichage();
			this.setVisible(true);
		}
		/*
		 * JMENU
		 */
		//INITIALISATION DU JMENU	
		public void initialiseMenu(){
			menu = new JMenuBar();//creation du JMenu
			this.setJMenuBar(menu);
			menuAction = new ActionMenu();//creation de l'ecouteur du JMenu
				JMenu fichier = new JMenu(FILE);//Menu fichier
				menu.add(fichier);
					JMenuItem nouveau = new JMenuItem(NEW);//Option nouveau de Fichier
					fichier.add(nouveau);
						nouveau.addActionListener(menuAction);//Ecouteur de nouveau

					JMenuItem save = new JMenuItem(SAVE);//Option nouveau de Fichier
					fichier.add(save);
						save.addActionListener(menuAction);//Ecouteur de nouveau
					
					JMenuItem load = new JMenuItem(LOAD);//Option charger de Fichier
					fichier.add(load);
						load.addActionListener(menuAction);//Ecouteur de nouveau
					
					JMenuItem saveAsImg = new JMenuItem(SAVEIMG);//Option save as image de Fichier
					fichier.add(saveAsImg);
						saveAsImg.addActionListener(menuAction);//Ecouteur de nouveau
							
						
					JMenuItem quitter = new JMenuItem(QUIT);//Option quitter de Fichier
					fichier.add(quitter);
						quitter.addActionListener(menuAction);//Ecouteur de quitter
				JMenu aPropos = new JMenu(INTERO);//Menu ?
				menu.add(aPropos);
					JMenuItem version = new JMenuItem(VERSION);//Option version de ?
					aPropos.add(version);
						version.addActionListener(menuAction);//Ecouteur de Version
		}
		/*
		 * Pour sauvegarder un labyrinthe
		 */
		public void save(){
			File repertoireCourant = null;
			try {
				repertoireCourant = new File(".").getCanonicalFile();
			} catch(IOException exep) {}
			JFileChooser dialogue = new JFileChooser(repertoireCourant);
			FileNameExtensionFilter filter = new FileNameExtensionFilter(F_TYPE, F_FORMAT);
			dialogue.setFileFilter(filter);
			dialogue.setDialogTitle(SAVE);
			dialogue.setApproveButtonText(SAVE) ;
			int returnVal = dialogue.showOpenDialog(null);
			if(returnVal == JFileChooser.APPROVE_OPTION){
				String fileFin = dialogue.getSelectedFile().getPath();
				if(!dialogue.getSelectedFile().getName().substring(dialogue.getSelectedFile().getName().lastIndexOf('.')+1, dialogue.getSelectedFile().getName().length() ).equals("i3d")){
					fileFin = fileFin+ F_FORMAT;	
				}
				//on met try si jamais il y a une exception
				try{
					/**
					 * BufferedWriter a besoin d un FileWriter, 
					 * les 2 vont ensemble, on donne comme argument le nom du fichier
					 * true signifie qu on ajoute dans le fichier (append), on ne marque pas par dessus
					 */
					FileWriter fw = new FileWriter(fileFin, true);
					// le BufferedWriter output auquel on donne comme argument le FileWriter fw cree juste au dessus
					BufferedWriter output = new BufferedWriter(fw);
					//on marque dans le fichier ou plutot dans le BufferedWriter qui sert comme un tampon(stream)
					//dimension du labyrinthe
					output.write(F_DIMENSION + F_DELIM + pLab.l.nbColonne + F_DELIM +pLab.l.nbLigne+"\n");
					//matrice de transition
					for(int i = 0; i < pLab.l.transitions.size(); i++){
						output.write(F_TRANSITION + F_DELIM + i );
						for(int j = 0; j < pLab.l.transitions.get(i).size(); j++){
							output.write(F_DELIM + pLab.l.transitions.get(i).get(j).x + F_DELIM + pLab.l.transitions.get(i).get(j).y);
						}
						output.write("\n");
					}
					//entrée du labyrinthe
					output.write(F_INPUT + F_DELIM + pLab.l.input.x + F_DELIM + pLab.l.input.y + "\n");
					//Minotaures
					for(int i = 0; i < pLab.l.minotaures.size(); i++){
						output.write(F_MINOTAURE + F_DELIM + pLab.l.minotaures.get(i).x + F_DELIM + pLab.l.minotaures.get(i).y + "\n");
					}
					//thesee
					output.write(F_THESEE + F_DELIM + pLab.l.thesee.x + F_DELIM + pLab.l.thesee.y + "\n");
					//sortie
					for(int i = 0; i < pLab.l.output.size(); i++){
						output.write(F_OUTPUT + F_DELIM + pLab.l.output.get(i).x + F_DELIM + pLab.l.output.get(i).y + "\n");
					}
					output.flush();
					//ensuite flush envoie dans le fichier	
					output.close();
					//et on le ferme
					System.out.println("fichier créé");
				}
				catch(IOException ioe){
					ioe.printStackTrace();
				}
			}	
		}
		/*
		* Permet de charger un fichier<br/>
		*/
		public void load(){
			File repertoireCourant = null;
			try {
				// obtention d'un objet File qui désigne le répertoire courant. Le
				// "getCanonicalFile" n'est pas absolument nécessaire mais permet
				// d'éviter les /Truc/./Chose/ ...
				repertoireCourant = new File(".").getCanonicalFile();
			} catch(IOException exep) {}
			// création de la boîte de dialogue dans ce répertoire courant
			// (ou dans "home" s'il y a eu une erreur d'entrée/sortie, auquel
			// cas repertoireCourant vaut null)
			JFileChooser dialogue = new JFileChooser(repertoireCourant);
			//FileNameExtensionFilter filter = new FileNameExtensionFilter(F_TYPE, F_FORMAT);
			//dialogue.setFileFilter(filter);
			dialogue.setDialogTitle(LOAD);
			dialogue.setApproveButtonText(LOAD) ;
			int returnVal = dialogue.showOpenDialog(null);
			if(returnVal == JFileChooser.APPROVE_OPTION){
				//lecture du fichier texte	
				try{
					InputStream ips=new FileInputStream(dialogue.getSelectedFile()); 
					InputStreamReader ipsr=new InputStreamReader(ips);
					BufferedReader br=new BufferedReader(ipsr);
					String ligne = br.readLine(), tok;
					StringTokenizer st;
					System.out.println(ligne);
					st = new StringTokenizer(ligne, F_DELIM);
					
					if(st.nextToken().equals(F_DIMENSION)){
						pLab.l = new Labyrinthe(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
						pLab.setPreferredSize(new Dimension(pLab.l.nbColonne*WCASE+FYCASE+FYCASE, pLab.l.nbLigne*HCASE+FXCASE+FXCASE));
					}else{
						System.out.println(F_ERROR_FORMAT);
					}
					while ((ligne=br.readLine())!=null){
						//System.out.println(ligne);
						st = new StringTokenizer(ligne, F_DELIM);
						tok = st.nextToken();
						if(tok.equals(F_INPUT)){
							pLab.l.input = new Point(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
						}else if(tok.equals(F_OUTPUT)){
							pLab.l.output.add(new Point(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken())));
						}else if(tok.equals(F_THESEE)){
							pLab.l.thesee = new Point(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
						}else if(tok.equals(F_MINOTAURE)){
							pLab.l.minotaures.add(new Point(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken())));
						}else if(tok.equals(F_TRANSITION)){
							int id = Integer.parseInt(st.nextToken());
							while(st.hasMoreTokens()){
								pLab.l.transitions.get(id).add(new Point(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken())));
								
							}
						}else{
							System.out.println(F_ERROR_FORMAT);
						}
					}
					br.close(); 
				}		
				catch (Exception e){
					System.out.println(e.toString());
				}
				scroller.setPreferredSize(pLab.getPreferredSize());
				scroller.doLayout();
				pLab.repaint();
			}
		}
		
		
		public void saveAsImg(){
			File repertoireCourant = null;
			try {
				// obtention d'un objet File qui désigne le répertoire courant. Le
				// "getCanonicalFile" n'est pas absolument nécessaire mais permet
				// d'éviter les /Truc/./Chose/ ...
				repertoireCourant = new File(".").getCanonicalFile();
			} catch(IOException exep) {}
			// création de la boîte de dialogue dans ce répertoire courant
			// (ou dans "home" s'il y a eu une erreur d'entrée/sortie, auquel
			// cas repertoireCourant vaut null)
			JFileChooser dialogue = new JFileChooser(repertoireCourant);
			//FileNameExtensionFilter filter = new FileNameExtensionFilter(F_TYPE, F_FORMAT);
			//dialogue.setFileFilter(filter);
			dialogue.setDialogTitle(SAVEIMG);
			dialogue.setApproveButtonText(SAVEIMG) ;
			int returnVal = dialogue.showOpenDialog(null);
			if(returnVal == JFileChooser.APPROVE_OPTION){
				String fileFout = dialogue.getSelectedFile().getPath();
				if(!dialogue.getSelectedFile().getName().substring(dialogue.getSelectedFile().getName().lastIndexOf('.')+1, dialogue.getSelectedFile().getName().length() ).equals("png")){
					fileFout = fileFout+ F_IFORMAT;    
				}
				try{
					ImgGenerator imgG = new ImgGenerator(pLab.l);
					imgG.generate(fileFout);
				}catch(IOException ioe){
					ioe.printStackTrace();
				}
			}
		}
		
		
		//INNER CLASSE  ECOUTANT LE MENU
		class ActionMenu implements ActionListener{
			public void actionPerformed(ActionEvent e){
				String s = e.getActionCommand();//Recuperation de l'item du menu selectionné
				if(s.equals(QUIT)){//ITEM QUITTER
					int rep=JOptionPane.showConfirmDialog(null,IHM_QUITTER, QUIT, JOptionPane.YES_NO_OPTION);
					if(rep==JOptionPane.YES_OPTION) LabyWindow.this.removeNotify();
				}else if(s.equals(NEW)){//ITEM NOUVEAU
					int rep=JOptionPane.showConfirmDialog(null,IHM_NOUVEAU, NEW, JOptionPane.YES_NO_OPTION);
					if(rep==JOptionPane.YES_OPTION){
						LabyWindow.this.removeNotify();
						new LabyWindow();
					}
				}else if(s.equals(SAVE)){
					save();
				}else if(s.equals(LOAD)){
					load();
				}else if(s.equals(SAVEIMG)){
					saveAsImg();
				}else if(s.equals(VERSION)){//ITEM VERSION
					JOptionPane.showMessageDialog(null, IHM_VERSION, VERSION, JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
		/*
		 * Panel de generation
		 */
		public JPanel panelGeneration(){
			JPanel pGenerate = new JPanel();
			pGenerate.setLayout(new BoxLayout(pGenerate, BoxLayout.Y_AXIS));
			pGenerate.setBorder(BorderFactory.createTitledBorder(GENERATE));
		
			pComboGenerate = new JComboBox();
			pComboGenerate.addItem(GENERATE_FUSION);
			pComboGenerate.addItem(GENERATE_EXPLORATION);
					
			pTextLabX = new JTextField();
			pTextLabY = new JTextField();
			pTextLabOutput = new JTextField();
			pTextLabMinotaures = new JTextField();
				
			JButton pButtonGenerate = new JButton(TO_GENERATE);
			pButtonGenerate.addActionListener(this.outilAction);
			
			JButton regenerate = new JButton(REGENERATE);
		    regenerate.addActionListener(this.outilAction);
				
			Box vBox = Box.createVerticalBox();
		    vBox.add(pComboGenerate);
		    vBox.add(new JLabel(LABY_WIDTH));
		    vBox.add(pTextLabX);
		    vBox.add(new JLabel(LABY_HEIGHT));
		    vBox.add(pTextLabY);
		    vBox.add(new JLabel(LABY_NB_OUTPUT));
		    vBox.add(pTextLabOutput);
		    vBox.add(new JLabel(LABY_NB_MINOTAURE));
		    vBox.add(pTextLabMinotaures);
		    vBox.add(pButtonGenerate);
		    vBox.add(regenerate);
		    pGenerate.add(vBox);
		    
		    return pGenerate; 
		}
		
		/*
	     * Panel de resolution
	     */
		public JPanel panelResolution(){
			JPanel pResolution = new JPanel(new BorderLayout());
			pResolution.setBorder(BorderFactory.createTitledBorder(RESOLVE));
			
			pComboResolve = new JComboBox();
			pComboResolve.addItem(DFS);
			pComboResolve.addItem(DIJKSTRA);

			JButton pButtonResolve = new JButton(TO_RESOLVE);
			pButtonResolve.addActionListener(outilAction);
			pResolution.add(pComboResolve, BorderLayout.CENTER);
			pResolution.add(pButtonResolve, BorderLayout.SOUTH);
			return pResolution;
		}
		
		/*
		 * Panel de legende
		 */
		public JPanel panelLegende(){
		
			JPanel pLegende = new JPanel(new GridLayout(6, 2));
				
			pLegende.setBorder(BorderFactory.createTitledBorder(L_LEGEND));
			
			JLabel aL = new JLabel(), bL = new JLabel(), cL = new JLabel(), dL = new JLabel() , eL = new JLabel(), fL = new JLabel();
			aL.setBackground(L_C_WAY_DFS);
			aL.setOpaque(true);
			bL.setBackground(L_C_WAY_DIJKSTRA);
			bL.setOpaque(true);
			cL.setBackground(L_C_MINOTAURE);
			cL.setOpaque(true);
			dL.setBackground(L_C_OUTPUT);
			dL.setOpaque(true);
			eL.setBackground(L_C_INPUT);
			eL.setOpaque(true);
			fL.setBackground(L_C_THESEE);
			fL.setOpaque(true);
			pLegende.add(new JLabel(L_WAY_DFS));
			pLegende.add(aL);
			pLegende.add(new JLabel(L_WAY_DIJKSTRA));
			pLegende.add(bL);
			pLegende.add(new JLabel(L_MINOTAURE));
			pLegende.add(cL);
			pLegende.add(new JLabel(L_OUTPUT));
			pLegende.add(dL);
			pLegende.add(new JLabel(L_INPUT));
			pLegende.add(eL);
			pLegende.add(new JLabel(L_THESEE));
			pLegende.add(fL);
			return pLegende;
		}	
		/*
		 * INNER WINDOW
		 */
		//INITIALISATION DE LA FENETRE
		public void initAffichage(){
			c=this.getContentPane();//CONTAINER
			c.setLayout(new BorderLayout());
			outilAction = new ActionOutil();
			/*
			 * Panel d'affichage du labyrinthe
			 */
			pLab = new PanelLabyrinthe();
			//pLab.setPreferredSize(new Dimension(200,200));
			scroller = new JScrollPane(pLab);
			scroller.setPreferredSize(pLab.getPreferredSize());

			//int v=ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
	        //int h=ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS;
	        //JScrollPane jsb=new JScrollPane(pLab,v,h);			
	        //jsb.setPreferredSize(new Dimension(200,200));
			
			pLab.setBorder(BorderFactory.createBevelBorder(1));
			
			pUtil = new JPanel();
			pUtil.setBorder(BorderFactory.createLineBorder(Color.black));
			pUtil.setLayout(new BoxLayout(pUtil, BoxLayout.Y_AXIS));
			pUtil.setBorder(BorderFactory.createEtchedBorder());
			pUtil.add(this.panelGeneration());
			pUtil.add(this.panelResolution());
			pUtil.add(this.panelLegende());
			
			c.add(pUtil, BorderLayout.EAST);
			c.add(scroller, BorderLayout.CENTER);
			//scroller.revalidate();
		}
		
		//INNER CLASSE  ECOUTANT LE PANEL D'OUTIL
		class ActionOutil implements ActionListener{
			public void actionPerformed(ActionEvent e){
				String s = e.getActionCommand();//Recuperation de l'item du menu selectionné
				if(s.equals(TO_GENERATE) && !pTextLabX.getText().equals("") && !pTextLabY.getText().equals("")){//ITEM QUITTER
					if(pComboGenerate.getSelectedItem().equals(GENERATE_FUSION)){
						pLab.cheminDFS.clear();
						pLab.cheminPCC.clear();
						pLab.generation(GENERATE_FUSION,Integer.parseInt(pTextLabX.getText()), Integer.parseInt(pTextLabY.getText()), Integer.parseInt(pTextLabOutput.getText()), Integer.parseInt(pTextLabMinotaures.getText()));
						pLab.revalidate();
						pLab.repaint();
					}else if(pComboGenerate.getSelectedItem().equals(GENERATE_EXPLORATION)){
						pLab.cheminDFS.clear();
						pLab.cheminPCC.clear();
						pLab.generation(GENERATE_EXPLORATION,Integer.parseInt(pTextLabX.getText()), Integer.parseInt(pTextLabY.getText()), Integer.parseInt(pTextLabOutput.getText()), Integer.parseInt(pTextLabMinotaures.getText()));
						pLab.revalidate();
						pLab.repaint();
					}
				}
				if(s.equals(REGENERATE) && pLab.l!=null){
					pLab.cheminDFS.clear();
					pLab.cheminPCC.clear();
					pLab.repaint();
				}
				if(s.equals(TO_RESOLVE) && pLab.l!=null){//ITEM QUITTER
					if(pComboResolve.getSelectedItem().equals(DFS)){
						pLab.cheminDFS.clear();
						//pLab.cheminPCC.clear();
						pLab.resolution(DFS);
						pLab.repaint();
						
					}else if(pComboResolve.getSelectedItem().equals(DIJKSTRA)){
						//pLab.cheminDFS.clear();
						pLab.cheminPCC.clear();
						pLab.resolution(DIJKSTRA);
						pLab.repaint();
					}
				}
			}
		}
		/*
		 * Variables
		 */
		//JMenu
		JMenuBar menu;
		
		ActionMenu menuAction;//Ecouteur du JMenu
		ActionOutil outilAction;//Ecouteur du panel Outil
		
		Container c;//Container de la fenetre
		JPanel pUtil;//Les deux panels de la fenetre
		PanelLabyrinthe pLab;
	
		//generation d'un labyrinthe
		JComboBox pComboGenerate;
		JTextField pTextLabX, pTextLabY, pTextLabOutput, pTextLabMinotaures;
		//Resolution d'un labyrinthe
		JComboBox pComboResolve;
		private static final long serialVersionUID = 117L;
		private JScrollPane scroller;
}
