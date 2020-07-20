package com.xwars.main;

import com.xwars.states.Settings;

import javax.sound.sampled.*;
import java.io.*;
import java.net.URISyntaxException;

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
            AudioInputStream inputStream;
            if (Settings.environment.equals("JAR")) inputStream = AudioSystem.getAudioInputStream(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath() + "/res" + filePath).getAbsoluteFile());
            else inputStream = AudioSystem.getAudioInputStream(new File(System.getProperty("user.dir") + "/res" + filePath).getAbsoluteFile());

            Clip clip = AudioSystem.getClip();
            clip.open(inputStream);

            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * volume) + gainControl.getMinimum();
            gainControl.setValue(gain);

            clip.start();
        }
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException | URISyntaxException e)
        {
            e.printStackTrace();
        }
    }
}