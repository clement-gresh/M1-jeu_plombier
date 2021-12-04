package projetIG.model;

public enum TypeTuyau {
    SOURCE,
    LINE,
    OVER,
    TURN,
    FORK,
    CROSS;
    
    static public int appartient(String s){
        switch (s){
                case "SOURCE":
                        return 0;
                case "S":
                        return 0;
                case "LINE":
                        return 1;
                case "L":
                        return 1;
                case "OVER":
                        return 2;
                case "O":
                        return 2;
                case "TURN":
                        return 3;
                case "T":
                        return 3;
                case "FORK":
                        return 4;
                case "F":
                        return 4;
                case "CROSS":
                        return 5;
                case "C":
                        return 5;
        }
        return -1;
    }
}
