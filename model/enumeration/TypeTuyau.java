package projetIG.model.enumeration;

import java.util.ArrayList;
import java.util.Arrays;

public enum TypeTuyau {
    SOURCE,
    LINE,
    OVER,
    TURN,
    FORK,
    CROSS;
    
    static public final boolean AJOUT = true;
    static public final boolean SOUSTRAIRE = false;
    
    static public final Dir[][][] ouvertures = {
            {{Dir.N}},
            {{Dir.N, Dir.S}},
            {{Dir.N, Dir.S}, {Dir.E, Dir.O}},
            {{Dir.N, Dir.E}},
            {{Dir.N, Dir.E, Dir.S}},
            {{Dir.N, Dir.E, Dir.S, Dir.O}}};
    
    
    // Renvoie vrai si l'ouverture est trouvée parmi les ouvertures du tuyau
    public boolean aOuverture(Dir dir, Dir rotation){
        Dir dirModele = dir.rotation(rotation, SOUSTRAIRE);
        
        // On détermine si le modèle de tuyau a une ouverture
        for(Dir[] directions : ouvertures[this.ordinal()]){
            for(int i = 0; i < directions.length; i++){
                if(directions[i] == dirModele) return true;
            }
        }
        return false;
    }
    
    
    // Renvoie la liste des ouvertures connectees à l'ouverture d'entree
    public Dir[] dirSorties(Dir dirEntree, Dir rotation){
        
        // On determine la direction correspondante sur le modele
        Dir dirModele = dirEntree.rotation(rotation, SOUSTRAIRE);
        
        for(Dir[] directions : ouvertures[this.ordinal()]){
            for(int i = 0; i < directions.length; i++){
                if(directions[i] == dirModele) {
                    
                    // On enleve l'ouverture d'entree et on applique la rotation aux ouvertures restantes
                    ArrayList<Dir> dirConnectees = new ArrayList<>(Arrays.asList(directions));
                    dirConnectees.remove(i);
                    
                    for(int j = 0; j < dirConnectees.size(); j++){ 
                        dirConnectees.set(j, dirConnectees.get(j).rotation(rotation, AJOUT));
                    }
                    
                    return dirConnectees.toArray(new Dir[0]);
                }
            }
        }
        
        return new Dir[0];
    }
}
