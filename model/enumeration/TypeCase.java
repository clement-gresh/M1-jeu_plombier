package projetIG.model.enumeration;

public enum TypeCase {
    MARRON_FONCE,
    MARRON_CLAIR,
    GRIS,
    COIN,
    BORDURE,
    FIXE;
    
    // Determine si une case du plateau est un COIN, une BORDURE ou une CASE quelconque
    public static int typeCase(int ligne, int colonne, int nbrCasesLargeur, int nbrCasesHauteur){
        
        if((ligne == 0 || ligne == nbrCasesHauteur - 1) 
            && (colonne==0 || colonne == nbrCasesLargeur - 1)){
            return TypeCase.COIN.ordinal();
        }

        else if((ligne == 0 || ligne == nbrCasesHauteur - 1) 
            || (colonne==0 || colonne == nbrCasesLargeur - 1)){
            return TypeCase.BORDURE.ordinal();
        }

        else return TypeCase.MARRON_FONCE.ordinal();
    }
}
