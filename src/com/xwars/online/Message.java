package com.xwars.online;

import java.awt.*;
import java.io.Serializable;

/**
 * The <code>Message</code> class is used to store data sent between
 * server and client.
 *
 * @author soni801
 */

public class Message implements Serializable
{
    public String mode;
    
    // Start game, mode = "start"
    public String name; // Player name
    public Color color; // Player color
    public int[] size; // Board size
    public int[] foundation; // Foundation position
    
    // Place tile, mode = "tile"
    public int[] position = new int[2]; // Tile position
    public boolean invade; // Whether the player is invading the enemy player's foundation unit
}