package com.himself12794.guipractice;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.metal.MetalSliderUI;

public class SoundMonitor extends Thread implements LineListener, MouseListener, ChangeListener {

	public static final String LENGTH_FORMAT = "%02d:%02d / %02d:%02d";
	private Clip clip;
	private File audioFile = null;
	private String soundName = "";
	private final JLabel textComponent;
	private final JSlider mySlider;
	private final MainFrame mainFrame;
	private boolean noSliderUpdate = false;
	private static final AudioFormat preferredAudioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100.0F,
			16, 2, 4, 44100.0F, false);

	public SoundMonitor(String sound, JLabel text, JSlider slider, MainFrame frame) throws LineUnavailableException {
		super("Sound Monitor");
		mainFrame = frame;
		textComponent = text;
		loadClip();

		if (!(sound == null || "".equals(sound))) {
			soundName = sound;
			mainFrame.setTitle(soundName);
		}
		mySlider = slider;

		synchronized (clip) {
			clip.addLineListener(this);
		}

		synchronized (mySlider) {
			mySlider.addChangeListener(this);
			mySlider.addMouseListener(this);
			mySlider.setMaximum((int) (clip.getMicrosecondLength() / (1000)));
			mySlider.setUI(new MetalSliderUI() {
				protected void scrollDueToClickInTrack(int direction) {

					int value = slider.getValue();

					if (slider.getOrientation() == JSlider.HORIZONTAL) {
						value = this.valueForXPosition(slider.getMousePosition().x);
					} else if (slider.getOrientation() == JSlider.VERTICAL) {
						value = this.valueForYPosition(slider.getMousePosition().y);
					}
					slider.setValue(value);
				}
			});
		}
	}

	private void loadClip() throws LineUnavailableException {

		Line.Info linfo = new Line.Info(Clip.class);
		Line line = AudioSystem.getLine(linfo);
		clip = (Clip) line;

	}

	public Clip getAudioClip() {
		return clip;
	}

	public synchronized void loadNewFile(File file) {

		clip.stop();
		clip.flush();
		clip.close();

		try {

			loadClip();
			AudioInputStream in = AudioSystem.getAudioInputStream(file);
			if (!in.getFormat().matches(preferredAudioFormat)) {
				if (AudioSystem.isConversionSupported(preferredAudioFormat, in.getFormat())) {
					in = AudioSystem.getAudioInputStream(preferredAudioFormat, in);
				} else {
					mainFrame.notifyWarning("Unknown Audio Format", "The audio format \"" + in.getFormat() + "\" is not recognized and may not work.");
				}
			}
			clip.open(in);
			audioFile = file;
			synchronized (mySlider) {
				mySlider.setMaximum((int) (clip.getMicrosecondLength() / (1000)));
			}
			mainFrame.setTitle(audioFile.getName());
			recalculatePositionFromClip();
			clip.start();
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
			e.printStackTrace();
		}

	}

	public static int[] getTime(long micros) {
		long length = micros / (1000 * 1000);
		int minutes = (int) Math.floor(length / 60);
		int seconds = (int) Math.floor(length % 60);

		return new int[] { minutes, seconds };
	}

	@Override
	public void run() {

		do {

			if (!noSliderUpdate) {
				recalculatePositionFromClip();
			}

			try {
				sleep(1);
			} catch (InterruptedException e) {
			}

		} while (true);

	}

	public void recalculatePositionFromClip() {

		int[] length = getTime(clip.getMicrosecondLength());

		if (clip.isActive()) {
			int[] pos = getTime(clip.getMicrosecondPosition());
			synchronized (textComponent) {
				textComponent.setText(String.format(LENGTH_FORMAT, pos[0], pos[1], length[0], length[1]));
			}

			synchronized (mySlider) {
				mySlider.setValue((int) (clip.getMicrosecondPosition() / 1000));
			}
		}

	}

	public void recalculatePositionFromSlider() {

		if (noSliderUpdate) {

			int[] length = getTime(clip.getMicrosecondLength());
			int[] pos = getTime(mySlider.getValue() * 1000);

			synchronized (textComponent) {
				textComponent.setText(String.format(LENGTH_FORMAT, pos[0], pos[1], length[0], length[1]));
			}

		}

	}

	@Override
	public void update(LineEvent event) {
		if (event.getType() == Type.STOP) {
			if (clip.getMicrosecondPosition() == clip.getMicrosecondLength()) {
				synchronized (clip) {
					clip.setMicrosecondPosition(0);
				}
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getComponent().equals(mySlider)) {
			noSliderUpdate = true;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {

		if (e.getComponent().equals(mySlider)) {

			synchronized (clip) {
				clip.setMicrosecondPosition((long) mySlider.getValue() * (1000));
				recalculatePositionFromClip();
				noSliderUpdate = false;

			}

		}

	}
	
	public File getFile() {
		return audioFile;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void stateChanged(ChangeEvent e) {

		if (e.getSource().equals(mySlider)) {
			recalculatePositionFromSlider();
		}

	}

}
