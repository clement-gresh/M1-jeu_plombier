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
import projetIG.model.enumeration.Type;
import static projetIG.model.enumeration.Type.CROSS;
import static projetIG.model.enumeration.Type.FORK;
import static projetIG.model.enumeration.Type.LINE;
import static projetIG.model.enumeration.Type.OVER;
import static projetIG.model.enumeration.Type.SOURCE;
import static projetIG.model.enumeration.Type.TURN;
import static projetIG.model.enumeration.Dir.N;
import static projetIG.model.enumeration.Dir.E;
import static projetIG.model.enumeration.Dir.S;
import static projetIG.model.enumeration.Dir.O;

public abstract class ParserNiveau {
    public static final int L_RESERVE = 2;
    public static final int H_RESERVE = 6; 
    
    // Enregistre la hauteur et la largeur du plateau,
    // initialise le plateau avec les sources et tuyaux inamovibles
    // et initialise la reserve avec les tuyaux restants
    static public Niveau parser(String file){
        File fichierNiveau = new File(file);
        TuyauP[][] plateau;
        TuyauR[][] reserve = {{new TuyauR(CROSS, N), new TuyauR(OVER, N)},
                               {new TuyauR(LINE, N), new TuyauR(LINE, E)},
                               {new TuyauR(TURN, E), new TuyauR(TURN, S)},
                               {new TuyauR(TURN, N), new TuyauR(TURN, O)},
                               {new TuyauR(FORK, N), new TuyauR(FORK, E)},
                               {new TuyauR(FORK, O), new TuyauR(FORK, S)}
        };
        try (Scanner scanner = new Scanner(fichierNiveau)) {
            int hauteur = scanner.nextInt();
            int largeur = scanner.nextInt();
            plateau = new TuyauP[hauteur][largeur];
            int colonne = 0;
            int ligne = 0;
            
            while(scanner.hasNext()){
                // Si on arrive au bout de la ligne, on passe a la suivante
                if(colonne == largeur) {
                    ligne = ligne + 1;
                    colonne = 0;
                }
                String caseP = scanner.next();
                Type type;
                boolean fixe = false;
                Dir rotation;
                
                // Determine s'il s'agit d'un tuyau fixe
                if(caseP.startsWith("*")){
                    fixe = true;
                    caseP = caseP.substring(1);
                }
                
                // S'il s'agit d'un tuyau, on determine ses caracteristiques
                if(!caseP.startsWith(".") && !caseP.startsWith("X")) {
                    type = type(caseP.substring(0, 1));
                    int nbrRotations = Integer.parseInt(caseP.substring(1, 2));
                    rotation = Dir.values()[nbrRotations];

                    // CONSTRUCTION DU PLATEAU
                    // (sources et tuyaux inamovibles)
                    if(type == SOURCE || fixe){
                        Couleur couleur = (type == SOURCE) ?
                                couleur(caseP.substring(0, 1)) : BLANC;
                        plateau[ligne][colonne] =
                               new TuyauP(type, rotation, true, couleur);
                    }
                    else {
                        plateau[ligne][colonne] = null;

                        // CONSTRUCTION DE LA RESERVE
                        boolean tuyauTrouve = false;
                        
                        for(int l = 0; l < H_RESERVE; l++){
                            for(int c = 0; c < L_RESERVE; c++){
                                if( reserve[l][c].getType() == type
                                        && reserve[l][c].getRotation() 
                                           == rotation){
                                    reserve[l][c].augmenter();
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
            return new Niveau(hauteur, largeur, plateau, reserve);
        }
        catch (Exception exception) {
            System.err.println("Exception scanner sur le niveau (Niveau.java) "
                    + exception.getMessage());
            return new Niveau(0, 0, new TuyauP[0][0], new TuyauR[0][0]);
        }
    }
    
    static public Type type(String s){
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
    
    static public Couleur couleur(String s){
            return switch (s){
                    case "R" -> ROUGE;
                    case "G" -> VERT;
                    case "B" -> BLEU;
                    default -> JAUNE;
        };
    }
}
