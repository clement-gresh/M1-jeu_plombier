package projetIG.model.niveau;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import projetIG.model.enumeration.CouleurTuyau;
import projetIG.model.enumeration.Rotation;
import projetIG.model.enumeration.TypeTuyau;

public abstract class ParserNiveau {
    static public Niveau parserNiveau(String file){
        File fichierNiveau = new File(file);
        
        ArrayList<ArrayList<TuyauPlateau>> tuyauxPlateau = new ArrayList<>();
        tuyauxPlateau.add(new ArrayList<>());
        
        ArrayList<ArrayList<TuyauReserve>> tuyauxReserve = new ArrayList<>();
        tuyauxReserve.add(new ArrayList<>(Arrays.asList(new TuyauReserve(TypeTuyau.CROSS, Rotation.PAS_DE_ROTATION),
                                                        new TuyauReserve(TypeTuyau.OVER, Rotation.PAS_DE_ROTATION))));
        tuyauxReserve.add(new ArrayList<>(Arrays.asList(new TuyauReserve(TypeTuyau.LINE, Rotation.PAS_DE_ROTATION),
                                                        new TuyauReserve(TypeTuyau.LINE, Rotation.QUART_TOUR_TRIGO))));
        tuyauxReserve.add(new ArrayList<>(Arrays.asList(new TuyauReserve(TypeTuyau.TURN, Rotation.QUART_TOUR_HORAIRE),
                                                        new TuyauReserve(TypeTuyau.TURN, Rotation.DEMI_TOUR))));
        tuyauxReserve.add(new ArrayList<>(Arrays.asList(new TuyauReserve(TypeTuyau.TURN, Rotation.PAS_DE_ROTATION),
                                                        new TuyauReserve(TypeTuyau.TURN, Rotation.QUART_TOUR_TRIGO))));
        tuyauxReserve.add(new ArrayList<>(Arrays.asList(new TuyauReserve(TypeTuyau.FORK, Rotation.PAS_DE_ROTATION),
                                                        new TuyauReserve(TypeTuyau.FORK, Rotation.QUART_TOUR_HORAIRE))));
        tuyauxReserve.add(new ArrayList<>(Arrays.asList(new TuyauReserve(TypeTuyau.FORK, Rotation.QUART_TOUR_TRIGO),
                                                        new TuyauReserve(TypeTuyau.FORK, Rotation.DEMI_TOUR))));
        
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
                
                typeTuyau = TypeTuyau.appartient(casePlateau.substring(0, 1));
                
                
                if(typeTuyau != TypeTuyau.NOT_A_PIPE) {
                    int nbrRotations = Integer.parseInt(casePlateau.substring(1, 2));
                    rotation = Rotation.values()[nbrRotations];


                    // CONSTRUCTION DU PLATEAU
                    // (sources et tuyaux inamovibles)
                    if(typeTuyau == TypeTuyau.SOURCE || inamovible){
                        CouleurTuyau couleur = (typeTuyau == TypeTuyau.SOURCE) ?
                                CouleurTuyau.appartient(casePlateau.substring(0, 1))
                                : CouleurTuyau.BLANC;
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
}
