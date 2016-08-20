package com.himself12794.guipractice;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel{
	
	private static final long serialVersionUID = 7904889758413996800L;
	
	private BufferedImage image;

    public ImagePanel() {
       try {                
          image = ImageIO.read(new File("images/trollface.jpg"));
       } catch (IOException ex) {
            // handle exception...
       }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters            
    }

}
