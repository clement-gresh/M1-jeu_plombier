package projetIG.model;

import java.util.ArrayList;
import java.util.Arrays;

public enum Orientation {
    NORTH,
    EAST,
    SOUTH,
    WEST;
    
    static public ArrayList<Orientation> orientations(TypeTuyau type){   
        ArrayList<Orientation> orientations = new ArrayList<>();
        
        switch (type){
                case SOURCE :
                    orientations.add(Orientation.NORTH);
                    break;
                case LINE :
                    orientations.add(Orientation.NORTH);
                    orientations.add(Orientation.SOUTH);
                    break;
                case OVER :
                    orientations.add(Orientation.NORTH);
                    orientations.add(Orientation.EAST);
                    orientations.add(Orientation.SOUTH);
                    orientations.add(Orientation.WEST);
                    break;
                case TURN :
                    orientations.add(Orientation.NORTH);
                    orientations.add(Orientation.EAST);
                    break;
                case FORK :
                    orientations.add(Orientation.NORTH);
                    orientations.add(Orientation.EAST);
                    orientations.add(Orientation.SOUTH);
                    break;
                case CROSS :
                    orientations.add(Orientation.NORTH);
                    orientations.add(Orientation.EAST);
                    orientations.add(Orientation.SOUTH);
                    orientations.add(Orientation.WEST);
                    break; 
        }
        
        return orientations;
    }
}


