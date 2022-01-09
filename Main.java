package projetIG;

import javax.swing.JFrame;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Plumber");
        frame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        Plombier plombier = new Plombier(frame,
                                "src/main/java/projetIG/model/niveau/banque",
                                "src/main/java/projetIG/view/image/pipes.gif");
        frame.getContentPane().add(plombier);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
