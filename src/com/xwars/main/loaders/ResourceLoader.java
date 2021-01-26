package com.xwars.main.loaders;

import com.xwars.main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * The <code>ResourceLoader</code> class is used to load resources into
 * the application.
 *
 * @author soni801
 */

public class ResourceLoader
{
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
    
    public String getAudioClip(String path)
    {
        return path;
    }
    
    public ResourceBundle loadBundle(String path)
    {
        return ResourceBundle.getBundle(path);
    }
    
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