package com.xwars.main;

import javax.sound.sampled.*;
import java.io.IOException;

/**
 * The <code>AudioPlayer</code> class is used to play an audio clip
 * using its <code>playAudio</code> method
 *
 * @author soni801
 */

public class AudioPlayer
{
    static AudioPlayer audioPlayer = new AudioPlayer();

    public static void playAudio(String filePath, float volume)
    {
        audioPlayer.play(filePath, volume);
    }

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
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e)
        {
            e.printStackTrace();
        }
    }
}