package com.xwars.main;

import com.xwars.gameobjects.Tile;

import java.awt.*;
import java.util.ConcurrentModificationException;

/**
 * This class, when instantiated, handles ticking and rendering of all GameObjects,
 * using its <code>tick()</code> and <code>render()</code> methods
 *
 * @author soni801
 */

public class Handler
{
    public Tile[][] tiles = new Tile[0][0];

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