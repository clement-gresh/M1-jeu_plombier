package projetIG.model.enumeration;

public enum TypeTuyau {
    SOURCE,
    LINE,
    OVER,
    TURN,
    FORK,
    CROSS,
    NOT_A_PIPE;
    
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
