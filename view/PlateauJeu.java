package projetIG.view;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JComponent;
import projetIG.model.level.Niveau;

public class PlateauJeu extends JComponent {

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
        
        Niveau niveau = new Niveau();   //debug
        
        /*
        ImageIcon pipes = new ImageIcon("src/main/java/projetIG/view/image/pipes.png");
        this.add(new JLabel(pipes));
        */
    }
}
