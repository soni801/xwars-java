package com.xwars.gameobjects;

import com.xwars.main.GameObject;
import com.xwars.states.Customise;
import com.xwars.states.Settings;

import java.awt.*;

/**
 * GameObject used as individual sqaures in the game itself
 *
 * @author soni801
 */

public class Tile extends GameObject
{
    private final Customise customise;

    public int posX, posY;

    public int player;
    public int hover;
    public int foundation;

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
        
        if (player == 0)
        {
            switch (hover)
            {
                case 1  : g.setColor(new Color((float)customise.playerColor[0].getRed() / 255, (float)customise.playerColor[0].getGreen() / 255, (float)customise.playerColor[0].getBlue() / 255, .5f)); break;
                case 2  : g.setColor(new Color((float)customise.playerColor[1].getRed() / 255, (float)customise.playerColor[1].getGreen() / 255, (float)customise.playerColor[1].getBlue() / 255, .5f)); break;
                default : g.setColor(new Color(0, 0, 0, 0));
            }
        }
        else
        {
            switch (player)
            {
                case 1  : g.setColor(customise.playerColor[0]); break;
                case 2  : g.setColor(customise.playerColor[1]); break;
                default : g.setColor(new Color(0, 0, 0, 0));
            }
        }
        
        switch (foundation)
        {
            case 1:
                g.fillRect(x, y, 5, 25);
                g.fillRect(x, y, 25, 5);
                break;
            case 2:
                g.fillRect(x, y, 25, 5);
                g.fillRect(x + 20, y, 5, 25);
                break;
            case 3:
                g.fillRect(x, y, 5, 25);
                g.fillRect(x, y + 20, 25, 5);
                break;
            case 4:
                g.fillRect(x + 20, y, 5, 25);
                g.fillRect(x, y + 20, 25, 5);
                break;
            default:
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
}