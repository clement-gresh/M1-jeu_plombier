package projetIG;

import java.io.File;
import javax.swing.JFrame;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

public class Main {
    public static final int NBR_BANQUES = 2;

    public static void main(String[] args) {
        //Chemin dependant du compilateur / de l'IDE
        // 1 : chemin NetBeans, 2 : chemin compilateur javac
        String[] chemins = {"./src/main/java/projetIG", "./projetIG"};
        String chemin = "";
        String cheminImg = "";
        try{
            for(String s : chemins){
                if(new File(s).exists()) {
                    chemin = s + "/model/niveau/banque";
                    cheminImg = s + "/view/image";
                }
            }
            if(chemin.equals("")) { throw new Exception(
               "Main.java : aucun des chemins vers les niveaux ne correspond.");
            }
        }
        catch(Exception e){ System.err.println(e.getMessage()); }
        
        JFrame frame = new JFrame("Plumber");
        frame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        Plombier plombier = new Plombier(frame, chemin, cheminImg);
        frame.getContentPane().add(plombier);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
