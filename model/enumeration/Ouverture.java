package projetIG.model.enumeration;

import java.util.ArrayList;

public enum Ouverture {
    NORTH,
    EAST,
    SOUTH,
    WEST;
    
    static public int changementLigne(Ouverture orientation){
        switch (orientation){
                case NORTH :
                    return -1;
                case SOUTH :
                    return +1;
                default :
                    return 0;
        }
    }
    
    
    static public int changementColonne(Ouverture orientation){
        switch (orientation){
                case EAST :
                    return +1;
                case WEST :
                    return -1;
                default :
                    return 0;
        }
    }
}


