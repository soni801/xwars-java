package com.xwars.main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Used to load image files into <code>BufferedImage</code> objects
 */

public class BufferedImageLoader
{
    BufferedImage image;

    public BufferedImage loadImage(String path)
    {
        try
        {
            image = ImageIO.read(getClass().getResource(path));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return image;
    }
}