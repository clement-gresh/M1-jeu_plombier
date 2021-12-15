package projetIG.model.niveau;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import projetIG.model.Rotation;
import projetIG.model.TypeTuyau;

public class Niveau {
    protected int nbrCasesPlateauHauteur;
    protected int nbrCasesPlateauLargeur;
    protected ArrayList<TuyauPlateau> plateauCourant = new ArrayList<>();
    //protected ArrayList<ArrayList<TuyauPlateau>> plateauCourant = new ArrayList<>();
    protected ArrayList<TuyauReserve> tuyauxReserve = new ArrayList<>(Arrays.asList(
            new TuyauReserve("C0"), new TuyauReserve("O0"),
            new TuyauReserve("L0"), new TuyauReserve("L1"),
            new TuyauReserve("T1"), new TuyauReserve("T2"), new TuyauReserve("T0"), new TuyauReserve("T3"),
            new TuyauReserve("F0"), new TuyauReserve("F1"), new TuyauReserve("F3"), new TuyauReserve("F2")
    ));
    //protected GrapheCouleurs grapheCouleurs;
    
    public Niveau() {
        this.initialiserNiveau("src/main/java/projetIG/model/niveau/banque1/level3.p");
    }
    
    
    // Enregistre la hauteur et la largeur du plateau,
    // initialise le plateau avec les sources et tuyaux inamovibles
    // et remplit la reserve
    private void initialiserNiveau(String file){
        File niveau = new File(file);
        
        try {
            Scanner scanner = new Scanner(niveau);
            
            this.nbrCasesPlateauHauteur = scanner.nextInt();
            this.nbrCasesPlateauLargeur = scanner.nextInt();
            
            System.out.println("Hauteur : " + this.nbrCasesPlateauHauteur + " et Largeur : " + this.nbrCasesPlateauLargeur); // debug
            
            //Variables locales pour compter les colonnes et lignes du plateau
            int colonne = 0;
            int ligne = 0;
            
            while(scanner.hasNext()){
                // Si on arrive au bout de la ligne, on passe a une nouvelle
                // et on revient a la premiere colonne
                if(colonne == nbrCasesPlateauLargeur) {
                    ligne = ligne + 1;
                    colonne = 0;
                }
                
                
                String casePlateau = scanner.next();
                
                
                // Construction du plateau initial (sources et tuyaux inamovibles)
                if(TypeTuyau.appartient(casePlateau.substring(0, 1)) == TypeTuyau.SOURCE
                        || casePlateau.startsWith("*")){
                    this.plateauCourant.add(new TuyauPlateau(casePlateau, ligne, colonne));
                }
                
                /*
                //debug : affiche tous les tuyaux sur le plateau (remplace le bloc precedent en le commentant)
                if(TypeTuyau.appartient(casePlateau.substring(0, 1)) == TypeTuyau.SOURCE
                        || (!casePlateau.startsWith(".") && !casePlateau.startsWith("X"))){
                    this.plateauCourant.add(new TuyauPlateau(casePlateau, ligne, colonne));
                }
                //fin debug
                */
                
                // CONSTRUCTION DE LA RESERVE
                // Tuyaux sans rotation
                else if( TypeTuyau.appartient(casePlateau.substring(0, 1)) == TypeTuyau.CROSS
                        || TypeTuyau.appartient(casePlateau.substring(0, 1)) == TypeTuyau.OVER) {
                    
                    for(TuyauReserve tuyau : this.tuyauxReserve){
                        if( tuyau.getNom() == TypeTuyau.appartient(casePlateau.substring(0, 1))){
                            tuyau.augmenterNombre();
                            break;
                        }
                    }
                }
                
                // Tuyaux avec rotation
                else if( TypeTuyau.appartient(casePlateau.substring(0, 1)) != TypeTuyau.NOT_A_PIPE ){
                    int nbrRotations = Integer.parseInt(casePlateau.substring(1, 2));
                    Rotation rotation = Rotation.values()[nbrRotations];
                    
                    for(TuyauReserve tuyau : this.tuyauxReserve){
                        if( tuyau.getNom() == TypeTuyau.appartient(casePlateau.substring(0, 1))
                                && tuyau.getRotation() == rotation ){
                            tuyau.augmenterNombre();
                            break;
                        }
                    }
                }
                
                colonne = colonne+1;
            }
                        
            scanner.close();
            
            //debug
            System.out.println(plateauCourant);  
            for(TuyauPlateau tuyau : this.plateauCourant) {
                System.out.print("nom: " + tuyau.getNom() + ", inamovible : " + tuyau.isInamovible() + ", ");
            }
            System.out.println("");
            
            for(TuyauReserve tuyau : this.tuyauxReserve) {
                System.out.print("nom: " + tuyau.getNom() + ", nombre : " + tuyau.getNombre() + ", ");
            }
            System.out.println("");
            //fin debug
        }
        
        catch (Exception exception) { System.err.println("Exception scanner sur le niveau (Niveau.java) " + exception.getMessage()); }
    }
    
    //Getters
    public int getNbrCasesPlateauHauteur() {
        return nbrCasesPlateauHauteur;
    }

    public int getNbrCasesPlateauLargeur() {
        return nbrCasesPlateauLargeur;
    }
    public ArrayList<TuyauPlateau> getPlateauCourant() {
        return plateauCourant;
    }

    public ArrayList<TuyauReserve> getTuyauxReserve() {
        return tuyauxReserve;
    }
    
}
