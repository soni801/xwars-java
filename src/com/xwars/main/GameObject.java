package com.xwars.main;

/*
 * Author: soni801
 */

import java.awt.*;

public abstract class GameObject
{
    public int x, y;
    public int velX, velY;

    public GameObject(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public abstract void tick();
    public abstract void render(Graphics g);
    public abstract Rectangle getBounds();

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setVelX(int velX) { this.velX = velX; }
    public void setVelY(int velY) { this.velY = velY; }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getVelX() { return velX; }
    public int getVelY() { return velY; }
}