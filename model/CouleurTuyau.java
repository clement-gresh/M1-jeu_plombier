package projetIG.model;

public enum CouleurTuyau {
    BLANC,
    ROUGE,
    VERT,
    BLEU,
    JAUNE,
    NOIR,
    PAS_UNE_COULEUR;
    
    
    static public CouleurTuyau appartient(String s){
            switch (s){
                    case "BLANC":
                        return BLANC;
                    case "W":
                        return BLANC;
                    case "ROUGE":
                        return ROUGE;
                    case "R":
                        return ROUGE;
                    case "VERT":
                        return VERT;
                    case "G":
                        return VERT;
                    case "BLEU":
                        return BLEU;
                    case "B":
                        return BLEU;
                    case "JAUNE":
                        return JAUNE;
                    case "Y":
                        return JAUNE;
                    case "NOIR":
                        return NOIR;
                    default:
                        return PAS_UNE_COULEUR;
        }
    }
}
