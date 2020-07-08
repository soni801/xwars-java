package com.xwars.gameobjects;

/*
 * Author: soni801
 */

import com.xwars.main.GameObject;
import com.xwars.states.Customise;
import com.xwars.states.Settings;

import java.awt.*;

public class Tile extends GameObject
{
    private Customise customise;

    public int posX, posY;

    public int player;

    public Tile(int x, int y, int posX, int posY, Customise customise)
    {
        super(x, y);
        this.posX = posX;
        this.posY = posY;
        this.customise = customise;
    }

    @Override
    public void tick()
    {

    }

    @Override
    public void render(Graphics g)
    {
        switch (Settings.settings.get("theme"))
        {
            case "light" : g.setColor(new Color(200, 200, 200)); break;
            case "dark"  : g.setColor(new Color(50, 50, 50));    break;
        }

        g.drawRect(x, y, 25, 25);
        g.drawRect(x + 1, y + 1, 25 - 2, 25 - 2);

        if (player != 0)
        {
            switch (player)
            {
                case 1 : g.setColor(customise.playerColor[0]); break;
                case 2 : g.setColor(customise.playerColor[1]); break;
            }

            g.fillRect(x, y, 5, 5);
            g.fillRect(x + 5, y + 5, 5, 5);
            g.fillRect(x + 10, y + 10, 5, 5);
            g.fillRect(x + 15, y + 5, 5, 5);
            g.fillRect(x + 20, y, 5, 5);
            g.fillRect(x, y + 20, 5, 5);
            g.fillRect(x + 5, y + 15, 5, 5);
            g.fillRect(x + 15, y + 15, 5, 5);
            g.fillRect(x + 20, y + 20, 5, 5);
        }
    }

    @Override
    public Rectangle getBounds()
    {
        return null;
    }
}