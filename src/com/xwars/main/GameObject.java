package com.xwars.main;

import java.awt.*;

/**
 * Abstract class containing info for all GameObjects
 *
 * @author soni801
 */

public abstract class GameObject
{
    public int x, y;

    public GameObject(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public abstract void tick();
    public abstract void render(Graphics g);
}