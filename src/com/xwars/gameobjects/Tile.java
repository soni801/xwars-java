package com.xwars.gameobjects;

import com.xwars.states.Customise;
import com.xwars.states.HUD;
import com.xwars.states.Settings;

import java.awt.*;

/**
 * GameObject used as individual sqaures in the game itself
 *
 * @author soni801
 */

public class Tile
{
    private final Customise customise;
    private final Settings settings;
    private final HUD hud;

    public int x, y;
    public int posX, posY;

    public int player;
    public int hover;
    public int foundation;
    public boolean highlighted;
    public boolean invaded;

    public Tile(int x, int y, int posX, int posY, Customise customise, Settings settings, HUD hud)
    {
        this.x = x;
        this.y = y;
        this.posX = posX;
        this.posY = posY;
        
        this.customise = customise;
        this.settings = settings;
        this.hud = hud;
    }

    public void render(Graphics g)
    {
        Color enemyColor;
        
        if (player == 1) enemyColor = customise.playerColor[1];
        else enemyColor = customise.playerColor[0];
        
        if (highlighted) g.setColor(new Color((float)customise.playerColor[hud.currentPlayer - 1].getRed() / 255, (float)customise.playerColor[hud.currentPlayer - 1].getGreen() / 255, (float)customise.playerColor[hud.currentPlayer - 1].getBlue() / 255, .3f));
        else
        {
            switch (settings.settings.get("theme"))
            {
                case "light" : g.setColor(new Color(200, 200, 200)); break;
                case "dark"  : g.setColor(new Color(50, 50, 50));    break;
            }
        }

        g.drawRect(x, y, 25, 25);
        g.drawRect(x + 1, y + 1, 25 - 2, 25 - 2);
        
        if (foundation != 0)
        {
            if (invaded) g.setColor(enemyColor);
            else
            {
                switch (hover)
                {
                    case 1  : g.setColor(new Color((float)customise.playerColor[0].getRed() / 255, (float)customise.playerColor[0].getGreen() / 255, (float)customise.playerColor[0].getBlue() / 255, .35f)); break;
                    case 2  : g.setColor(new Color((float)customise.playerColor[1].getRed() / 255, (float)customise.playerColor[1].getGreen() / 255, (float)customise.playerColor[1].getBlue() / 255, .35f)); break;
                    default : g.setColor(new Color(0, 0, 0, 0));
                }
            }
    
            switch (foundation)
            {
                case 1:
                    g.fillRect(x + 5, y + 5, 4, 4);
                    g.fillRect(x + 9, y + 9, 4, 4);
                    g.fillRect(x + 13, y + 13, 4, 4);
                    g.fillRect(x + 17, y + 17, 4, 4);
                    g.fillRect(x + 21, y + 21, 4, 4);
                    g.fillRect(x + 5, y + 21, 4, 4);
                    g.fillRect(x + 9, y + 17, 4, 4);
                    g.fillRect(x + 17, y + 9, 4, 4);
                    g.fillRect(x + 21, y + 5, 4, 4);
                    break;
                case 2:
                    g.fillRect(x, y + 5, 4, 4);
                    g.fillRect(x + 4, y + 9, 4, 4);
                    g.fillRect(x + 8, y + 13, 4, 4);
                    g.fillRect(x + 12, y + 17, 4, 4);
                    g.fillRect(x + 16, y + 21, 4, 4);
                    g.fillRect(x, y + 21, 4, 4);
                    g.fillRect(x + 4, y + 17, 4, 4);
                    g.fillRect(x + 12, y + 9, 4, 4);
                    g.fillRect(x + 16, y + 5, 4, 4);
                    break;
                case 3:
                    g.fillRect(x + 5, y, 4, 4);
                    g.fillRect(x + 9, y + 4, 4, 4);
                    g.fillRect(x + 13, y + 8, 4, 4);
                    g.fillRect(x + 17, y + 12, 4, 4);
                    g.fillRect(x + 21, y + 16, 4, 4);
                    g.fillRect(x + 5, y + 16, 4, 4);
                    g.fillRect(x + 9, y + 12, 4, 4);
                    g.fillRect(x + 17, y + 4, 4, 4);
                    g.fillRect(x + 21, y, 4, 4);
                    break;
                case 4:
                    g.fillRect(x, y, 4, 4);
                    g.fillRect(x + 4, y + 4, 4, 4);
                    g.fillRect(x + 8, y + 8, 4, 4);
                    g.fillRect(x + 12, y + 12, 4, 4);
                    g.fillRect(x + 16, y + 16, 4, 4);
                    g.fillRect(x, y + 16, 4, 4);
                    g.fillRect(x + 4, y + 12, 4, 4);
                    g.fillRect(x + 12, y + 4, 4, 4);
                    g.fillRect(x + 16, y, 4, 4);
                    break;
            }
        }
        
        if (player == 0)
        {
            switch (hover)
            {
                case 1  : g.setColor(new Color((float)customise.playerColor[0].getRed() / 255, (float)customise.playerColor[0].getGreen() / 255, (float)customise.playerColor[0].getBlue() / 255, .35f)); break;
                case 2  : g.setColor(new Color((float)customise.playerColor[1].getRed() / 255, (float)customise.playerColor[1].getGreen() / 255, (float)customise.playerColor[1].getBlue() / 255, .35f)); break;
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