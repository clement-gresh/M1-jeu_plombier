package projetIG.model.enumeration;

import java.util.ArrayList;

public enum TypeTuyau {
    SOURCE,
    LINE,
    OVER,
    TURN,
    FORK,
    CROSS,
    NOT_A_PIPE;
    
    static public final boolean AJOUTER_ROTATION = true;
    static public final boolean SOUSTRAIRE_ROTATION = false;
    
    static public final ArrayList<ArrayList<ArrayList<Ouverture>>> ouvertures = new ArrayList<>();
    
    
    // Initialisation de l'ArrayList ouvertures
    static {
        Ouverture[][][] ouverturesTableau = {
            {{Ouverture.NORTH}},
            {{Ouverture.NORTH, Ouverture.SOUTH}},
            {{Ouverture.NORTH, Ouverture.SOUTH}, {Ouverture.EAST, Ouverture.WEST}},
            {{Ouverture.NORTH, Ouverture.EAST}},
            {{Ouverture.NORTH, Ouverture.EAST, Ouverture.SOUTH}},
            {{Ouverture.NORTH, Ouverture.EAST, Ouverture.SOUTH, Ouverture.WEST}}
        };
        
        int i = 0;
        int j = 0;
        
        for(Ouverture[][] array1 : ouverturesTableau){
            ouvertures.add(new ArrayList<>());
            
            for(Ouverture[] array2 : array1){
                ouvertures.get(i).add(new ArrayList<>());
                
                for(Ouverture ouverture : array2){
                    ouvertures.get(i).get(j).add(ouverture);
                }
                j = j + 1;
            }
            i = i + 1;
            j = 0;
        }
        
        System.out.println("ouvertures : " + ouvertures);
    }
    
    
    
    // Renvoie vrai si l'ouverture est trouvée parmi les ouvertures du tuyau
    static public boolean aUneOuverture(TypeTuyau tuyau, Ouverture ouverture, Rotation rotation){
        Ouverture ouvertureModele = ouvertureAvecRotation(ouverture, rotation, SOUSTRAIRE_ROTATION);
        
        // On détermine si le modèle de tuyau a une ouverture
        for(ArrayList<Ouverture> tableauOuverture : ouvertures.get(tuyau.ordinal())){
            if(tableauOuverture.contains(ouvertureModele)) return true;
        }
        return false;
    }
    
    
    // Renvoie la liste des ouvertures connectees, autres que l'ouverture d'entree
    static public ArrayList<Ouverture> ouverturesConnectees(TypeTuyau tuyau, Ouverture ouvertureEntree, Rotation rotation){
        Ouverture ouvertureModele = ouvertureAvecRotation(ouvertureEntree, rotation, SOUSTRAIRE_ROTATION);
        
        ArrayList<Ouverture> ouverturesConnectees = new ArrayList<>();
        
        for(ArrayList<Ouverture> tableauOuvertures : ouvertures.get(tuyau.ordinal())){
            if(tableauOuvertures.contains(ouvertureModele)) {
                
                for(Ouverture ouvertureSortie : tableauOuvertures){
                    if(ouvertureSortie != ouvertureModele){
                        ouverturesConnectees.add(ouvertureAvecRotation(ouvertureSortie, rotation, AJOUTER_ROTATION));
                    }
                }
                break;
            }
        }
        
        return ouverturesConnectees;
    }
    
    
    public static Ouverture ouvertureAvecRotation(Ouverture ouverture, Rotation rotation, boolean ajouterRotation){
        
        int taille = Ouverture.values().length;
        
        int ordinalOuverture = (ajouterRotation)
                ? (ouverture.ordinal() + rotation.ordinal()) % taille
                : ((ouverture.ordinal() - rotation.ordinal()) % taille + taille) % taille;
                  // Utilise la formule (a % b + b ) % b pour obtenir un modulo positif
        
        Ouverture ouvertureModele = Ouverture.values()[ordinalOuverture];
        
        return ouvertureModele;
    }
}
