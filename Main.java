package projetIG;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Plumber");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        Plumber plumber = new Plumber(frame);
        frame.getContentPane().add(plumber);
        
        
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
