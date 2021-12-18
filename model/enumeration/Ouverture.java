package projetIG.model.enumeration;

import java.util.ArrayList;

public enum Ouverture {
    NORTH,
    EAST,
    SOUTH,
    WEST;
    
    /*
    // Renvoie toutes les orientations d'un tuyau en fonction de sa rotation
    static public ArrayList<ArrayList<Ouverture>> ouvertures(TypeTuyau type, Rotation rotation){   
        ArrayList<ArrayList<Ouverture>> ouvertures = new ArrayList<>();
        ouvertures.add(new ArrayList<>());
        
        switch (type){
                case SOURCE :
                    ouvertures.get(0).add(Ouverture.values()[
                            (NORTH.ordinal() + rotation.ordinal())
                            % Ouverture.values().length]);
                    break;
                case LINE :
                    ouvertures.get(0).add(Ouverture.values()[
                            (NORTH.ordinal() + rotation.ordinal())
                            % Ouverture.values().length]);
                    ouvertures.get(0).add(Ouverture.values()[
                            (SOUTH.ordinal() + rotation.ordinal())
                            % Ouverture.values().length]);
                    break;
                case OVER :
                    ouvertures.get(0).add(Ouverture.values()[
                            (NORTH.ordinal() + rotation.ordinal())
                            % Ouverture.values().length]);
                    ouvertures.get(0).add(Ouverture.values()[
                            (SOUTH.ordinal() + rotation.ordinal())
                            % Ouverture.values().length]);
                    
                    ouvertures.add(new ArrayList<>());
                    ouvertures.get(1).add(Ouverture.values()[
                            (EAST.ordinal() + rotation.ordinal())
                            % Ouverture.values().length]);
                    ouvertures.get(1).add(Ouverture.values()[
                            (WEST.ordinal() + rotation.ordinal())
                            % Ouverture.values().length]);
                    break;
                case TURN :
                    ouvertures.get(0).add(Ouverture.values()[
                            (NORTH.ordinal() + rotation.ordinal())
                            % Ouverture.values().length]);
                    ouvertures.get(0).add(Ouverture.values()[
                            (EAST.ordinal() + rotation.ordinal())
                            % Ouverture.values().length]);
                    break;
                case FORK :
                    ouvertures.get(0).add(Ouverture.values()[
                            (NORTH.ordinal() + rotation.ordinal())
                            % Ouverture.values().length]);
                    ouvertures.get(0).add(Ouverture.values()[
                            (EAST.ordinal() + rotation.ordinal())
                            % Ouverture.values().length]);
                    ouvertures.get(0).add(Ouverture.values()[
                            (SOUTH.ordinal() + rotation.ordinal())
                            % Ouverture.values().length]);
                    break;
                case CROSS :
                    ouvertures.get(0).add(Ouverture.NORTH);
                    ouvertures.get(0).add(Ouverture.EAST);
                    ouvertures.get(0).add(Ouverture.SOUTH);
                    ouvertures.get(0).add(Ouverture.WEST);
                    break; 
        }
        
        return ouvertures;
    }
    */
    
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


