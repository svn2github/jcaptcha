package com.octo.captcha.component.sound.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.TreeMap;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.octo.captcha.CaptchaException;

/**
 * @author Gandin Mathieu
 * @version 1.0
 */
public class StringToSound {
	
	protected String bundle;
	protected TreeMap list;
	
	public StringToSound(String thebundle) {
		this.bundle = thebundle;
		this.generateAlphabetSoundList();
	}
	
	public Sound getSound(String word) {
		char[] characters = word.toCharArray();
		
		Sound result = null;
		try {
			String firstToken = (String)this.list.get(String.valueOf(characters[1]));
			AudioInputStream ais = getAudioInputStream(
				firstToken);
			SourceDataLine line = getLine(ais);
			AudioInputStream[] listais = new AudioInputStream[characters.length];
			for(int i = 0;i < characters.length;i++) {
				String token = (String)this.list.get(String.valueOf(characters[i]));
				if(token != null) {
					listais[i] = getAudioInputStream(token);
				}
			}
			result = new Sound(line,listais);
			
		} catch (Exception e) {
			throw new CaptchaException(e);
		}
		return result;
	}
	
	protected AudioInputStream getAudioInputStream(String fileName) 
			throws UnsupportedAudioFileException, IOException {
		
		//File soundFile = new File("src/conf/"+fileName);
		InputStream is = StringToSound.class.getResourceAsStream("/"+fileName);
		
		AudioInputStream audioInputStream = 
			AudioSystem.getAudioInputStream(is);
		return audioInputStream;
	}
	
	protected SourceDataLine getLine(AudioInputStream audioInputStream) 
		throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		
		AudioFormat audioFormat1 = audioInputStream.getFormat();

		DataLine.Info info = new DataLine.Info(SourceDataLine.class,audioFormat1);
		SourceDataLine line = (SourceDataLine)AudioSystem.getLine(info);
		line.open(audioFormat1);
		
		return line;
	}
	
	protected void generateAlphabetSoundList() {
		this.list = new TreeMap();
		ResourceBundle resourceBundle = ResourceBundle.getBundle(this.bundle);
		Enumeration words = resourceBundle.getKeys();
		
		while(words.hasMoreElements()) {
			String line = (String)words.nextElement();
			String[] elements = line.split(";");
			list.put(elements[0],elements[1]);
		}
	}
}
