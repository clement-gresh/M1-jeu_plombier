package projetIG.model.enumeration;

import java.util.ArrayList;

public enum TypeTuyau {
    SOURCE,
    LINE,
    OVER,
    TURN,
    FORK,
    CROSS;
    
    static public final boolean AJOUT = true;
    static public final boolean SOUSTRAIRE = false;
    
    static public final ArrayList<ArrayList<ArrayList<Dir>>> ouvertures = new ArrayList<>();
    
    
    // Initialisation de l'ArrayList ouvertures
    static {
        Dir[][][] ouverturesTableau = {
            {{Dir.N}},
            {{Dir.N, Dir.S}},
            {{Dir.N, Dir.S}, {Dir.E, Dir.O}},
            {{Dir.N, Dir.E}},
            {{Dir.N, Dir.E, Dir.S}},
            {{Dir.N, Dir.E, Dir.S, Dir.O}}
        };
        
        int i = 0;
        int j = 0;
        
        for(Dir[][] array1 : ouverturesTableau){
            ouvertures.add(new ArrayList<>());
            
            for(Dir[] array2 : array1){
                ouvertures.get(i).add(new ArrayList<>());
                
                for(Dir ouverture : array2){
                    ouvertures.get(i).get(j).add(ouverture);
                }
                j = j + 1;
            }
            i = i + 1;
            j = 0;
        }
    }
    
    
    
    // Renvoie vrai si l'ouverture est trouvée parmi les ouvertures du tuyau
    public boolean aOuverture(Dir ouverture, Dir rotation){
        Dir ouvertureModele = ouverture.rotation(rotation, SOUSTRAIRE);
        
        // On détermine si le modèle de tuyau a une ouverture
        for(ArrayList<Dir> tableauOuverture : ouvertures.get(this.ordinal())){
            if(tableauOuverture.contains(ouvertureModele)) return true;
        }
        return false;
    }
    
    
    // Renvoie la liste des ouvertures connectees, autres que l'ouverture d'entree
    public ArrayList<Dir> dirSorties(Dir ouvertureEntree, Dir rotation){
        Dir ouvertureModele = ouvertureEntree.rotation(rotation, SOUSTRAIRE);
        
        ArrayList<Dir> ouverturesConnectees = new ArrayList<>();
        
        for(ArrayList<Dir> tableauOuvertures : ouvertures.get(this.ordinal())){
            if(tableauOuvertures.contains(ouvertureModele)) {
                
                for(Dir ouvertureSortie : tableauOuvertures){
                    if(ouvertureSortie != ouvertureModele){
                        ouverturesConnectees.add(ouvertureSortie.rotation(rotation, AJOUT));
                    }
                }
                break;
            }
        }
        
        return ouverturesConnectees;
    }
}
