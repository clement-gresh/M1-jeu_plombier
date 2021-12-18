package projetIG.model.enumeration;

public enum Rotation {
    PAS_DE_ROTATION,
    QUART_TOUR_HORAIRE,
    DEMI_TOUR,
    QUART_TOUR_TRIGO;
    
    
    // Determine le nombre de rotations necessaires de l'image pour un COIN, une BORDURE ou une CASE quelconque
    public static Rotation nombreRotationsCase(int ligne, int colonne, int nbrCasesLargeur, int nbrCasesHauteur){
        
        // Pour les coins du plateau
        if(ligne == 0 && colonne == 0) return Rotation.PAS_DE_ROTATION;
        else if(ligne == 0 && colonne == nbrCasesLargeur - 1) return Rotation.QUART_TOUR_HORAIRE;
        else if(ligne == nbrCasesHauteur - 1 && colonne == 0) return Rotation.QUART_TOUR_TRIGO;
        else if(ligne == nbrCasesHauteur - 1 && colonne == nbrCasesLargeur - 1) return Rotation.DEMI_TOUR;
        
        // Pour les bords du plateau
        else if(ligne == 0) return Rotation.PAS_DE_ROTATION;
        else if(ligne == nbrCasesHauteur - 1) return Rotation.DEMI_TOUR;
        else if(colonne == 0) return Rotation.QUART_TOUR_TRIGO;
        else if(colonne == nbrCasesLargeur - 1) return Rotation.QUART_TOUR_HORAIRE;
        
        //Pour les autres cases du plateau
        else return Rotation.PAS_DE_ROTATION;
    }
}
