package projetIG.model.niveau;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import projetIG.model.grapheCouleurs.GrapheCouleurs;

public class Niveau {
    protected int nbrCasesPlateauHauteur;
    protected int nbrCasesPlateauLargeur;
    protected ArrayList<ArrayList<String>> plateauGagnant = new ArrayList<>();
    protected ArrayList<ArrayList<String>> plateauCourant = new ArrayList<>();
    protected ArrayList<Tuyau> tuyauxDisponibles = new ArrayList<>(Arrays.asList(
            new Tuyau("C0", 0, 0, 5), new Tuyau("O0", 0, 0, 2),
            new Tuyau("L0", 0, 0, 1), new Tuyau("L1", 1, 0, 1),
            new Tuyau("T1", 1, 0, 3), new Tuyau("T2", 2, 0, 3), new Tuyau("T0", 0, 0, 3), new Tuyau("T3", 3, 0, 3),
            new Tuyau("F0", 0, 0, 4), new Tuyau("F1", 1, 0, 4), new Tuyau("F3", 3, 0, 4), new Tuyau("F2", 2, 0, 4)));
    protected GrapheCouleurs grapheCouleurs;
    
    public Niveau() {
        // Enregistre la hauteur, la largeur et la configuration gagnante du plateau
        // a partir du fichier de niveau
        this.importerNiveauGagnant("src/main/java/projetIG/model/niveau/banque1/level3.p");
        
        
        // Cree la configuration initiale du plateau courant
        // et determine le nombre de tuyaux disponibles dans la reserve
        this.initialiserNiveauCourant();
        
        // Determine la couleur de chaque tuyau du plateau
        //this.couleurTuyaux();
    }
    /*
    //TO DO
    private void couleurTuyaux(){
        ArrayList<ArrayList<Integer>> plateauCouleursTuyaux = new ArrayList<>();
        ArrayList<ArrayList<Integer>> sommetsGraphe = new ArrayList<>();
        ArrayList<ArrayList<Integer>> arcsGraphe = new ArrayList<>();
        
        // Variables locales representant la ligne et la colonne du plateau de couleurs
        int lignePC = 0;
        int colonnePC = 0;
        
        for(ArrayList<String> ligne: this.plateauCourant){            
            for(String casePlateau : ligne) {
                
            }
        }
    }*/
    
    
    private void importerNiveauGagnant(String file){
        File niveau = new File(file);
        
        try {
            Scanner scanner = new Scanner(niveau);
            
            this.nbrCasesPlateauHauteur = scanner.nextInt();
            this.nbrCasesPlateauLargeur = scanner.nextInt();
            
            System.out.println("Hauteur : " + this.nbrCasesPlateauHauteur + " et Largeur : " + this.nbrCasesPlateauLargeur); // debug
            
            //Variables locales pour compter les colonnes et remplir les lignes du plateau
            int colonne = 0;
            int ligne = 0;
            this.plateauGagnant.add(new ArrayList<>());
            
            while(scanner.hasNext()){
                // Si on arrive au bout de la ligne, on cree une nouvelle ligne
                // et on revient a la premiere colonne
                if(colonne == nbrCasesPlateauLargeur) {
                    this.plateauGagnant.add(new ArrayList<>());
                    ligne = ligne + 1;
                    colonne = 0;
                }
                
                // On remplit la case suivante de la ligne courante
                String casePlateau = scanner.next();
                this.plateauGagnant.get(ligne).add(casePlateau);
                colonne = colonne+1;
            }
                        
            scanner.close();
            System.out.println(plateauGagnant);  //debug
        }
        
        catch (Exception exception) {
            System.err.println("Exception scanner sur le niveau (Niveau.java)");
        }
    }
    
    
    private void initialiserNiveauCourant() {
        // Variables locales representant la ligne et la colonne du plateau courant
        int lignePC = 0;
        int colonnePC = 0;
        
        for(ArrayList<String> ligne: this.plateauGagnant){
            //On cree une nouvelle ligne dans le plateau courant
            this.plateauCourant.add(new ArrayList<>());
            
            for(String casePlateau : ligne) {
                // Si on est sur la bordure du plateau gagnant,
                // ou sur une case vide,
                // ou sur un tuyau inamovible,
                // on recopie la case dans le plateau courant
                if(lignePC == 0
                        || lignePC == this.nbrCasesPlateauHauteur - 1
                        || colonnePC == 0
                        || colonnePC == this.nbrCasesPlateauLargeur - 1
                        || casePlateau.equals(".")
                        || casePlateau.startsWith("*")) {
                    
                    this.plateauCourant.get(lignePC).add(casePlateau);
                }
                
                // Sinon, on ajoute le tuyau a la reserve
                else {
                    for(Tuyau tuyau : this.tuyauxDisponibles) {
                        if(tuyau.getNom().equals(casePlateau)) {
                            this.plateauCourant.get(lignePC).add(".");
                            tuyau.setNombre(tuyau.getNombre() + 1);
                            break; // debug : le break fait il sortir du if ou du for ?
                        }
                    }
                }
                
                colonnePC = colonnePC + 1;
            }
            
            //On passe a la ligne suivante et on revient a la premiere colonne
            lignePC = lignePC + 1;
            colonnePC = 0;
        }
        
        System.out.println(this.plateauCourant); //debug
        
        for(Tuyau tuyau : this.tuyauxDisponibles) { //debug
            System.out.print(" [" + tuyau.getNom() + " ; " + tuyau.getNombre() + "] ");
        }
        
        System.out.println(""); //debug
        System.out.println(""); //debug
    }
    
    //Getters
    public int getNbrCasesPlateauHauteur() {
        return nbrCasesPlateauHauteur;
    }

    public int getNbrCasesPlateauLargeur() {
        return nbrCasesPlateauLargeur;
    }

    public ArrayList<ArrayList<String>> getPlateauGagnant() {
        return plateauGagnant;
    }

    public ArrayList<ArrayList<String>> getPlateauCourant() {
        return plateauCourant;
    }

    public ArrayList<Tuyau> getTuyauxDisponibles() {
        return tuyauxDisponibles;
    }
    
    
}
