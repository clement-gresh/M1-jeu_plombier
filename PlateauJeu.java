package projetIG;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JComponent;

public class PlateauJeu extends JComponent {

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
        
        //ImageIcon pipes = new ImageIcon("images/pipes.svg");
        //this.add(new JLabel(pipes));
    }
}
