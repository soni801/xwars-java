package com.xwars.main.loaders;

import com.xwars.main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Loads resources into the application
 *
 * @author Soni
 * @version 1
 */
public class ResourceLoader
{
    /**
     * Loads an image into the application
     *
     * @param path Path to the image
     * @return The image as a BufferedImage object
     */
    public BufferedImage loadImage(String path)
    {
        try
        {
            return ImageIO.read(getClass().getResource(path));
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Loads a font into the application
     *
     * @param path Path to the font
     * @return The font as a Font object
     */
    public Font loadFont(String path)
    {
        try
        {
            return Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(path)));
        }
        catch (FontFormatException | IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Get the path to an audio file
     *
     * @param path Path to the audio file
     * @return Path to the audio file
     */
    public String getAudioClip(String path)
    {
        return path;
    }
    
    /**
     * Loads a ResourceBundle into the application
     *
     * @param path Path to the ResourceBundle
     * @return The ResourceBundle found at the specified path
     */
    public ResourceBundle loadBundle(String path)
    {
        return ResourceBundle.getBundle(path);
    }
    
    /**
     * Gets the name of a String in the current application language
     *
     * @param id ID of the String to get
     * @return The name of the specified String in the current application language
     */
    public static String nameOf(String id)
    {
        try
        {
            return Game.BUNDLE.getString(id);
        }
        catch (Exception e)
        {
            return id;
        }
    }
}