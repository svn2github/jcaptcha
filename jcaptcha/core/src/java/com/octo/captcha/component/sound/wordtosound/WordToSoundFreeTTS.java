/*
 * Created on 16 mars 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.octo.captcha.component.sound.wordtosound;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.Vector;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;

import com.octo.captcha.CaptchaException;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import com.sun.speech.freetts.audio.AudioPlayer;
import com.sun.speech.freetts.util.Utilities;

/**
 * @author Benoit TODO To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Style - Code Templates
 */
public class WordToSoundFreeTTS extends AbstractWordToSound
{
    private static String defaultVoice = "kevin16";

    private int maxAcceptedWordLenght;

    private int minAcceptedWordLenght;

    private String voiceName = null;

    private Voice voice = null;

    private VoiceManager voiceManager = null;

    private boolean isInitiated = false;

    /**
     * Constructor
     * 
     * @deprecated
     */
    public WordToSoundFreeTTS()
    {
        voiceName = WordToSoundFreeTTS.defaultVoice;
        minAcceptedWordLenght = 4;
        maxAcceptedWordLenght = 6;
    }

    public WordToSoundFreeTTS(String voiceName, int minAcceptedWordLenght, int maxAcceptedWordLenght)
    {
        this.voiceName = voiceName;
        this.minAcceptedWordLenght = minAcceptedWordLenght;
        this.maxAcceptedWordLenght = maxAcceptedWordLenght;
    }

    public int getMaxAcceptedWordLenght()
    {
        return maxAcceptedWordLenght;
    }

    public int getMinAcceptedWordLenght()
    {
        return minAcceptedWordLenght;
    }

    /**
     * @see com.octo.captcha.component.sound.wordtosound.AbstractWordToSound#modifySound(javax.sound.sampled.AudioInputStream)
     */
    AudioInputStream modifiedSound(String word)
    {
        //Simple implementation, no modification
        return stringToSound(word);
    }

    /**
     * Method to initialise the toolkit
     * 
     * @param voice
     *            name
     */
    private void init()
    {
        if (!isInitiated)
        {
            //Voices use by freeTTS, we define where they are, currently in the java en_us.jar
            System
                .getProperties()
                .put(
                    "freetts.voices",
                    "com.sun.speech.freetts.en.us.cmu_time_awb.AlanVoiceDirectory,com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
            // The VoiceManager manages all the voices for FreeTTS.
            voiceManager = VoiceManager.getInstance();

            this.voice = voiceManager.getVoice(this.voiceName);
            // Allocates the resources for the voice.
            this.voice.allocate();
            isInitiated = true;
        }
    }

    /**
     * Main method for this service Return an image with the specified. Synchronisation is very
     * important, for multi threading execution
     * 
     * @return the generated sound
     * @throws com.octo.captcha.CaptchaException
     *             if word is invalid or an exception occurs during the sound generation
     */
    public synchronized AudioInputStream stringToSound(String sentence) throws CaptchaException
    {
        init();
        //use the custom (see inner class) InputStreamAudioPlayer, which provide interface to
        // Audio Stream
        InputStreamAudioPlayer audioPlayer = new InputStreamAudioPlayer();

        this.voice.setAudioPlayer(audioPlayer);

        // Synthesize speech.
        this.voice.speak(sentence);

        AudioInputStream ais = audioPlayer.getAudioInputStream();
        return ais;
    }

    /**
     * @see com.octo.captcha.component.sound.wordtosound.WordToSound#getVoiceName()
     */
    public String getSoundName()
    {
        init();
        return voiceName;
    }

    /**
     * @see com.octo.captcha.component.sound.wordtosound.WordToSound#getSoundVolume()
     */
    public float getSoundVolume()
    {
        init();
        return voice.getVolume();
    }

    /**
     * @see com.octo.captcha.component.sound.wordtosound.WordToSound#getSoundPitch()
     */
    public float getSoundPitch()
    {
        init();
        return voice.getPitch();
    }

    /**
     * @see com.octo.captcha.component.sound.wordtosound.WordToSound#getSoundSpeakingRate()
     */
    public float getSoundSpeakingRate()
    {
        init();
        return voice.getRate();
    }

    /**
     * Implementation of freeTTS AudioPlayer interface, to produce an audioInputStream, this is not
     * a very clean way since it doesn't really play. But it is the only way to get a stream easily
     */
    public class InputStreamAudioPlayer implements AudioPlayer
    {
        private boolean debug = false;

        private AudioFormat currentFormat = null;

        private byte[] outputData;

        private int curIndex = 0;

        private int totBytes = 0;

        private Vector outputList;

        private AudioInputStream audioInputStream;

        /**
         * Constructs a InputStreamAudioPlayer
         * 
         * @param baseName
         *            the base name of the audio file
         * @param type
         *            the type of audio output
         */
        public InputStreamAudioPlayer()
        {
            debug = Utilities.getBoolean("com.sun.speech.freetts.audio.AudioPlayer.debug");
            outputList = new Vector();
        }

        /**
         * Sets the audio format for this player
         * 
         * @param format
         *            the audio format
         * @throws UnsupportedOperationException
         *             if the line cannot be opened with the given format
         */
        public synchronized void setAudioFormat(AudioFormat format)
        {
            currentFormat = format;
        }

        /**
         * Gets the audio format for this player
         * 
         * @return format the audio format
         */
        public AudioFormat getAudioFormat()
        {
            return currentFormat;
        }

        /**
         * Pauses audio output
         */
        public void pause()
        {
        }

        /**
         * Resumes audio output
         */
        public synchronized void resume()
        {
        }

        /**
         * Cancels currently playing audio
         */
        public synchronized void cancel()
        {
        }

        /**
         * Prepares for another batch of output. Larger groups of output (such as all output
         * associated with a single FreeTTSSpeakable) should be grouped between a reset/drain pair.
         */
        public synchronized void reset()
        {
        }

        /**
         * Starts the first sample timer
         */
        public void startFirstSampleTimer()
        {
        }

        /**
         * Closes this audio player
         */
        public synchronized void close()
        {
            try
            {
                audioInputStream.close();
            }
            catch (IOException ioe)
            {
                System.err.println("Problem while closing the audioInputSteam");
            }

        }

        public AudioInputStream getAudioInputStream()
        {
            InputStream tInputStream = new SequenceInputStream(outputList.elements());
            AudioInputStream tAudioInputStream = new AudioInputStream(tInputStream, currentFormat,
                totBytes / currentFormat.getFrameSize());

            return tAudioInputStream;
        }

        /**
         * Returns the current volume.
         * 
         * @return the current volume (between 0 and 1)
         */
        public float getVolume()
        {
            return 1.0f;
        }

        /**
         * Sets the current volume.
         * 
         * @param volume
         *            the current volume (between 0 and 1)
         */
        public void setVolume(float volume)
        {
        }

        /**
         * Starts the output of a set of data. Audio data for a single utterance should be grouped
         * between begin/end pairs.
         * 
         * @param size
         *            the size of data between now and the end
         */
        public void begin(int size)
        {
            outputData = new byte[size];
            curIndex = 0;
        }

        /**
         * Marks the end of a set of data. Audio data for a single utterance should be groupd
         * between begin/end pairs.
         * 
         * @return true if the audio was output properly, false if the output was cancelled or
         *         interrupted.
         */
        public boolean end()
        {
            outputList.add(new ByteArrayInputStream(outputData));
            totBytes += outputData.length;
            return true;
        }

        /**
         * Waits for all queued audio to be played
         * 
         * @return true if the audio played to completion, false if the audio was stopped
         */
        public boolean drain()
        {
            return true;
        }

        /**
         * Gets the amount of played since the last mark
         * 
         * @return the amount of audio in milliseconds
         */
        public synchronized long getTime()
        {
            return -1L;
        }

        /**
         * Resets the audio clock
         */
        public synchronized void resetTime()
        {
        }

        /**
         * Writes the given bytes to the audio stream
         * 
         * @param audioData
         *            audio data to write to the device
         * @return <code>true</code> of the write completed successfully, <code> false </code> if
         *         the write was cancelled.
         */
        public boolean write(byte[] audioData)
        {
            return write(audioData, 0, audioData.length);
        }

        /**
         * Writes the given bytes to the audio stream
         * 
         * @param bytes
         *            audio data to write to the device
         * @param offset
         *            the offset into the buffer
         * @param size
         *            the size into the buffer
         * @return <code>true</code> of the write completed successfully, <code> false </code> if
         *         the write was cancelled.
         */
        public boolean write(byte[] bytes, int offset, int size)
        {
            System.arraycopy(bytes, offset, outputData, curIndex, size);
            curIndex += size;
            return true;
        }

        /**
         * Waits for resume. If this audio player is paused waits for the player to be resumed.
         * Returns if resumed, cancelled or shutdown.
         * 
         * @return true if the output has been resumed, false if the output has been cancelled or
         *         shutdown.
         */
        private synchronized boolean waitResume()
        {
            return true;
        }

        /**
         * Returns the name of this audioplayer
         * 
         * @return the name of the audio player
         */
        public String toString()
        {
            return "AudioInputStreamAudioPlayer";
        }

        /**
         * Outputs a debug message if debugging is turned on
         * 
         * @param msg
         *            the message to output
         */
        private void debugPrint(String msg)
        {
            if (debug)
            {
                System.out.println(toString() + ": " + msg);
            }
        }

        /**
         * Shows metrics for this audio player
         */
        public void showMetrics()
        {
        }
    }

}

