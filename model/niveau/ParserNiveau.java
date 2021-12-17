package projetIG.model.niveau;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import projetIG.model.enumeration.Rotation;
import projetIG.model.enumeration.TypeTuyau;

public class ParserNiveau {
    static public Niveau parserNiveau(String file){
        File fichierNiveau = new File(file);
        
        ArrayList<ArrayList<TuyauPlateau>> tuyauxPlateau = new ArrayList<>();
        tuyauxPlateau.add(new ArrayList<>());
        
        ArrayList<ArrayList<TuyauReserve>> tuyauxReserve = new ArrayList<>();
        
        tuyauxReserve.add(new ArrayList<>(Arrays.asList(new TuyauReserve("C0"), new TuyauReserve("O0"))));
        tuyauxReserve.add(new ArrayList<>(Arrays.asList(new TuyauReserve("L0"), new TuyauReserve("L1"))));
        tuyauxReserve.add(new ArrayList<>(Arrays.asList(new TuyauReserve("T1"), new TuyauReserve("T2"))));
        tuyauxReserve.add(new ArrayList<>(Arrays.asList(new TuyauReserve("T0"), new TuyauReserve("T3"))));
        tuyauxReserve.add(new ArrayList<>(Arrays.asList(new TuyauReserve("F0"), new TuyauReserve("F1"))));
        tuyauxReserve.add(new ArrayList<>(Arrays.asList(new TuyauReserve("F3"), new TuyauReserve("F2"))));
        
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
                
                
                // CONSTRUCTION DU PLATEAU
                // (sources et tuyaux inamovibles)
                if(TypeTuyau.appartient(casePlateau.substring(0, 1)) == TypeTuyau.SOURCE
                        || casePlateau.startsWith("*")){
                    tuyauxPlateau.get(ligne).add(new TuyauPlateau(casePlateau));
                }
                
                
                else {
                     tuyauxPlateau.get(ligne).add(null);

                     
                    // CONSTRUCTION DE LA RESERVE
                    TypeTuyau typeTuyau = TypeTuyau.appartient(casePlateau.substring(0, 1));
                    
                    if(typeTuyau != TypeTuyau.NOT_A_PIPE) {
                        boolean tuyauTrouve = false;
                        
                        // Tuyaux sans rotation
                        if( typeTuyau == TypeTuyau.CROSS || typeTuyau == TypeTuyau.OVER) {
                            Rotation rotation = Rotation.PAS_DE_ROTATION;
                        }

                        // Tuyaux avec rotation
                        else {
                            int nbrRotations = Integer.parseInt(casePlateau.substring(1, 2));
                            Rotation rotation = Rotation.values()[nbrRotations];
                        }
                        
                        // Augmentation du nombre disponible
                        for(ArrayList<TuyauReserve> ligneReserve : tuyauxReserve){
                            for(TuyauReserve tuyau : ligneReserve){
                                if( tuyau.getNom() == TypeTuyau.appartient(casePlateau.substring(0, 1))){
                                    tuyau.augmenterNombre();
                                    tuyauTrouve = true;
                                    break;
                                }
                            }
                            if(tuyauTrouve) break;
                        }
                    }
                }
                
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
