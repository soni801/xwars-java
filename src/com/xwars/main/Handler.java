package com.xwars.main;

import com.xwars.gameobjects.Tile;

import java.awt.*;
import java.util.ConcurrentModificationException;

/**
 * This class, when instantiated, handles rendering of all tiles
 *
 * @author Soni
 * @version 2
 */
public class Handler
{
    public Tile[][] tiles = new Tile[0][0];
    
    /**
     * Renders the tiles
     *
     * @param g Graphics object used for rendering
     */
    public void render(Graphics g)
    {
        try
        {
            for (Tile[] tileArray : tiles)
            {
                for (Tile tile : tileArray)
                {
                    tile.render(g);
                }
            }
        }
        catch (ConcurrentModificationException | NullPointerException ignored) {}
    }
}