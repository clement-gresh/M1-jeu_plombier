package projetIG.model.niveau;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import projetIG.model.enumeration.CouleurTuyau;
import static projetIG.model.enumeration.CouleurTuyau.BLANC;
import static projetIG.model.enumeration.CouleurTuyau.BLEU;
import static projetIG.model.enumeration.CouleurTuyau.JAUNE;
import static projetIG.model.enumeration.CouleurTuyau.PAS_UNE_COULEUR;
import static projetIG.model.enumeration.CouleurTuyau.ROUGE;
import static projetIG.model.enumeration.CouleurTuyau.VERT;
import projetIG.model.enumeration.Rotation;
import static projetIG.model.enumeration.Rotation.DEMI_TOUR;
import static projetIG.model.enumeration.Rotation.PAS_DE_ROTATION;
import static projetIG.model.enumeration.Rotation.QUART_TOUR_HORAIRE;
import static projetIG.model.enumeration.Rotation.QUART_TOUR_TRIGO;
import projetIG.model.enumeration.TypeTuyau;
import static projetIG.model.enumeration.TypeTuyau.CROSS;
import static projetIG.model.enumeration.TypeTuyau.FORK;
import static projetIG.model.enumeration.TypeTuyau.LINE;
import static projetIG.model.enumeration.TypeTuyau.NOT_A_PIPE;
import static projetIG.model.enumeration.TypeTuyau.OVER;
import static projetIG.model.enumeration.TypeTuyau.SOURCE;
import static projetIG.model.enumeration.TypeTuyau.TURN;

public abstract class ParserNiveau {
    // Enregistre la hauteur et la largeur du plateau,
    // initialise le plateau avec les sources et tuyaux inamovibles
    // et initialise la reserve avec les tuyaux restants
    static public Niveau parserNiveau(String file){
        File fichierNiveau = new File(file);
        
        ArrayList<ArrayList<TuyauPlateau>> tuyauxPlateau = new ArrayList<>();
        tuyauxPlateau.add(new ArrayList<>());
        
        ArrayList<ArrayList<TuyauReserve>> tuyauxReserve = new ArrayList<>();
        tuyauxReserve.add(new ArrayList<>(Arrays.asList(new TuyauReserve(CROSS, PAS_DE_ROTATION),
                                                        new TuyauReserve(OVER, PAS_DE_ROTATION))));
        tuyauxReserve.add(new ArrayList<>(Arrays.asList(new TuyauReserve(LINE, PAS_DE_ROTATION),
                                                        new TuyauReserve(LINE, QUART_TOUR_HORAIRE))));
        tuyauxReserve.add(new ArrayList<>(Arrays.asList(new TuyauReserve(TURN, QUART_TOUR_HORAIRE),
                                                        new TuyauReserve(TURN, DEMI_TOUR))));
        tuyauxReserve.add(new ArrayList<>(Arrays.asList(new TuyauReserve(TURN, PAS_DE_ROTATION),
                                                        new TuyauReserve(TURN, QUART_TOUR_TRIGO))));
        tuyauxReserve.add(new ArrayList<>(Arrays.asList(new TuyauReserve(FORK, PAS_DE_ROTATION),
                                                        new TuyauReserve(FORK, QUART_TOUR_HORAIRE))));
        tuyauxReserve.add(new ArrayList<>(Arrays.asList(new TuyauReserve(FORK, QUART_TOUR_TRIGO),
                                                        new TuyauReserve(FORK, DEMI_TOUR))));
        
        try {
            Scanner scanner = new Scanner(fichierNiveau);
            
            int nbrCasesPlateauHauteur = scanner.nextInt();
            int nbrCasesPlateauLargeur = scanner.nextInt();
            
            System.out.println("Hauteur : " + nbrCasesPlateauHauteur + " et Largeur : " + nbrCasesPlateauLargeur); // debug
            
            //Variables locales pour compter les colonnes et lignes du plateau
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
                Rotation rotation;
                
                if(casePlateau.startsWith("*")){
                    inamovible = true;
                    casePlateau = casePlateau.substring(1);
                }
                
                typeTuyau = typeTuyau(casePlateau.substring(0, 1));
                
                
                if(typeTuyau != NOT_A_PIPE) {
                    
                    int nbrRotations = Integer.parseInt(casePlateau.substring(1, 2));
                    rotation = Rotation.values()[nbrRotations];

                    // CONSTRUCTION DU PLATEAU
                    // (sources et tuyaux inamovibles)
                    if(typeTuyau == SOURCE || inamovible){
                        CouleurTuyau couleur = (typeTuyau == SOURCE) ?
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
                case "C":
                        return CROSS;
                default:
                        return NOT_A_PIPE;
        }
    }
    
    
    static public CouleurTuyau couleurTuyau(String s){
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
