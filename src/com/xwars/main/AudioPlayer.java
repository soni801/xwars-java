package com.xwars.main;

import javax.sound.sampled.*;
import java.io.IOException;

/**
 * Used to play audio clips
 *
 * @author Soni
 * @version 1
 */
public class AudioPlayer
{
    static AudioPlayer audioPlayer = new AudioPlayer();
    
    /**
     * Plays an audio clip
     *
     * @param filePath Path to the audio clip
     * @param volume Volume to play the audio clip at
     */
    public static void playAudio(String filePath, float volume)
    {
        audioPlayer.play(filePath, volume);
    }
    
    /**
     * Plays an audio clip
     *
     * @param filePath Path to the audio clip
     * @param volume Volume to play the audio clip at
     */
    private void play(String filePath, float volume)
    {
        try
        {
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(this.getClass().getResource(filePath));

            Clip clip = AudioSystem.getClip();
            clip.open(inputStream);

            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * volume) + gainControl.getMinimum();
            gainControl.setValue(gain);

            clip.start();
        }
        catch (IOException | LineUnavailableException | UnsupportedAudioFileException e)
        {
            e.printStackTrace();
        }
    }
}