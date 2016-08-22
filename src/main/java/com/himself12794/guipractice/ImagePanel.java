package com.himself12794.guipractice;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;

public class ImagePanel extends JPanel{
	
	private static final long serialVersionUID = 7904889758413996800L;
	
	private Image image;

    public ImagePanel(String location) {
    	image = Toolkit.getDefaultToolkit().getImage(location);
    	setSize(new Dimension(this.image.getHeight(null), this.image.getWidth(null)));
    }

    @Override
    protected void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	g.drawImage(image, 0, 0, null);
    }

}
