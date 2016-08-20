package com.himself12794.guipractice;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 2097142934919914076L;
	
	private JButton jbtCalcResults = new JButton("Click for Prize");

	private ImagePanel imgPan;
	private ImageIcon img = new ImageIcon("images/icon.gif");
	
	public MainFrame() {
		
		JPanel p1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		p1.add(jbtCalcResults);
		
		imgPan = new ImagePanel();
		imgPan.setVisible(false);
		
		
		add(imgPan);
		add(p1, BorderLayout.SOUTH);
		
		jbtCalcResults.addActionListener(i -> {
			setBounds(0, 0, 625, 650);
			imgPan.setVisible(true);
			playSound();
			p1.setVisible(false);
		});
		
		setIconImage(img.getImage());
	}
	
	public static void main(String[] args) {
		MainFrame frame = new MainFrame();
		frame.pack();
		frame.setTitle("Trololol");
		frame.setLocationRelativeTo(null);
		frame.setBounds(0, 0, 100, 100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public void playSound() {
		try {
		    File soundFile = new File("audio/troll_song.wav");
		    System.out.println(soundFile.exists());
		    InputStream in = new FileInputStream(soundFile);

		    // create an audiostream from the inputstream
		    AudioStream audioStream = new AudioStream(in);
		    System.out.println(audioStream.getLength());
		    // play the audio clip with the audioplayer class
		    AudioPlayer.player.start(audioStream);
		    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
