package projetIG.model;

import java.util.ArrayList;

public enum Orientation {
    NORTH,
    EAST,
    SOUTH,
    WEST;
    
    // Renvoie toutes les orientations d'un tuyau en fonction de sa rotation
    static public ArrayList<ArrayList<Orientation>> orientations(TypeTuyau type, Rotation rotation){   
        ArrayList<ArrayList<Orientation>> orientations = new ArrayList<>();
        orientations.add(new ArrayList<>());
        
        switch (type){
                case SOURCE :
                    orientations.get(0).add(Orientation.values()[
                            (NORTH.ordinal() + rotation.ordinal())
                            % Orientation.values().length]);
                    break;
                case LINE :
                    orientations.get(0).add(Orientation.values()[
                            (NORTH.ordinal() + rotation.ordinal())
                            % Orientation.values().length]);
                    orientations.get(0).add(Orientation.values()[
                            (SOUTH.ordinal() + rotation.ordinal())
                            % Orientation.values().length]);
                    break;
                case OVER :
                    orientations.get(0).add(Orientation.values()[
                            (NORTH.ordinal() + rotation.ordinal())
                            % Orientation.values().length]);
                    orientations.get(0).add(Orientation.values()[
                            (SOUTH.ordinal() + rotation.ordinal())
                            % Orientation.values().length]);
                    
                    orientations.add(new ArrayList<Orientation>());
                    orientations.get(1).add(Orientation.values()[
                            (EAST.ordinal() + rotation.ordinal())
                            % Orientation.values().length]);
                    orientations.get(1).add(Orientation.values()[
                            (WEST.ordinal() + rotation.ordinal())
                            % Orientation.values().length]);
                    break;
                case TURN :
                    orientations.get(0).add(Orientation.values()[
                            (NORTH.ordinal() + rotation.ordinal())
                            % Orientation.values().length]);
                    orientations.get(0).add(Orientation.values()[
                            (EAST.ordinal() + rotation.ordinal())
                            % Orientation.values().length]);
                    break;
                case FORK :
                    orientations.get(0).add(Orientation.values()[
                            (NORTH.ordinal() + rotation.ordinal())
                            % Orientation.values().length]);
                    orientations.get(0).add(Orientation.values()[
                            (EAST.ordinal() + rotation.ordinal())
                            % Orientation.values().length]);
                    orientations.get(0).add(Orientation.values()[
                            (SOUTH.ordinal() + rotation.ordinal())
                            % Orientation.values().length]);
                    break;
                case CROSS :
                    orientations.get(0).add(Orientation.NORTH);
                    orientations.get(0).add(Orientation.EAST);
                    orientations.get(0).add(Orientation.SOUTH);
                    orientations.get(0).add(Orientation.WEST);
                    break; 
        }
        
        return orientations;
    }
    
    
    static public int changementLigne(Orientation orientation){
        switch (orientation){
                case NORTH :
                    return -1;
                case SOUTH :
                    return +1;
                default :
                    return 0;
        }
    }
    
    
    static public int changementColonne(Orientation orientation){
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


