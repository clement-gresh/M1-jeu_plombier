package projetIG.model.enumeration;

public enum Dir {
    N,
    E,
    S,
    O;
    
    // Renvoie la direction apres ajout ou soustraction d'une rotation
    public Dir rotation(Dir rotation, boolean ajout){
        int taille = Dir.values().length;
        int ordinal = (ajout)
           ? (this.ordinal() + rotation.ordinal()) % taille
           : ((this.ordinal() - rotation.ordinal()) % taille + taille) % taille;
           // Utilise la formule (a % b + b ) % b pour obtenir un modulo positif
        return Dir.values()[ordinal];
    }
    
    // Donne la composante ligne du deplacement sur le plateau lors d'un
    // deplacement dans la direction donnee
    public int ligne(){
        return switch (this){
                case N -> -1;
                case S -> +1;
                default -> 0;
        };
    }
    
    // Donne la composante colonne du deplacement sur le plateau lors d'un
    // deplacement dans la direction donnee
    public int colonne(){
        return switch (this){
                case E -> +1;
                case O -> -1;
                default -> 0;
        };
    }
}


