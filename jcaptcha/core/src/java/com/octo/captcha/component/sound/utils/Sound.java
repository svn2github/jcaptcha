package com.octo.captcha.component.sound.utils;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.SourceDataLine;

import com.octo.captcha.CaptchaException;

/**
 * @author Gandin Mathieu
 * @version 1.0
 */
public class Sound {

	protected SourceDataLine line;
	protected AudioInputStream[] ais;
	private static final int EXTERNAL_BUFFER_SIZE = 128000;

	public Sound(SourceDataLine theline,AudioInputStream[] theais) {
		this.line = theline;
		this.ais = theais;
	}
	
	public AudioInputStream[] getais() {
		return this.ais;	
	}
	
	public int getSize() {
		return EXTERNAL_BUFFER_SIZE;
	}
	
	public void play() {
		for(int i = 0; i <this.ais.length;i++) {
			if(ais[i] != null) {
				playaSound(ais[i]);
			}
		}
	}

	private void playaSound(AudioInputStream audioInputStream) {
		try {
			line.start();
			int nbBytesRead = 0;
			byte[] abData = new byte[EXTERNAL_BUFFER_SIZE];
			while(nbBytesRead != -1) {
				nbBytesRead = audioInputStream.read(abData,0,abData.length);
				if(nbBytesRead >= 0) {
					int nbByteWritten = line.write(abData,0,nbBytesRead);
				}
				audioInputStream.close();
			}
			line.drain();
			line.stop();
		} catch (IOException e) {
			throw new CaptchaException(e);
		}
	}

}
