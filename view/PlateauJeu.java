package projetIG.view;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

public class PlateauJeu extends JComponent {

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
        
        ImageIcon pipes = new ImageIcon("src/main/java/projetIG/view/image/pipes.png");
        this.add(new JLabel(pipes));
    }
}
