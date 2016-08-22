package com.himself12794.guipractice;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 2097142934919914076L;
	
	private static final FileNameExtensionFilter SOUND_FILE_FILTER = new FileNameExtensionFilter("WAV File", "wav");
	
	// Buttons
	//private final JButton trollButton 		= new JButton("Click for Prize");
	private final JButton playPauseButton 	= new JButton("Play / Pause");
	private final JMenuBar menuBar			= new JMenuBar();
	private final JMenu fileMenu			= new JMenu("File");
	private final JMenuItem fileOption		= new JMenuItem("Open .wav file");

	// Panels
	private final JPanel imagePanel 		= new ImagePanel("images/trollface.jpg");
	private final JPanel audioButtonPanel 	= new JPanel(new FlowLayout(FlowLayout.CENTER));
	
	// Image Icon
	private final ImageIcon img 			= new ImageIcon("images/icon.gif");
	
	// Audio
	private final JLabel audioLocation 			= new JLabel(String.format(SoundMonitor.LENGTH_FORMAT, 0, 0, 0, 0));
	private final JSlider audioPositionSlider 	= new JSlider(0, 1, 0);
	private final SoundMonitor soundMonitor;
	
	public MainFrame() throws LineUnavailableException {

		soundMonitor = new SoundMonitor(null, audioLocation, audioPositionSlider, this);
		//soundMonitor.loadNewFile(loadWaveFile());
		menuBar.add(fileMenu);
		setJMenuBar(menuBar);
		fileMenu.add(fileOption);
		fileOption.addActionListener(l -> {
			
			File file = loadWaveFile();
			
			if (file != null) {

				soundMonitor.loadNewFile(file);
				
			}
		});
		
		audioButtonPanel.add(playPauseButton);
		audioButtonPanel.add(audioLocation);
		audioButtonPanel.add(audioPositionSlider);
		
		playPauseButton.addActionListener(e -> {
			synchronized (soundMonitor.getAudioClip()) {
				if (soundMonitor.getAudioClip().isRunning()) {
					soundMonitor.getAudioClip().stop();
				} else {
					soundMonitor.getAudioClip().start();
				}
			}
			
		});
		
		imagePanel.setVisible(false);
		
		add(audioButtonPanel, BorderLayout.SOUTH);
		setIconImage(img.getImage());
		
		setBounds(0, 0, audioButtonPanel.getWidth(), audioButtonPanel.getHeight());
		soundMonitor.start();
	}
	
	public File loadWaveFile() {
		final JFileChooser fc = new JFileChooser();
		
		fc.setFileFilter(SOUND_FILE_FILTER);
		
        int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            return file;
        } else {
        	return null;
        }
	}
	
	public static void main(String[] args) throws LineUnavailableException {
		MainFrame frame = new MainFrame();
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		if (args.length > 0) {
			File file = new File(args[0]);
			if (file.exists() && SOUND_FILE_FILTER.accept(file)) {
				frame.soundMonitor.loadNewFile(file);
			}
		}
		
	}
	
	public void notifyError(String title, String error) {
		JOptionPane.showMessageDialog(this, error, title, JOptionPane.ERROR_MESSAGE);
	}
	
	public void notifyWarning(String title, String error) {
		JOptionPane.showMessageDialog(this, error, title, JOptionPane.WARNING_MESSAGE);
	}

}
