package projetIG.model.niveau;

import java.io.File;
import java.util.Scanner;
import projetIG.model.enumeration.Couleur;
import static projetIG.model.enumeration.Couleur.BLANC;
import static projetIG.model.enumeration.Couleur.BLEU;
import static projetIG.model.enumeration.Couleur.JAUNE;
import static projetIG.model.enumeration.Couleur.ROUGE;
import static projetIG.model.enumeration.Couleur.VERT;
import projetIG.model.enumeration.Dir;
import projetIG.model.enumeration.TypeTuyau;
import static projetIG.model.enumeration.TypeTuyau.CROSS;
import static projetIG.model.enumeration.TypeTuyau.FORK;
import static projetIG.model.enumeration.TypeTuyau.LINE;
import static projetIG.model.enumeration.TypeTuyau.OVER;
import static projetIG.model.enumeration.TypeTuyau.SOURCE;
import static projetIG.model.enumeration.TypeTuyau.TURN;
import static projetIG.model.enumeration.Dir.N;
import static projetIG.model.enumeration.Dir.E;
import static projetIG.model.enumeration.Dir.S;
import static projetIG.model.enumeration.Dir.O;

public abstract class ParserNiveau {
    public static final int LARGEUR_RESERVE = 2;
    public static final int HAUTEUR_RESERVE = 6; 
    
    // Enregistre la hauteur et la largeur du plateau,
    // initialise le plateau avec les sources et tuyaux inamovibles
    // et initialise la reserve avec les tuyaux restants
    static public Niveau parserNiveau(String file){
        File fichierNiveau = new File(file);
        
        TuyauPlateau[][] plateau;
        
        TuyauReserve[][] reserve = {{new TuyauReserve(CROSS, N), new TuyauReserve(OVER, N)},
                                    {new TuyauReserve(LINE, N), new TuyauReserve(LINE, E)},
                                    {new TuyauReserve(TURN, E), new TuyauReserve(TURN, S)},
                                    {new TuyauReserve(TURN, N), new TuyauReserve(TURN, O)},
                                    {new TuyauReserve(FORK, N), new TuyauReserve(FORK, E)},
                                    {new TuyauReserve(FORK, O), new TuyauReserve(FORK, S)}
        };
        
        try {
            Scanner scanner = new Scanner(fichierNiveau);
            
            int hauteur = scanner.nextInt();
            int largeur = scanner.nextInt();
            
            plateau = new TuyauPlateau[hauteur][largeur];
            
            int colonne = 0;
            int ligne = 0;
            
            while(scanner.hasNext()){
                // Si on arrive au bout de la ligne, on passe a la suivante
                if(colonne == largeur) {
                    ligne = ligne + 1;
                    colonne = 0;
                }
                
                String casePlateau = scanner.next();
                
                TypeTuyau type;
                boolean fixe = false;
                Dir rotation;
                
                // Determine s'il s'agit d'un tuyau fixe
                if(casePlateau.startsWith("*")){
                    fixe = true;
                    casePlateau = casePlateau.substring(1);
                }
                
                // S'il s'agit bien d'un tuyau, on determine ses caracteristiques
                if(!casePlateau.startsWith(".") && !casePlateau.startsWith("X")) {
                    
                    type = typeTuyau(casePlateau.substring(0, 1));
                    int nbrRotations = Integer.parseInt(casePlateau.substring(1, 2));
                    rotation = Dir.values()[nbrRotations];

                    // CONSTRUCTION DU PLATEAU
                    // (sources et tuyaux inamovibles)
                    if(type == SOURCE || fixe){
                        Couleur couleur = (type == SOURCE) ?
                                couleurTuyau(casePlateau.substring(0, 1)) : BLANC;
                        plateau[ligne][colonne] = new TuyauPlateau(type, rotation, true, couleur);
                    }

                    else {
                        plateau[ligne][colonne] = null;

                        // CONSTRUCTION DE LA RESERVE
                        boolean tuyauTrouve = false;
                        
                        for(int l = 0; l < HAUTEUR_RESERVE; l++){
                            for(int c = 0; c < LARGEUR_RESERVE; c++){
                                
                                if( reserve[l][c].getNom() == type
                                        && reserve[l][c].getRotation() == rotation){
                                    
                                    reserve[l][c].augmenterNombre();
                                    tuyauTrouve = true;
                                    break;
                                }
                            }
                            if(tuyauTrouve) break;
                        }
                    }
                }
                
                else { plateau[ligne][colonne] = null; }
                
                colonne = colonne+1;
            }
                        
            scanner.close();
            
            
            return new Niveau(hauteur, largeur, plateau, reserve);
        }
        
        catch (Exception exception) {
            System.err.println("Exception scanner sur le niveau (Niveau.java) " + exception.getMessage());
            
            return new Niveau(0, 0, new TuyauPlateau[0][0], new TuyauReserve[0][0]);
        }
    }
    
    
    static public TypeTuyau typeTuyau(String s){
         return switch (s){
                case "R" -> SOURCE;
                case "G" -> SOURCE;
                case "B" -> SOURCE;
                case "Y" -> SOURCE;
                case "L" -> LINE;
                case "O" -> OVER;
                case "T" -> TURN;
                case "F" ->FORK;
                default -> CROSS;
        };
    }
    
    
    static public Couleur couleurTuyau(String s){
            return switch (s){
                    case "R" -> ROUGE;
                    case "G" -> VERT;
                    case "B" -> BLEU;
                    default -> JAUNE;
        };
    }
}
