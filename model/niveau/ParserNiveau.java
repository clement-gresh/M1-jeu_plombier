package projetIG.model.niveau;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import projetIG.model.enumeration.Couleur;
import static projetIG.model.enumeration.Couleur.BLANC;
import static projetIG.model.enumeration.Couleur.BLEU;
import static projetIG.model.enumeration.Couleur.JAUNE;
import static projetIG.model.enumeration.Couleur.PAS_UNE_COULEUR;
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
    private static final int AUCUN = -1;
    
    // Enregistre la hauteur et la largeur du plateau,
    // initialise le plateau avec les sources et tuyaux inamovibles
    // et initialise la reserve avec les tuyaux restants
    static public Niveau parserNiveau(String file){
        File fichierNiveau = new File(file);
        
        ArrayList<ArrayList<TuyauPlateau>> tuyauxPlateau = new ArrayList<>();
        tuyauxPlateau.add(new ArrayList<>());
        
        ArrayList<ArrayList<TuyauReserve>> tuyauxReserve = new ArrayList<>();
        tuyauxReserve.add(new ArrayList<>(Arrays.asList(new TuyauReserve(CROSS, N),
                                                        new TuyauReserve(OVER, N))));
        tuyauxReserve.add(new ArrayList<>(Arrays.asList(new TuyauReserve(LINE, N),
                                                        new TuyauReserve(LINE, E))));
        tuyauxReserve.add(new ArrayList<>(Arrays.asList(new TuyauReserve(TURN, E),
                                                        new TuyauReserve(TURN, S))));
        tuyauxReserve.add(new ArrayList<>(Arrays.asList(new TuyauReserve(TURN, N),
                                                        new TuyauReserve(TURN, O))));
        tuyauxReserve.add(new ArrayList<>(Arrays.asList(new TuyauReserve(FORK, N),
                                                        new TuyauReserve(FORK, E))));
        tuyauxReserve.add(new ArrayList<>(Arrays.asList(new TuyauReserve(FORK, O),
                                                        new TuyauReserve(FORK, S))));
        
        try {
            Scanner scanner = new Scanner(fichierNiveau);
            
            int nbrCasesPlateauHauteur = scanner.nextInt();
            int nbrCasesPlateauLargeur = scanner.nextInt();
            
            int colonne = 0;
            int ligne = 0;
            
            while(scanner.hasNext()){
                // Si on arrive au bout de la ligne, on en cree un nouvelle
                // et on revient a la premiere colonne
                if(colonne == nbrCasesPlateauLargeur) {
                    tuyauxPlateau.add(new ArrayList<>());
                    ligne = ligne + 1;
                    colonne = 0;
                }
                
                String casePlateau = scanner.next();
                
                TypeTuyau typeTuyau;
                boolean inamovible = false;
                Dir rotation;
                
                if(casePlateau.startsWith("*")){
                    inamovible = true;
                    casePlateau = casePlateau.substring(1);
                }
                
                
                if(!casePlateau.startsWith(".") && !casePlateau.startsWith("X")) {
                    
                    typeTuyau = typeTuyau(casePlateau.substring(0, 1));
                    int nbrRotations = Integer.parseInt(casePlateau.substring(1, 2));
                    rotation = Dir.values()[nbrRotations];

                    // CONSTRUCTION DU PLATEAU
                    // (sources et tuyaux inamovibles)
                    if(typeTuyau == SOURCE || inamovible){
                        Couleur couleur = (typeTuyau == SOURCE) ?
                                couleurTuyau(casePlateau.substring(0, 1))
                                : BLANC;
                        tuyauxPlateau.get(ligne).add(new TuyauPlateau(typeTuyau, rotation, true, couleur));
                    }

                    else {
                        tuyauxPlateau.get(ligne).add(null);

                        // CONSTRUCTION DE LA RESERVE
                        boolean tuyauTrouve = false;

                        for(ArrayList<TuyauReserve> ligneReserve : tuyauxReserve){
                            for(TuyauReserve tuyau : ligneReserve){
                                if( tuyau.getNom() == typeTuyau && tuyau.getRotation() == rotation){
                                    tuyau.augmenterNombre();
                                    tuyauTrouve = true;
                                    break;
                                }
                            }
                            if(tuyauTrouve) break;
                        }
                    }
                }
                
                else { tuyauxPlateau.get(ligne).add(null); }
                
                colonne = colonne+1;
            }
                        
            scanner.close();
            
            //debug
            /*
            System.out.println(tuyauxPlateau);  
            for(ArrayList<TuyauPlateau> lignePlateau : tuyauxPlateau) {
                for(TuyauPlateau tuyau : lignePlateau){
                    if(tuyau != null) System.out.print("nom: " + tuyau.getNom() + ", inamovible : " + tuyau.isInamovible() + ", ");
                }
            }
            System.out.println("");
            
            for(ArrayList<TuyauReserve> ligneReserve : tuyauxReserve) {
                for(TuyauReserve tuyau : ligneReserve){
                    System.out.print("nom: " + tuyau.getNom() + ", nombre : " + tuyau.getNombre() + ", ");
                }
            }
            System.out.println("");
            */
            //fin debug
            
            
            return new Niveau(nbrCasesPlateauHauteur, nbrCasesPlateauLargeur, tuyauxPlateau, tuyauxReserve);
        }
        
        catch (Exception exception) {
            System.err.println("Exception scanner sur le niveau (Niveau.java) " + exception.getMessage());
            
            return new Niveau(0, 0, new ArrayList<>(), new ArrayList<>());
        }
    }
    
    
    static public TypeTuyau typeTuyau(String s){
        switch (s){
                case "R":
                        return SOURCE;
                case "G":
                        return SOURCE;
                case "B":
                        return SOURCE;
                case "Y":
                        return SOURCE;
                case "L":
                        return LINE;
                case "O":
                        return OVER;
                case "T":
                        return TURN;
                case "F":
                        return FORK;
                default:
                        return CROSS;
        }
    }
    
    
    static public Couleur couleurTuyau(String s){
            switch (s){
                    case "R":
                        return ROUGE;
                    case "G":
                        return VERT;
                    case "B":
                        return BLEU;
                    case "Y":
                        return JAUNE;
                    default:
                        return PAS_UNE_COULEUR;
        }
    }
}
