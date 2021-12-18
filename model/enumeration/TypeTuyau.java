package projetIG.model.enumeration;

import java.util.ArrayList;
import java.util.Arrays;
import static projetIG.model.enumeration.Ouverture.EAST;
import static projetIG.model.enumeration.Ouverture.NORTH;
import static projetIG.model.enumeration.Ouverture.SOUTH;
import static projetIG.model.enumeration.Ouverture.WEST;

public enum TypeTuyau {
    SOURCE,
    LINE,
    OVER,
    TURN,
    FORK,
    CROSS,
    NOT_A_PIPE;
    
    static public final ArrayList<ArrayList<ArrayList<Ouverture>>> ouvertures = new ArrayList<>();
    
    static public final Ouverture[][][] ouverturesTableau = {
        {{Ouverture.NORTH}},
        {{Ouverture.NORTH, Ouverture.SOUTH}},
        {{Ouverture.NORTH, Ouverture.SOUTH}, {Ouverture.EAST, Ouverture.WEST}},
        {{Ouverture.NORTH, Ouverture.EAST}},
        {{Ouverture.NORTH, Ouverture.EAST, Ouverture.SOUTH}},
        {{Ouverture.NORTH, Ouverture.EAST, Ouverture.SOUTH, Ouverture.WEST}}
            
    };
    
    // Initialisation de l'ArrayList ouvertures a partir du tableau ouverturesTableau
    static {
        int i = 0;
        int j = 0;
        
        for(Ouverture[][] array1 : ouverturesTableau){
            ouvertures.add(new ArrayList<>());
            
            for(Ouverture[] array2 : array1){
                ouvertures.get(i).add(new ArrayList<>());
                
                for(Ouverture ouverture : array2){
                    ouvertures.get(i).get(j).add(ouverture);
                }
                j = j + 1;
            }
            i = i + 1;
            j = 0;
        }
        
        System.out.println("ouvertures : " + ouvertures);
    }
    
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
    
    /*
    static public final Ouverture[][][] ouvertures = {
        {{Ouverture.NORTH}},
        {{Ouverture.NORTH, Ouverture.SOUTH}},
        {{Ouverture.NORTH, Ouverture.SOUTH}, {Ouverture.EAST, Ouverture.WEST}},
        {{Ouverture.NORTH, Ouverture.EAST}},
        {{Ouverture.NORTH, Ouverture.EAST, Ouverture.SOUTH}},
        {{Ouverture.NORTH, Ouverture.EAST, Ouverture.SOUTH, Ouverture.WEST}}
    };
    
    static public ArrayList<ArrayList<Ouverture>> listeOuvertures(TypeTuyau typeTuyau){
        ArrayList<ArrayList<Ouverture>> listeOuvertures;
        
        for(int i = 0; i < ouvertures[typeTuyau.ordinal()].length; i ++){
            ArrayList<Ouverture> liste = Arrays.asList(ouvertures[typeTuyau.ordinal()][i]);
            listeOuvertures.add();
        }
    }
    
    
    static public final boolean[][][] ouverturesBool = new boolean[ouvertures.length][2][Ouverture.values().length];
    
    static {
        // Initialisation de toutes les valeurs à false
        for(boolean[][] array1 : ouverturesBool){
            for(boolean[] array2 : array1){
                Arrays.fill(array2, false);
            }
        }
        
        // Mise a true pour les ouvertures disponibles
        for(int i = 0; i < ouvertures.length; i++){
            for(int j = 0; j < ouvertures[i].length; j++){
                for(Ouverture ouverture : ouvertures[i][j]) {
                    switch (ouverture) {
                        case NORTH :
                            ouverturesBool[i][j][0] = true;
                            break;
                        case EAST :
                            ouverturesBool[i][j][1] = true;
                            break;
                        case SOUTH :
                            ouverturesBool[i][j][2] = true;
                            break;
                        case WEST :
                            ouverturesBool[i][j][3] = true;
                            break;
                    }
                }
            }
        }
    }
    */
    
    static public TypeTuyau appartient(String s){
        switch (s){
                case "SOURCE":
                        return SOURCE;
                case "R":
                        return SOURCE;
                case "G":
                        return SOURCE;
                case "B":
                        return SOURCE;
                case "Y":
                        return SOURCE;
                case "LINE":
                        return LINE;
                case "L":
                        return LINE;
                case "OVER":
                        return OVER;
                case "O":
                        return OVER;
                case "TURN":
                        return TURN;
                case "T":
                        return TURN;
                case "FORK":
                        return FORK;
                case "F":
                        return FORK;
                case "CROSS":
                        return CROSS;
                case "C":
                        return CROSS;
                default:
                        return NOT_A_PIPE;
        }
    }
}
