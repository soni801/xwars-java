package com.xwars.main;

import javax.sound.sampled.*;
import java.io.*;

/**
 * The <code>AudioPlayer</code> class is used to play an audio clip
 * using its <code>playAudio</code> method
 *
 * @author soni801
 */

public class AudioPlayer
{
    public static void playAudio(String filePath, float volume)
    {
        try
        {
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(inputStream);

            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * volume) + gainControl.getMinimum();
            gainControl.setValue(gain);

            clip.start();
        }
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException e)
        {
            e.printStackTrace();
        }
    }
}