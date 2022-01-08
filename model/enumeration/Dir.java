package projetIG.model.enumeration;

public enum Dir {
    N,
    E,
    S,
    O;
    
    
    public Dir rotation(Dir rotation, boolean ajout){
        
        int taille = Dir.values().length;
        
        int ordinalOuverture = (ajout)
                ? (this.ordinal() + rotation.ordinal()) % taille
                : ((this.ordinal() - rotation.ordinal()) % taille + taille) % taille;
                  // Utilise la formule (a % b + b ) % b pour obtenir un modulo positif
        
        Dir ouvertureModele = Dir.values()[ordinalOuverture];
        
        return ouvertureModele;
    }
    
    public int ligne(){
        switch (this){
                case N :
                    return -1;
                case S :
                    return +1;
                default :
                    return 0;
        }
    }
    
    
    public int colonne(){
        switch (this){
                case E :
                    return +1;
                case O :
                    return -1;
                default :
                    return 0;
        }
    }
}


